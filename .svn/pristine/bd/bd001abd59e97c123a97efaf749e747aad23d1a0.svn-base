/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BillList.java
 *
 * Created on Dec 12, 2008, 11:47:36 AM
 */

package com.openbravo.pos.sales.restaurant;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.sales.BillInfo;
import com.openbravo.pos.sales.BillListTableModel;
import com.openbravo.pos.sales.BillLogic;
import com.openbravo.pos.sales.Billpage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author swathi
 */
public class BillList extends javax.swing.JDialog {

    private BillListTableModel billtablemodel;
    private CounterTotalsTableModel counterTotalmodel;
    private List<BillInfo> blist;
    private BillLogic blogic;
    private DataLogicSales dlSales;
    private boolean resultok = false;
    private CustomerInfo customer;
    /** Creates new form BillList */
    public BillList(java.awt.Frame parent, DataLogicSales dlSales, BillLogic blogic, CustomerInfo customer) {
        super(parent, true);
        this.dlSales = dlSales;
        this.blogic = blogic;
        this.customer = customer;
      }

     public BillList(java.awt.Dialog parent, DataLogicSales dlSales, BillLogic blogic, CustomerInfo customer) {
        super(parent, true);
        this.dlSales = dlSales;
        this.blogic = blogic;
        this.customer = customer;
  
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

      public static BillList getDialog(Component parent, DataLogicSales dlSales, BillLogic blogic, CustomerInfo customer) {

        Window window = getWindow(parent);

        BillList mybilllogic;

        if (window instanceof Frame) {
            mybilllogic = new BillList((Frame) window, dlSales, blogic, customer);
        } else {
            mybilllogic = new BillList((Dialog) window, dlSales, blogic, customer );
        }

        return mybilllogic;
    }

    private void loadUnpaidBills(List<BillInfo> bl) {
        blist = new ArrayList<BillInfo>();
        double amount=0.0;
       // for (BillInfo b : bl) {
       //    amount+=b.getAmount();
       // }
      //  ptotal.setText(String.valueOf(For))
        blist.addAll(bl);
    }

    private void refreshModel() throws BasicException {
        loadUnpaidBills(blogic.getBillList1());
        ((BillListTableModel) jTable1.getModel()).refresh(blist);
        List<CounterTotals> list=blogic.getPendingBillTotal();
        if(list==null || list.size()<=0)
        list=new ArrayList<CounterTotals>();
       // counterTotalmodel=new CounterTotalsTableModel(list);
        ((CounterTotalsTableModel)jTable2.getModel()).refresh(list);
    }

    public void init() throws BasicException {

        loadUnpaidBills(blogic.getBillList1());

        initComponents();
        List<CounterTotals> list=blogic.getPendingBillTotal();
        if(list==null || list.size()<=0)
        list=new ArrayList<CounterTotals>();
        counterTotalmodel=new CounterTotalsTableModel(list);
        jTable2.setModel(counterTotalmodel);
        billtablemodel = new BillListTableModel(blist);
        jTable1.setModel(billtablemodel);

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(2).setPreferredWidth(60);
        columnModel.getColumn(2).setMaxWidth(60);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(80);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(4).setMaxWidth(100);
         columnModel.getColumn(5).setPreferredWidth(120);
        columnModel.getColumn(5).setMaxWidth(120);
         columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(6).setMaxWidth(80);


    }

       public boolean showDialog()
       {
           try {
               init();
               setVisible(true);
           } catch (BasicException e) {
               new MessageInf(e).show(getParent());
           }
           return resultok;
       }

       public void displayBill() {
        try {
            int i = jTable1.getSelectedRow();
            if(i >= 0 && i < blist.size())
            {
                BillInfo b = blist.get(i);
                Billpage billpage = Billpage.getDialog(this, dlSales, null, b.getCustomer());
                billpage.init(b);
                billpage.showDialog();
                refreshModel();
            }
        } catch (BasicException e) {
            new MessageInf(e).show(this);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent)c;
                    jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                    if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                        jc.setBackground(Color.cyan);
                    }
                    else {
                        jc.setBackground(Color.white);
                    }
                    if(isCellSelected(rowIndex, vColIndex))
                    jc.setBackground(Color.lightGray);
                }
                return c;
            }};
            jButton1 = new javax.swing.JButton();
            jButton2 = new javax.swing.JButton();
            jScrollPane2 = new javax.swing.JScrollPane();
            jTable2 = new javax.swing.JTable(){
                @Override
                public Component prepareRenderer(TableCellRenderer renderer,
                    int rowIndex, int vColIndex) {
                    Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                    if (c instanceof JComponent) {
                        JComponent jc = (JComponent)c;
                        jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                        if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                            jc.setBackground(Color.cyan);
                        }
                        else {
                            jc.setBackground(Color.white);
                        }
                        if(isCellSelected(rowIndex, vColIndex))
                        jc.setBackground(Color.lightGray);
                    }
                    return c;
                }};

                setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                setTitle("Pending Bill List");

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                    },
                    new String [] {
                        "Sl No", "Member Name", "Member ID", "Bill No", "Date"
                    }
                ) {
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false
                    };

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });
                jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        jTable1MouseClicked(evt);
                    }
                });
                jScrollPane1.setViewportView(jTable1);

                jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
                jButton1.setText("Select");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });

                jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
                jButton2.setText("Cancel");
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton2ActionPerformed(evt);
                    }
                });

                jTable2.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] {
                        {},
                        {},
                        {},
                        {}
                    },
                    new String [] {

                    }
                ));
                jTable2.getTableHeader().setReorderingAllowed(false);
                jScrollPane2.setViewportView(jTable2);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(26, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(11, 11, 11))
                );

                pack();
            }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        displayBill();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()>1){
        displayBill();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    /**
    * @param args the command line arguments
    */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

}
