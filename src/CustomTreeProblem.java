import java.util.*;
import java.util.Map.Entry;
public class CustomTreeProblem {

	public static void main(String[] args) {
		String[] arr={"a:b","b:c","a:d","d:e","c:f","o:p"};
		buildForest(arr);
	}
	
	public static void buildForest(String[] arr){
		TreeMap<String,TreeNode> map=new TreeMap<String,TreeNode>();
		TreeMap<String, TreeNode> firstNodes=new TreeMap<String, TreeNode>();
		for(String str : arr){
			
			TreeNode firstNode=null;
			TreeNode secondNode=null;
			
			String[] parts=str.split(":");
			
			if(map.containsKey(parts[0])){
				firstNode=map.get(parts[0]);
			}
			else{
				firstNode=new TreeNode(parts[0]);
				map.put(parts[0], firstNode);
				firstNodes.put(parts[0], firstNode);
			}
			
			if(map.containsKey(parts[1])){
				secondNode=map.get(parts[1]);
			}
			else{
				secondNode=new TreeNode(parts[1]);
				
			}
			firstNode.add(secondNode);
			map.put(parts[1], secondNode);
		}
		
		Iterator<Entry<String, TreeNode>> ite= firstNodes.entrySet().iterator();
		while(ite.hasNext()){
			TreeNode forestNode=ite.next().getValue();
			forestNode.Print("");
			System.out.println("");
		}
		
		
	}
	
	

}

class TreeNode {
	String identifier;
	ArrayList<TreeNode> children = null;
	
	public TreeNode(String ch){
		this.identifier=ch;
		children=new ArrayList<TreeNode>();
	}
	
	public void Print(String indent){
		System.out.println(indent + "-->" +identifier);
		for(TreeNode child: children){
			child.Print(indent + "   |");
		}
	}
	
	public void add(TreeNode child){
		this.children.add(child);
	}
}
