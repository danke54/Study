package structdemo;

/**
 * 
 * 二叉树
 * 
 * @author zhangke
 */
public class BinaryTree {

	public static void main(String[] args) {
		BinaryTree binaryTree = new BinaryTree();
		binaryTree.createBinaryTree();
		System.out.println("height：" + binaryTree.getHeight());
		System.out.println("size：" + binaryTree.getSize());

		binaryTree.preOrder(binaryTree.root);
		binaryTree.midOrder(binaryTree.root);
		binaryTree.postOrder(binaryTree.root);
	}

	/**
	 * 构建二叉树
	 * 
	 *      A
	 * 
	 *   B     C
	 * 
	 * D   E     F
	 */
	public void createBinaryTree() {
		TreeNode nodeB = new TreeNode(2, "B");
		TreeNode nodeC = new TreeNode(3, "C");
		TreeNode nodeD = new TreeNode(4, "D");
		TreeNode nodeE = new TreeNode(5, "E");
		TreeNode nodeF = new TreeNode(6, "F");
		root.leftChild = nodeB;
		root.rightChild = nodeC;
		nodeB.leftChild = nodeD;
		nodeB.rightChild = nodeE;
		nodeC.rightChild = nodeF;
	}
	
	
	/**
	 * 根节点
	 */
	private TreeNode root = null;

	public BinaryTree() {
		root = new TreeNode(1, "A");
	}

	/**
	 * 获取树的深度
	 * 
	 * @return
	 */
	public int getHeight() {
		return getHeight(root);
	}

	/**
	 * 获取指定结点的深度
	 */
	private int getHeight(TreeNode node) {
		if (node == null) {
			return 0;
		}

		// 每当一个结点存在子结点时，Height就加一个
		int i = getHeight(node.leftChild);
		int j = getHeight(node.rightChild);
		return (i > j) ? (i + 1) : (j + 1);
	}

	/**
	 * 获取结点个数
	 * 
	 * @return
	 */
	public int getSize() {
		return getSize(root);
	}

	/**
	 * 获取树的节点个数
	 * 
	 * @return
	 */
	private int getSize(TreeNode node) {
		if (node == null) {
			return 0;
		}
		return 1 + getSize(node.leftChild) + getSize(node.rightChild);
	}

	/**
	 * 前序遍历
	 * 
	 * parent->left->right
	 */
	public void preOrder(TreeNode node) {
		if (node == null) {
			return;
		}

		System.out.print(node.data + "\t");
		preOrder(node.leftChild);
		preOrder(node.rightChild);
	}

	/**
	 * 中序遍历
	 * 
	 * left->parent->right
	 */
	public void midOrder(TreeNode node) {
		if (node == null) {
			return;
		}

		midOrder(node.leftChild);
		System.out.print(node.data + "\t");
		midOrder(node.rightChild);
	}

	/**
	 * 后序遍历
	 * 
	 * left->right->parent
	 */
	public void postOrder(TreeNode node) {
		if (node == null) {
			return;
		}

		postOrder(node.leftChild);
		postOrder(node.rightChild);
		System.out.print(node.data + "\t");
	}

	/**
	 * 定义树的结点
	 *
	 * @author zhangke
	 */
	public class TreeNode {

		/**
		 * 下标
		 */
		private int index;
		/**
		 * 数据
		 */
		private String data;
		/**
		 * 左孩子
		 */
		private TreeNode leftChild;
		/**
		 * 右孩子
		 */
		private TreeNode rightChild;

		public TreeNode(int index, String data) {
			this.index = index;
			this.data = data;
		}
	}

}
