package sort;

import java.util.Random;

/**
 * Created by zhangke on 2018/6/27.
 */

public abstract class Sort {

    public static final int BUBULE_SORT = 0;
    public static final int SELECT_SORT = 1;
    public static final int QUICK_SORT = 2;
    public static final int INSERT_SORT = 3;
    public static final int MERGE_SORT = 4;
    public static final int SHELL_SORT = 5;
    public static final int HEAP_SORT = 6;
    public static final int RADIX_SORT = 7;

    private final int MAX_LENGTH = 20;
    protected String sortName;

    /**
     * @param type
     */
    public static void execSort(int type) {
        Sort sort = null;
        switch (type) {
            case BUBULE_SORT:
                sort = new BubbleSort();
                break;
            case SELECT_SORT:
                sort = new SelectSort();
                break;
            case QUICK_SORT:
                sort = new QuickSort();
                break;
            case INSERT_SORT:
                sort = new InsertSort(0);
                break;
            case MERGE_SORT:
                sort = new MergeSort();
                break;
            case SHELL_SORT:
                sort = new ShellSort();
                break;
            case HEAP_SORT:
                sort = new HeapSort();
                break;
            case RADIX_SORT:
                sort = new RadixSort();
                break;

        }
        sort.execSort();
    }


    /**
     * 执行算法
     */
    private void execSort() {
        int[] array = createArray();
        sort(array);
        printArray(array);
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
    private int[] createArray() {
        int[] array = new int[MAX_LENGTH];
        Random random = new Random();

        System.out.println(sortName);
        System.out.println("原数组：");
        for (int i = 0; i < MAX_LENGTH; i++) {
            int nextInt = random.nextInt(100);
            array[i] = nextInt;
            System.out.print(nextInt + "\t");
        }
        System.out.println("");
        return array;
    }

    /**
     * 打印数组
     *
     * @param array
     */
    public void printArray(int[] array) {
        System.out.println("排序后：");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println("");
        System.out.println("---------------------------------------");
        System.out.println("");

    }
}
