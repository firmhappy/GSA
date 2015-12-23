import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GSA2 {
	int D = 4, N = 80;// 维度与粒子个数
	Point[] x = new Point[N];// 粒子群
//	Point[] M = new Point[N];// 物体i的质量
//	Point[] m = new Point[N];// 物体i的惯性质量
//	Point[] fitness = new Point[N];// 物体i的适应度
	double best, worst, G = 0, G0 , ga = 0, fa ;// 最优解与最差解
//	double[][][] f = new double[N][N][D];// 物体j在k维上收到物体i的万有引力
//	double[][] F = new double[N][D];// 物体i在k维度上受到的万有引力总和
	long T = 1000, t = 0;// 最大迭代次数
//	double[][] a = new double[N][D];// 物体i在k维度上获得的加速度
//	double[][] v = new double[N][D];// 物体i在k维度上的速度
	ArrayList<Integer> kbest = new ArrayList<Integer>();
	/////////////////////////优化算法//////////////////////////////////////////
	double c1,c2;//学习因子
	double[] pworstg=new double[D];//种群历史最差位置
	double[] pbestg=new double[D];//种群历史最好位置
//	double[][] pworsti=new double[N][D];//个体历史最差位置
//	double[][] pbesti=new double[N][D];//个体历史最好位置

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
			// 计算每个粒子的适应值，并规约在0~1之间；更新最优适应值与最差适应值；更新G;
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
			// 计算每个粒子的M
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
					double psovalue=0;//粒子个体记忆和种群记忆对GSA算法速度的优化
					psovalue+=c1*Math.random()*(x[i].getPbesti()[k]-x[i].getX()[k])+c2*Math.random()*(pbestg[k]-x[i].getX()[k]);
					// 更新每个粒子在不同维度上的速度
					x[i].setvi(k, Math.random()*x[i].getV()[k]+x[i].getA()[k]+psovalue);
					vsb.append(x[i].getV()[k]+"\t");
					// System.out.println(i+"\t"+"\t"+k+"\t"+M[i]+"\t"+v[i][k]);
					
					xsb.append(x[i].getX()[k]+"\r\n");
				}
				
				//更新每个粒子自身的最好位置与最坏位置
				
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
	private double f(double G, double Maj, double Rij, double xkj,
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
