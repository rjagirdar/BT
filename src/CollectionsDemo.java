import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

public class CollectionsDemo {

	public static void main(String[] args) {
		for(int i=0; i<5; i++){
			System.out.println(randInt(0, 1));
		}
		System.out.println();
		PriorityQueue<Integer> down_floors=new PriorityQueue<Integer>(1024, Collections.reverseOrder());
		for(int i=0; i<5; i++)
			down_floors.add(i);
		while(!down_floors.isEmpty()){
			System.out.println(down_floors.remove());
		}
	}
	
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

}
