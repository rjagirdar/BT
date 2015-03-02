package BinaryTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;





public class BinaryTreeTest {

	public static void main(String[] args) {
		
		/*BinaryTree tree = getTree();
		System.out.println("LCA of 6 and 2 is "+tree.LCA(6, 2).data);
		System.out.println("Distance between 1 and 2 is "+tree.getDistance(1, 2));
		tree.printNodeAtKDistanceFromLeaf(2);
		System.out.println("================================================");
		tree.printNodesWithoutSibling();
		System.out.println("================================================");
		System.out.println("Linked List is");
		Node head = tree.getLinkedList();
		while(head!=null){
			System.out.print(head.data+" ");
			head = head.right;
		}
		System.out.println();
		System.out.println("================================================");
		System.out.println("Linked List of Leaf Nodes are");
		Node head = tree.extractLeaves();
		while(head!=null){
			System.out.print(head.data+" ");
			head = head.left;
		}*/
		
		BinaryTree tree = getTTree();
		System.out.println(tree.secondMin());
	}
	public static BinaryTree getTree(){
		Node root= new Node(1);
		root.left = new Node(2);
		root.right=new Node(3);
		root.left.left=new Node(4);
		root.left.right=new Node(5);
		root.right.left=new Node(6);
		root.right.right =new Node(7);
		root.right.right.right=new Node(8);
		root.right.right.right.right=new Node(9);
		BinaryTree tree=new BinaryTree(root);
		return tree;
	}
	
	public static BinaryTree getTTree(){
		Node root = new Node(2);
		root.left = new Node(2);
		root.right = new Node(3);
		
		root.left.left = new Node(4);
		root.left.right = new Node(2);
		
		root.right.left = new Node(5);
		root.right.right = new Node(3);
		
		
		return new BinaryTree(root);
		
		
	}
}

class Node {

	Node left;
	int data;
	Node right;

	public Node(int data){
		this.data=data;
		left=null;
		right=null;
	}
	
	public static Comparator<Node> comp = new Comparator<Node>() {

		@Override
		public int compare(Node o1, Node o2) {
			
			return o1.data-o2.data;
		}
	};
	
	@Override
	public String toString(){
		return String.format("" + this.data);
	}
}

class BinaryTree{
	Node root;
	int distance;
	HashSet<Node> visitedNodes;
	Node head;
	Node prev;
	List<Node> leafNodesList = new ArrayList<Node>();
	
	public int secondMin(){
		getLeaves(this.root);
		Collections.sort(leafNodesList, Node.comp);
		return leafNodesList.get(1).data;
		
		
	}
	
	private void getLeaves(Node root){
		if(root == null)
            return;
            
     if(root.left == null && root.right==null){
         leafNodesList.add(root);
     }
     getLeaves(root.left);
     getLeaves(root.right);
	}
	
	public BinaryTree(){
		root = null;
		head = null;
		prev = null;
	}
	
	public BinaryTree(Node root){
		this.root = root;
		head = null;
		prev = null;
	}
	
	public Node LCA(int n1, int n2){
		
		Stack<Node> path1 = new Stack<Node>();
		Stack<Node> path2 = new Stack<Node>();
		if(!findPath(root, path1, n1) || !findPath(root, path2, n2) )
			return null;
		int i;
		for(i =0; i<path1.size() && i<path2.size(); i++){
			if(path1.elementAt(i) != path2.elementAt(i))
				break;
		}
		return path1.elementAt(i-1);
		
		
	}
	
	public Node getLinkedList(){
		getLinkedListUtil(this.root);
		return head;
	}
	
	public void getLinkedListUtil(Node current){
		if(current== null) return;
		getLinkedListUtil(current.left);
		if(prev==null){
			head = current;
		}
		else{
			current.left = prev;
			prev.right = current;
		}
		prev = current;
		getLinkedListUtil(current.right);
		
	}
	
	public int getDistance(int n1, int n2){
		Node lca = LCA(n1,n2);
		findPath(root, n1, 0);
		int d1 = this.distance;
		this.distance = 0;
		findPath(root, n2, 0);
		int d2 = this.distance;
		this.distance = 0;
		findPath(root, lca.data, 0);
		int d_lca = this.distance;
		return d1+d2-2*d_lca;
				
	}
	
	private boolean findPath(Node current, Stack<Node> stack, int key){
		if(current == null)
			return false;
		stack.push(current);
		if(current.data == key)
			return true;
		if((findPath(current.left, stack, key)) || (findPath(current.right, stack, key)))
				return true;
		stack.pop();
		return false;
	}
	
	private boolean findPath(Node current, int key, int distance){
		if(current == null)
			return false;
		if(current.data == key)
			this.distance = distance;
		if(findPath(current.left, key, distance+1) || findPath(current.right, key, distance+1))
			return true;
		
		return false;
	}
	
	public void printNodeAtKDistanceFromLeaf(int k){
		Stack<Node> stack = new Stack<Node>();
		visitedNodes = new HashSet<Node>();
		printNodeAtKDistanceFromLeafUtil(this.root, stack, k);
		if(visitedNodes.size()>0){
			Iterator<Node> ite = visitedNodes.iterator();
			while(ite.hasNext())
				System.out.println(ite.next().data);
		}
	}
	
	private void printNodeAtKDistanceFromLeafUtil(Node current, Stack<Node> stack, int k){
		if(current == null) return;
		
		if(current.left== null && current.right == null){
			if(stack.size()>=k){
				if(!visitedNodes.contains(stack.get(stack.size()-k)))
					visitedNodes.add(stack.get(stack.size()-k));
			}
		}
		stack.push(current);
		printNodeAtKDistanceFromLeafUtil(current.left, stack, k);
		printNodeAtKDistanceFromLeafUtil(current.right, stack, k);
		stack.pop();
	}
	
	public void printNodesWithoutSibling(){
		printNodesWithoutSiblingUtil(this.root);
	}
	
	private void printNodesWithoutSiblingUtil(Node current){
		if(current== null) return;
		
		if(current.right== null && current.left!=null){
			System.out.println(current.left.data);
		}
		else if(current.left==null && current.right!=null)
			System.out.println(current.right.data);
		
		printNodesWithoutSiblingUtil(current.left);
		printNodesWithoutSiblingUtil(current.right);
	}
	
	public Node extractLeaves(){
		head = null;
		extractLeavesUtil(this.root);
		return head;
	}
	
	public Node extractLeavesUtil(Node current){
		if(current == null ) return null;
		if(current.left == null && current.right == null){
			if(head == null)
				head = current;
			else{
				head.right = current;
				current.left = head;
				head = current;
			}
			return null;
		}
		current.left = extractLeavesUtil(current.left);
		current.right = extractLeavesUtil(current.right);
		return current;
	}
	
}



