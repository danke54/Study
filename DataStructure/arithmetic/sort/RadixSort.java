package sort;

/**
 * 基数排序
 *
 * @author zhangke
 */
public class RadixSort extends Sort {
    public RadixSort() {
        sortName = "基数排序";
    }

    @Override
    public int[] sort(int[] array) {

        return radixSort(array);
    }

    /**
     * 基数排序
     */
    public int[] radixSort(int[] array) {
        int maxLength = getMaxLength(array);

        // 创建一个二维数组
        int[][] tubeArray = new int[10][array.length];

        // 位数
        int n = 1;

        //记录在该基数下有几个数字
        int[] baseArray = new int[10];

        // 最大数为多少位，就循环多少次
        while (maxLength > 0) {

            // 将所有数字都放到指定的位数对应的数组下
            for (int i = 0; i < array.length; i++) {

                // 获取指定位数上的数字
                int position = getValueAtPosition(array[i], n);

                tubeArray[position][baseArray[position]] = array[i];
                baseArray[position]++;
            }


            // 将二维数组的数据重新生成一个序列
            int k = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < baseArray[i]; j++) {
                    array[k] = tubeArray[i][j];
                    k++;
                }
                baseArray[i] = 0;
            }

            k = 0;
            n *= 10;
            maxLength--;
        }

        return array;

    }

    /**
     * 获取一个数在指定位数上的数字
     *
     * @param value
     * @param position 1 10 100
     * @return
     */
    private int getValueAtPosition(int value, int position) {
        /*
         * 计算百位的数字: 155/100 = 1 ; 1%10 = 1 </br>155/10 = 15 ; 15%10 = 5
		 */
        value = (value / position) % 10;

        return value;
    }

    /**
     * 获取最大数的位数
     *
     * @param array
     */
    private int getMaxLength(int[] array) {
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (maxValue < array[i]) {
                maxValue = array[i];
            }
        }

        int length = 0;
        while (maxValue > 0) {
            maxValue = maxValue / 10;
            length++;
        }

        return length;
    }
}
