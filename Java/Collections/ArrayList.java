package java.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import libcore.util.EmptyArray;

/**
 *
 * ArrayList是基于数组实现的，是一个动态数组，其容量能自动增长，类似于C语言中的动态申请内存，动态增长内存。
 * 
 * ArrayList不是线程安全的，只能在单线程环境下，多线程环境下可以考虑用collections.synchronizedList(List l)函数
 * 返回一个线程安全的ArrayList类，也可以使用concurrent并发包下的CopyOnWriteArrayList类。
 * 
 * ArrayList实现了Serializable接口，因此它支持序列化，能够通过序列化传输，实现了RandomAccess接口，
 * 支持快速随机访问，实际上就是通过下标序号进行快速访问，实现了Cloneable接口，能被克隆。
 *
 */
public class ArrayList<E> extends AbstractList<E> implements Cloneable, Serializable, RandomAccess {

	/**
	 * 扩容因子，在jre中默认是10，在android中修改成12
	 */
	private static final int MIN_CAPACITY_INCREMENT = 12;

	/**
	 * ArrayList的容量大小
	 */
	int size;

	/**
	 * ArrayList底层使用的数组实现
	 * 
	 * transient关键字修饰变量，表示该变量不能被序列化
	 */
	transient Object[] array;

	public ArrayList(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("capacity < 0: " + capacity);
		}
		array = (capacity == 0 ? EmptyArray.OBJECT : new Object[capacity]);
	}

	/**
	 * 默认创建一个容量为0的ArrayList
	 */
	public ArrayList() {
		array = EmptyArray.OBJECT;
	}

	/**
	 * 通过已有的集合创建一个ArrayList
	 */
	public ArrayList(Collection<? extends E> collection) {
		if (collection == null) {
			throw new NullPointerException("collection == null");
		}

		Object[] a = collection.toArray();
		if (a.getClass() != Object[].class) {
			Object[] newArray = new Object[a.length];
			System.arraycopy(a, 0, newArray, 0, a.length);
			a = newArray;
		}
		array = a;
		size = a.length;
	}

	/**
	 * 添加一个元素
	 */
	@Override
	public boolean add(E object) {
		Object[] a = array;
		int s = size;

		// 如果数组已满，执行扩容操作
		if (s == a.length) {

			// 如果数组长度小于6，那么数组容量增加12，否则增加之前的长度的一半
			Object[] newArray = new Object[s + (s < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : s >> 1)];
			System.arraycopy(a, 0, newArray, 0, s);
			array = a = newArray;
		}

		// 将元素添加到s位
		a[s] = object;
		size = s + 1;
		modCount++;
		return true;
	}

	/**
	 * 在指定位置添加元素
	 */
	@Override
	public void add(int index, E object) {
		Object[] a = array;
		int s = size;
		if (index > s || index < 0) {
			throwIndexOutOfBoundsException(index, s);
		}

		if (s < a.length) {
			/**
			 * 集合容量小于数组容量，即可以在数组中添加元素
			 * 
			 * 将原有数组从第index个元素向后移动一位 s-index表示需要复制多少个元素
			 */
			System.arraycopy(a, index, a, index + 1, s - index);
		} else {

			// 集合容量和数组容量大小相同，那么数组需要扩容
			Object[] newArray = new Object[newCapacity(s)];
			// 将原始数组分成两次copy，首先复制0-index， 然后复制index后部分，空出新数组的index位，用于插入新元素
			System.arraycopy(a, 0, newArray, 0, index);
			System.arraycopy(a, index, newArray, index + 1, s - index);
			array = a = newArray;
		}
		// 给数组index位赋值
		a[index] = object;
		size = s + 1;
		modCount++;
	}

	/**
	 * 如果数组长度小于6，那么数组容量增加12，否则在数组原长度的基础上增加一半
	 */
	private static int newCapacity(int currentCapacity) {
		int increment = (currentCapacity < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT
				: currentCapacity >> 1);
		return currentCapacity + increment;
	}

	/**
	 * 添加一个集合中的所有元素，到集合末尾
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		Object[] newPart = collection.toArray();
		int newPartSize = newPart.length;
		if (newPartSize == 0) {
			// 集合是否为空判断
			return false;
		}

		Object[] a = array;
		int s = size;
		int newSize = s + newPartSize; // If add overflows, arraycopy will fail
		// 溢出判断：增加之后的长度是否大于数组长度
		if (newSize > a.length) {
			// 扩容
			int newCapacity = newCapacity(newSize - 1); // ~33% growth room
			Object[] newArray = new Object[newCapacity];
			// 赋值原数组元素
			System.arraycopy(a, 0, newArray, 0, s);
			array = a = newArray;
		}
		// 复制所有要添加的元素
		System.arraycopy(newPart, 0, a, s, newPartSize);
		size = newSize;
		modCount++;
		return true;
	}

	/**
	 * 添加元素到指定位置
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		int s = size;
		if (index > s || index < 0) {
			throwIndexOutOfBoundsException(index, s);
		}
		Object[] newPart = collection.toArray();
		int newPartSize = newPart.length;
		if (newPartSize == 0) {
			return false;
		}
		Object[] a = array;
		int newSize = s + newPartSize; // If add overflows, arraycopy will fail
		if (newSize <= a.length) {
			// 如果数组容量能够添加新的集合，首先将原数组index位置之后的元素移动newPartSize位，空出newPartSize位用于存放新集合的元素
			System.arraycopy(a, index, a, index + newPartSize, s - index);
		} else {
			int newCapacity = newCapacity(newSize - 1); // ~33% growth room
			Object[] newArray = new Object[newCapacity];
			System.arraycopy(a, 0, newArray, 0, index);
			System.arraycopy(a, index, newArray, index + newPartSize, s - index);
			array = a = newArray;
		}
		System.arraycopy(newPart, 0, a, index, newPartSize);
		size = newSize;
		modCount++;
		return true;
	}

	static IndexOutOfBoundsException throwIndexOutOfBoundsException(int index, int size) {
		throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
	}

	@Override
	public void clear() {
		if (size != 0) {
			Arrays.fill(array, 0, size, null);
			size = 0;
			modCount++;
		}
	}

	@Override
	public Object clone() {
		try {
			ArrayList<?> result = (ArrayList<?>) super.clone();
			result.array = array.clone();
			return result;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	/**
	 * 指定数组的长度
	 */
	public void ensureCapacity(int minimumCapacity) {
		Object[] a = array;
		if (a.length < minimumCapacity) {
			Object[] newArray = new Object[minimumCapacity];
			System.arraycopy(a, 0, newArray, 0, size);
			array = newArray;
			modCount++;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index >= size) {
			throwIndexOutOfBoundsException(index, size);
		}
		return (E) array[index];
	}

	/**
	 * Returns the number of elements in this {@code ArrayList}.
	 *
	 * @return the number of elements in this {@code ArrayList}.
	 */
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 判断集合是否包含某个元素
	 */
	@Override
	public boolean contains(Object object) {
		Object[] a = array;
		int s = size;
		if (object != null) {
			for (int i = 0; i < s; i++) {

				if (object.equals(a[i])) {
					return true;
				}
			}
		} else {
			for (int i = 0; i < s; i++) {

				if (a[i] == null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int indexOf(Object object) {
		Object[] a = array;
		int s = size;
		if (object != null) {
			for (int i = 0; i < s; i++) {
				if (object.equals(a[i])) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < s; i++) {
				// 如果包含多个null元素，那么找到第一个的时候就返回
				if (a[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 判断最后一个元素
	 */
	@Override
	public int lastIndexOf(Object object) {
		Object[] a = array;
		if (object != null) {
			for (int i = size - 1; i >= 0; i--) {
				if (object.equals(a[i])) {
					return i;
				}
			}
		} else {
			for (int i = size - 1; i >= 0; i--) {
				if (a[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 移除并返回指定位置的元素
	 */
	@Override
	public E remove(int index) {
		Object[] a = array;
		int s = size;
		if (index >= s) {
			throwIndexOutOfBoundsException(index, s);
		}
		@SuppressWarnings("unchecked")
		// 获取元素
		E result = (E) a[index];
		// 将index后一位整体向前移动一位
		System.arraycopy(a, index + 1, a, index, --s - index);
		// 将最后一位赋值为空
		a[s] = null; // Prevent memory leak
		size = s;
		modCount++;
		return result;
	}

	/**
	 * 移除指定对象
	 */
	@Override
	public boolean remove(Object object) {
		Object[] a = array;
		int s = size;
		if (object != null) {
			for (int i = 0; i < s; i++) {
				if (object.equals(a[i])) {
					System.arraycopy(a, i + 1, a, i, --s - i);
					a[s] = null; // Prevent memory leak
					size = s;
					modCount++;
					return true;
				}
			}
		} else {
			for (int i = 0; i < s; i++) {
				if (a[i] == null) {
					System.arraycopy(a, i + 1, a, i, --s - i);
					a[s] = null; // Prevent memory leak
					size = s;
					modCount++;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		if (fromIndex == toIndex) {
			return;
		}
		Object[] a = array;
		int s = size;
		if (fromIndex >= s) {
			throw new IndexOutOfBoundsException("fromIndex " + fromIndex + " >= size " + size);
		}
		if (toIndex > s) {
			throw new IndexOutOfBoundsException("toIndex " + toIndex + " > size " + size);
		}
		if (fromIndex > toIndex) {
			throw new IndexOutOfBoundsException("fromIndex " + fromIndex + " > toIndex " + toIndex);
		}

		System.arraycopy(a, toIndex, a, fromIndex, s - toIndex);
		int rangeSize = toIndex - fromIndex;
		Arrays.fill(a, s - rangeSize, s, null);
		size = s - rangeSize;
		modCount++;
	}

	/**
	 * 设置指定的值
	 */
	@Override
	public E set(int index, E object) {
		Object[] a = array;
		if (index >= size) {
			throwIndexOutOfBoundsException(index, size);
		}
		@SuppressWarnings("unchecked")
		E result = (E) a[index];
		a[index] = object;
		return result;
	}

	@Override
	public Object[] toArray() {
		int s = size;
		Object[] result = new Object[s];
		System.arraycopy(array, 0, result, 0, s);
		return result;
	}

	@Override
	public <T> T[] toArray(T[] contents) {
		int s = size;
		if (contents.length < s) {
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) Array.newInstance(contents.getClass().getComponentType(), s);
			contents = newArray;
		}
		System.arraycopy(this.array, 0, contents, 0, s);
		if (contents.length > s) {
			contents[s] = null;
		}
		return contents;
	}

	public void trimToSize() {
		int s = size;
		if (s == array.length) {
			return;
		}
		if (s == 0) {
			array = EmptyArray.OBJECT;
		} else {
			Object[] newArray = new Object[s];
			System.arraycopy(array, 0, newArray, 0, s);
			array = newArray;
		}
		modCount++;
	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator();
	}

	private class ArrayListIterator implements Iterator<E> {
		/** Number of elements remaining in this iteration */
		private int remaining = size;

		/** Index of element that remove() would remove, or -1 if no such elt */
		private int removalIndex = -1;

		/** The expected modCount value */
		private int expectedModCount = modCount;

		public boolean hasNext() {
			return remaining != 0;
		}

		/**
		 * 获取元素
		 */
		@SuppressWarnings("unchecked")
		public E next() {
			ArrayList<E> ourList = ArrayList.this;
			int rem = remaining;

			// 在对集合进行遍历时不能对集合做CRUD操作，否则就会抛出异常
			if (ourList.modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (rem == 0) {
				throw new NoSuchElementException();
			}
			remaining = rem - 1;
			return (E) ourList.array[removalIndex = ourList.size - rem];
		}

		/**
		 * 通过迭代器移除元素
		 */
		public void remove() {
			Object[] a = array;
			int removalIdx = removalIndex;
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (removalIdx < 0) {
				throw new IllegalStateException();
			}
			System.arraycopy(a, removalIdx + 1, a, removalIdx, remaining);
			a[--size] = null; // Prevent memory leak
			removalIndex = -1;
			expectedModCount = ++modCount;
		}
	}

	@Override
	public int hashCode() {
		Object[] a = array;
		int hashCode = 1;
		for (int i = 0, s = size; i < s; i++) {
			Object e = a[i];
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof List)) {
			return false;
		}
		List<?> that = (List<?>) o;
		int s = size;
		if (that.size() != s) {
			return false;
		}
		Object[] a = array;
		if (that instanceof RandomAccess) {
			for (int i = 0; i < s; i++) {
				Object eThis = a[i];
				Object ethat = that.get(i);
				if (eThis == null ? ethat != null : !eThis.equals(ethat)) {
					return false;
				}
			}
		} else { // Argument list is not random access; use its iterator
			Iterator<?> it = that.iterator();
			for (int i = 0; i < s; i++) {
				Object eThis = a[i];
				Object eThat = it.next();
				if (eThis == null ? eThat != null : !eThis.equals(eThat)) {
					return false;
				}
			}
		}
		return true;
	}

	private static final long serialVersionUID = 8683452581122892189L;

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeInt(array.length);
		for (int i = 0; i < size; i++) {
			stream.writeObject(array[i]);
		}
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		int cap = stream.readInt();
		if (cap < size) {
			throw new InvalidObjectException("Capacity: " + cap + " < size: " + size);
		}
		array = (cap == 0 ? EmptyArray.OBJECT : new Object[cap]);
		for (int i = 0; i < size; i++) {
			array[i] = stream.readObject();
		}
	}
}
