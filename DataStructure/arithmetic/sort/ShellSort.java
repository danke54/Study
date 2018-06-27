package sort;

/**
 * 希尔排序
 * <p>
 * 通过设置增量因子d将序列分成d个子序列， 然后分别对每组进行直接插入排序（当d为1时，其实希尔排序就是直接插入排序） 例如： array[i],
 * array[i+d] , array[i+2d] , array[i+3d]...为一组
 */
public class ShellSort extends Sort {

    @Override
    public int[] sort(int[] array) {

        return shellSort(array);
    }

    private int[] shellSort(int[] array) {
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

        return array;

    }


}
