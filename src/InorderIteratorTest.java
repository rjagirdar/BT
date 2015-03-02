import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.ietf.jgss.Oid;

enum IteratorType{
	INORDER, PREORDER, POSTORDER, LEVELORDER
}


public class InorderIteratorTest {

	public static void main(String[] args) {
		
		int in[] = {4,8,10,12,14,20,22};
		int pre[]= {20,8,4,12,10,14,22};
		int level[] = {20,8,22,4,12,10,14};
		BinarySearchTree tree = new BinarySearchTree(in, pre, IteratorType.INORDER, IteratorType.PREORDER);
		//BinarySearchTree tree=getTree();
		
		System.out.println("Height of the Tree is " + tree.height());
		System.out.println("Number of Leaf Nodes is " + tree.LeafNodeSize());
		System.out.println("Is Binary Search Tree " + tree.isBST());
		System.out.println("Tree is height balanced "+tree.isHeightBalanced());
		System.out.println("LCA of 10 and 14 is " + tree.LCA(10, 14).toString());
		System.out.println("5th smallest element is " +tree.smallest(5));
		Iterator<Node> it=tree.iterator(IteratorType.LEVELORDER);
		while(it.hasNext()){
			System.out.println(it.next().data);
		}
		System.out.println();
		for(LinkedList<Node> levelList : tree.spiralLevelOrder()){
			for(Node current : levelList){
				System.out.print(current.data +" ");
			}
			System.out.println();
		}
		
		System.out.println("==========================================================");
		tree.printLeftView();
		System.out.println("==========================================================");
		System.out.println("Are Leaves at Same Level "+ tree.areLeavesAtSameLevel());
		
		System.out.println("==========================================================");
		System.out.println("Vertical Order of the Tree is ");
		TreeMap<Integer, List<Node>> verticalMap = tree.getVerticalOrder();
		for(Map.Entry<Integer, List<Node>> entry : verticalMap.entrySet()){
			for(Node tempNode : entry.getValue())
				System.out.print(tempNode.data+" ");
			System.out.println();
		}
		System.out.println("==========================================================");
		System.out.println("Right View of the Tree is ");
		tree.printRightView();
		System.out.println("==========================================================");
		
		tree = getTree();
		
		
		/*System.out.println();
		BinarySearchTree newTree=tree.getMirrorTree();
		it=newTree.iterator(IteratorType.LEVELORDER);
		while(it.hasNext()){
			System.out.println(it.next().data);
		}
		*/
		
		/*tree.ConvertToSumTree();
		System.out.println();
		for(LinkedList<Node> levelList : tree.spiralLevelOrder()){
			for(Node current : levelList){
				System.out.print(current.data +" ");
			}
			System.out.println();
		}
		System.out.println("Is Binary Search Tree " + tree.isBST());*/
		
		BinarySearchTree newTree = new BinarySearchTree();
		newTree.insert(1);
		newTree.insert(2);
		newTree.insert(3);
		newTree.insert(4);
		newTree.insert(5);
		newTree.insert(6);
		newTree.insert(7);
		
		it=newTree.iterator(IteratorType.LEVELORDER);
		while(it.hasNext()){
			System.out.println(it.next().data);
		}
		System.out.println();
		
		

	}
	
	public static BinarySearchTree getTree(){
		Node root= new Node(1);
		root.left = new Node(2);
		root.right=new Node(3);
		root.left.left=new Node(4);
		root.left.right=new Node(5);
		root.right.left=new Node(6);
		root.right.right =new Node(7);
		//root.right.right.right=new Node(8);
		//root.right.right.right.right=new Node(9);
		BinarySearchTree tree=new BinarySearchTree(root);
		return tree;
	}

}

class Node {

	Node left;
	int data;
	Node right;
	int height;

	public Node(int data){
		this.data=data;
		left=null;
		right=null;
		this.height=1;
	}
	
	@Override
	public String toString(){
		return String.format("" + this.data);
	}
}





class BinarySearchTree {

	private Node root;
	private Node current;
	private int currentLevel;
	private int leavesLevel;
	private IteratorFactory factory;
	private int preIndex;
	private TreeMap<Integer, List<Node>> verticalOrder = new TreeMap<Integer, List<Node>>();
	
	
	public TreeMap<Integer, List<Node>> getVerticalOrder(){
		getVerticalOrderUtil(this.root, 0);
		return verticalOrder;
	}
	
