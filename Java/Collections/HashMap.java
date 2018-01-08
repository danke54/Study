package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import libcore.util.Objects;


/**
 * HashMap的数据结构是数组加链表的一个结合，称之为"散列链表"
 */
public class HashMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {
	/**
	 * HashMap的最小容量
	 */
	private static final int MINIMUM_CAPACITY = 4;

	/**
	 * HashMap的最大容量(Integer.MAX_VALUE为Math.abs(1<<31));
	 */
	private static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * 创建一个数组，该数组长度为最小容量的一半
	 */
	private static final Entry[] EMPTY_TABLE = new HashMapEntry[MINIMUM_CAPACITY >>> 1];

	/**
	 * 默认的加载因子
	 */
	static final float DEFAULT_LOAD_FACTOR = .75F;

	/**
	 * hash表，在该表中存储的主要存放 (key!=null)的映射
	 */
	transient HashMapEntry<K, V>[] table;

	/**
	 * 在HashMap中，该变量用于存储键为 ( key==null)的映射
	 * 
	 * 从该定义中可以看出，key为null的键值对不会在是hashmap的数据结构中体现
	 */
	transient HashMapEntry<K, V> entryForNullKey;

	/**
	 * hashmap的数量
	 */
	transient int size;

	/**
	 * hashmap被操作的次数，CRUD都会让该变量增加
	 */
	transient int modCount;

	/**
	 * HashMap阈值，当HashMap的size超过这个阈值，table将被扩容，通常（threshold = 容量*加载因子）；当容量是0是，为空表
	 */
	private transient int threshold;

	private transient Set<K> keySet;
	private transient Set<Entry<K, V>> entrySet;
	private transient Collection<V> values;

	@SuppressWarnings("unchecked")
	public HashMap() {
		table = (HashMapEntry<K, V>[]) EMPTY_TABLE;
		// 创建一个空表，当put方法被调用时保证创建一个新表代替空表
		threshold = -1; 
	}

	/**
	 * 创建指定大小的HashMap
	 */
	public HashMap(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Capacity: " + capacity);
		}

		if (capacity == 0) {
			@SuppressWarnings("unchecked")
			HashMapEntry<K, V>[] tab = (HashMapEntry<K, V>[]) EMPTY_TABLE;
			table = tab;
			threshold = -1; // Forces first put() to replace EMPTY_TABLE
			return;
		}

