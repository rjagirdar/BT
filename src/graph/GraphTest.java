package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class GraphTest {

	public static void main(String[] args) {
		Graph g =  new Graph(6, Type.DIRECTED, EdgeType.UNWEIGHTED);
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		//g.addEdge(2, 0);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		//g.addEdge(5, 0);
		
		for(int[] row : g.getAdjacencyMatrix()){
			for(int val : row){
				System.out.print(val+" ");
			}
			System.out.println();
		}
		
		g.DFS();
		System.out.println();
		System.out.println("===========================");
		g.BFS();
		System.out.println();
		System.out.println("===========================");
		if(g.hasCycle())
			System.out.println("Graph Has Cycles");
		else
			System.out.println("Graph Has No Cycles");
		System.out.println();
		System.out.println("===========================");
		if(g.isReachable(0, 5))
			System.out.println("5 is reachable from 0");
		//g.hasHamiltonianCycle();
		
		Graph g1 = new Graph(4, Type.DIRECTED, EdgeType.WEIGHTED);
		g1.addEdge(0, 1, 5);
		g1.addEdge(0, 3, 10);
		g1.addEdge(1, 2, 3);
		g1.addEdge(2, 3, 1);
		
		System.out.println();
		System.out.println("===========================");
		
		for(int[] row : g1.getAdjacencyMatrix()){
			for(int val : row){
				System.out.print(val+" \t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("===========================");
		for(int[] row : g1.floydWarshall()){
			for(int dist : row){
				System.out.print(dist+" \t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("===========================");
		
		g1.DFS();
		
		
		
	}

}

enum Type{
	DIRECTED, UNDIRECTED
}

enum EdgeType{
	WEIGHTED, UNWEIGHTED
}

class Graph{
	private int V;
	private Type graphType;
	private EdgeType edgeType;
	private TreeMap<Integer,ArrayList<Integer>> adjacentList;
	private TreeMap<Integer,ArrayList<Edge>> adjacentEdgeList;
	int[] path;
	HashSet<Integer> visitedVertices;
	public Graph(int V){
		this.V = V;
	}
	
	public Graph(int V, Type type, EdgeType edgeType){
		this.V = V;
		this.graphType = type;
		this.edgeType = edgeType;
		ConcurrentHashMap<Integer, String> map;
		if(this.edgeType == EdgeType.UNWEIGHTED){
			adjacentList = new TreeMap<Integer, ArrayList<Integer>>();
		}
			
		else
			adjacentEdgeList = new TreeMap<Integer, ArrayList<Edge>>();
	}
	
	public int[][] getAdjacencyMatrix(){
		int[][] adjacencyMatrix = new int[this.V][this.V];
		int INF = 1000;
		for(int i=0; i<this.V; i++){
			if(this.edgeType == EdgeType.UNWEIGHTED){
				if(this.adjacentList.get(i)!=null){
					for(int destVertex : this.adjacentList.get(i)){
						adjacencyMatrix[i][destVertex] =1;
					}
				}
			}
			else{
				for(int j=0; j<this.V; j++){
					if(i==j){
						adjacencyMatrix[i][j] = 0;
						continue;
					}
					else
						adjacencyMatrix[i][j] = INF;
					if(this.adjacentEdgeList.get(i)!=null){
						for(Edge edge : this.adjacentEdgeList.get(i)){
							if(edge.destinationVertex == j){
								adjacencyMatrix[i][edge.destinationVertex] = edge.weight;
								break;
							}
						}
					}
					
				}
				
			}
		}
		return adjacencyMatrix;
	}
	
	public int[][] floydWarshall(){
		int[][] dist = this.getAdjacencyMatrix();
		for(int k=0; k<this.V; k++){
			for(int i=0; i<this.V; i++){
				for(int j=0; j<this.V; j++){
					if(dist[i][k]+dist[k][j] <dist[i][j]){
						dist[i][j] = dist[i][k]+dist[k][j];
					}
				}
			}
		}
		
		return dist;
	}
	
	
	public void addEdge(int sourceVertex, int destinationVertex){
		if(adjacentList.get(sourceVertex)==null){
			adjacentList.put(sourceVertex, new ArrayList<Integer>());
		}
		adjacentList.get(sourceVertex).add(destinationVertex);
		
		if(this.graphType == Type.UNDIRECTED){
			if(adjacentList.get(destinationVertex) == null)
				adjacentList.put(destinationVertex, new ArrayList<Integer>());
			adjacentList.get(destinationVertex).add(sourceVertex);
		}
	}
	
	public void addEdge(int sourceVertex, int destinationVertex, int weight){
		
		if(adjacentEdgeList.get(sourceVertex) == null)
			adjacentEdgeList.put(sourceVertex, new ArrayList<Edge>());
		adjacentEdgeList.get(sourceVertex).add(new Edge(destinationVertex, weight));
		
		if(graphType == Type.UNDIRECTED){
			if(adjacentEdgeList.get(destinationVertex) == null)
				adjacentEdgeList.put(destinationVertex, new ArrayList<Edge>());
			adjacentEdgeList.get(destinationVertex).add(new Edge(sourceVertex, weight));
		}
	}
	
	public void DFS(){
		boolean[] visited = new boolean[this.V];
		System.out.println("DFS Order is");
		if(this.edgeType == EdgeType.UNWEIGHTED){
			DFSUnweightedUtil(0,visited);
		}
		else{
			DFSWeightedUtil(0,visited);
		}
		
		
	}
	
	public boolean hasCycle(){
		boolean[] visited = new boolean[this.V];
		boolean[] recStack = new boolean[this.V];
		boolean result = false;
		for(int i=0; i<this.V; i++){
			if(!visited[i]){
				result = result || detectCycle(i, visited, recStack);
			}
				
		}
			
		return result;
	}
	
	public void hasHamiltonianCycle(){
		path= new int[this.V];
		path[0] = 0;
		visitedVertices = new HashSet<Integer>();
		if(!hasHamiltonianCycle(1, 1)){
			System.out.println("Solution Doesn't exist");
		}
		else{
			System.out.println();
			System.out.println("=========================================");
			System.out.println("Hamiltonian cycle exists and the path is ");
			for(int i=0; i<this.V; i++){
				System.out.print(path[i]+" ");
			}
		}
		
	}
	
	private boolean isSafe(int v, int pos){
		int sourceVertex = path[pos-1];
		if(!this.adjacentList.get(sourceVertex).contains(v))
			return false;
		if(visitedVertices.contains(v))
			return false;
		return true;
		
	}
	
	private boolean hasHamiltonianCycle(int v, int pos){
		if(pos == V){
			if(this.adjacentList.get(v)!=null){
				if(this.adjacentList.get(v).contains(0))
					return true;
				else
					return false;
			}
			return false;
		}
		for(int i=1; i<this.V; i++){
			if(isSafe(i, pos)){
				path[pos] = i;
				visitedVertices.add(i);
				if(hasHamiltonianCycle(i, pos+1))
					return true;
				path[pos] =-1;
				visitedVertices.remove(i);
			}
		}
		return false;
	}
	
	public boolean isReachable(int s, int d){
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean[] visited = new boolean[this.V];
		queue.add(s);
		visited[s] = true;
		while(!queue.isEmpty()){
			int tempSource = queue.poll();
			//System.out.print(tempSource+" ");
			if(this.adjacentList.get(tempSource)!=null){
				
				
				for(int destVertex : adjacentList.get(tempSource)){
					if(destVertex == d)
						return true;
					
					if(!visited[destVertex]){
						visited[destVertex] = true;
						queue.add(destVertex);
					}
				}
			}
		}
		return false;
	}
	
	public boolean detectCycle(int v, boolean[] visited,boolean[] recStack){
		visited[v] = true;
		recStack[v] = true;
		boolean result = false;
		if(this.adjacentList.get(v)!=null){
			for(int destVertex : this.adjacentList.get(v)){
				if(visited[destVertex] && recStack[destVertex])
					return true;
				else
					result = result || detectCycle(destVertex, visited, recStack);
			}
		}
		recStack[v]= false;
		return result;
	}
	
	public void BFS(){
		if(this.edgeType == EdgeType.UNWEIGHTED){
			boolean[] visited = new boolean[this.V];
			System.out.println("BFS Order is");
			BFSUnweightedUtil(0,visited);
		}
	}
	
	private void BFSUnweightedUtil(int v, boolean[] visited){
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(v);
		visited[v] = true;
		while(!queue.isEmpty()){
			int tempSource = queue.poll();
			System.out.print(tempSource+" ");
			if(this.adjacentList.get(tempSource)!=null){
				for(int destVertex : adjacentList.get(tempSource)){
					if(!visited[destVertex]){
						visited[destVertex] = true;
						queue.add(destVertex);
					}
				}
			}
			
		}
	}
	
	private void DFSUnweightedUtil(int v, boolean[] visited){
		visited[v] = true;
		System.out.print(v+" ");
		if(this.adjacentList.get(v)!=null){
			for(int destVertex : this.adjacentList.get(v)){
				if(!visited[destVertex])
					DFSUnweightedUtil(destVertex, visited);
			}
		}
		
		
	}
	
	private void DFSWeightedUtil(int v, boolean[] visited){
		visited[v] = true;
		System.out.print(v+" ");
		if(this.adjacentEdgeList.get(v)!=null){
			for(Edge dest : this.adjacentEdgeList.get(v)){
				if(!visited[dest.destinationVertex])
					DFSWeightedUtil(dest.destinationVertex, visited);
			}
		}
		
		
	}
	
	
	
	private class Edge{
		int destinationVertex;
		int weight;
		
		public Edge(int destinationVertex, int weight){
			this.destinationVertex = destinationVertex;
			this.weight = weight;
		}
		
	}
	
}
