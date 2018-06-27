package sort;

/**
 *  插入排序：将元素插入到一个有序数组的指定位置，形成一个新的有序数组
 * <p>
 * 时间复杂度：平均情况：O(n^2)
 */
public class InsertSort extends Sort {

    public int insertType = 0;

    /**
     * @param insertType 0:直接插入   1：二分插入
     */
    public InsertSort(int insertType) {
        this.insertType = insertType;
        sortName = "插入排序";
    }

    @Override
    public int[] sort(int[] array) {
        if (insertType == 1) {
            return binaryInsertSort(array);
        }
        return insertSort(array);
    }

    /**
     * 直接插入排序
     * <p>
     * 算法步骤：
     * 1、第一步，将第一个元素作为有序数组元素，然后将第二个元素插入到该有序数组中，形成一个新的有序数组
     * 2、第二部，将第三个元素插入到前两个元素形成的有序素组总，形成新的有序数组
     * 3、。。。。
     */
    public int[] insertSort(int[] array) {
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

        return array;
    }


    /**
     * 二分插入排序
     * <p>
     * 排序的结果和直接插入排序相同，每次确定前i个序列的顺序，这里对插入的算法进行了二分法
     */
    public int[] binaryInsertSort(int[] array) {
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

        return array;
    }

    /**
     * 希尔排序
     * <p>
     * 通过设置增量因子d将序列分成d个子序列， 然后分别对每组进行直接插入排序（当d为1时，其实希尔排序就是直接插入排序） 例如： array[i],
     * array[i+d] , array[i+2d] , array[i+3d]...为一组
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