		if (capacity < MINIMUM_CAPACITY) {
			capacity = MINIMUM_CAPACITY;
		} else if (capacity > MAXIMUM_CAPACITY) {
			capacity = MAXIMUM_CAPACITY;
		} else {
			// 找出一个2的n次幂的数，大于或等于capacity
			capacity = Collections.roundUpToPowerOfTwo(capacity);
		}
		makeTable(capacity);
	}

	/**
	 * 指定"容量大小"和"加载因子"的构造函数 
	 * 
	 * 忽略加载因子，让加载因子始终为0.75，这样可以改善性能
	 */
	public HashMap(int capacity, float loadFactor) {
		this(capacity);

		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Load factor: " + loadFactor);
		}

	}

	public HashMap(Map<? extends K, ? extends V> map) {
		this(capacityForInitSize(map.size()));
		constructorPutAll(map);
	}

	final void constructorPutAll(Map<? extends K, ? extends V> map) {
		if (table == EMPTY_TABLE) {
			doubleCapacity(); // Don't do unchecked puts to a shared table.
		}
		for (Entry<? extends K, ? extends V> e : map.entrySet()) {
			constructorPut(e.getKey(), e.getValue());
		}
	}

	static int capacityForInitSize(int size) {
		int result = (size >> 1) + size; // Multiply by 3/2 to allow for growth

		// boolean expr is equivalent to result >= 0 && result<MAXIMUM_CAPACITY
		return (result & ~(MAXIMUM_CAPACITY - 1)) == 0 ? result : MAXIMUM_CAPACITY;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		/*
		 * This could be made more efficient. It unnecessarily hashes all of the
		 * elements in the map.
		 */
		HashMap<K, V> result;
		try {
			result = (HashMap<K, V>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}

		// Restore clone to empty state, retaining our capacity and threshold
		result.makeTable(table.length);
		result.entryForNullKey = null;
		result.size = 0;
		result.keySet = null;
		result.entrySet = null;
		result.values = null;

		result.init(); // Give subclass a chance to initialize itself
		result.constructorPutAll(this); // Calls method overridden in subclass!!
		return result;
	}

	void init() {
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * 通过key获取元素
	 */
	public V get(Object key) {
		/**
		 * 如果key为null，那么返回entryForNullKey的value，
		 * 
		 * 在hashmap中就定义了一个entryForNullKey用于存放key==null的键值对，这就能保存key==null的元素在map中值存在一个
		 */
		if (key == null) {
			HashMapEntry<K, V> e = entryForNullKey;
			return e == null ? null : e.value;
		}

		// 二次hash，获取hash值，获取指定key在表中的位置
		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)]; e != null; e = e.next) {
			K eKey = e.key;
			if (eKey == key || (e.hash == hash && key.equals(eKey))) {
				return e.value;
			}
		}
		return null;
	}

	/**
	 * 判断是否包含该key
	 */
	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return entryForNullKey != null;
		}

		// 计算二次哈希值
		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		/*
		 *  遍历哈希表，通过 hash & (tab.length - 1)计算值  ，通过e = e.next遍历链表
		 */
		for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)]; e != null; e = e.next) {
			K eKey = e.key;
			if (eKey == key || (e.hash == hash && key.equals(eKey))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		HashMapEntry[] tab = table;
		int len = tab.length;
		if (value == null) {
			// 首先遍历哈希表中是否存在value=null
			for (int i = 0; i < len; i++) {
				for (HashMapEntry e = tab[i]; e != null; e = e.next) {
					if (e.value == null) {
						return true;
					}
				}
			}
			// 如果在哈希表中不存在，在判断key=null的键值对的value是否为null
			return entryForNullKey != null && entryForNullKey.value == null;
		}

		// value is non-null
		for (int i = 0; i < len; i++) {
			for (HashMapEntry e = tab[i]; e != null; e = e.next) {
				if (value.equals(e.value)) {
					return true;
				}
			}
		}
		return entryForNullKey != null && value.equals(entryForNullKey.value);
	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			// 当key== null时，存储vale，
			return putValueForNullKey(value);
		}

		// 这里通过一个两次hash算法计算出当前key的hash值
		int hash = Collections.secondaryHash(key);
		
		HashMapEntry<K, V>[] tab = table;
		
		//通过hash值和数组的与运算指定当前key在数组中的位置，可以快速找到key在数组中的位置
		int index = hash & (tab.length - 1);
		
		for (HashMapEntry<K, V> e = tab[index]; e != null; e = e.next) {
			
			/**
			 * 如果当前key指定的键值对存在，那么使用新的value替换旧的value
			 */
			if (e.hash == hash && key.equals(e.key)) {
				preModify(e);
				V oldValue = e.value;
				e.value = value;
				return oldValue;
			}
		}

		// 如果不存在当前key指定的键值对，那么重新添加一个键值对
		modCount++;
		if (size++ > threshold) {
			// 数组扩容
			tab = doubleCapacity();
			// 扩容之后要重新计算当前key在新table中的位置
			index = hash & (tab.length - 1);
		}
		// 每次添加一个值，是添加到hash表，至于原来该位置上的值被当做新结点的next结点
		addNewEntry(key, value, hash, index);
		return null;
	}
	
	/**
	 * 存放key为null的value
	 */
	private V putValueForNullKey(V value) {
		HashMapEntry<K, V> entry = entryForNullKey;
		if (entry == null) {
			addNewEntryForNullKey(value);
			size++;
			modCount++;
			return null;
		} else {
			preModify(entry);
			V oldValue = entry.value;
			entry.value = value;
			return oldValue;
		}
	}
	
	/**
	 * 添加一个非空key键值对
	 */
	void addNewEntry(K key, V value, int hash, int index) {
		table[index] = new HashMapEntry<K, V>(key, value, hash, table[index]);
	}

	/**
	 * 创建key为null的键值对
	 */
	void addNewEntryForNullKey(V value) {
		entryForNullKey = new HashMapEntry<K, V>(null, value, 0, null);
	}
	
	
	/**
	 * HashMap扩容
	 */
	private HashMapEntry<K, V>[] doubleCapacity() {
		HashMapEntry<K, V>[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			return oldTable;
		}
		
		// 容量扩大两倍
		int newCapacity = oldCapacity * 2;
		HashMapEntry<K, V>[] newTable = makeTable(newCapacity);
		if (size == 0) {
			return newTable;
		}

		/*
		 * 整个for循环的目的是让oldTable中的所有元素都copy到newTable中
		 */
		for (int j = 0; j < oldCapacity; j++) {
			HashMapEntry<K, V> e = oldTable[j];
			if (e == null) {
				continue;
			}
			
			int highBit = e.hash & oldCapacity;
			HashMapEntry<K, V> broken = null;
			newTable[j | highBit] = e;
			
			/*
			 * 因为HashMap中的数据结构是散列链表，不但要copy哈希表中的元素到新表中，
			 * 还要重新计算哈希表每个元素链表中的元素在newTable中的位置，并在新数组中放入
			 */
			for (HashMapEntry<K, V> n = e.next; n != null; e = n, n = n.next) {
				int nextHighBit = n.hash & oldCapacity;
				if (nextHighBit != highBit) {
					if (broken == null)
						newTable[j | nextHighBit] = n;
					else
						broken.next = n;
					broken = e;
					highBit = nextHighBit;
				}
			}
			if (broken != null)
				broken.next = null;
		}
		return newTable;
	}

	/**
	 * 该方法主要会用于LinkedHashMap中，用于在put方法是，调整双向链表结点位置，把 最新插入的数据放在末位
	 */
	void preModify(HashMapEntry<K, V> e) {
	}

	/**
	 * This method is just like put, except that it doesn't do things that are
	 * inappropriate or unnecessary for constructors and pseudo-constructors
	 * (i.e., clone, readObject). In particular, this method does not check to
	 * ensure that capacity is sufficient, and does not increment modCount.
	 */
	private void constructorPut(K key, V value) {
		if (key == null) {
			HashMapEntry<K, V> entry = entryForNullKey;
			if (entry == null) {
				entryForNullKey = constructorNewEntry(null, value, 0, null);
				size++;
			} else {
				entry.value = value;
			}
			return;
		}

		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		int index = hash & (tab.length - 1);
		HashMapEntry<K, V> first = tab[index];
		for (HashMapEntry<K, V> e = first; e != null; e = e.next) {
			if (e.hash == hash && key.equals(e.key)) {
				e.value = value;
				return;
			}
		}

		// No entry for (non-null) key is present; create one
		tab[index] = constructorNewEntry(key, value, hash, first);
		size++;
	}

	

	/**
	 * 创建一个新数组
	 */
	private HashMapEntry<K, V>[] makeTable(int newCapacity) {
		@SuppressWarnings("unchecked")
		// 创建一个数组
		HashMapEntry<K, V>[] newTable = (HashMapEntry<K, V>[]) new HashMapEntry[newCapacity];
		table = newTable;
		// 重新设置阈值为容量的3/4 
		threshold = (newCapacity >> 1) + (newCapacity >> 2); // 3/4 capacity
		return newTable;
	}

	/**
	 * 移除该key的键值对
	 */
	@Override
	public V remove(Object key) {
		if (key == null) {
			return removeNullKey();
		}
		
		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		int index = hash & (tab.length - 1);
		
		for (HashMapEntry<K, V> e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (e.hash == hash && key.equals(e.key)) {
				if (prev == null) {
					// prev == null，表示该key在哈希表中，tab[index] = e.next
					tab[index] = e.next;
				} else {
					// 该key不在哈希表中，将e前一个结点指向移除结点的后一个结点，该e被删除
					prev.next = e.next;
				}
				modCount++;
				size--;
				postRemove(e);
				return e.value;
			}
		}
		return null;
	}

	private V removeNullKey() {
		HashMapEntry<K, V> e = entryForNullKey;
		if (e == null) {
			return null;
		}
		entryForNullKey = null;
		modCount++;
		size--;
		postRemove(e);
		return e.value;
	}

	/**
	 * 该方法主要用于子类LinkedHashMap中，在移除接结点时，会移除LinkedHashMap中定义的双向链表中的结点
	 */
	void postRemove(HashMapEntry<K, V> e) {
	}

	/**
	 * Removes all mappings from this hash map, leaving it empty.
	 *
	 * @see #isEmpty
	 * @see #size
	 */
	@Override
	public void clear() {
		if (size != 0) {
			Arrays.fill(table, null);
			entryForNullKey = null;
			modCount++;
			size = 0;
		}
	}

	/**
	 * Returns a set of the keys contained in this map. The set is backed by
	 * this map so changes to one are reflected by the other. The set does not
	 * support adding.
	 *
	 * @return a set of the keys.
	 */
	@Override
	public Set<K> keySet() {
		Set<K> ks = keySet;
		return (ks != null) ? ks : (keySet = new KeySet());
	}

	@Override
	public Collection<V> values() {
		Collection<V> vs = values;
		return (vs != null) ? vs : (values = new Values());
	}

	/**
	 * 返回键值对的set集合
	 */
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> es = entrySet;
		return (es != null) ? es : (entrySet = new EntrySet());
	}
	
	HashMapEntry<K, V> constructorNewEntry(K key, V value, int hash, HashMapEntry<K, V> first) {
		return new HashMapEntry<K, V>(key, value, hash, first);
	}

	/**
	 * 是否包含键值对
	 */
	private boolean containsMapping(Object key, Object value) {
		if (key == null) {
			HashMapEntry<K, V> e = entryForNullKey;
			return e != null && Objects.equal(value, e.value);
		}

		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		int index = hash & (tab.length - 1);
		for (HashMapEntry<K, V> e = tab[index]; e != null; e = e.next) {
			if (e.hash == hash && key.equals(e.key)) {
				return Objects.equal(value, e.value);
			}
		}
		return false; // No entry for key
	}

	/**
	 * 移除键值对
	 */
	private boolean removeMapping(Object key, Object value) {
		if (key == null) {
			HashMapEntry<K, V> e = entryForNullKey;
			if (e == null || !Objects.equal(value, e.value)) {
				return false;
			}
			entryForNullKey = null;
			modCount++;
			size--;
			postRemove(e);
			return true;
		}

		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		int index = hash & (tab.length - 1);
		for (HashMapEntry<K, V> e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (e.hash == hash && key.equals(e.key)) {
				if (!Objects.equal(value, e.value)) {
					return false; // Map has wrong value for key
				}
				if (prev == null) {
					tab[index] = e.next;
				} else {
					prev.next = e.next;
				}
				modCount++;
				size--;
				postRemove(e);
				return true;
			}
		}
		return false; // No entry for key
	}

	
	/**
	 * 添加一个map
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		ensureCapacity(map.size());
		super.putAll(map);
	}

	/**
	 * 扩容
	 */
	private void ensureCapacity(int numMappings) {
		int newCapacity = Collections.roundUpToPowerOfTwo(capacityForInitSize(numMappings));
		HashMapEntry<K, V>[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (newCapacity <= oldCapacity) {
			return;
		}
		if (newCapacity == oldCapacity * 2) {
			doubleCapacity();
			return;
		}

		// We're growing by at least 4x, rehash in the obvious way
		HashMapEntry<K, V>[] newTable = makeTable(newCapacity);
		if (size != 0) {
			int newMask = newCapacity - 1;
			for (int i = 0; i < oldCapacity; i++) {
				for (HashMapEntry<K, V> e = oldTable[i]; e != null;) {
					HashMapEntry<K, V> oldNext = e.next;
					int newIndex = e.hash & newMask;
					HashMapEntry<K, V> newNext = newTable[newIndex];
					newTable[newIndex] = e;
					e.next = newNext;
					e = oldNext;
				}
			}
		}
	}
	
	

	/**
	 * 定义一个HashMapEntry，表示一个键值对对象
	 */
	static class HashMapEntry<K, V> implements Entry<K, V> {
		/**
		 * 键
		 */
		final K key;
		/**
		 * 值
		 */
		V value;
		/**
		 * 哈希值
		 */
		final int hash;
		/**
		 * 下一个结点
		 */
		HashMapEntry<K, V> next;

		HashMapEntry(K key, V value, int hash, HashMapEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.hash = hash;
			this.next = next;
		}

		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@Override
		public final boolean equals(Object o) {
			if (!(o instanceof Entry)) {
				return false;
			}
			Entry<?, ?> e = (Entry<?, ?>) o;
			return Objects.equal(e.getKey(), key) && Objects.equal(e.getValue(), value);
		}

		@Override
		public final int hashCode() {
			return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
		}

		@Override
		public final String toString() {
			return key + "=" + value;
		}
	}

	
	/**
	 * 定义了一个hash迭代器，关于key、value和entry的迭代器
	 */
	private abstract class HashIterator {
		int nextIndex;
		HashMapEntry<K, V> nextEntry = entryForNullKey;
		HashMapEntry<K, V> lastEntryReturned;
		int expectedModCount = modCount;

		HashIterator() {
			if (nextEntry == null) {
				HashMapEntry<K, V>[] tab = table;
				HashMapEntry<K, V> next = null;
				while (next == null && nextIndex < tab.length) {
					next = tab[nextIndex++];
				}
				nextEntry = next;
			}
		}

		public boolean hasNext() {
			return nextEntry != null;
		}

		HashMapEntry<K, V> nextEntry() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (nextEntry == null)
				throw new NoSuchElementException();

			HashMapEntry<K, V> entryToReturn = nextEntry;
			HashMapEntry<K, V>[] tab = table;
			HashMapEntry<K, V> next = entryToReturn.next;
			while (next == null && nextIndex < tab.length) {
				next = tab[nextIndex++];
			}
			nextEntry = next;
			return lastEntryReturned = entryToReturn;
		}

		public void remove() {
			if (lastEntryReturned == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			HashMap.this.remove(lastEntryReturned.key);
			lastEntryReturned = null;
			expectedModCount = modCount;
		}
	}

	private final class KeyIterator extends HashIterator implements Iterator<K> {
		public K next() {
			return nextEntry().key;
		}
	}

	private final class ValueIterator extends HashIterator implements Iterator<V> {
		public V next() {
			return nextEntry().value;
		}
	}

	private final class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {
		public Entry<K, V> next() {
			return nextEntry();
		}
	}

	// Subclass (LinkedHashMap) overrides these for correct iteration order
	Iterator<K> newKeyIterator() {
		return new KeyIterator();
	}

	Iterator<V> newValueIterator() {
		return new ValueIterator();
	}

	Iterator<Entry<K, V>> newEntryIterator() {
		return new EntryIterator();
	}

	private final class KeySet extends AbstractSet<K> {
		public Iterator<K> iterator() {
			return newKeyIterator();
		}

		public int size() {
			return size;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean contains(Object o) {
			return containsKey(o);
		}

		public boolean remove(Object o) {
			int oldSize = size;
			HashMap.this.remove(o);
			return size != oldSize;
		}

		public void clear() {
			HashMap.this.clear();
		}
	}

	/**
	 * 该类用于创建并返回values集合
	 */
	private final class Values extends AbstractCollection<V> {
		public Iterator<V> iterator() {
			return newValueIterator();
		}

		public int size() {
			return size;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean contains(Object o) {
			return containsValue(o);
		}

		public void clear() {
			HashMap.this.clear();
		}
	}

	private final class EntrySet extends AbstractSet<Entry<K, V>> {
		public Iterator<Entry<K, V>> iterator() {
			return newEntryIterator();
		}

		public boolean contains(Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry<?, ?> e = (Entry<?, ?>) o;
			return containsMapping(e.getKey(), e.getValue());
		}

		public boolean remove(Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry<?, ?> e = (Entry<?, ?>) o;
			return removeMapping(e.getKey(), e.getValue());
		}

		public int size() {
			return size;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public void clear() {
			HashMap.this.clear();
		}
	}

	private static final long serialVersionUID = 362498820763181265L;

	private static final ObjectStreamField[] serialPersistentFields = {
			new ObjectStreamField("loadFactor", float.class) };

	private void writeObject(ObjectOutputStream stream) throws IOException {
		// Emulate loadFactor field for other implementations to read
		ObjectOutputStream.PutField fields = stream.putFields();
		fields.put("loadFactor", DEFAULT_LOAD_FACTOR);
		stream.writeFields();

		stream.writeInt(table.length); // Capacity
		stream.writeInt(size);
		for (Entry<K, V> e : entrySet()) {
			stream.writeObject(e.getKey());
			stream.writeObject(e.getValue());
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		int capacity = stream.readInt();
		if (capacity < 0) {
			throw new InvalidObjectException("Capacity: " + capacity);
		}
		if (capacity < MINIMUM_CAPACITY) {
			capacity = MINIMUM_CAPACITY;
		} else if (capacity > MAXIMUM_CAPACITY) {
			capacity = MAXIMUM_CAPACITY;
		} else {
			capacity = Collections.roundUpToPowerOfTwo(capacity);
		}
		makeTable(capacity);

		int size = stream.readInt();
		if (size < 0) {
			throw new InvalidObjectException("Size: " + size);
		}

		init(); // Give subclass (LinkedHashMap) a chance to initialize itself
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			K key = (K) stream.readObject();
			@SuppressWarnings("unchecked")
			V val = (V) stream.readObject();
			constructorPut(key, val);
		}
	}
}
