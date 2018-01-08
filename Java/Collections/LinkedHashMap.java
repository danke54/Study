package java.util;

/**
 * LinkedHashMap是HashMap的子类，与HashMap有着同样的存储结构，但它加入了一个双向链表的头结点，
 * 将所有put到LinkedHashmap的节点一一串成了一个双向循环链表，因此它保留了节点插入的顺序，可以使节点的输出顺序与输入顺序相同。
 * 
 * 
 * LinkedHashMap是由
 * HashMap和LinkedList两个集合类的存储结构的结合。在LinkedHashMapMap中，所有put进来的Entry都保存在如第一个图所示的哈希表中，
 * 但它又额外定义了一个以head为头结点的空的双向循环链表，每次put进来Entry，除了将其保存到对哈希表中对应的位置上外，还要将其插入到双向循环链表的尾部。
 */
public class LinkedHashMap<K, V> extends HashMap<K, V> {

	/**
	 * 头结点
	 * 
	 * 第一个结点是header.nxt,最后一个结点是header.prv 如果map为空，那么header.nxt == header &&
	 * header.prv == header
	 */
	transient LinkedEntry<K, V> header;

	/**
	 * true：访问排序 ；false：插入排序
	 */
	private final boolean accessOrder;

	public LinkedHashMap() {
		init();
		// 默认是插入排序
		accessOrder = false;
	}

	public LinkedHashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	public LinkedHashMap(int initialCapacity, float loadFactor) {
		this(initialCapacity, loadFactor, false);
	}

