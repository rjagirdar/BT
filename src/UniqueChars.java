import java.util.*;
public class UniqueChars {

	public static void main(String[] args) {
	
		String str="raghuveerjagirdar";
		if(!hasUnique(str)){
			System.out.println(str +" has no unique charecters");
		}
		System.out.println(removeDuplicateChars(str));
	}
	public static boolean hasUnique(String str){
		boolean[] bool=new boolean[256];
		boolean result=true;
		for( char ch : str.toCharArray()){
			if(bool[ch]){
				result= false;
			}
			else
				bool[ch]=true;
		}
		return result;
	}
	
	
	
	public static String removeDuplicateChars(String str){
		boolean[] bool=new boolean[256];
		for(int i=0; i<256; i++){
			bool[i]=false;
		}
		StringBuffer buffer=new StringBuffer();
		for(int i=0; i<str.length(); i++){
			if(!bool[str.charAt(i)]){
				bool[str.charAt(i)]=true;
				buffer.append(str.charAt(i));
			}
		}
		return buffer.toString();
	}

}
