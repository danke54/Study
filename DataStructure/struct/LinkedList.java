package structdemo;

/**
 * 双向链表
 * 
 * @author zhangke
 *
 */
public class LinkedList<E> {

	/**
	 * 定义头结点
	 */
	private Node header;

	private int size;

	public LinkedList() {
		// 初始化头节点，此时头结点的前驱和后继都指向自己
		header = new Node(null, null, null);
		header.preNode = header;
		header.nextNode = header;
	}

	/**
	 * 添加，默认在末尾添加
	 * 
	 * @param data
	 */
	public void add(E data) {
		add(size, data);
	}

	/**
	 * 指定位置添加
	 */
	public void add(int index, E data) {
		Node node = node(index);

		Node newNode = new Node(node.preNode, node, data);
		node.preNode.nextNode = newNode;
		node.preNode = newNode;

		size++;
	}

	/**
	 * 删除指定位置节点
	 * 
	 * @param indext
	 * @return
	 */
	public E delete(int indext) {
		Node node = node(indext);
		node.preNode.nextNode = node.nextNode;
		node.nextNode.preNode = node.preNode;

		size--;
		return node.data;
	}

	/**
	 * 查询指定位置节点
	 */
	public E get(int index) {
		Node node = node(index);
		return node.data;
	}

	/**
	 * 获取指定位置的节点
	 * 
	 * @param index
	 * @return
	 */
	private Node node(int index) {
		if (index > size || index < 0) {
			throw new IndexOutOfBoundsException();
		}

		Node node = header;

		/**
		 * 这里使用了二分法的思想，如果插入的元素在链表后半段，那么从后查找，否则从前查找，可以节约寻址时间
		 */
		if (index >= size / 2) {
			for (int i = size - 1; i >= index; i--) {
				node = node.preNode;
			}
		} else {
			for (int i = 0; i <= index; i++) {
				node = node.nextNode;
			}
		}
		return node;
	}

	/**
	 * 返回链表的大小
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 链表是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (header.preNode == header.nextNode);
		// return (size == 0)? true:false;

	}

	/**
	 * 定义链表的节点
	 * 
	 * @author zhangke
	 *
	 */
	private class Node {

		public Node(Node pre, Node next, E data) {
			this.preNode = pre;
			this.nextNode = next;
			this.data = data;
		}

		/**
		 * 前驱节点
		 */
		public Node preNode;
		/**
		 * 后继节点
		 */
		public Node nextNode;
		/**
		 * 节点的数据
		 */
		public E data;

	}

}
