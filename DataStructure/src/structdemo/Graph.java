/*--------------------------------------------------
 * Copyright (C) 2015 The Android Y-CarPlus Project
 *                http://www.yesway.cn/
 * 创建时间：2017年5月17日
 * 内容说明：
 * 
 * 编号                日期                     担当者             内容                  
 * -------------------------------------------------
 *
 * -------------------------------------------------- */
package structdemo;

import java.util.LinkedList;

/**
 *
 * 图
 *
 * @author zhangke
 */
public class Graph {
	/**
	 * 两个顶点之间没有连接，在矩阵中值为MAX_WEIGHT
	 */
	private static final int MAX_WEIGHT = 1000;

	/**
	 * 顶点数量
	 */
	private int vertexSize;
	/**
	 * 顶点集合
	 */
	private int[] vertexs;
	/**
	 * 边集合
	 */
	private int[][] edgeMatreix;

	public Graph(int vertexSize) {
		this.vertexSize = vertexSize;
		vertexs = new int[vertexSize];
		edgeMatreix = new int[vertexSize][vertexSize];

		// 初始化
		for (int i = 0; i < vertexSize; i++) {
			vertexs[i] = i;
		}
	}

	/**
	 * 获取顶点的出度 （出度：顶点index到其他顶点）
	 * 
	 * @param index
	 */
	public int getOutDegree(int index) {
		int degree = 0;
		for (int i = 0; i < edgeMatreix[index].length; i++) {
			int weight = edgeMatreix[index][i];
			if (weight != 0 && weight != MAX_WEIGHT) {
				degree++;
			}
		}
		return degree;
	}

	/**
	 * 获取顶点的入度 （入度：其他顶点到顶点index）
	 * 
	 * @param index
	 */
	public int getInDegree(int index) {
		int degree = 0;
		for (int i = 0; i < vertexSize; i++) {
			int weight = edgeMatreix[i][index];
			if (weight != 0 && weight != MAX_WEIGHT) {
				degree++;
			}
		}

		return degree;
	}

	private boolean[] searched;

	/**
	 * 深度优先搜索 1、从顶点v开始搜索，该顶点就为第一个搜索到的顶点 2、获取顶点v的第一个邻接点，然后在对邻接点进行搜索（同样的操作），
	 * 3、当对v的邻接点遍历完成，在获取v的下一个邻接点，执行同样的操作，直到所有的顶点被遍历
	 */
	public void depthFirstSearch() {
		searched = new boolean[vertexSize];
		// 遍历每个顶点，直到所有顶点都被遍历
		for (int i = 0; i < vertexSize; i++) {
			if (!searched[i]) {
				sout(i);
				depthFirstSearch(i);
			}

		}
	}

	/**
	 * 从index结点开始遍历
	 * 
	 * @param index
	 */
	private void depthFirstSearch(int index) {
		searched[index] = true;
		int w = getFirstNeighbor(index);
		while (w != -1) {
			// 该邻接点没有被遍历，对该结点继续执行深度优先遍历
			if (!searched[w]) {
				sout(w);
				depthFirstSearch(w);
			}
			// 如果第一个邻接点遍历完成，那么获得index的下一个邻接结点继续开始遍历
			w = getNextNeighbor(index, w);
		}

	}

	/**
	 * 广度优先算法
	 * 
	 * 1、从顶点index开始，作为第一层开始遍历 2、遍历完成遍历第二层
	 */
	public void broadFirstSearch() {
		searched = new boolean[vertexSize];
		// 遍历每个顶点，直到所有顶点都被遍历
		for (int i = 0; i < vertexSize; i++) {
			if (!searched[i]) {

				sout(i);
				searched[i] = true;

				broadFirstSearch(i);
			}

		}
	}

	/**
	 * 广度优先算法 遍历结点i的下一层所有结点
	 */
	private void broadFirstSearch(int i) {
		LinkedList<Integer> queue = new LinkedList<Integer>();

		queue.add(i);
		while (!queue.isEmpty()) {
			int v = queue.removeFirst().intValue();
			int w = getFirstNeighbor(v);
			while (w != -1) {
				if (!searched[w]) {

					sout(w);
					searched[w] = true;
					// 遍历到结点后把结点加到队列中，以便遍历该邻接点的所有结点
					queue.add(w);
				}
				w = getNextNeighbor(v, w);
			}

		}

	}

	/**
	 * 
	 * 根据当前邻接点获取下一个邻接点
	 * 
	 * @param index
	 * @param w
	 */
	private int getNextNeighbor(int index, int w) {
		for (int i = w + 1; i < vertexSize; i++) {
			int weigth = edgeMatreix[index][i];
			if (weigth > 0 && weigth < MAX_WEIGHT) {
				return i;
			}
		}
		return -1;

	}

	/**
	 * 获取第一个邻接点
	 * 
	 * @param index
	 * @return 不存在邻接点，返回-1
	 */
	private int getFirstNeighbor(int index) {
		for (int i = 0; i < vertexSize; i++) {
			int weigth = edgeMatreix[index][i];
			if (weigth > 0 && weigth < MAX_WEIGHT) {
				return i;
			}
		}

		return -1;
	}

	private void sout(int index) {
		System.out.println("遍历到了顶点：" + index);
	}

	public static void main(String[] args) {
		Graph graph = new Graph(9);

		int[] a0 = new int[] { 0, 10, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT };
		int[] a1 = new int[] { 10, 0, 18, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, MAX_WEIGHT, 12 };
		int[] a2 = new int[] { MAX_WEIGHT, MAX_WEIGHT, 0, 22, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 8 };
		int[] a3 = new int[] { MAX_WEIGHT, MAX_WEIGHT, 22, 0, 20, MAX_WEIGHT, MAX_WEIGHT, 16, 21 };
		int[] a4 = new int[] { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 20, 0, 26, MAX_WEIGHT, 7, MAX_WEIGHT };
		int[] a5 = new int[] { 11, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 26, 0, 17, MAX_WEIGHT, MAX_WEIGHT };
		int[] a6 = new int[] { MAX_WEIGHT, 16, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 17, 0, 19, MAX_WEIGHT };
		int[] a7 = new int[] { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 16, 7, MAX_WEIGHT, 19, 0, MAX_WEIGHT };
		int[] a8 = new int[] { MAX_WEIGHT, 12, 8, 21, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 0 };

		graph.edgeMatreix[0] = a0;
		graph.edgeMatreix[1] = a1;
		graph.edgeMatreix[2] = a2;
		graph.edgeMatreix[3] = a3;
		graph.edgeMatreix[4] = a4;
		graph.edgeMatreix[5] = a5;
		graph.edgeMatreix[6] = a6;
		graph.edgeMatreix[7] = a7;
		graph.edgeMatreix[8] = a8;

		// graph.depthFirstSearch();
		graph.broadFirstSearch();
	}

}
