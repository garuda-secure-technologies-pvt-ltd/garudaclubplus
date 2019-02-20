/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PdtConversionManager.java
 *
 * Created on Jan 31, 2009, 10:07:26 AM
 */

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
//import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.inventory.PdtConversionTable;
import java.awt.Dimension;
import java.util.Date;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author swathi
 */
public class PdtConversionManager extends javax.swing.JPanel implements JPanelView, BeanFactoryApp{

    /** Creates new form PdtConversionManager */
     private AppView m_App;
    private DataLogicSystem m_dlSystem;
     private SentenceList m_sentpdt;
    private ComboBoxValModel m_ProductModel1;
     private ComboBoxValModel m_ProductModel2;
    private DataLogicSales dlSales = null;
    private PdtConversionTable pdtconverion;
    private Session s;
    public PdtConversionManager() throws BasicException{
        initComponents();
      //  dlSales=new DataLogicSales() ;
          // m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        


    }
    
     public void init(AppView app) throws BeanFactoryException{
          m_App = app;

        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
          dlSales=(DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
         m_sentpdt=dlSales.getProductList();
        try{
         m_ProductModel1=new ComboBoxValModel(m_sentpdt.list());
        m_ProductModel2=new ComboBoxValModel(m_sentpdt.list());
        pdtqty1.setText(" ");
        pdtqty2.setText(" ");
        }
        catch(Exception e)
        {
        }

           pdtcontable.setDefaultRenderer(Object.class, new TableRendererBasic(
           new Formats[] {Formats.STRING, Formats.DOUBLE, Formats.STRING, Formats.DOUBLE, Formats.TIMESTAMP,Formats.STRING,Formats.BOOLEAN}));
         pdtcontable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane1 .getVerticalScrollBar().setPreferredSize(new Dimension(25,25));
         pdtcontable.getTableHeader().setReorderingAllowed(false);
         pdtcontable.setRowHeight(25);
        pdtcontable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

         productlist1.setModel(m_ProductModel1);
         productlist2.setModel(m_ProductModel2);
        pdtqty1.setEnabled(true);
        pdtqty1.setEditable(true);
        pdtqty2.setEnabled(true);
        pdtqty2.setEditable(true);
        pdtunittype1.setEnabled(false);
         pdtunittype2.setEnabled(false);
         productlist1.setEnabled(true);
         productlist2.setEnabled(true);

        
     }
     
      public Object getBean() {
        return this;
    }


     public String getTitle()
     {
         return "Product Conversion Manager";
     }
     public void activate() throws BasicException
     {
         loadData();
     }

     public void loadData () throws BasicException
     {
         pdtcontable.setModel(new DefaultTableModel());

        // LoadData
        pdtconverion = PdtConversionTable.loadInstance(m_App);
         pdtqty1.setEnabled(true);
         pdtqty1.setEditable(true);
         pdtqty2.setEnabled(true);
         pdtqty2.setEditable(true);
         productlist1.setModel(m_ProductModel1);
          productlist2.setModel(m_ProductModel2);
         pdtunittype1.setEnabled(false);
         pdtunittype2.setEnabled(false);
         productlist1.setEnabled(true);
         productlist2.setEnabled(true);

          pdtcontable.setModel(pdtconverion.getPdtConversionModel());

        TableColumnModel jColumns = pdtcontable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(200);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(50);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(2).setPreferredWidth(200);
        jColumns.getColumn(2).setResizable(false);
        jColumns.getColumn(3).setPreferredWidth(50);
        jColumns.getColumn(3).setResizable(false);
        jColumns.getColumn(4).setPreferredWidth(80);
        jColumns.getColumn(4).setResizable(false);
        jColumns.getColumn(5).setPreferredWidth(100);
        jColumns.getColumn(5).setResizable(false);
        jColumns.getColumn(6).setPreferredWidth(80);
        jColumns.getColumn(6).setResizable(false);


     }
      public JComponent getComponent() {
        return this;
    }

   private int checkForProduct(String pdt){
    //   Object[] obj;
       try{
         Object[]  obj= (Object[])new  StaticSentence(m_App.getSession()
            , "SELECT COUNT(*) FROM CONVERTER WHERE ACTIVE = TRUE AND (PRODUCTFST = ?  OR  PRODUCTSCN = ?) "
            , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING})
            ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find(new Object[]{pdt,pdt});


       if(obj!=null){
          return Integer.parseInt(obj[0].toString());
       }//else

           }
       catch(Exception e){
       }
        return 0;
   }

    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate
        return true;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productlist1 = new javax.swing.JComboBox();
        productlist2 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pdtunittype1 = new javax.swing.JTextField();
        pdtunittype2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        pdtqty1 = new javax.swing.JTextField();
        pdtqty2 = new javax.swing.JTextField();
        add = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pdtcontable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        productlist1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productlist1ItemStateChanged(evt);
            }
        });

        productlist2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productlist2ItemStateChanged(evt);
            }
        });

        jLabel1.setText("Product 1 :");

        jLabel2.setText("Product 2 :");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Name");

        pdtunittype1.setEditable(false);

        pdtunittype2.setEditable(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Unit Type");

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Ratio");

        pdtcontable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(pdtcontable);

        jButton1.setText("Deactivate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(576, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(productlist1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(productlist2, 0, 254, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(add)))
                .addGap(85, 85, 85)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pdtunittype1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pdtunittype2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                    .addComponent(jLabel4))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pdtqty2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pdtqty1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                    .addComponent(jLabel5))
                .addGap(120, 120, 120))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productlist1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(pdtqty1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdtunittype1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productlist2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(pdtqty2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdtunittype2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(31, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private String pdtid1;
    private String pdtid2;
    private void productlist1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productlist1ItemStateChanged
        // TODO add your handling code here:
        Object pdt1=productlist1.getSelectedItem();
        if(pdt1!=null)
        {
        String selectedItem=pdt1.toString();
         String[] temparr=selectedItem.split(" - ");
        try{
         Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT UNITTYPE,ID FROM PRODUCTS WHERE NAME = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING,Datas.STRING})).find(temparr[1]);
         if(obj1!=null){
             if(obj1[0]!=null)
             {
                 Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM UNIT WHERE ID = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(obj1[0].toString());

                 pdtunittype1.setEnabled(true);
                 pdtunittype1.setText(obj[0].toString());
             }
             pdtid1=obj1[1].toString();
         }

        }

         catch(Exception e){
         }
        }

    }//GEN-LAST:event_productlist1ItemStateChanged

    private void productlist2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productlist2ItemStateChanged
        // TODO add your handling code here:
        Object pdt2=productlist2.getSelectedItem();
        if(pdt2!=null)
        {
         String selectedItem=pdt2.toString();
         String[] temparr=selectedItem.split(" - ");
        try{
         Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT UNITTYPE,ID FROM PRODUCTS WHERE NAME = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING,Datas.STRING})).find(temparr[1]);
         if(obj1!=null){
             if(obj1[0]!=null)
             {
                  Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM UNIT WHERE ID = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(obj1[0].toString());
                  pdtunittype2.setEnabled(true);
                 pdtunittype2.setText(obj[0].toString());
             }
             pdtid2=obj1[1].toString();
         }

        }

         catch(Exception e){
         }
        }
    }//GEN-LAST:event_productlist2ItemStateChanged

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        // TODO add your handling code here:
        int count1=checkForProduct(pdtid1);
        int count2=checkForProduct(pdtid2);
        if(count1==0 && count2==0){
        if(!pdtqty1.getText().equals(" ") && !pdtqty2.getText().equals(" ") && productlist1.getSelectedItem()!=null && productlist2.getSelectedItem()!=null)
        {
        String selecteditem1=productlist1.getSelectedItem().toString();
         String selecteditem2=productlist2.getSelectedItem().toString();
         final Date dnow;
         int flag=0;
         if(selecteditem1.equals(selecteditem2))
         {
             //warning message
             JOptionPane.showMessageDialog(this,"Both the selected item is same", "Error", JOptionPane.WARNING_MESSAGE);
         }
         else
         {
          //   int del=pdtqty1.getText().length();
          //   String del1=pdtqty2.getText();
             if(!pdtqty1.getText().equals(" ") && !pdtqty2.getText().equals(" "))
             {
              try{
              Object[] o= (Object[])new  StaticSentence(m_App.getSession()
            , "SELECT ID FROM CONVERTER WHERE PRODUCTFST = ?  AND  PRODUCTSCN = ? AND ACTIVE = TRUE"

            , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING})
            ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find(new Object[]{pdtid1,pdtid2});

                Object[] o1= (Object[])new  StaticSentence(m_App.getSession()
            , "SELECT ID FROM CONVERTER WHERE PRODUCTFST = ?  AND  PRODUCTSCN = ? AND ACTIVE = TRUE"

            , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING})
           ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find(new Object[]{pdtid2,pdtid1});
              dnow=new Date();

              if(o==null && o1==null)
              {

               SentenceExec unitInsert = new PreparedSentence(LookupUtilityImpl.getInstance(null).getAppView().getSession()
                                   ,"INSERT INTO CONVERTER(ID,PRODUCTFST,NOFST,PRODUCTSCN,NOSEC,CREATEDDATE,USER_,ACTIVE) VALUES(?,?,?,?,?,?,?,?)"
                                   ,SerializerWriteParams.INSTANCE);
                           unitInsert.exec(new DataParams() {
                               public void writeValues() throws BasicException {
                                   setString(1,UUID.randomUUID().toString());
                                   setString(2,pdtid1 );
                                   setDouble(3,Double.parseDouble(pdtqty1.getText()));
                                   setString(4,pdtid2 );
                                   setDouble(5,Double.parseDouble(pdtqty2.getText()));
                                   setTimestamp(6, dnow);
                                   setString(7, m_App.getAppUserView().getUser().getName().toString());
                                   setBoolean(8,true);
                               }
                           });
                           
                            pdtqty1.setText(" ");
                            pdtqty2.setText(" ");
                            pdtunittype1.setText(" ");
                             pdtunittype1.setText(" ");
                           //  productlist2.setSelectedItem("");
                          // productlist1.setSelectedItem("");
                            flag=1;
                             productlist2.setSelectedIndex(-1);
                              productlist1.setSelectedIndex(-1);
                              loadData();
              }
              else
              {
                   JOptionPane.showMessageDialog(this,"Products Already exist", "Cannot Insert", JOptionPane.OK_OPTION);
                    pdtqty1.setText(" ");
                            pdtqty2.setText(" ");
                             pdtunittype1.setText(" ");
                             pdtunittype1.setText(" ");
                           //  productlist2.setSelectedItem("");
                          // productlist1.setSelectedItem("");
                            flag=1;
                             productlist2.setSelectedIndex(-1);
                              productlist1.setSelectedIndex(-1);
              }
                            
             }
             catch(Exception e)
             {
                // e.printStackTrace();
                 if(flag==1)
                  productlist1.setSelectedIndex(-1);
                 try{
                 loadData();
                 }
                 catch(Exception e1){}
             }
             }
             else
             {
                   JOptionPane.showMessageDialog(this,"Please specify the quantities", "Cannot Insert", JOptionPane.OK_OPTION);
             }
         }
        }else
                 JOptionPane.showMessageDialog(this,"Please fill the form compeletely", "Cannot Insert", JOptionPane.OK_OPTION);
        }
        else{
              JOptionPane.showMessageDialog(this,"Product already exist", "Cannot Insert", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_addActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
      int row=  pdtcontable.getSelectedRow();
      Boolean status=(Boolean)pdtcontable.getValueAt(row, 6);
      String pdtfst=pdtcontable.getValueAt(row, 0).toString();
       String pdtsec=pdtcontable.getValueAt(row, 2).toString();
      if(status==true)
      {
          status=false;
          
        Datas[] datas = new Datas[] {Datas.BOOLEAN, Datas.STRING, Datas.STRING};
        try{
            Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID FROM PRODUCTS WHERE NAME = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(pdtfst);
              Object[] obj2=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID FROM PRODUCTS WHERE NAME = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(pdtsec);
              if(obj1!=null && obj2!=null)
              {
               Object[] values = new Object[] {status,obj1[0].toString(),obj2[0].toString()};
        new PreparedSentence(m_App.getSession()
                , "UPDATE CONVERTER SET ACTIVE = ? WHERE PRODUCTFST = ? AND PRODUCTSCN=?"
                , new SerializerWriteBasicExt(datas, new int[] {0,1, 2})).exec(values);
              }
              String temp="";
               loadData ();
        }
        catch(Exception e){
            e.printStackTrace();
                    }
       
      }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable pdtcontable;
    private javax.swing.JTextField pdtqty1;
    private javax.swing.JTextField pdtqty2;
    private javax.swing.JTextField pdtunittype1;
    private javax.swing.JTextField pdtunittype2;
    private javax.swing.JComboBox productlist1;
    private javax.swing.JComboBox productlist2;
    // End of variables declaration//GEN-END:variables

}
