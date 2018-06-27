package structdemo;

/**
 * 队列
 * 
 * @author zhangke
 *
 * @param <E>
 */
public class Queue<E> {

	/**
	 * 初始大小
	 */
	private static final int DEFAULT_CAPACITY = 10;

	private Object[] elementData;

	/**
	 * 队头和队尾
	 */
	private E head, tail;

	public Queue() {
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
	 * 入队
	 * 
	 * @param e
	 */
	public void enqueue(E e) {
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
	 * 弹出
	 * 
	 * @return
	 */
	public E dequeue() {
		if (size == 0) {
			return null;
		}
		E e = (E) elementData[0];
		System.arraycopy(elementData, 1, elementData, 0, size - 1);
		elementData[--size] = null;
		return e;
	}

	/**
	 * 队头
	 * 
	 * @return
	 */
	public E getHead() {
		if (size == 0) {
			return null;
		}
		return (E) elementData[0];
	}

	/**
	 * 队尾
	 * 
	 * @return
	 */
	public E getTail() {
		if (size == 0) {
			return null;
		}
		return (E) elementData[size - 1];
	}

}