	private void getVerticalOrderUtil(Node current, int currentDistance){
		if(current == null)
			return;
		if(verticalOrder.get(currentDistance)== null)
			verticalOrder.put(currentDistance, new ArrayList<Node>());
		verticalOrder.get(currentDistance).add(current);
		getVerticalOrderUtil(current.left, currentDistance-1);
		getVerticalOrderUtil(current.right, currentDistance+1);
	}
	
	public Iterator<Node> iterator(IteratorType type, Object... args){
		return this.factory.getiterator(type);
	}
	
	public boolean isBST(){
		return isBSTUtil(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public int LeafNodeSize(){
		return LeafNodeCount(this.root);
	}
	
	public void ConvertToSumTree(){
		ConvertToSumTreeUtil(this.root);
	}
	
	public boolean isHeightBalanced(){
		return isHeightBalancedUtil(root);
	}
	
	public Node LCA(int n1, int n2){
		return LCAUtil(root, n1, n2);
	}
	
	public void printLeftView(){
		printLeftViewUtil(root, 0);
	}
	
	public void printRightView(){
		this.currentLevel=0;
		printRightViewUtil(root, this.currentLevel);
	}
	
	public boolean areLeavesAtSameLevel(){
		this.leavesLevel=Integer.MIN_VALUE;
		return this.areLeavesAtSameLevelUtil(root, 0);
		
	}
	
	private boolean areLeavesAtSameLevelUtil(Node current, int currentLevel){
		if(current == null)
			return true;
		if(current.left == null && current.right == null){
			if(this.leavesLevel == Integer.MIN_VALUE){
				this.leavesLevel = currentLevel;
				return true;
			}
			else
				return this.leavesLevel == currentLevel;
		}
		else
			return areLeavesAtSameLevelUtil(current.left, currentLevel+1) && areLeavesAtSameLevelUtil(current.right, currentLevel+1);
	}
	
	public int size(){
		return sizeUtil(root);
	}
	
	public BinarySearchTree getMirrorTree(){
		Node newRoot=getMirrorTreeUtil(root);
		return new BinarySearchTree(newRoot);
	}
	
	public Node getMirrorTreeUtil(Node current){
		if(current == null){
			return null;
		}
		Node tmp=getMirrorTreeUtil(current.left);
		current.left=current.right;
		current.right=tmp;
		return current;
	}
	
	private void printLeftViewUtil(Node current, int level){
		if(current == null)
			return;
		if(level == this.currentLevel){
			System.out.println(current.data);
			this.currentLevel+=1;
		}
		
		printLeftViewUtil(current.left, level+1);
		printLeftViewUtil(current.right,level+1);
	}
	
	private void printRightViewUtil(Node current, int level){
		if(current == null)
			return;
		if(level == this.currentLevel){
			System.out.println(current.data);
			this.currentLevel+=1;
		}
		printRightViewUtil(current.right, level+1);
		printRightViewUtil(current.left, level+1);
		
	}
	
	public int smallest(){
		Node current=root;
		while(current.left != null)
			current=current.left;
		return current.data;
	}
	
	public int smallest(int k){
		Node current = root;
		Node next = null;
		Stack<Node> stack = new Stack<Node>();
		if(k > this.size())
			return -1;
		for(int i=0; i < k; i++){
			while(current!=null){
				stack.push(current);
				current=current.left;
			}
			if(stack.isEmpty() && current == null)
				break;
			else{
				next=stack.pop();
				current=next;
				current=current.right;
			}
		}
		
		return next.data;
	}
	
	private Node LCAUtil(Node current, int n1, int n2){
		if(current==null)
			return null;
		if(current.data < n1 && current.data < n2)
			return LCAUtil(current.right, n1, n2);
		if(current.data > n1 && current.data > n2)
			return LCAUtil(current.left, n1, n2);
		return current;
	}
	
	private boolean isBSTUtil(Node current, int lower, int upper){
		if(current == null)
			return true;
		if(current.data>lower && current.data<upper)
			return isBSTUtil(current.left, lower, current.data) && isBSTUtil(current.right, current.data, upper);
		else
			return false;
	}
	
	private boolean isHeightBalancedUtil(Node current){
		if(current==null)
			return true;
		int leftHeight=getHeight(current.left);
		int rightHeight=getHeight(current.right);
		
		if(Math.abs(leftHeight-rightHeight) <=1 && isHeightBalancedUtil(current.left) && isHeightBalancedUtil(current.right))
			return true;
		else
			return false;
		
	}
	
	private int getHeight(Node current){
		if(current==null)
			return 0;
		else
			return 1+ Math.max(getHeight(current.left), getHeight(current.right));
	}
	
	public int height(){
		if(root== null)
			return 0;
		Queue<Node> queue=new LinkedList<Node>();
		queue.add(root);
		int height=0;
		while(!queue.isEmpty()){
			int levelSize=queue.size();
			while(levelSize!=0){
				Node current=queue.poll();
				if(current.left!=null)
					queue.add(current.left);
				if(current.right!=null)
					queue.add(current.right);
				levelSize--;
			}
			height+=1;
		}
		return height;
	}
	
	private int sizeUtil(Node current){
		if(current == null)
			return 0;
		return 1+sizeUtil(current.left)+sizeUtil(current.right);
	}
	
	private int LeafNodeCount(Node current){
		if(current == null)
			return 0;
		if(current.left == null && current.right == null)
			return 1;
		else
			return LeafNodeCount(current.left) + LeafNodeCount(current.right);
			
	}
	
	private int ConvertToSumTreeUtil(Node current){
		int tmpData = current.data;
		int left=0, right=0;
		if(!(current.left == null && current.right == null)){
			
			if(current.left!=null)
				left=ConvertToSumTreeUtil(current.left);
			if(current.right!=null)
				right=ConvertToSumTreeUtil(current.right);
			current.data=left + right;
		}
		return tmpData;
	}
	
	public ArrayList<LinkedList<Node>> spiralLevelOrder(){
		ArrayList<LinkedList<Node>> spiralList=new ArrayList<LinkedList<Node>>();
		if(root== null)
			return null;
		Deque<Node> queue=new LinkedList<Node>();
		
		Deque<Node> childQueue=new LinkedList<Node>();
		queue.addFirst(root);
		int level=0;
		while(!queue.isEmpty()){
			int levelSize=queue.size();
			LinkedList<Node> levelList=new LinkedList<Node>();
			levelList.addAll(queue);
			spiralList.add(levelList);
			while(levelSize!=0){
				Node current;
				if(level%2==0){
					current=queue.poll();
					if(current.right!=null)
						childQueue.add(current.right);
					if(current.left!=null)
						childQueue.add(current.left);
				}
				else{
					current=queue.pollLast();
					if(current.left!=null)
						childQueue.add(current.left);
					if(current.right!=null)
						childQueue.add(current.right);
				}
				levelSize--;
			}
			if(!childQueue.isEmpty()){
				queue.addAll(childQueue);
				childQueue.clear();
			}
			
			level=level+1;
		}
		return spiralList;
	}

	
	
	public BinarySearchTree(Node root){
		this.root =root;
		factory = new IteratorFactory();
		this.currentLevel=0;
	}

	public BinarySearchTree(){
		root=null;
		this.currentLevel=0;
		factory = new IteratorFactory();
	}
	
	public BinarySearchTree(int[] o1, int[] o2, IteratorType o1Type, IteratorType o2Type){
		factory = new IteratorFactory();
		if(o1Type==IteratorType.INORDER && o2Type== IteratorType.PREORDER){
			this.preIndex=0;
			this.root = buildTreeINPRE(o1, o2, 0, o1.length-1);
		}
		else if(o1Type==IteratorType.INORDER && o2Type== IteratorType.LEVELORDER){
			this.root = buildTreeINLEVEL(o1, o2, 0, o1.length-1);
		}
	}
	
	private int search(int[] arr, int start, int end, int val){
		for(int i=start; i<=end; i++){
			if(arr[i] == val)
				return i;
		}
		return -1;
	}
	
	public Node buildTreeINPRE(int[] in, int[] pre, int start, int end){
		
		if(start>end)
			return null;
		
		Node root = new Node(pre[preIndex++]);
		if(start == end)
			return root;
		int index = search(in, start, end, root.data);
		
		root.left = buildTreeINPRE(in, pre, start, index-1);
		root.right= buildTreeINPRE(in, pre, index+1, end);
		return root;
		
	}
	private int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}

	private int[] extractValues(int[] in, int[] level, int start, int end){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<level.length; i++){
			if(search(in, start, end, level[i]) != -1)
				list.add(level[i]);
		}
		return toIntArray(list);
	}
	
