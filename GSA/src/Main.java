import java.io.IOException;
import java.text.NumberFormat;


public class Main {
	public static void main(String[] args) {
		NumberFormat nf=NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(3);
		GSA gsa=new GSA();
		try {
			System.out.println("Begin");
			double k=0;
			double total=1000;
			double result=-1;
			for(int i=0;i<total;i++){
				gsa=new GSA();
				result=gsa.gsa();
				System.out.println("NO."+i+" calculating..."+result);
				if(result==0){
					k++;
				}
			}
			System.out.println("Finish:"+nf.format(k/total));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
