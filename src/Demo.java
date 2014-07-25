import java.util.*;
public class Demo {

	public static void main(String[] args) {
		System.out.println("Hello Raghuveer");
		Collection<String> coll= new ArrayList<String>();
		coll.add("Raghuveer");
		coll.add("Vishwanath");
		coll.add("Bhavana");
		
		Iterator<String> it = coll.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		System.out.println(coll.getClass() +" "+coll.size());
		
		String[] arr=new String[coll.size()];
		arr=coll.toArray(arr);
		for(String str : arr)
			System.out.println(str);
	}

}
