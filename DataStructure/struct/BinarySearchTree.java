package structdemo;

/**
 * 二叉排序树、二叉搜索树
 * 
 * @author zhangke
 *
 */
public class BinarySearchTree {

	public static void main(String[] args) {
		BinarySearchTree createTree = BinarySearchTree.createTree();

		createTree.delete(4);

		System.out.println("前序遍历：");
		createTree.preOrder(createTree.getRoot());
		System.out.println("\n\n中序遍历：");
		createTree.midOrder(createTree.getRoot());

	}

	/**
	 * 创建二叉搜索树
	 * 
	 * @return
	 */
	public static BinarySearchTree createTree() {
		BinarySearchTree tree = new BinarySearchTree();

		int[] data = { 4, 2, 1, 0, 3, 6, 5, 10, 8, 7, 9 };
		for (int i = 0; i < 11; i++) {
			tree.put(data[i]);
		}

		return tree;
	}

	/**
	 * 树的根节点
	 */
	private TreeNode root;

	public TreeNode getRoot() {
		return root;
	}

	/**
	 * 添加节点
	 * 
	 * @param data
	 */
	public void put(int data) {
		// 防止插入一样的节点
		if (get(data) != null) {
			return;
		}

		// 创建即将插入的节点
		TreeNode newNode = new TreeNode(data, null, null, null);

		// 每次添加节点都从root节点开始
		TreeNode currNode = root;

		// 创建根节点
		if (currNode == null) {
			root = newNode;
			return;
		}

		while (currNode != null) {
			// 判断插入值和当前节点值的大小
			if (newNode.data >= currNode.data) {

				/*
				 * 当currNode的值比较大时，我们需要判断当前节点的右孩子和当前值得大小
				 */
				if (currNode.rightChild == null) {
					newNode.parent = currNode;
					currNode.rightChild = newNode;
					return;
				} else {

					currNode = currNode.rightChild;
					continue;
				}
			} else {

				if (currNode.leftChild == null) {
					newNode.parent = currNode;
					currNode.leftChild = newNode;
					return;
				} else {
					currNode = currNode.leftChild;
					continue;
				}

			}
		}

	}

	/**
	 * 查找指定节点
	 * 
	 * @param node
	 */
	public TreeNode get(int data, TreeNode node) {
		if (node == null) {
			return null;
		}

		if (data == node.data) {
			return node;
		}

		if (data > node.data) {
			return get(data, node.rightChild);
		} else {
			return get(data, node.leftChild);
		}

	}

	/**
	 * 查询指定结点
	 * 
	 * @param data
	 * @return
	 */
	public TreeNode get(int data) {
		return get(data, root);
	}

	/**
	 * 删除节点
	 * 
	 */
	public boolean delete(int data) {
		/*
		 * 删除结点有以下几种情况：1、如果该结点没有孩子结点，那么直接删除（设置该结点的parent为null）
		 * 2、如果该结点只有一个孩子，那么直接使用孩子结点替换该节点即可 3、如果该结点有两个孩子，寻找后继结点替换该节点
		 */

		TreeNode deleteNode = get(data);
		if (deleteNode == null) {
			System.out.println("该结点不存在");
			// throw new IllegalArgumentException("该节点");
			return false;
		} else if (root == deleteNode) {
			return deleteRoot();
		}

		TreeNode leftChild = deleteNode.leftChild;
		TreeNode rightChild = deleteNode.rightChild;
		TreeNode parent = deleteNode.parent;

		// 1、没有孩子节点
		if (leftChild == null && rightChild == null) {

			if (deleteNode == parent.leftChild) {
				parent.leftChild = null;
			} else {
				parent.rightChild = null;
			}

			deleteNode.parent = null;

			return true;
		}

		// 2、只有一个左孩子节点
		if (leftChild != null && rightChild == null) {

			if (deleteNode == parent.leftChild) {
				parent.leftChild = deleteNode.leftChild;
			} else {
				parent.rightChild = deleteNode.leftChild;
			}
			deleteNode.parent = null;

			return true;
		}

		// 2、只有一个右孩子节点
		if (leftChild == null && rightChild != null) {

			if (deleteNode == parent.leftChild) {
				parent.leftChild = rightChild;
			} else {
				parent.rightChild = rightChild;
			}
			deleteNode.parent = null;

			return true;
		}

		// 3、存在两个孩子节点
		if (leftChild != null && rightChild != null) {
			// 获取要删除的节点的后继节点
			TreeNode nextNode = getNextNode(deleteNode);
			delete(nextNode.data);
			deleteNode.data = nextNode.data;

			return true;
		}

		return false;

	}

	/**
	 * 删除根节点
	 * 
	 * @param data
	 * @return
	 */
	private boolean deleteRoot() {
		TreeNode nextNode = getNextNode(root);
		// 1、后继节点为空，左孩子节点为跟节点
		if (nextNode == null) {
			root = root.leftChild;
			if (root.leftChild != null) {
				root.leftChild.parent = null;
			}
			return true;
		}
		
		int data = nextNode.data;
		delete(data);
		root.data = data;
		return true;
	}

	/**
	 * 获取后继节点
	 * 
	 * @param deleteNode
	 * @return
	 */
	private TreeNode getNextNode(TreeNode node) {
		if (node == null) {
			throw null;
		}

		// 获取节点的后继节点，只要获取右子树的最小节点即可
		return getTreeMinNode(node.rightChild);
	}

	/**
	 * 获取最小节点
	 * 
	 * @param node
	 * @return
	 */
	public TreeNode getTreeMinNode(TreeNode node) {
		if (node == null) {
			return null;
		}
		TreeNode minNode = node;
		while (minNode.leftChild != null) {
			minNode = minNode.leftChild;
		}

		return minNode;
	}

	/**
	 * 获取最大节点
	 * 
	 * @param node
	 * @return
	 */
	public TreeNode getTreMaxNode(TreeNode node) {
		if (node == null) {
			return null;
		}
		TreeNode maxNode = node;
		while (maxNode.rightChild != null) {
			maxNode = maxNode.rightChild;
		}

		return maxNode;
	}

	/**
	 * 获取一个树的最小结点
	 * 
	 * @param node
	 */
	public TreeNode getTreeMinNode() {
		return getTreeMinNode(root);
	}

	/**
	 * 获取一个树的最大结点
	 * 
	 * @param node
	 */
	public TreeNode getTreMaxNode() {
		return getTreMaxNode(root);
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
	 * 树的节点
	 */
	public class TreeNode {

		public int data;
		public TreeNode parent;
		public TreeNode leftChild;
		public TreeNode rightChild;

		public TreeNode(int data) {
			this.data = data;
		}

		public TreeNode(int data, TreeNode parent, TreeNode leftChild, TreeNode rightChild) {
			this.data = data;
			this.parent = parent;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}
	}

}
