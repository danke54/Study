package algorithmdemo;

public class HannotaTest {

	public static void main(String[] args) {
//		new HannotaTest().hannota(3,'A', 'B', 'C');
		System.out.println(new HannotaTest().euclidean(36, 24));
	}

	private int count;

	/**
	 * 
	 * @param n
	 * @param from
	 *            A柱子
	 * @param relyon
	 *            B柱子
	 * @param to
	 *            C柱子
	 */
	public void hannota(int n, char from, char relyon, char to) {
		if (n == 1) {
			move(n, from, to);
		} else {
			
			/**
			 * 1、n-1个盘子，从              A -> [C] ->B
			 * 2、第 n 个盘子，从             A -> C
			 * 3、n-1个盘子此时全部在B，那么从 B -> [A] ->C
			 */
			hannota(n - 1, from, to, relyon);
			move(n, from, to);
			hannota(n - 1, relyon, from, to);
		}
	}

	private void move(int n, char from, char to) {
		System.out.println("第" + ++count + "步: " + from + " ----> " + to);
	}
	
	/**
	 * 欧几米德算法（辗转相除法）
	 */
	public int euclidean(int large, int small){
		if(small == 0){
			return large;
			
		}else{
			return euclidean(small, large%small);
		}
	}
}
