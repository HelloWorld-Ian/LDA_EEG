import Algorithm.LDA;
import Algorithm.dataImport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UI extends JFrame {
        JTextField trainItems;
        JTextField y0;
        JTextField y1;
        JTextField y2;
        JTextField predict;
        JButton start;
        JTable data;
        DefaultTableModel dataCollect=new DefaultTableModel();

    public UI() throws HeadlessException {
        JButton c=new JButton(" ");
        c.setContentAreaFilled(false);		//按键透明
        c.setBorderPainted(false);		//边框透明
        c.setEnabled(false);

        JLabel title=new JLabel("脑信号二分类");
        title.setLocation(200,-100);
        title.setFont(new Font(null, Font.PLAIN,80));
        title.setSize(600,300);
        this.add(title);

        start=new JButton("开始");
        start.setSize(90,40);
        start.setLocation(530,125);
        this.add(start);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addData(Integer.parseInt(trainItems.getText()));
            }
        });

        JButton clear=new JButton("清空");
        clear.setSize(90,40);
        clear.setLocation(670,125);
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                trainItems.setText("");
                y0.setText("");
                y1.setText("");
                y2.setText("");
                predict.setText("");
                dataCollect.setRowCount(0);

            }
        });
        this.add(clear);

        JLabel trainItemsLabel=new JLabel("建模数据量");
        trainItemsLabel.setLocation(160,32);
        trainItemsLabel.setFont(new Font(null, Font.PLAIN,20));
        trainItemsLabel.setSize(300,225);
        this.add(trainItemsLabel);

        trainItems=new JTextField();
        trainItems.setSize(140,30);
        trainItems.setLocation(290,130);
        trainItems.setFont(new Font(null, Font.PLAIN,20));
        this.add(trainItems);


        JLabel y1Label=new JLabel("y1");
        y1Label.setLocation(60,167);
        y1Label.setFont(new Font(null, Font.PLAIN,20));
        y1Label.setSize(300,75);
        this.add(y1Label);

        y1=new JTextField();
        y1.setSize(100,30);
        y1.setLocation(100,190);
        y1.setFont(new Font(null, Font.PLAIN,20));
        this.add(y1);

        JLabel y2Label=new JLabel("y2");
        y2Label.setLocation(260,167);
        y2Label.setFont(new Font(null, Font.PLAIN,20));
        y2Label.setSize(290,75);
        this.add(y2Label);

        y2=new JTextField();
        y2.setSize(100,30);
        y2.setLocation(300,190);
        y2.setFont(new Font(null, Font.PLAIN,20));
        this.add(y2);

        JLabel y0Label=new JLabel("y0");
        y0Label.setLocation(460,167);
        y0Label.setFont(new Font(null, Font.PLAIN,20));
        y0Label.setSize(290,75);
        this.add(y0Label);

        y0=new JTextField();
        y0.setSize(100,30);
        y0.setLocation(500,190);
        y0.setFont(new Font(null, Font.PLAIN,20));
        this.add(y0);

        JLabel predictLabel=new JLabel("正确率");
        predictLabel.setLocation(630,167);
        predictLabel.setFont(new Font(null, Font.PLAIN,20));
        predictLabel.setSize(290,75);
        this.add(predictLabel);

        predict=new JTextField();
        predict.setSize(100,30);
        predict.setLocation(700,190);
        predict.setFont(new Font(null, Font.PLAIN,20));
        this.add(predict);




      dataCollect=new DefaultTableModel();
      JTable table=new JTable(dataCollect);
      //addData();
      JScrollPane test=new JScrollPane();
      test.setSize(600,300);
      test.setLocation(130,300);
      test.setViewportView(table);
      this.add(test);

    }

    private void addData(int trainItems){
        Vector<String>columns=new Vector<String>()
        {{add("编号");add("y");add("预测类型");add("真实类型");}};

        Vector<Vector<String>>rowData=new Vector<Vector<String>>();
        dataImport data = new dataImport(trainItems);
        LDA test = new LDA(data.getA1(), data.getA2());
        test.outPut(data);

        int size=test.getY_vector().size();
        Vector<Double>y_Vector=test.getY_vector();
        Vector<Integer>test_Vector=test.getTestLab_vector();
        Vector<Integer>true_Vector=test.getTrueLab_vector();
        for(int i=0;i<size;i++){
            Vector<String>row=new Vector<String>();
            String convert=null;
            int remain=0;
            row.add(Integer.toString(i));
            convert=Double.toString(y_Vector.get(i));
            remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
            row.add(convert.substring(0,remain));
            convert=Double.toString(test_Vector.get(i));
            remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
            row.add(convert.substring(0,remain));
            convert=Double.toString(true_Vector.get(i));
            remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
            row.add(convert.substring(0,remain));
            rowData.add(row);
        }
        dataCollect.setDataVector(rowData,columns);
        String convert="";
        int remain=0;
        convert=Double.toString(test.getY1());
        remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
        y1.setText(convert.substring(0,remain));
        convert=Double.toString(test.getY2());
        remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
        y2.setText(convert.substring(0,remain));
        convert=Double.toString(test.getY0());
        remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+6:convert.length()-1;
        y0.setText(convert.substring(0,remain));
        convert=Double.toString(test.getPercent());
        remain=convert.indexOf('.')+3<convert.length()?convert.indexOf('.')+3:convert.length()-1;
        predict.setText(convert.substring(0,remain)+"%");
    }
    public void launchFrame(){
        this.setLocation(100,100);
        this.setSize(900,700);
        this.setLayout(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
               new UI().launchFrame();
    }
}
