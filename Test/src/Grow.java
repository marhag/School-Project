import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Grow extends JFrame {

    private static final Object[][] rowData = {{"Hello", "World"}};
    private static final Object[] columnNames = {"A", "B"};

    private Object[][] rad = new Object[1][2];
    
    private JTable table;
    private DefaultTableModel model;

    public Grow() {
    	rad[0][0] = "hei";
    	rad[0][1] = "hade";
         Container c = getContentPane();
         c.setLayout(new BorderLayout());

         model = new DefaultTableModel(rowData, columnNames);
         table = new JTable();
         table.setModel(model);
         c.add(new JScrollPane(table), BorderLayout.CENTER);
         JButton add = new JButton("Add");
         c.add(add, BorderLayout.SOUTH);
         add.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent ae) {
                 model.addRow(rad[0]);
             }
         });
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         pack(); 
    }

   /* public static void main(String[] args) {
        Grow g = new Grow();
        g.setLocationRelativeTo(null);
        g.setVisible(true);
    }*/
}