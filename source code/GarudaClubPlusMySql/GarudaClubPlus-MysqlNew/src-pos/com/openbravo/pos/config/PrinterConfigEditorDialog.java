/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BillList.java
 *
 * Created on Dec 12, 2008, 11:47:36 AM
 */

package com.openbravo.pos.config;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.util.ReportUtils;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author swathi
 */
public class PrinterConfigEditorDialog extends javax.swing.JDialog {

    private int id;
    private Map<String, String> printerConfig;
    private boolean resultok = false;

    /** Creates new form BillList */
    public PrinterConfigEditorDialog(java.awt.Frame parent, int id, Map<String, String> printerConfig) {
        super(parent, true);
        this.id = id;
        this.printerConfig = printerConfig;
      }

     public PrinterConfigEditorDialog(java.awt.Dialog parent, int id, Map<String, String> printerConfig) {
        super(parent, true);
        this.id = id;
        this.printerConfig = printerConfig;
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

      public static PrinterConfigEditorDialog getDialog(Component parent, int id, Map<String, String> printerConfig) {
        Window window = getWindow(parent);

        PrinterConfigEditorDialog mybilllogic;

        if (window instanceof Frame) {
            mybilllogic = new PrinterConfigEditorDialog((Frame) window, id, printerConfig);
        } else {
            mybilllogic = new PrinterConfigEditorDialog((Dialog) window, id, printerConfig);
        }

        return mybilllogic;
    }

      public void init() throws BasicException {
        initComponents();

        jTextPrinterNo.setText(String.valueOf(id));
      //    String[] printernames = ReportUtils.getPrintNames();
     /*   for (String name : printernames) {
           jComboType.addItem(name);

        }*/

        jComboType.addItem("screen");
        jComboType.addItem("printer");
        jComboType.addItem("epson");
        jComboType.addItem("tmu220");
        jComboType.addItem("star");
        jComboType.addItem("ithaca");
        jComboType.addItem("surepos");
        jComboType.addItem("javapos");
        jComboType.addItem("Not defined");
        jComboType.setSelectedItem(printerConfig.get(JPanelConfigGeneral.PRINTER_CONFIG_TYPE));

        jComboConn.addItem("serial");
        jComboConn.addItem("file");
        jComboConn.setSelectedItem(printerConfig.get(JPanelConfigGeneral.PRINTER_CONFIG_CONN));

        jComboSerial.addItem("COM1");
        jComboSerial.addItem("COM2");
        jComboSerial.addItem("COM3");
        jComboSerial.addItem("COM4");
         jComboSerial.addItem("COM5");

        jComboSerial.addItem("LPT1");
        jComboSerial.addItem("/dev/ttyS0");
        jComboSerial.addItem("/dev/ttyS1");
        jComboSerial.addItem("/dev/ttyS2");
        jComboSerial.addItem("/dev/ttyS3");
        jComboSerial.setSelectedItem(printerConfig.get(JPanelConfigGeneral.PRINTER_CONFIG_SERIAL));

        jTextJPOSPrinter.setText(printerConfig.get(JPanelConfigGeneral.PRINTER_CONFIG_JPOSPRINTER));

        handlePrinterTypeChange();
    }

    private void handlePrinterTypeChange() {
        jComboConn.setEnabled(false);
        jComboSerial.setEnabled(false);
        jTextJPOSPrinter.setEnabled(false);
        String sMachineDisplay = (String) jComboType.getSelectedItem();
        if ("epson".equals(sMachineDisplay)  || "ld200".equals(sMachineDisplay) || "surepos".equals(sMachineDisplay) || "star".equals(sMachineDisplay)) {
            jComboConn.setEnabled(true);
            jComboSerial.setEnabled(true);
        } else if ("javapos".equals(sMachineDisplay)) {
            jTextJPOSPrinter.setEnabled(true);
        }
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextPrinterNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboType = new javax.swing.JComboBox();
        jComboConn = new javax.swing.JComboBox();
        jComboSerial = new javax.swing.JComboBox();
        jTextJPOSPrinter = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pending Bill List"); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        jButton1.setText("Ok"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jButton2.setText("Cancel"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Printer No"); // NOI18N

        jTextPrinterNo.setEnabled(false);

        jLabel2.setText("Printer Type"); // NOI18N

        jComboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboTypeActionPerformed(evt);
            }
        });

        jLabel3.setText("Mode"); // NOI18N

        jLabel4.setText("Port"); // NOI18N

        jLabel5.setText("Name"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextPrinterNo, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboConn, 0, 102, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboSerial, 0, 101, Short.MAX_VALUE))
                            .addComponent(jTextJPOSPrinter, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextPrinterNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboConn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboSerial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextJPOSPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        printerConfig.put(JPanelConfigGeneral.PRINTER_CONFIG_TYPE, (String) jComboType.getSelectedItem());
        if (jComboConn.isEnabled()) {
            printerConfig.put(JPanelConfigGeneral.PRINTER_CONFIG_CONN, (String) jComboConn.getSelectedItem());
        } else {
            printerConfig.remove(JPanelConfigGeneral.PRINTER_CONFIG_CONN);
        }
        if (jComboSerial.isEnabled()) {
            printerConfig.put(JPanelConfigGeneral.PRINTER_CONFIG_SERIAL, (String) jComboSerial.getSelectedItem());
        } else {
            printerConfig.remove(JPanelConfigGeneral.PRINTER_CONFIG_SERIAL);
        }
        if (jTextJPOSPrinter.isEnabled()) {
            printerConfig.put(JPanelConfigGeneral.PRINTER_CONFIG_JPOSPRINTER, (String) jTextJPOSPrinter.getText());
        } else {
            printerConfig.remove(JPanelConfigGeneral.PRINTER_CONFIG_JPOSPRINTER);
        }
        resultok = true;
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboTypeActionPerformed
        handlePrinterTypeChange();
    }//GEN-LAST:event_jComboTypeActionPerformed

    /**
    * @param args the command line arguments
    */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboConn;
    private javax.swing.JComboBox jComboSerial;
    private javax.swing.JComboBox jComboType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextJPOSPrinter;
    private javax.swing.JTextField jTextPrinterNo;
    // End of variables declaration//GEN-END:variables

}
