package algorithmdemo.sort;

import java.util.Random;

public class BubbleSort {
	private static int MAX_LENGTH = 10;

	public static void main(String[] args) {

		int[] array = new int[MAX_LENGTH];
		Random random = new Random();

		System.out.println("原序列：");
		for (int i = 0; i < MAX_LENGTH; i++) {
			int nextInt = random.nextInt(100);
			array[i] = nextInt;
			System.out.print(nextInt + "\t");
		}

		BubbleSort sort = new BubbleSort();
		// sort.bubbleSort(array);
		sort.selectSort(array);
		sort.sout(array, "选择排序");
	}

	/**
	 * 冒泡排序 </br>
	 * 第1趟，在待排序记录r[1]~r[n]中每每比较相邻两个元素，将较大的元素交换到r[n]；
	 * 第2趟，在待排序记录r[1]~r[n-1]中每每比较相邻两个元素，将较大的元素交换到r[n-1]；
	 * 
	 * @param array
	 */
	private void bubbleSort(int[] array) {

		int length = array.length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length - i - 1; j++) {
				if (array[j] > array[j + 1]) {
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * 选择排序</br>
	 * 第1趟，在待排序记录r[1]~r[n]中选出最小的记录，将它与r[1]交换；
	 * 第2趟，在待排序记录r[2]~r[n]中选出最小的记录，将它与r[2]交换；
	 * 以此类推，第i趟在待排序记录r[i]~r[n]中选出最小的记录，将它与r[i]交换，使有序序列不断增长直到全部排序完毕。
	 * 
	 * @param array
	 */
	private void selectSort(int[] array) {
		int length = array.length;
		for (int i = 0; i < length; i++) {

			int minIndex = i;
			for (int j = i + 1; j < length; j++) {
				if (array[minIndex] > array[j]) {
					minIndex = j;
				}
			}

			if (minIndex != i) {
				int temp = array[i];
				array[i] = array[minIndex];
				array[minIndex] = temp;
			}
		}
	}

	public void sout(int[] array, String sortName) {
		System.out.println("\n");
		System.out.println(sortName + ":");
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + "\t");
		}
	}
}
