package ip;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
//import sun.net.ftp.FtpClient;

public class Main implements ActionListener,MouseListener
{
        ServerRequestCheck src = new ServerRequestCheck();
        Thread src1= new Thread(src);
        MyTableModel model = new MyTableModel();
        refresh re = new refresh(model);            // refresh for receiving.
        Thread re1= new Thread(re);
        
        browse br = new browse ();
        File file;
        JFrame f;

	public JTextArea ta = new JTextArea(10,35),tb,err;
	JTextField tf;
        JLabel l1,l2,err1;
	JScrollPane sp,sp1,err2;
        JTable t ;
        JPanel p = new JPanel();
        JPanel p1 = new JPanel();
        JPanel errp = new JPanel();

        int[] total;
//        ClientFile cf;
        int totallength=0;
        String rowvalue=null;
        
        JButton b,b1,browse;
	String com;
        boolean stop = false;
	int v,h,i,j;
        public void actionPerformed(ActionEvent e)
        {   if(e.getSource()==b)
            {   total = t.getSelectedRows();
                totallength=0;
                rowvalue=null;
                System.out.println("Sending data to  ==>>  "+rowvalue);
                while(totallength<total.length)
                {   
                    rowvalue = t.getValueAt(total[totallength], 0) + "";
                    System.out.println("Sending data to  ==>>  "+rowvalue);
                    new Thread(new ClientFile(ta.getText(), tb.getText(), rowvalue)).start();
                    System.out.println("Sending data to  ==>>  "+rowvalue);
                    totallength++;
                }
                System.out.println("Sending data to  ==>>  "+rowvalue);
            }
            if(e.getSource()==b1)
            {   refresh_func();
            }
            if(e.getSource()==browse)
            {   List<String> path = new ArrayList<String>();
                int a=0;
                path = br.main();
                for (String value : path)
                {
                    tb.append(value);
                }
            }
        }
        public void body()
        {   
            src1.start();
            f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(null);
            tb = new JTextArea(10,35);
            err = new JTextArea(10,35);
            t = new JTable(model);
            t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            t.getTableHeader().setReorderingAllowed(false);
            //t.addMouseListener(this);
            v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
            h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
            ta.getAutoscrolls();
            ta.setLineWrap(true);
            tb.getAutoscrolls();
            tb.setLineWrap(true);
            sp=new JScrollPane(ta,v,h);
            sp1=new JScrollPane(tb,v,h);
            err2=new JScrollPane(err,v,h);
            JScrollPane scrollpane = new JScrollPane(t);
            scrollpane.addMouseListener(this);
            b = new JButton("Send");
            b1 = new JButton("Refresh");
            browse = new JButton("Browse File");
            l1 = new JLabel("Send Message");
            l2 = new JLabel("Send File");
            err1 = new JLabel("Error");
            f.add(scrollpane);
            f.add(b);
            f.add(b1);
            f.add(browse);
            p.add(l1);
            p.add(sp);
            f.add(p);
            p1.add(l2);
            p1.add(sp1);
            f.add(p1);
            errp.add(err1);
            errp.add(err2);
            f.add(errp);
            f.setSize(1000,1000);
            f.setResizable(true);
            f.setVisible(true);
            scrollpane.setBounds(10, 10, 550, 350);
            b.setBounds(800, 10, 100, 50);
            b1.setBounds(800, 100, 100, 50);
            browse.setBounds(800, 200, 100, 50);
            p.setBounds(10,400,400,200);
            p1.setBounds(500,400, 400, 200);
            errp.setBounds(900,400, 400, 200);
            b1.addActionListener(this);
            b.addActionListener(this);
            browse.addActionListener(this);
        }
        public static void main(String[] args)
        {   Main base = new Main();
            base.body();
            base.refresh_func();
        }
        public void refresh_func()
        {
            if(re1.isAlive())
            {   model.delete();
                re1.interrupt();
                System.out.println("Killed.");
//                model = new MyTableModel();
//                t = new JTable(model);
//                re = new refresh(model);
                re1 = new Thread(re);
            }
            re1.start();
        }

        public void mousePressed(MouseEvent e) {
       //saySomething("Mouse pressed; # of clicks: "
         //           + e.getClickCount(), e);
    }

    public void mouseReleased(MouseEvent e) {
     //  saySomething("Mouse released; # of clicks: "
       //             + e.getClickCount(), e);
    }

    public void mouseEntered(MouseEvent e) {
    //   saySomething("Mouse entered", e);
    }

    public void mouseExited(MouseEvent e) {
      // saySomething("Mouse exited", e);
    }

    public void mouseClicked(MouseEvent e) {
      // saySomething("Mouse clicked (# of clicks: "
        //            + e.getClickCount() + ")", e);
        t.clearSelection();
    }

    void saySomething(String eventDescription, MouseEvent e) {
        System.out.println(eventDescription + " detected on "
                        + e.getComponent().getClass().getName()
                        + ".");
    }

}


class MyTableModel  extends AbstractTableModel{
        private String[] columnNames = {"Ip Address",
                                        "Host",
                                        "User"};
        private ArrayList<String[]> data = new ArrayList<String[]>();
        

        @Override
        public int getColumnCount() {
            
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }
//        public int getCoulumnNumber()
//        {   for(String i : columnNames)
//                if()
//            return Arrays.asList(columnNames).indexOf("Ip Address");
//            //return ArrayUtils.indexOf(columnNames, "Ip Address");
//        }
        public boolean isCellEditable(int row, int col) {
            if (col < 3) {
                return false;
            } else {
                return true;
            }
        }
        public void setValueAt(String[] value) {
            

            data.add(value);
            fireTableStructureChanged();

        }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    public boolean search(String[] chk)
    {
        Object chk1 = chk[0];
        System.out.println("The element being checked is : "+chk1);
        boolean ret = false;
        System.out.println("The data size is :   "+data.size());
        for(int i=0;i<data.size();i++)
        {
            if(chk[0].equalsIgnoreCase(data.get(i)[0]))
            {
                ret = true;
                break;
            }
        }
        return ret;
    }
    public void delete()
    {
        data.clear();
        fireTableStructureChanged();
//        try {
//                            System.in.read();
//                        } catch (IOException ex) {
//                            Logger.getLogger(refresh.class.getName()).log(Level.SEVERE, null, ex);
//                        }
        System.out.println("The current situation is : "+data.isEmpty());
    }
}