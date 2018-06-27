package sort;

import java.util.Random;

/**
 * Created by zhangke on 2018/6/27.
 */

public abstract class Sort {

    public static int MAX_LENGTH = 10;

    public void execSort(String sortName) {
        int[] array = createArray();
        sort(array);
        sout(array, sortName);
    }

    /**
     * 排序逻辑
     *
     * @param array
     */
    public abstract int[] sort(int[] array);

    /**
     * 创建数组
     *
     * @return
     */
    public int[] createArray() {
        int[] array = new int[MAX_LENGTH];
        Random random = new Random();

        System.out.println("原序列：");
        for (int i = 0; i < MAX_LENGTH; i++) {
            int nextInt = random.nextInt(100);
            array[i] = nextInt;
            System.out.print(nextInt + "\t");
        }
        return array;
    }

    /**
     * 打印数组
     *
     * @param array
     * @param sortName
     */
    public void sout(int[] array, String sortName) {
        System.out.println("\n");
        System.out.println(sortName + ":");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
    }
}
