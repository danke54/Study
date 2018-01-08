package algorithmdemo.sort;

import java.util.Random;

/**
 * 快速排序
 * 
 * @author zhangke
 *
 */
public class QuickSort {

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

		QuickSort sort = new QuickSort();
		sort.quickSort(array);

	}

	public void sout(int[] array, String sortName) {
		System.out.println("\n");
		System.out.println(sortName + ":");
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + "\t");
		}
	}

	/**
	 * 快速排序
	 * 
	 * 选定一个基数，每次排序都将序列分成左右两部分，其中左边都比基数小，右边都比基数大
	 * 
	 */
	public void quickSort(int[] array) {
		quickSort(array, 0, array.length - 1);

		sout(array, "快速排序");
	}

	/**
	 * 分成左右两部分
	 * 
	 * @param array
	 * @param left
	 * @param right
	 */
	private void quickSort(int[] array, int left, int right) {
		if (left < right) {
			int middle = getMiddle(array, left, right);
			quickSort(array, 0, middle - 1);
			quickSort(array, middle + 1, right);
		}

	}

	/**
	 * 获取基数的位置，从该位置将序列分成左右两部分，其中左边都小于基数，右边都大于基数
	 * 
	 * @param array
	 * @param left
	 * @param right
	 * @return
	 */
	private int getMiddle(int[] array, int left, int right) {
		int temp = array[left];// 基数
		while (left < right) {
			// 从右边开始找到比基数小的
			while (left < right && array[right] >= temp) {
				right--;
			}
			// 跳出右边遍历，表示找到了比temp小的，那么将array[right] 的值放在左边
			array[left] = array[right];

			// 右边遍历之后，在从左边开始找比基数大的，放到数组的右边
			while (left < right && array[left] <= temp) {
				left++;
			}
			/*
			 * 跳出右边遍历，表示找到了比temp大的，
			 * 由于在上一次从右边遍历开始array[right]比temp小，所以将从左边找到的比temp大的数放到该位置
			 */
			array[right] = array[left];
		}

		// 跳出循环，完成一次完成循环，此时left = right，也是temp的位置
		array[left] = temp;
		return left;
	}

}
