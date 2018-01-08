package algorithmdemo.sort;

import java.util.Random;

/**
 * 堆排序
 * 
 * @author zhangke
 *
 */
public class HeapSort {

	private static int MAX_LENGTH = 15;

	public static void main(String[] args) {

		int[] array = new int[MAX_LENGTH];
		Random random = new Random();

		System.out.println("原序列：");
		for (int i = 0; i < MAX_LENGTH; i++) {
			int nextInt = random.nextInt(100);
			array[i] = nextInt;
			System.out.print(nextInt + "\t");
		}

		HeapSort sort = new HeapSort();
		sort.heapSort(array);

	}

	/**
	 * 堆排序:
	 * 
	 * 1、首先需要根据数组创建一个最大堆 </br>
	 * 2、在最大堆中堆顶的元素就是最大的，将这个元素放置数组末尾 </br>
	 * 3、在将数组除末尾的元素重新生成一个最大堆 </br>
	 * 4、重新执行上过程
	 * 
	 */
	public void heapSort(int[] array) {
		// 创建最大堆
		buildMaxheap(array);
		// 每次循环将最大堆的堆顶的元素放到数组末尾
		for (int i = array.length - 1; i >= 1; i--) {
			// 将最大值（堆顶）放到最后
			exchangeElement(array, 0, i);

			// 确认第i个位置为最大以后，在把前i个最为一个新堆，在次遍历，
			// 重新调整为最大堆,将0位置值变成最大
			maxHeap(array, i, 0);
		}

		sout(array, "堆排序");

	}

	/**
	 * 创建一个最大堆
	 */
	private void buildMaxheap(int[] array) {
		if (array == null || array.length <= 1) {
			return;
		}

		// 根据二叉树的特性，要创建一个最大堆只要从array.lenght/2为最后一个最大子堆
		int half = array.length / 2;
		for (int i = half; i >= 0; i--) {
			maxHeap(array, array.length, i);
		}
	}

	/**
	 * 
	 * 创建一个堆
	 * 
	 * @param array
	 *            数组
	 * @param heapSize
	 *            最大堆的长度
	 * @param parentIndex
	 *            该结点和孩子节点创建最大堆
	 */
	private void maxHeap(int[] array, int heapSize, int parentIndex) {
		// 获取左右孩子下标
		int left = 2 * parentIndex + 1;
		int right = 2 * parentIndex + 2;
		// 最大元素
		int largest = parentIndex;

		// 找到三个元素的最大最
		if (left < heapSize && array[left] > array[largest]) {
			largest = left;
		}
		if (right < heapSize && array[right] > array[largest]) {
			largest = right;
		}

		// 将最大值赋值给父节点位置
		if (largest != parentIndex) {
			exchangeElement(array, parentIndex, largest);

			// 交换元素以后，需要重新调整最大堆，
			maxHeap(array, heapSize, largest);
		}

	}

	/**
	 * 交换两个元素
	 * 
	 * @param array
	 * @param index1
	 * @param index2
	 */
	private void exchangeElement(int[] array, int index1, int index2) {
		int temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	public void sout(int[] array, String sortName) {
		System.out.println("\n");
		System.out.println(sortName + ":");
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + "\t");
		}
	}

}
