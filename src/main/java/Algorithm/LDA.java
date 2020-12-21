package Algorithm;

import java.util.Vector;

public class LDA {
     /** 原始矩阵 */
     private final double[][] A1;
     private final double[][] A2;

    /** 矩阵平均值向量 */
     private final double[] M1;
     private final double[] M2;

     /** 离差矩阵 */
     private final double[][] A1s;
     private final double[][] A2s;

    /** 常数列向量 */
    private final double[]C;

    /** 解参数 */
    private double[]X;

    /** 带入判别方程后的结果 */
    private double y1;
    private double y2;

    /** 临界值 */
    private double y0;

    /** 测试数据计算结果y集合 */
    private Vector<Double>y_vector;

    /** 预测类别集合 */
    private Vector<Integer>testLab_vector;

    /** 真实类别集合 */
    private Vector<Integer>trueLab_vector;

    /** 预测正确率 */
    double percent;
    public LDA(double[][] a1, double[][] a2) {
        y_vector=new Vector<Double>();
        testLab_vector=new Vector<Integer>();
        trueLab_vector=new Vector<Integer>();
        A1 = a1;
        A2 = a2;
        A1s=new double[A1.length][64];
        A2s=new double[A2.length][64];
        M1=new double[64];
        M2=new double[64];
        C=new double[64];
        /* 计算平均值向量 */
        for (double[] doubles : A1) {
            for (int j = 0; j < 64; j++) {
                M1[j] += doubles[j];
            }
        }
        for(int i=0;i<64;i++){
               M1[i]=M1[i]/ A1.length;
        }
        for (double[] doubles : A2) {
            for (int j = 0; j < 64; j++) {
                M2[j] += doubles[j];
            }
        }
        for(int i=0;i<64;i++){
              M2[i]=M2[i]/ A2.length;
        }
    }

    public void setA1s() {
        for(int i=0;i< A1.length;i++)
            for(int j=0;j<64;j++){
                A1s[i][j]=A1[i][j]-M1[j];
            }
    }

    public void setA2s() {
        for(int i=0;i< A2.length;i++)
            for(int j=0;j<64;j++){
                A2s[i][j]=A2[i][j]-M2[j];
            }
    }

    public void setC() {
        for (int i = 0; i < 64; i++) {
            C[i] = M1[i] - M2[i];
        }
    }

    public void setY0() {
        this.y0=getEdge();
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }

    public double getY0() {
        return y0;
    }

    public double getPercent() {
        return percent;
    }

    public Vector<Double> getY_vector() {
        return y_vector;
    }

    public Vector<Integer> getTestLab_vector() {
        return testLab_vector;
    }

    public Vector<Integer> getTrueLab_vector() {
        return trueLab_vector;
    }

    private double[][] MatrixSelfMultiply(double[][]A){
         double[][]res=new double[64][64];
         for(int i=0;i<64;i++){
             for (double[] doubles : A) {
                 for (int k = 0; k < 64; k++) {
                     res[i][k] += doubles[i] * doubles[k];
                 }
             }
         }
         return res;
    }
    private double[][] MatrixAdd(double[][]A1,double[][]A2){
        double[][]res=new double[64][64];
        for(int i=0;i<64;i++)
            for(int j=0;j<64;j++){
                res[i][j]=A1[i][j]+A2[i][j];
            }
        return res;
    }
    private double[]getX(double[][]A,double[]C){
        double [][]Matrix=new double[64][65];
        for(int i=0;i<64;i++){
            for(int j=0;j<65;j++){
                if(j!=64) {
                    Matrix[i][j] = A[i][j];
                }else{
                    Matrix[i][64]=C[i];
                }
            }
        }
        double save1=0;
        double save2=0;
        for(int k=0;k<64;k++){
            for(int j=k+1;j<64;j++){
                save1=Matrix[j][k];
                save2=Matrix[k][k];
                for(int m=0;m<65;m++){
                    Matrix[j][m]-=Matrix[k][m]*save1/save2;
                }
            }
        }
        double[]x=new double[64];

        for(int i=63;i>=0;i--){
            double sum=0;
            for(int k=0;k<63;k++){
                if(k==i)
                    continue;
                sum+=x[k]*Matrix[i][k];
            }
            x[i]=(Matrix[i][64]-sum)/Matrix[i][i];
        }
        return x;
    }

    public double getEdge(){
        double avgY1=0;
        double avgY2=0;
        setA1s();
        setA2s();
        setC();
        double[][]S=MatrixAdd(MatrixSelfMultiply(A1s),MatrixSelfMultiply(A2s));
        X=getX(S,C);
        for(int i=0;i<64;i++){
            avgY1+=M1[i]*X[i];
            avgY2+=M2[i]*X[i];
        }
        y1=avgY1;
        y2=avgY2;
        return (avgY1+avgY2)/2;
    }

    public int sortDataSet(double[]dataSet,boolean out){
        double avg=0;
        for(int i=0;i<64;i++){
            avg+=dataSet[i]*X[i];
        }
        int a1_Mark = 1;
        int a2_Mark = -1;
        if(out)
            y_vector.add(avg);
        if(avg>=y0){
           return y1>y0? a1_Mark : a2_Mark;
        }else{
           return y1<y0? a1_Mark : a2_Mark;
        }
    }
    public void outPut( dataImport data){
        setY0();
//        System.out.println("y0:"+this.getY0());
//        System.out.println("y1:"+this.getY1());
//        System.out.println("y2:"+this.getY2());
        double success=0;
        for(int i=0;i<data.getTestSize();i++){
            //System.out.println("预测类别:"+this.sortDataSet(data.getTest()[i],true));
            testLab_vector.add(this.sortDataSet(data.getTest()[i],true));
            trueLab_vector.add(data.getTestLab()[i]);
            if(this.sortDataSet(data.getTest()[i],false)==(int)data.getTestLab()[i])
                success++;
        }
       percent=success/data.getTestSize()*100;
    }
    public String statistics(int size, dataImport data){
        double success=0;
        for(int i=0;i<data.getTestSize();i++){
            this.sortDataSet(data.getTest()[i],false);
            if(this.sortDataSet(data.getTest()[i],false)==data.getTestLab()[i])
                success++;
        }
        percent=success/data.getTestSize()*100;
        System.out.println("预测正确率:"+percent+"%");
        return new String(percent+"");
    }

    public static void main(String[] args) {
        dataImport data = new dataImport(225);
        LDA test = new LDA(data.getA1(), data.getA2());
        test.outPut(data);


    }
}
