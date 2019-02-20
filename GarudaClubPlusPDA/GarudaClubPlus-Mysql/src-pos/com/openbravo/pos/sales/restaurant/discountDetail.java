package com.openbravo.pos.sales.restaurant;


import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWriteBasic;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.QTicketLineInfo;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.ticket.ProductInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * discountdetail.java
 *
 * Created on Jan 9, 2009, 1:18:31 PM
 */

/**
 *
 * @author swathi
 */
public class discountDetail extends javax.swing.JDialog {

    /** Creates new form discountdetail */
    QTicketLineInfo qtline;
    DataLogicSales dlsales;
    public boolean resultok=false;
    private AppView app;
    private  String discountid;
    private String customername="";
    private String userid;
    private String productid;
    private int qty;
    private double rate;
    private double amount;
    private String reason;
    private String authorised="";
    private String qtitemid;
    private String productname;
    private int i=0;
    private int totalqty;
    private int limit;
  //  public discountDetail de=new discountDetail();



    protected discountDetail(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    public  discountDetail(Frame parent,DataLogicSales dlSales, QTicketLineInfo qtline,AppView app)
    {
        super(parent,true);
        dlsales=dlSales;
        this.qtline=qtline;
         this.app=app;
         this.limit=qtline.getMultiply();
           this.limit = limit > 0 ? -1 * limit : limit;
       //  i=qtline.getMultiply();*/
         initComponents();
    }
     public  discountDetail(Dialog parent,DataLogicSales dlSales, QTicketLineInfo qtline,AppView app)
    {
        super(parent,true);
        dlsales=dlSales;
        this.qtline=qtline;
        this.app=app;
         this.limit=qtline.getMultiply();
           this.limit = limit > 0 ? -1 * limit : limit;
        initComponents();
    }



    protected discountDetail(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
      protected static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }


    public static discountDetail getDialog(Component parent,DataLogicSales dlSales, QTicketLineInfo qtline,AppView app) {

        Window window = getWindow(parent);

       discountDetail dP;

        if (window instanceof Frame) {
            dP = new discountDetail((Frame) window, dlSales, qtline,app);
        } else {
            dP = new discountDetail((Dialog) window, dlSales, qtline,app);
        }

        return dP;
    }
    public  boolean showDialog(String customer,String qtid) throws BasicException {
        
        discountid=java.util.UUID.randomUUID().toString();

//         this.jTextField2.setEnabled(true);
        customername=customer;
        qtitemid=qtid;
         ProductInfoExt pi =  dlsales.getProductInfo(qtline.getProduct());

          ProductInfo pInfo = LookupUtilityImpl.getInstance(null).getProductsMap().get(qtline.getProduct());
         userid=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
          this.jTextField1.setEnabled(true);
        jTextField1.setText(customer);
         jTextField2.setEnabled(true);
         jTextField2.setText(userid);
        jTextField3.setEnabled(true);
        productid=qtline.getProduct();
        totalqty=qtline.getMultiply();
                //qtline.getProduct();
        jTextField3.setText(pInfo.getName());
        qty=qtline.getMultiply();
        jTextField4.setEnabled(true);
      




        jTextField4.setText(""+0);
        jTextField5.setEnabled(true);
        rate=qtline.getRate();
        jTextField5.setText(""+rate);
         jTextField6.setEnabled(true);
         amount= qtline.getMultiply() * qtline.getRate();
         jTextField6.setText(""+amount);
          jTextArea1.setEnabled(true);
          jTextArea1.setEditable(true);
        jTextArea1.setText(" ");
          jButton1.setEnabled(true);
         
          this.setVisible(true);
         return resultok;
          
    }
    public static class detail implements SerializableRead
    {
        private  String name;
        private int qty;
         public void readValues(DataRead dr) throws BasicException {
           name = dr.getString(1);
           qty = dr.getInt(2);
        }
        public String getname()
        {
            return name;
        }
        public int getqty()
        {
            return qty;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Customer Name :");

        jLabel2.setText("User Name       : ");

        jLabel3.setText("Product Name  :");

        jTextField3.setEditable(false);

        jLabel4.setText("Quantity          :");

        jLabel5.setText("Rate               :");

        jTextField5.setEditable(false);

        jLabel6.setText("Amount          :");

        jTextField6.setEditable(false);

        jLabel7.setText("Reason         :");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);

        jTextField2.setEditable(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnminus.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField4.setBackground(new java.awt.Color(236, 233, 216));
        jTextField4.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4)
                            .addComponent(jTextField6)
                            .addComponent(jTextField5)
                            .addComponent(jTextField1)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, 0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(jButton1)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(50, 50, 50)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      //  dispose();
        int qtytotal=0;
        String tempstr;
        reason=jTextArea1.getText();
        qty=Integer.parseInt(jTextField4.getText());
        qty*=-1;
        List<detail> arr1=new ArrayList<detail>();

        if(reason.equals(" "))
        {
           JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.reasonwarning"), AppLocal.getIntString("message.reasontitle"), JOptionPane.WARNING_MESSAGE);
            // dispose();
        }
        else
        {
           /*  Object[] obj = (Object [] )  new StaticSentence(app.getSession()
                        , "SELECT SUM(QTY) FROM DISCOUNTLIST WHERE QTITEMID=? AND PRODUCT=? "
                        , SerializerWriteString.INSTANCE
                ,new SerializerReadBasic(new Datas[] {Datas.STRING,Datas.STRING})).find(qtitemid,productid);
           Object[] obj1=( Object []) new StaticSentence(app.getSession()
                    ,"SELECT SUM(QTY) FROM DISCOUNTLIST WHERE QTITEMID=? AND PRODUCT=?"
                    ,SerializerWriteString.INSTANCE
                    ,new SerializerReadBasic (new Datas[] {Datas.STRING,Datas.STRING})).find(qtitemid,productid);*/
            try
            {
               
               // List<detail> arr=new ArrayList();
          List  arr =   new StaticSentence(app.getSession()
                        , "SELECT PRODUCT_ID,SUM(QTY) FROM DISCOUNTLIST WHERE QTITEMID = ? GROUP BY QTITEMID,PRODUCT_ID  "
                        , SerializerWriteString.INSTANCE
                ,new SerializerReadClass(discountDetail.detail.class)).list(qtitemid);
             
          String name,qty;
          detail detailtemp;
          int i=0,j=0;
          if(arr!=null)
              arr1=arr;
     if(arr != null )     {
      for(i=0;i<(arr1.size());i++)
      {
          detailtemp=arr1.get(i);
         if(detailtemp.getname().equals(productid))
         {
            // qty=obj1[i+j+1].toString();
             qtytotal=detailtemp.getqty();
             break;
         }
         // j=2;
      }
          }
     else
     {
              qtytotal=0;
     }
          
      
         

            }
            catch(BasicException e)
            {
             String aa="aa";
            }
           if(totalqty<(qtytotal+qty))
            {
                 JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.qtywarning"), AppLocal.getIntString("message.qtytitle"), JOptionPane.WARNING_MESSAGE);
                 // dispose();
            }

            else if(qty==0)
                {
                 JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.qtyzerowarning"), AppLocal.getIntString("message.qtytitle"), JOptionPane.WARNING_MESSAGE);
                // dispose();
            }
            else
            {
      //  customername="aaa";
       authorised=null;

        try{
        Date d=new Date();
        amount =rate * qty;
         Object[] values = new Object[] {discountid,customername,userid,productid,qty,rate,amount,reason,authorised,qtitemid,d};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.DOUBLE, Datas.DOUBLE,Datas.STRING, Datas.STRING,Datas.STRING,Datas.TIMESTAMP};
        new PreparedSentence(app.getSession()
                , "INSERT INTO DISCOUNTLIST (ID,CUSTOMER_ID,USER_ID,PRODUCT_ID,QTY,RATE,AMOUNT,REASON,AUTHORISED,QTITEMID,CRDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?)"
                , new SerializerWriteBasicExt(datas, new int[] {0,1, 2, 3, 4, 5, 6, 7, 8,9,10})).exec(values);
        }
        catch(BasicException e)
        {
            String aaa="aswd";
        }
  dispose();
            }
        }

        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
            if (i > limit) {
            i--;
            refresh();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
           if (i < 0) {
            i++;
            refresh();
        }
    }//GEN-LAST:event_jButton3ActionPerformed
 private void refresh() {
        jTextField4.setText(""+i);
         double changerate = Double.parseDouble(jTextField5.getText())*Integer.parseInt(jTextField4.getText());
         jTextField6.setText(String.valueOf(changerate));
         jButton1.setEnabled(true);
    }
    /**
    * @param args the command line arguments
    */
 /*   public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                discountdetail dialog = new discountdetail(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables

}
