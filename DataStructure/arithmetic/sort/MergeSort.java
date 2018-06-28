package sort;

/**
 * 归并排序
 *
 * 时间复杂度：O(n*logn)
 *
 * 算法步骤：
 * 1、将一个序列平均分为两个子序列，对这两个子序列进行排序
 * 2、将两个子序列排序后在合并成同样一个序列，
 * 3、通过递归的方式，重复上面的过程，完成整个排序
 *
 */
public class MergeSort extends Sort {
    public MergeSort() {
        sortName = "归并排序";
    }

    @Override
    public int[] sort(int[] array) {

        return mergeSort(array, 0, array.length - 1);
    }

    public int[] mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            merge(array, left, middle, right);
        }
        return array;
    }

    private void merge(int[] array, int left, int middle, int right) {
        int[] tmpArray = new int[array.length];
        int tmpIndex = left;

        // 两个合并数组的起始位置下标
        int firstCursor = left;
        int secondCursor = middle + 1;

        // 按照大小顺序将元素添加到临时数组中
        while (firstCursor <= middle && secondCursor <= right) {
            if (array[firstCursor] < array[secondCursor]) {
                tmpArray[tmpIndex++] = array[firstCursor++];
            } else {
                tmpArray[tmpIndex++] = array[secondCursor++];
            }
        }

        // 第一个数组数据没有添加完全
        while (firstCursor <= middle) {
            tmpArray[tmpIndex++] = array[firstCursor++];
        }

        // 第二个数组没有添加完全
        while (secondCursor <= right) {
            tmpArray[tmpIndex++] = array[secondCursor++];
        }

        // 两个数组按照顺组添加到临时数组后，将原来数组该段重新赋值为新数组
        while (left <= right) {
            array[left] = tmpArray[left];
            left++;
        }

    }


}
