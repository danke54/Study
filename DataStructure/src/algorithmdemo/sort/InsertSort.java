package algorithmdemo.sort;

import java.util.Random;

/**
 *  插入排序
 * 
 * @author zhangke
 */
public class InsertSort {

	private static int MAX_LENGTH = 20;

	public static void main(String[] args) {

		int[] array = new int[MAX_LENGTH];
		Random random = new Random();

		System.out.println("原序列：");
		for (int i = 0; i < MAX_LENGTH; i++) {
			int nextInt = random.nextInt(100);
			array[i] = nextInt;
			System.out.print(nextInt + "\t");
		}

		InsertSort sort = new InsertSort();
		// sort.insertSort(array);
		// sort.binaryInsertSort(array);
		sort.shellSort(array);
	}

	/**
	 * 直接插入排序
	 * 
	 * 直接插入排序由两次循环决定： </br>
	 * 1、外层循环标识要比较的数值（一般从第二个数据开始） </br>
	 * 2、内层循环为待比较数值确定其最终位置 </br>
	 * 3、当需要确定第i个数据（temp）的插入数据时，每次需要和前i-1个数据进行一次比较，如果temp比array[i]小，那么array[i]需要后移一位，否则就将temp插入到array[i]后面
	 * 
	 */
	public void insertSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int temp = array[i];

			int j;
			for (j = i - 1; j >= 0; j--) {
				/*
				 * 1、array[j] > temp ,将array[j]后移一位
				 * 2、如果整个循环遍历完成，说明temp最小应该是处于array[0]
				 * 3、如果循环提前终止，那么temp大于等于array[j],那么temp就应该在array[j+1]位置
				 */
				if (array[j] > temp) {
					array[j + 1] = array[j];
				} else {
					break;
				}
			}
			array[j + 1] = temp;
		}

		sout(array, "直接插入排序：");
	}

	/**
	 * 二分插入排序
	 * 
	 * 排序的结果和直接插入排序相同，每次确定前i个序列的顺序，这里对插入的算法进行了二分法
	 * 
	 */
	public void binaryInsertSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int temp = array[i];

			int left = 0;
			int right = i - 1;
			int mid;
			while (left <= right) {
				mid = (left + right) / 2;

				if (temp > array[mid]) {
					// temp需要插入到mid之後的位置
					left = mid + 1;
				} else {
					// temp需要插入到mid之前的位置
					right = mid - 1;
				}
			}

			/*
			 * 跳出循环的条件是left > right, 此时可以确认我们要插入的位置就是left，需要将left位置后的数据都后移一位
			 * 
			 * 这里分析下为什么插入的位置时left?
			 * 
			 * 出现left>right有两种情况：(出现left>right前一步，left = right = mid)
			 * 
			 * 1、当(temp > array[mid])跳出循环时,
			 * left=mid+1；temp需要插入到mid后一位，也就是left，并让left之后的位置后移一位 </br> 2、(temp
			 * <= array[mid])跳出循环，right =
			 * mid-1；temp需要插入到mid位置，也就是left，并让mid以及之后的后移
			 */
			for (int j = i - 1; j >= left; j--) {
				array[j + 1] = array[j];
			}
			array[left] = temp;

		}

		sout(array, "二分插入排序");
	}

	/**
	 * 希尔排序
	 * 
	 * 通过设置增量因子d将序列分成d个子序列， 然后分别对每组进行直接插入排序（当d为1时，其实希尔排序就是直接插入排序） 例如： array[i],
	 * array[i+d] , array[i+2d] , array[i+3d]...为一组
	 * 
	 */
	public void shellSort(int[] array) {
		int d = array.length;

		while (true) {
			d = d / 2;

			// 对d个子序列分别做直接插入排序
			for (int i = 0; i < d; i++) {

				// 对子序列做直接插入排序
				for (int j = i + d; j < array.length; j = j + d) {
					int temp = array[j];
					int k;
					for (k = j - d; k >= 0; k = k - d) {
						if (array[k] > temp) {
							array[k + d] = array[k];
						} else {
							break;
						}
					}
					array[k + d] = temp;
				}
			}

			if (d == 1) {
				break;
			}
		}

		sout(array, "希尔排序");

	}


	public void sout(int[] array, String sortName) {
		System.out.println("\n");
		System.out.println(sortName + ":");
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + "\t");
		}
	}

}
