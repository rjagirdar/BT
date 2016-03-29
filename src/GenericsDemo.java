
public class GenericsDemo {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException {
		Student st = GenericFactory.createInstance(Student.class);
		st.print();
		
	}

}

class Faculty {
	String profession;
	public Faculty(){
		this.profession = "Faculty";
	}
	
	public void print(){
		System.out.println("Print from "+this.profession);
	}
}

class Student {
	String profession;
	public Student(){
		this.profession = "Student";
	}
	
	public void print(){
		System.out.println("Print from "+this.profession);
	}
}

 class GenericFactory{

    public static <E> E createInstance(Class theClass)
    throws IllegalAccessException, InstantiationException {
        return (E) theClass.newInstance();
        
    }
}
