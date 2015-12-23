import java.io.IOException;
import java.text.NumberFormat;


public class Main {
	public static void main4(String[] args) {
		GSA gsa=new GSA();
		double[] x={-6.887852147682306,0.13025224061728835,1.976657166608124,1.3378255926617015};
		//System.out.println(gsa.fit(x));
	}
	public static void main(String[] args) {
		NumberFormat nf=NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(3);
		GSA gsa=new GSA();
		GSA2 gsa2=new GSA2();
		try {
			System.out.println("Begin");
			double k=0;
			double total=1000.;
			double result=-1;
			for(int i=0;i<total;i++){			
				gsa2=new GSA2();
				//result=gsa.gsa();
				result=gsa2.gsa();
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
