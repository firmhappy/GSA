public class Point {
	private double[] pworsti;// ������ʷ���λ��
	private double[] pbesti;// ������ʷ���λ��
	private double[] a;// ����i��kά���ϻ�õļ��ٶ�
	private double[] v ;// ����i��kά���ϵ��ٶ�
	private double[][] f;// ����j��kά���յ�����i����������
	private double[] F ;// ����i��kά�����ܵ������������ܺ�
	private double fitness;// ����i����Ӧ��
	private double[] x;// ����i��kά�ϵ�λ��
	private double M;// ����i������
	private double m;// ����i�Ĺ�������
	private long D;

	public long getD() {
		return D;
	}

	public void setD(long d) {
		D = d;
	}

	public Point(int D,int N){
		pworsti=new double[D];
		pbesti=new double[D];
		a=new double [D];
		v=new double[D];
		f=new double[N][D];
		F=new double[D];
		x=new double [D];
		this.D=D;
	}

	public double[] getPworsti() {
		return pworsti;
	}

	public void setPworsti(double[] pworsti) {
		this.pworsti = pworsti;
	}

	public double[] getPbesti() {
		return pbesti;
	}

	public void setPbesti(double[] pbesti) {
		this.pbesti = pbesti;
	}

	public double[] getA() {
		return a;
	}

	public void setA(double[] a) {
		this.a = a;
	}

	public double[] getV() {
		return v;
	}

	public void setV(double[] v) {
		this.v = v;
	}

	public double[][] getf() {
		return f;
	}

	public void setf(double[][] f) {
		this.f = f;
	}

	public double[] getF() {
		return F;
	}

	public void setF(double[] f) {
		this.F = f;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double[] getX() {
		return x;
	}

	public void setX(double[] x) {
		this.x = x;
	}

	public double getM() {
		return M;
	}

	public void setM(double M) {
		this.M = M;
	}

	public double getm() {
		return m;
	}

	public void setm(double m) {
		this.m = m;
	}
	
	public void setxi(int i,double value){
		this.x[i]=value;
	}
	public void setai(int i,double value){
		this.a[i]=value;
	}
	public void setvi(int i,double value){
		this.v[i]=value;
	}
	public void setpworstival(int i,double value){
		this.pworsti[i]=value;
	}
	public void setpbestival(int i,double value){
		this.pbesti[i]=value;
	}
	public void setfi(int i,int j,double value){
		this.f[i][j]=value;
	}
	public void setFi(int i,double value){
		this.F[i]=value;
	}
	public void updatePosition(int k){
		this.x[k]=this.x[k]+this.v[k];
//		this.x[k]=this.x[k]>30?30:this.x[k];
//		this.x[k]=this.x[k]<-30?-30:this.x[k];
	}
}
