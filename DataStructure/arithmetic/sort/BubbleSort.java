package sort;

/**
 * 冒泡排序：每次比较相邻元素，将元素值大的交换到右边。
 * <p>
 * 时间复杂度：平均情况：O(n^2)
 * <p>
 * 算法步骤：
 * 第1趟，在待排序记录r[1]~r[n]中每每比较相邻两个元素，将较大的元素交换到r[n]；
 * 第2趟，在待排序记录r[1]~r[n-1]中每每比较相邻两个元素，将较大的元素交换到r[n-1]；
 */
public class BubbleSort extends Sort {

    public BubbleSort() {
        sortName = "冒泡排序";
    }

    @Override
    public int[] sort(int[] array) {
        return bubbleSort(array);
    }

    private int[] bubbleSort(int[] array) {
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
        return array;
    }

}
