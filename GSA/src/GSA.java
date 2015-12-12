import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GSA {
	int D = 4, N = 10;// 维度与粒子个数
	double[][] x = new double[N][D];// 物体i在k维上的位置
	double[] M = new double[N];// 物体i的质量
	double[] m = new double[N];// 物体i的惯性质量
	double[] fitness = new double[N];// 物体i的适应度
	double best = 0, worst = 0, G = 0, G0 = 100, ga = 0, fa = 0;// 最优解与最差解
	double[][][] f = new double[N][N][D];// 物体j在k维上收到物体i的万有引力
	double[][] F = new double[N][D];// 物体i在k维度上受到的万有引力总和
	long T = 10000, t = 0;// 最大迭代次数
	double[][] a = new double[N][D];// 物体i在k维度上获得的加速度
	double[][] v = new double[N][D];// 物体i在k维度上的速度
	ArrayList<Integer> kbest = new ArrayList<Integer>();

	public void gsa() throws IOException {
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
		initial();
		while (kbest.size() > 1 && best < 1600) {
			sb.append("Time:"+t+"\r\n");
			best = -1;
			worst = Double.MAX_VALUE;// 计算每个粒子的M
			// 计算每个粒子的适应值，并规约在0~1之间；更新最优适应值与最差适应值；更新G
			G = G(G0, ga, T, t);
			sb.append("G:"+G+"\r\n");
			int worstindex = -1;
			sb.append("fitness:");
			for (int i = 0; i < N; i++) {
				fitness[i] = fit(x[i]);
				sb.append(fitness[i]+"\t");
				if(fitness[i]==800|fitness[i]==1200){
					System.out.println("");
				}
				best = fitness[i] > best ? fitness[i] : best;
				worstindex = fitness[i] <= worst ? i : worstindex;
				worst = fitness[i] <= worst ? fitness[i] : worst;
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
			// 计算每个粒子的M
			sb.append("\r\nM:");
			for (int i = 0; i < N; i++) {
				M[i] = m[i] / msum;
				sb.append(M[i]+"\r\n");
			}
			System.out.println("Time:" + t + "\tbest:" + best + "\tsize:"
					+ kbest.size());
			if (best <= 1) {
				System.out.println("Best:" + best);
			}
			sb.append("\r\n");
			StringBuffer fsb=new StringBuffer();
			StringBuffer asb=new StringBuffer();
			StringBuffer vsb=new StringBuffer();
			StringBuffer xsb=new StringBuffer();
			for (int i = 0; i < N; i++) {
				double fsum = 0;
				// 计算每个粒子在不同维度上的a
				sb.append("f-a-v-x-"+i+":\r\n");
				for (int k = 0; k < D; k++) {
					// 计算每个粒子在不同维度上受到的万有引力的总和
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
					// 更新每个粒子在不同维度上的位置
					v[i][k] = Math.random() * v[i][k] + a[i][k];
					v[i][k] = v[i][k] > 10 ? 10 : v[i][k];
					v[i][k] = v[i][k] < -10 ? -10 : v[i][k];
					vsb.append(v[i][k]+"\t");
					// System.out.println(i+"\t"+"\t"+k+"\t"+M[i]+"\t"+v[i][k]);
					x[i][k] = x[i][k] + v[i][k];
					x[i][k] = x[i][k] > 20 ? 20 : x[i][k];
					x[i][k] = x[i][k] < 0 ? 0 : x[i][k];
					xsb.append(x[i][k]+"\r\n");
				}
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

			}
			t++;
			sb.append("\r\n-********************************************************-\r\n");
			fos.write(sb.toString().getBytes());
			if (ob == best) {
				System.out.println("Best:" + best + "\t ERROR!\tG:" + G);
				break;
			} else {
				ob = best;
			}
		}
		fos.close();
		System.out.println(best);
	}

	private void initial() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < D; j++) {
				x[i][j] = Math.random() * 20;
				a[i][j] = 0;
				v[i][j] = 0;
				x[i][j] = x[i][j] > 20 ? 20 : x[i][j];
				x[i][j] = x[i][j] < 0 ? 0 : x[i][j];
			}
			m[i] = fit(x[i]);
			kbest.add(i);
		}
		ga = 70;
		fa = 0;
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
	 *            T时刻的万有引力常量
	 * @param Maj
	 *            物体j的惯性质量
	 * @param Mpi
	 *            物体i的惯性质量
	 * @param Rij
	 *            物体i与物体j的欧氏距离
	 * @param xkj
	 *            物体j在k维度下的位置
	 * @param xki
	 *            物体i在k维度下的位置
	 * @param a
	 *            很小的常量
	 * @return 物体j受到物体i的万有引力
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
	 *            万有引力常量的初值
	 * @param a
	 * @param T
	 *            最大迭代次数
	 * @param t
	 *            当前迭代次数
	 * @return 当前时刻的万有引力常量
	 */
	private double G(double G0, double a, double T, double t) {
		double result = G0 * Math.exp((-1) * a * t / T);
		return result;
	}

	/**
	 * 
	 * @param F
	 *            物体受到的万有引力总和
	 * @param M
	 *            物体的惯性质量
	 * @return 当前时刻和维度下物体的加速度
	 */
	private double a(double F, double M) {
		double result = F / M;
		return result;
	}

	/**
	 * 
	 * @param v
	 *            当前时间下物体的速度
	 * @param a
	 *            当前时间下物体的加速度
	 * @param x
	 *            当前时间下物体的位置
	 */
	private void updateXV(double v, double a, double x) {
	}

	/**
	 * 
	 * @param xi
	 *            第一个物体的位置
	 * @param xj
	 *            第二个物体的位置
	 * @return 两个物体间的欧几里得距离
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