	public Node buildTreeINLEVEL(int[] in, int[] level, int start, int end){
	
		Node root = new Node(level[0]);
		if(start == end)
			return root;
		int index = search(in, start, end, root.data);
		int[] leftVals = extractValues(in, level, start, index-1);
		int[] rightVals = extractValues(in, level, index+1, end);
		
		root.left = buildTreeINLEVEL(in, leftVals, start, index-1);
		root.right = buildTreeINLEVEL(in, rightVals, index+1, end);
		
		return root;
	}
	
	
	
	private class IteratorFactory{
		public Iterator<Node> getiterator(IteratorType type, Object...args){
			Iterator<Node> it=null;
			switch(type){
			case INORDER:  it= new InorderIterator();
							break;
			case PREORDER: it=  new PreorderIterator();
							break;
			case POSTORDER: it= new PostOrderIterator();
							break;
			case LEVELORDER: it= new LevelOrderIterator(args);
							break;
			}
			return it;
		}
	}
	
	private class InorderIterator implements Iterator<Node> {
		
		private Stack<Node> stack;
		
		public InorderIterator() {
			current=root;
			stack=new Stack<Node>();
		}
		public boolean hasNext() {
			if(current == null && stack.isEmpty())
				return false;
			else
				return true;
		}

		@Override
		public Node next() {
			Node next=null;
			while(current!=null){
				stack.push(current);
				current=current.left;
			}
			if(current == null){
				next=stack.pop();
				current=next;
				current=current.right;
			}
			return next;
		}
		@Override
		public void remove() {
			
		}
		
	}
	
