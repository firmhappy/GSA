import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GSA {
	int D = 4, N = 50;// ά�������Ӹ���
	double[][] x = new double[N][D];// ����i��kά�ϵ�λ��
	double[] M = new double[N];// ����i������
	double[] m = new double[N];// ����i�Ĺ�������
	double[] fitness = new double[N];// ����i����Ӧ��
	double best, worst, G = 0, G0 , ga = 0, fa ;// ���Ž�������
	double[][][] f = new double[N][N][D];// ����j��kά���յ�����i����������
	double[][] F = new double[N][D];// ����i��kά�����ܵ������������ܺ�
	long T = 1000, t = 0;// ����������
	double[][] a = new double[N][D];// ����i��kά���ϻ�õļ��ٶ�
	double[][] v = new double[N][D];// ����i��kά���ϵ��ٶ�
	ArrayList<Integer> kbest = new ArrayList<Integer>();
	
	/////////////////////////�Ż��㷨//////////////////////////////////////////
	double c1,c2;//�ĸ�ѧϰ����
	double[] pworstg=new double[D];//��Ⱥ��ʷ���λ��
	double[] pbestg=new double[D];//��Ⱥ��ʷ���λ��
	double[][] pworsti=new double[N][D];//������ʷ���λ��
	double[][] pbesti=new double[N][D];//������ʷ���λ��

	public double gsa() throws IOException {
		File file=new File("D:/GSA_Status.txt");
		if(!file.exists()){
			file.createNewFile();
		}else{
			file.delete();
			file.createNewFile();
		}
//		FileOutputStream fos=new FileOutputStream(file);
		StringBuffer sb=new StringBuffer();
		double ob = 0;
		initial();
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
				fitness[i] = fit(x[i]);
				sb.append(fitness[i]+"\t");
				pbestg=fitness[i]<=best?x[i]:pbestg;
				best = fitness[i] < best ? fitness[i] : best;
				worstindex = fitness[i] >= worst ? i : worstindex;
				pworstg=fitness[i]>=worst?x[i]:pworstg;
				worst = fitness[i]>= worst ? fitness[i] : worst;
			}
			sb.append("\r\nbest:"+best+"worst:"+worst+"\r\n");
			kbest.remove(Integer.valueOf(worstindex));
			sb.append("m:");
			for (int i = 0; i < N; i++) {
				m[i] = (fitness[i] - worst) / (best - worst);
				sb.append(m[i]+"\t");
			}
			double msum = 0;
			for (int i = 0; i < N; i++) {
				msum += m[i];
			}
			// ����ÿ�����ӵ�M
			sb.append("\r\nM:");
			for (int i = 0; i < N; i++) {
				M[i] = m[i] / msum;
				sb.append(M[i]+"\r\n");
			}
//			System.out.println("Time:" + t + "\tbest:" + best + "\tsize:"
//					+ kbest.size());
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
						if (j == i || !kbest.contains(Integer.valueOf(j))) {
							continue;
						}
						double r = R(x[i], x[j]);
						f[i][j][k] = r == 0 ? 0 : f(G, M[j], M[i], r, x[j][k],
								x[i][k], fa);
						fsb.append(f[i][j][k]+"\t");
						fsum += Math.random() * f[i][j][k];
					}
					fsb.append("\r\n\t");
					asb.append(a[i][k]+"\t");
					F[i][k] = fsum;
					a[i][k] = F[i][k] == 0 ? 0 : F[i][k];
					double psovalue=0;//���Ӹ���������Ⱥ�����GSA�㷨�ٶȵ��Ż�
					psovalue+=c1*Math.random()*(pbesti[i][k]-x[i][k])+c2*Math.random()*(pbestg[k]-x[i][k]);
					// ����ÿ�������ڲ�ͬά���ϵ�λ��
					v[i][k] = Math.random() * v[i][k] + a[i][k]+psovalue;
					vsb.append(v[i][k]+"\t");
					// System.out.println(i+"\t"+"\t"+k+"\t"+M[i]+"\t"+v[i][k]);
					x[i][k] = x[i][k] + v[i][k];
					x[i][k] = x[i][k] > 20 ? 20 : x[i][k];
					x[i][k] = x[i][k] < 0 ? 0 : x[i][k];
					xsb.append(x[i][k]+"\r\n");
					
					//����ÿ��������������λ�����λ��
					pworsti[i]=fit(pworsti[i])>=fit(x[i])?pworsti[i]:x[i];
					pbesti[i]=fit(pbesti[i])<fit(x[i])?pbesti[i]:x[i];
				}
				fsb.append("\r\n");
				asb.append("\r\n");
				vsb.append("\r\n");
				xsb.append("\r\n");
				sb.append(fsb);
				sb.append(asb);
				sb.append(vsb);
				sb.append(xsb);
//				fos.write(sb.toString().getBytes());
				sb=new StringBuffer();

			}
			t++;
			sb.append("\r\n-********************************************************-\r\n");
//			fos.write(sb.toString().getBytes());
			if (ob == best) {
//				System.out.println("Best:" + best + "\t ERROR!\tG:" + G);
				break;
			} else {
				ob = best;
			}
		}
//		fos.close();
		return best;
	}

	private void initial() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < D; j++) {
				x[i][j] = Math.random() * 20;
				a[i][j] = 0;
				v[i][j] = 0;
				x[i][j] = x[i][j] > 20 ? 20 : x[i][j];
				x[i][j] = x[i][j] < 0 ? 0 : x[i][j];
				pworsti[i][j]=x[i][j];
				pbesti[i][j]=x[i][j];
			}
			m[i] = fit(x[i]);
			kbest.add(i);
		}
		ga = 20;
		fa = 0;
		G0=100;
		c1=0.5;
		c2=0.5;
		best=Double.MAX_VALUE;
		worst=-1;
	}

	private double fit(double[] x) {
		double result = 0;
		for (int i = 0; i < x.length; i++) {
			result += x[i] * x[i];
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
	private double f(double G, double Maj, double Mpi, double Rij, double xkj,
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
