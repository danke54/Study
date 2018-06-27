package structdemo;

/**
 * 栈
 */
public class Stack<E> {

	/**
	 * 初始大小
	 */
	private static final int DEFAULT_CAPACITY = 10;

	private Object[] elementData;

	public Stack() {
		elementData = new Object[DEFAULT_CAPACITY];
	}

	private int size;

	public int getSize() {
		return size;
	}

	/**
	 * 判断元素是否为空
	 */
	public boolean isEmpty() {
		return size == 0 ? true : false;
	}

	/**
	 * 压入
	 * 
	 * @param e
	 */
	public void push(E e) {
		ensureCapacity();
		elementData[size] = e;
		size++;
	}

	/**
	 * 数组扩容
	 */
	private void ensureCapacity() {
		int oldCapacity = elementData.length;
		if (size == oldCapacity) {
			int newCapacity = oldCapacity + (oldCapacity >> 1);
			Object[] newTable = new Object[newCapacity];
			System.arraycopy(elementData, 0, newTable, 0, size);
			elementData = newTable;
		}
	}

	/**
	 * 出栈
	 * 
	 * @return
	 */
	public E pop() {
		if (size == 0) {
			return null;
		}
		E e = (E) elementData[size - 1];
		size--;
		return e;
	}

}