	private class PreorderIterator implements Iterator<Node>{
		
		Stack<Node> stack = null;
		private Node current;
		
		public PreorderIterator(){
			stack=new Stack<Node>();
			current=root;
			stack.push(current);
		}
		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public Node next() {
			Node next=stack.pop();
			if(next.right!=null)
				stack.push(next.right);
			if(next.left!=null)
				stack.push(next.left);
			return next;
			
		}
		@Override
		public void remove() {
			
		}
		
	}
	
	private class PostOrderIterator implements Iterator<Node>{
		
		Stack<Node> firstStack=null;
		Stack<Node> secondStack=null;
		Node current=null;
		public PostOrderIterator(){
			firstStack=new Stack<Node>();
			secondStack=new Stack<Node>();
			current=root;
			firstStack.push(current);
			prepStep();
		}
		
		private void prepStep(){
			while(!firstStack.isEmpty()){
				current=firstStack.pop();
				
				if(current.left!=null)
					firstStack.push(current.left);
				if(current.right!=null)
					firstStack.push(current.right);
				
				secondStack.push(current);
			}
		}

		@Override
		public boolean hasNext() {
			return !secondStack.isEmpty();
		}
		@Override
		public Node next() {
			Node next = secondStack.pop();
			return next;
		}
		@Override
		public void remove() {
		}
	}
	
	private class LevelOrderIterator implements Iterator<Node>{
		Queue<Node> queue=null;
		Node current=null;
		boolean spiralFlag=false;
		public LevelOrderIterator(Object...args){
			/*if(args[0]!=null)
				this.spiralFlag=(boolean)args[0];*/
			queue= new LinkedList<Node>();
			current =root;
			queue.add(current);
		}
		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public Node next() {
			Node next=queue.poll();
			if(next.left!=null)
				queue.add(next.left);
			if(next.right!=null)
				queue.add(next.right);
			return next;
		}

		@Override
		public void remove() {
			
		}
	}
	
	public void insert(int key){
		this.root = this.insertUtil(root, key);
		
	}
	
	private int height(Node node){
		if(node == null) return 0;
		return node.height;
	}
	
	private int getbalance(Node node){
		return height(node.left) - height(node.right);
	}
	
	private Node rightRotate(Node y){
		Node x = y.left;
		Node T2 = x.right;
		x.right = y;
		y.left = T2;
		
		y.height = Math.max(height(y.left), height(y.right))+1;
		x.height = Math.max(height(x.left), height(x.right))+1;
		
		return x;
				
		
	}
	
	private Node leftRotate(Node x){
		Node y = x.right;
		Node T2=y.left;
		
		x.right = T2;
		y.left = x;
		
		x.height = Math.max(height(x.left), height(x.right))+1;
		y.height = Math.max(height(y.left), height(y.right))+1;
		
		return y;
		
	}
	
	private Node insertUtil(Node node, int key){
		if(node == null){
			return new Node(key);
		}
		if(node.data>key)
			node.left = insertUtil(node.left, key);
		else
			node.right = insertUtil(node.right, key);
		
		node.height = Math.max(this.height(node.left), this.height(node.right))+1;
		
		int balance = getbalance(node);
		
		if(balance > 1 && key < node.left.data)
			return rightRotate(node);
		if(balance> 1 && key > node.left.data){
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}
		
		if(balance<-1 && key > node.right.data)
			return leftRotate(node);
		if(balance < -1 && key < node.right.data){
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}
		return node;
	}
}

