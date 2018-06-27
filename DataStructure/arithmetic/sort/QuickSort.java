package sort;

/**
 * 快速排序：快速排序使用分治法策略来把一个数组分为两个子数组，通过递归完成排序
 * <p>
 * 时间复杂度：平均情况：Ο(nlogn)   最坏情况：Ο(n2)
 * <p>
 * 算法步骤：
 * 1、从数组中选定一个元素，作为基数
 * 2、将比基数小的元素放置到基数左边，比基数大的放在右边
 * 3、获取基数位置后，在将基数左边和右边的数组分别递归运行
 */
public class QuickSort extends Sort {

    public QuickSort() {
        sortName = "快速排序";
    }

    @Override
    public int[] sort(int[] array) {
        return quickSort(array, 0, array.length - 1);
    }

    private int[] quickSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = getMiddle(array, left, right);
            quickSort(array, 0, middle - 1);
            quickSort(array, middle + 1, right);
        }
        return array;
    }

    /**
     * 获取基数的位置，从该位置将序列分成左右两部分，其中左边都小于基数，右边都大于基数
     */
    private int getMiddle(int[] array, int left, int right) {
        int temp = array[left]; // 选定一个基数
        while (left < right) {
            /*
             *  从右边开始找到比基数小的元素
             *  跳出while循环，表示找到了比基数temp小的元素，将该元素放到left下标位置
             */
            while (left < right && array[right] >= temp) {
                right--;
            }
            array[left] = array[right];

            /*
             * 从左边开始找比基数大的元素
             * 跳出while循环，表示找到了比基数temp大的元素，将该元素交换到基数temp位置
             */
            while (left < right && array[left] <= temp) {
                left++;
            }
            array[right] = array[left];
        }

        // 当left = right时，此时所有比基数小的元素都被交换到基数左边，所有比技术大的元素都被交换到基数右边
        array[left] = temp;
        return left;
    }

}
