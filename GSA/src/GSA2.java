import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GSA2 {
	int D = 4, N = 80;// ά�������Ӹ���
	Point[] x = new Point[N];// ����Ⱥ
//	Point[] M = new Point[N];// ����i������
//	Point[] m = new Point[N];// ����i�Ĺ�������
//	Point[] fitness = new Point[N];// ����i����Ӧ��
	double best, worst, G = 0, G0 , ga = 0, fa ;// ���Ž�������
//	double[][][] f = new double[N][N][D];// ����j��kά���յ�����i����������
//	double[][] F = new double[N][D];// ����i��kά�����ܵ������������ܺ�
	long T = 1000, t = 0;// ����������
//	double[][] a = new double[N][D];// ����i��kά���ϻ�õļ��ٶ�
//	double[][] v = new double[N][D];// ����i��kά���ϵ��ٶ�
	ArrayList<Integer> kbest = new ArrayList<Integer>();
	/////////////////////////�Ż��㷨//////////////////////////////////////////
	double c1,c2;//ѧϰ����
	double[] pworstg=new double[D];//��Ⱥ��ʷ���λ��
	double[] pbestg=new double[D];//��Ⱥ��ʷ���λ��
//	double[][] pworsti=new double[N][D];//������ʷ���λ��
//	double[][] pbesti=new double[N][D];//������ʷ���λ��

	public double gsa() throws IOException {
		File file=new File("D:/GSA_Status.txt");
		if(!file.exists()){
			file.createNewFile();
		}else{
			file.delete();
			file.createNewFile();
		}
		FileOutputStream fos=new FileOutputStream(file);
		StringBuffer sb=new StringBuffer();
		double ob = 0;
		for(int i=0;i<N;i++){
			x[i]=new Point(D,N);
		}
		initial(x); 
		while (kbest.size() > 1 && best>0) {
			sb.append("Time:"+t+"\r\n");
			best = Double.MAX_VALUE;
			worst = -1;
			// ����ÿ�����ӵ���Ӧֵ������Լ��0~1֮�䣻����������Ӧֵ�������Ӧֵ������G;
			G = G(G0, ga, T, t);
			sb.append("G:"+G+"\r\n");
			int worstindex = -1;
			sb.append("fitness:");
			for (int i = 0; i < N; i++) {
				x[i].setFitness(fit(x[i].getX()));
				if(i%10==0){
					sb.append("\r\n");
				}
				sb.append(i+":"+x[i].getFitness()+"\t");
				pbestg=x[i].getFitness()<=best?x[i].getX():pbestg;
				best = x[i].getFitness() <= best ? x[i].getFitness() : best;
				worstindex = x[i].getFitness()>= worst ? i : worstindex;
				pworstg=x[i].getFitness()>=worst?x[i].getX():pworstg;
				worst = x[i].getFitness()>= worst ? x[i].getFitness() : worst;
			}
			sb.append("\r\nbest:"+best+"worst:"+worst+"\r\n");
			kbest.remove(Integer.valueOf(worstindex));
			sb.append("m:");
			for (int i = 0; i < N; i++) {
				if(i%10==0){
					sb.append("\r\n");
				}
				x[i].setm((x[i].getFitness() - worst) / (best - worst));;
				sb.append(i+":"+x[i].getm()+"\t");
			}                                                                                                 
			double msum = 0;
			for (int i = 0; i < N; i++) {
				msum += x[i].getm();
			}
			// ����ÿ�����ӵ�M
			sb.append("\r\nM:");
			for (int i = 0; i < N; i++) {
				x[i].setM(x[i].getm()/msum);
				if(i%10==0){
					sb.append("\r\n");
				}
				sb.append(x[i].getM()+"\t");
			}
			sb.append("\r\n");
			StringBuffer fsb=new StringBuffer();
			StringBuffer asb=new StringBuffer();
			StringBuffer vsb=new StringBuffer();
			StringBuffer xsb=new StringBuffer();
			for (int i = 0; i < N; i++) {
				double fsum = 0;
				// ����ÿ�������ڲ�ͬά���ϵ�a
				sb.append("f-a-v-x-"+i+":\r\n");
				for (int k = 0; k < D; k++) {
					// ����ÿ�������ڲ�ͬά�����ܵ��������������ܺ�
					fsum = 0;
					fsb.append("\t"+i+"-"+k+"-f:");
					asb.append("\t"+i+"-"+k+"-a:");
					vsb.append("\t"+i+"-"+k+"-v:");
					xsb.append("\t"+i+"-"+k+"-x:");
					for (int j = 0; j < N; j++) {
						if (j == i ) {
							continue;
						}
						double r = R  (x[i].getX(), x[j].getX());
						x[i].setfi(j, k, f(G,x[j].getM(),r,x[j].getX()[k],x[i].getX()[k],fa));
						fsb.append(x[i].getf()[j][k]+"\t");
						fsum += Math.random() * x[i].getf()[j][k];
					}
					fsb.append("\r\n\t");
					x[i].setFi(k, fsum);
					x[i].setai(k, x[i].getF()[k]);
					asb.append(x[i].getA()[k]+"\t");
					double psovalue=0;//���Ӹ���������Ⱥ�����GSA�㷨�ٶȵ��Ż�
					psovalue+=c1*Math.random()*(x[i].getPbesti()[k]-x[i].getX()[k])+c2*Math.random()*(pbestg[k]-x[i].getX()[k]);
					// ����ÿ�������ڲ�ͬά���ϵ��ٶ�
					x[i].setvi(k, Math.random()*x[i].getV()[k]+x[i].getA()[k]+psovalue);
					vsb.append(x[i].getV()[k]+"\t");
					// System.out.println(i+"\t"+"\t"+k+"\t"+M[i]+"\t"+v[i][k]);
					
					xsb.append(x[i].getX()[k]+"\r\n");
				}
				
				//����ÿ��������������λ�����λ��
				
				fsb.append("\r\n");
				asb.append("\r\n");
				vsb.append("\r\n");
				xsb.append("\r\n");
				sb.append(fsb);
				sb.append(asb);
				sb.append(vsb);
				sb.append(xsb);
				fos.write(sb.toString().getBytes());
				sb=new StringBuffer();
				fsb=new StringBuffer();
				asb=new StringBuffer();
				vsb=new StringBuffer();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				xsb=new StringBuffer();

			}
			for(int i=0;i<N;i++){
				for(int j=0;j<D;j++){
					x[i].updatePosition(j);
				}
				x[i].setPworsti(fit(x[i].getPworsti())>=fit(x[i].getX())?x[i].getPworsti():x[i].getX());
				x[i].setPbesti(fit(x[i].getPbesti())<fit(x[i].getX())?x[i].getPbesti():x[i].getX());
			}
			
			t++;
			sb.append("\r\n-********************************************************-\r\n");
			fos.write(sb.toString().getBytes());
			if (ob == best) {
//				System.out.println("Best:" + best + "\t ERROR!\tG:" + G);
				break;
			} else {
				ob = best;
			}
		}
		fos.close();
		return best;
	}

	private void initial(Point[] points) {
		for (int i = 0; i < points.length; i++) {
			Point p=points[i];
			switch(i){
			case 0:{
				p.setX(new double[]{9.032881867211277, 17.69758311929579, 3.4832867752779495, 11.155414933577664});
				break;
			}
			case 1:{
				p.setX(new double[]{4.670223097500987, 12.497027915675815, 1.4579657737250873, 19.695986421653057});
				break;
			}
			case 2:{
				p.setX(new double[]{2.16340040896428, 4.3942277127950895, 3.21118661049532, 7.8688055593849615});
				break;
			}
			case 3:{
				p.setX(new double[]{6.399578253633731, 13.782892839519008, 10.598845137672981, 3.1871890206787357});
				break;
			}
			case 4:{
				p.setX(new double[]{15.149923287942435, 0.012301486662729122, 6.085073749042415, 16.579065505232684});
				break;
			}
			}
			
//			for (int j = 0; j < p.getD() ; j++) {
//				double xi=Math.random()*60-30;
//				while(xi>30||xi<-30){
//					xi=Math.random()*60-30;
//				}
//				p.setxi(j, Math.random()*20);
//				p.setai(j, 0);
//				p.setvi(j, 0);
//				p.setpworstival(j, p.getX()[j]);
//				p.setpbestival(j, p.getX()[j]);
//			}
			kbest.add(i);
		}
		ga = 20;
		fa = Double.MIN_VALUE;
		G0=100;
		c1=0.5;
		c2=0.5;
		best=Double.MAX_VALUE;
		worst=-1;
	}

	public double fit(double[] x) {
		double result = 0;
		for (int i = 0; i < x.length; i++) {
			double dxi=x[i]+5;
			dxi=dxi*dxi;
			//double temp=(Math.pow(x[i+1]-x[i], x.length)*100+Math.pow(dxi, 2));
			double temp=x[i]*x[i];
			result += dxi;
		}
		return result;
	}

	/**
	 * 
	 * @param G
	 *            Tʱ�̵�������������
	 * @param Maj
	 *            ����j�Ĺ�������
	 * @param Mpi
	 *            ����i�Ĺ�������
	 * @param Rij
	 *            ����i������j��ŷ�Ͼ���
	 * @param xkj
	 *            ����j��kά���µ�λ��
	 * @param xki
	 *            ����i��kά���µ�λ��
	 * @param a
	 *            ��С�ĳ���
	 * @return ����j�ܵ�����i����������
	 */
	private double f(double G, double Maj, double Rij, double xkj,
			double xki, double a) {
		double res = 0.0;
		res = G * Maj * (xkj - xki) / (Rij + a);
		return res;
	}

	/**
	 * 
	 * @param G0
	 *            �������������ĳ�ֵ
	 * @param a
	 * @param T
	 *            ����������
	 * @param t
	 *            ��ǰ��������
	 * @return ��ǰʱ�̵�������������
	 */
	private double G(double G0, double a, double T, double t) {
		double result = G0 * Math.exp((-1) * a * t / T);
		return result;
	}

	/**
	 * 
	 * @param xi
	 *            ��һ�������λ��
	 * @param xj
	 *            �ڶ��������λ��
	 * @return ����������ŷ����þ���
	 */
	private double R(double[] xi, double[] xj) {
		double result = 0;
		for (int i = 0; i < xi.length; i++) {
			result += (Math.pow((xi[i] - xj[i]), 2));
		}
		result = Math.sqrt(result);
		return result;
	}

}
