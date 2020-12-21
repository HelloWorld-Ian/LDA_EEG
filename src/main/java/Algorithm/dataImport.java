package Algorithm;
import javax.imageio.stream.FileImageInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class dataImport {
        private final int size;
        private final int testSize;
        private final double[][] a1;
        private final double[][] a2;
        private final double[][] test;
        private final int[]lab=new int[278];
        private final int[]testLab;
    public dataImport(int size) {
        this.size = size;
        this.testSize=278-size;
        test=new double[testSize][64];
        try {
            FileImageInputStream cin=new FileImageInputStream(new File("lab.txt"));
            String line=null;
            int index=0;
            while((line=cin.readLine())!=null){
                String[] lines=line.split("\\s+");
                lab[index]=Integer.parseInt(lines[0]);
                index++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        int a1_size=0;
        int a2_size=0;
        for(int i=0;i<size;i++){
             if(lab[i]==1){
                 a1_size++;
             }else if(lab[i]==-1){
                 a2_size++;
             }
        }
        a1=new double[a1_size][64];
        a2=new double[a2_size][64];
        testLab=new int[testSize];
        if (testSize >= 0) System.arraycopy(lab, size, testLab, 0, testSize);
        importData();
    }
    public void importData(){
            Queue<Double> a=new LinkedList<Double>();
            Queue<Double> b=new LinkedList<Double>();
            try {
                FileImageInputStream cin=new FileImageInputStream(new File("data.txt"));
                String line=null;
                while((line=cin.readLine())!=null) {
                    a.add(Double.parseDouble(line));
                }
                int labIndex=0;
                int a1_index=0;
                int a2_index=0;
                     for(int i=0;i<size;i++){
                         if(lab[labIndex]==1) {
                             for (int k = 0; k < 64; k++) {
                                 a1[a1_index][k] = a.poll();
                             }
                             a1_index++;
                         }else if(lab[labIndex]==-1) {
                             for (int k = 0; k < 64; k++) {
                                 a2[a2_index][k] = a.poll();
                             }
                             a2_index++;
                         }
                         labIndex++;
                     }

                     for(int i=0;i<testSize;i++){
                        for(int k=0;k<64;k++){
                        test[i][k]=a.poll();
                        }
                     }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public double[][] getA1() {
        return a1;
    }

    public double[][] getA2() {
        return a2;
    }

    public double[][] getTest() {
        return test;
    }

    public int[] getTestLab() {
        return testLab;
    }

    public int getTestSize() {
        return testSize;
    }
}