	public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor);
		init();
		this.accessOrder = accessOrder;
	}

	public LinkedHashMap(Map<? extends K, ? extends V> map) {
		this(capacityForInitSize(map.size()));
		constructorPutAll(map);
	}

	/**
	 * 创建一个头结点
	 */
	@Override
	void init() {
		header = new LinkedEntry<K, V>();
	}

	/**
	 * 定义一个LinkedEntry，表示在LinkedHashMap中存储的结点
	 */
	static class LinkedEntry<K, V> extends HashMapEntry<K, V> {
		/**
		 * 前驱结点
		 */
		LinkedEntry<K, V> nxt;
		/**
		 * 后继结点
		 */
		LinkedEntry<K, V> prv;

		/**
		 * 创建一个头结点
		 */
		LinkedEntry() {
			/**
			 * 
			 * HashMapEntry(K key, V value, int hash, HashMapEntry<K, V> next) {
			 * 
			 * this.key = key; this.value = value; this.hash = hash; this.next =
			 * next; }
			 */
			super(null, null, 0, null);
			nxt = prv = this;
		}

		/**
		 * 创建一个普通结点，指定前驱和后继
		 */
		LinkedEntry(K key, V value, int hash, HashMapEntry<K, V> next, LinkedEntry<K, V> nxt, LinkedEntry<K, V> prv) {
			// 1、执行完supre，表示结点按照HashMap的逻辑完成添加
			super(key, value, hash, next);
			// 2、存储到双向链表中
			this.nxt = nxt;
			this.prv = prv;
		}
	}

	/**
	 * 返回最老的结点，也就是header.nxt
	 * 
	 * @hide
	 */
	public Entry<K, V> eldest() {
		LinkedEntry<K, V> eldest = header.nxt;
		return eldest != header ? eldest : null;
	}

	/**
	 * LinkedHashMap中，依旧使用的是父类HashMap的相关put方法
	 * 
	 * 该方法重写了添加结点到hash表中的逻辑
	 */
	@Override
	void addNewEntry(K key, V value, int hash, int index) {
		LinkedEntry<K, V> header = this.header;

		LinkedEntry<K, V> eldest = header.nxt;

		// removeEldestEntry默认返回false，所以该逻辑不会被执行，但是在Lru中
		if (eldest != header && removeEldestEntry(eldest)) {
			remove(eldest.key);
		}

		// 获取最后一个结点
		LinkedEntry<K, V> oldTail = header.prv;
		// 创建一个新结点，前驱和后继分别指向头结点和原最后一个结点
		LinkedEntry<K, V> newTail = new LinkedEntry<K, V>(key, value, hash, table[index], header, oldTail);

		/*
		 * 即把新结点放入到hash表中，同时也把结点放入到双向链表中 1、 table[index] = newTail; 2、
		 * oldTail.nxt = header.prv = newTail;
		 */
		table[index] = oldTail.nxt = header.prv = newTail;
	}

	@Override
	void addNewEntryForNullKey(V value) {
		LinkedEntry<K, V> header = this.header;

		LinkedEntry<K, V> eldest = header.nxt;
		if (eldest != header && removeEldestEntry(eldest)) {
			remove(eldest.key);
		}

		LinkedEntry<K, V> oldTail = header.prv;
		LinkedEntry<K, V> newTail = new LinkedEntry<K, V>(null, value, 0, null, header, oldTail);
		/*
		 * 同HashMap，entryForNullKey不会在hash表中存储，但是在LinkedHashMap中要在链表中存储
		 */
		entryForNullKey = oldTail.nxt = header.prv = newTail;
	}

	@Override
	HashMapEntry<K, V> constructorNewEntry(K key, V value, int hash, HashMapEntry<K, V> next) {
		LinkedEntry<K, V> header = this.header;
		LinkedEntry<K, V> oldTail = header.prv;
		LinkedEntry<K, V> newTail = new LinkedEntry<K, V>(key, value, hash, next, header, oldTail);
		return oldTail.nxt = header.prv = newTail;
	}

	@Override
	public V get(Object key) {

		if (key == null) {
			HashMapEntry<K, V> e = entryForNullKey;
			if (e == null)
				return null;

			// 如果是访问排序,调整顺序
			if (accessOrder)
				makeTail((LinkedEntry<K, V>) e);

			return e.value;
		}

		int hash = Collections.secondaryHash(key);
		HashMapEntry<K, V>[] tab = table;
		for (HashMapEntry<K, V> e = tab[hash & (tab.length - 1)]; e != null; e = e.next) {
			K eKey = e.key;
			if (eKey == key || (e.hash == hash && key.equals(eKey))) {

				// 如果是访问排序,调整顺序
				if (accessOrder)
					makeTail((LinkedEntry<K, V>) e);
				return e.value;
			}
		}
		return null;
	}

	/**
	 * 重新调整双向链表的顺序，把该结点放到末位
	 */
	private void makeTail(LinkedEntry<K, V> e) {
		// 把e结点前后结点相连
		e.prv.nxt = e.nxt;
		e.nxt.prv = e.prv;

		// 把e结点插入到到链表的末位
		LinkedEntry<K, V> header = this.header;
		LinkedEntry<K, V> oldTail = header.prv;
		e.nxt = header;
		e.prv = oldTail;
		oldTail.nxt = header.prv = e;
		modCount++;
	}

	/**
	 * 该方法主要会用于LinkedHashMap中，用于在put方法是，调整双向链表结点位置，把 最新插入的数据放在末位
	 */
	@Override
	void preModify(HashMapEntry<K, V> e) {
		if (accessOrder) {
			makeTail((LinkedEntry<K, V>) e);
		}
	}

	/**
	 * 该方法主要用于子类LinkedHashMap中，在移除接结点时，会移除LinkedHashMap中定义的双向链表中的结点
	 */
	@Override
	void postRemove(HashMapEntry<K, V> e) {
		LinkedEntry<K, V> le = (LinkedEntry<K, V>) e;
		le.prv.nxt = le.nxt;
		le.nxt.prv = le.prv;
		le.nxt = le.prv = null;
	}

	/**
	 * LinkedHashMap中，对原本HashMap中的方法做了优化，在HashMap中要遍历整个hash表及链表，
	 * 而在LinkedHashMap中，只需要对定义的双向链表遍历即可
	 */
	@Override
	public boolean containsValue(Object value) {
		if (value == null) {
			for (LinkedEntry<K, V> header = this.header, e = header.nxt; e != header; e = e.nxt) {
				if (e.value == null) {
					return true;
				}
			}
			return false;
		}

		for (LinkedEntry<K, V> header = this.header, e = header.nxt; e != header; e = e.nxt) {
			if (value.equals(e.value)) {
				return true;
			}
		}
		return false;
	}

	public void clear() {
		super.clear();

		// 将所有结点都设置为null
		LinkedEntry<K, V> header = this.header;
		for (LinkedEntry<K, V> e = header.nxt; e != header;) {
			LinkedEntry<K, V> nxt = e.nxt;
			e.nxt = e.prv = null;
			e = nxt;
		}

		// 双向链表设置为null
		header.nxt = header.prv = header;
	}

	private abstract class LinkedHashIterator<T> implements Iterator<T> {
		LinkedEntry<K, V> next = header.nxt;
		LinkedEntry<K, V> lastReturned = null;
		int expectedModCount = modCount;

		public final boolean hasNext() {
			return next != header;
		}

		final LinkedEntry<K, V> nextEntry() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			LinkedEntry<K, V> e = next;
			if (e == header)
				throw new NoSuchElementException();
			next = e.nxt;
			return lastReturned = e;
		}

		public final void remove() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (lastReturned == null)
				throw new IllegalStateException();
			LinkedHashMap.this.remove(lastReturned.key);
			lastReturned = null;
			expectedModCount = modCount;
		}
	}

	private final class KeyIterator extends LinkedHashIterator<K> {
		public final K next() {
			return nextEntry().key;
		}
	}

	private final class ValueIterator extends LinkedHashIterator<V> {
		public final V next() {
			return nextEntry().value;
		}
	}

	private final class EntryIterator extends LinkedHashIterator<Map.Entry<K, V>> {
		public final Map.Entry<K, V> next() {
			return nextEntry();
		}
	}

	@Override
	Iterator<K> newKeyIterator() {
		return new KeyIterator();
	}

	@Override
	Iterator<V> newValueIterator() {
		return new ValueIterator();
	}

	@Override
	Iterator<Map.Entry<K, V>> newEntryIterator() {
		return new EntryIterator();
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return false;
	}

	private static final long serialVersionUID = 3801124242820219131L;
}
