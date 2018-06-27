package sort;

/**
 * 归并排序
 * <p>
 * 1、将一个序列平均分为两个子序列，对这两个子序列进行排序 2、将两个子序列排序后在合并成同样一个序列，
 * <p>
 * 通过递归的方式，重复上面的过程，完成整个排序
 */
public class MergeSort extends Sort {

    @Override
    public int[] sort(int[] array) {
        return mergeSort(array, 0, array.length - 1);
    }

    private int[] mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            // 合并
            merge(array, left, middle, right);
        }
        return array;
    }

    /**
     * 合并两个序列
     *
     * @param array
     * @param left
     * @param middle
     * @param right
     */
    private void merge(int[] array, int left, int middle, int right) {
        int index = left;

        // 创建一个临时数组，用于存放合并是的序列
        int[] tmpArray = new int[array.length];
        int tmpIndex = left;
        int rightCursor = middle + 1;

        //将序列的一边完整放入到数组中
        while (left <= middle && rightCursor <= right) {

            if (array[left] <= array[rightCursor]) {
                // 如果左边的值小，那么左边的先加入到临时数组
                tmpArray[tmpIndex++] = array[left++];
            } else {
                tmpArray[tmpIndex++] = array[rightCursor++];
            }
        }

        // 如果左边序列还存在元素没有放入，那么将剩余的放入
        while (left <= middle) {
            tmpArray[tmpIndex++] = array[left++];
        }

        // 如果右边序列还存在元素没有放入，那么将剩余的放入
        while (rightCursor <= right) {
            tmpArray[tmpIndex] = array[rightCursor++];
        }

        // 将左右两个序列按照顺序合并到临时数组后，在将临时数组的值赋值给原数组中，这样对原数组该部分的序列完成了排序
        while (index <= right) {
            array[index] = tmpArray[index++];
        }
    }

}
