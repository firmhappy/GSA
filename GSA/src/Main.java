import java.io.IOException;


public class Main {
	public static void main(String[] args) {
		GSA gsa=new GSA();
		try {
			gsa.gsa();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
