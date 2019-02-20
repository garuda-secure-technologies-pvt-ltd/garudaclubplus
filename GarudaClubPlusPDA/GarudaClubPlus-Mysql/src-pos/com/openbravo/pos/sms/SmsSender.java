/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SmsSender.java
 *
 * Created on Aug 12, 2009, 6:58:10 PM
 */

package com.openbravo.pos.sms;

import RMI.ComputePi;
import com.lowagie.text.pdf.TextField;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
//import com.openbravo.data.loader.Datas;
//import com.openbravo.data.loader.SerializerReadBasic;
//import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.Facility;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
//import com.sun.media.sound.Toolkit;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.String;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author swathi
 */
public class SmsSender extends javax.swing.JPanel implements JPanelView,BeanFactoryApp{

    /** Creates new form SmsSender */
    private AppView m_App;
    private ComboBoxValModel cmodel;
    private DataLogicFacilities dlfac;
    private String prefix;
    public SmsSender() {
        initComponents();
    }
    public void init(AppView app) throws BeanFactoryException {
        m_App=app;
        dlfac=(DataLogicFacilities)app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        jTextField2.setDocument(new MyPlainDocument(12));
        jTextField2.setDocument(new MyPlainDocument(17));
        jTextArea1.setDocument(new MyPlainDocument(125,jLabel3));
       // jTextArea1.getDocument().addDocumentListener(new TextEdited(jLabel3,125,jTextArea1));
       // jTextField2.getDocument().addDocumentListener(new TextEdited(null,12,jTextField2));
       // jTextField3.getDocument().addDocumentListener(new TextEdited(null,17,jTextField3));
    }

     public String getTitle() {
        return null;
    }

    public void activate() throws BasicException {
       Facility f=new Facility();
       f.setID("ALL");
       f.setName("ALL");
       List<Facility> faclist=dlfac.getFacility();
       faclist.add(0,f);
       cmodel=new ComboBoxValModel(faclist);
       jComboBox1.setModel(cmodel);
       jRadioButton9.setSelected(true);
       jRadioButton1.setSelected(true);
       jTextField1.setEditable(false);

       jLabel8.setVisible(false);
       jTextField3.setVisible(false);
    }

    public boolean deactivate() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }


    public Object getBean() {
        return this;
    }

    private class MyPlainDocument extends PlainDocument {
        private JLabel label;
        private int max;
       // private JTextArea textarea;
       // private JTextField textfield;
        public MyPlainDocument (int max){
            //this.label=label;
            this.max=max;
           // this.textarea=null;
        }
        public MyPlainDocument (int max,JLabel label){
            this.label=label;
            this.max=max;
           // this.textarea=null;
        }
      /*  public TextEdited(JLabel label,int max,JTextArea textarea){
            this.label=label;
            this.max=max;
            this.textarea=textarea;
            this.textfield=null;
        }
        private void update(){
          if(textarea!=null){
               int len=textarea.getText().length();
               int maxtemp=max;
               if(prefix!=null && jRadioButton6.isSelected()==false)
                   maxtemp=max+17-prefix.length();
               jLabel2.setText("/"+String.valueOf(maxtemp));
               if(len<=maxtemp){
                   label.setText(String.valueOf(textarea.getText().length()));

               }else{
                   label.setText(String.valueOf(maxtemp));
                   textarea.setText(textarea.getText().substring(0, maxtemp));
               }
            }else  if(textfield!=null){
               int len=textfield.getText().length();
               if(len>max){
                   textfield.setText("");
                   textfield.setText(textfield.getText().substring(0, max-1));
               }
            }
        }
        public void insertUpdate(DocumentEvent e) {
         //   update();
        }

        public void removeUpdate(DocumentEvent e) {
          //  update();
        }

        public void changedUpdate(DocumentEvent e) {
          //  update();
        }*/

        @Override
        public void insertString(int offset, String str, AttributeSet a) throws
		BadLocationException {
         if(label!=null){
           // max=max+17-prefix.length();
          //  jLabel2.setText("/"+max);
            int len=getLength() + str.length();
            int oldlen=getLength();
            if (!((len) > max)) {
			   super.insertString(offset, str, a);
               label.setText(String.valueOf(len));
		    }else{
                if (oldlen < max) {
                   String str1=str.substring(0, max-oldlen);
                   super.insertString(offset, str1, a);
                }else
                Toolkit.getDefaultToolkit().beep();
            }
                //label.setText(String.valueOf(max));
         }else{
             int oldlen=getLength();
		   if (!((getLength() + str.length()) > max)) {
			super.insertString(offset, str, a);
		   }else{
                if (oldlen < max) {
                   String str1=str.substring(0, max-oldlen);
                   super.insertString(offset, str1, a);
                }else
                   Toolkit.getDefaultToolkit().beep();
            }
      }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();

        jLabel1.setText("Message :");
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Send to all");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("/125");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("0");
        jLabel3.setName("jLabel3"); // NOI18N

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Members");
        jRadioButton1.setName("jRadioButton1"); // NOI18N
        jRadioButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton1StateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Other List");
        jRadioButton2.setName("jRadioButton2"); // NOI18N
        jRadioButton2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton2StateChanged(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel4.setText("Import Data From Excel File");
        jLabel4.setName("jLabel4"); // NOI18N

        jTextField1.setName("jTextField1"); // NOI18N

        jButton2.setText("Browse");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Sender Name");
        jLabel6.setName("jLabel6"); // NOI18N

        jTextField2.setName("jTextField2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jComboBox1.setName("jComboBox1"); // NOI18N

        jLabel5.setText("Facility");
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setName("jPanel3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel4.setName("jPanel4"); // NOI18N

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setText("Dear Sir");
        jRadioButton3.setName("jRadioButton3"); // NOI18N
        jRadioButton3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton3StateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("Dear Mam");
        jRadioButton4.setName("jRadioButton4"); // NOI18N
        jRadioButton4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton4StateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Dear Sir/Mam");
        jRadioButton5.setName("jRadioButton5"); // NOI18N
        jRadioButton5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton5StateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton6);
        jRadioButton6.setText("Dear $Name$");
        jRadioButton6.setName("jRadioButton6"); // NOI18N
        jRadioButton6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton6StateChanged(evt);
            }
        });

        jLabel7.setText("Select a Prefix :");
        jLabel7.setName("jLabel7"); // NOI18N

        buttonGroup2.add(jRadioButton7);
        jRadioButton7.setText("Other Prefix");
        jRadioButton7.setName("jRadioButton7"); // NOI18N
        jRadioButton7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton7StateChanged(evt);
            }
        });

        jTextField3.setName("jTextField3"); // NOI18N

        jLabel8.setText("Prefix");
        jLabel8.setName("jLabel8"); // NOI18N

        buttonGroup2.add(jRadioButton8);
        jRadioButton8.setText("No Prefix");
        jRadioButton8.setName("jRadioButton8"); // NOI18N
        jRadioButton8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton8StateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioButton9);
        jRadioButton9.setText("Dear Member");
        jRadioButton9.setName("jRadioButton9"); // NOI18N
        jRadioButton9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton9StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jRadioButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jRadioButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jRadioButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton8)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                                .addComponent(jRadioButton4)))))
                .addGap(16, 16, 16))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jRadioButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton3))
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jRadioButton9)
                        .addGap(3, 3, 3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(568, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(421, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(278, 278, 278))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String sendtype=null;
            String mobiledata=null;
            boolean withname=false;
            
            String suffuix=jTextField2.getText();
            String msg=prefix+"\r"+jTextArea1.getText()+"-"+suffuix;
            //String prefix;
            if(jRadioButton1.isSelected()){
               if(prefix.contains("$Name$")){
                   withname=true;
                List<Object[]> mobilelist=new ArrayList();
                if(jComboBox1.getSelectedItem() != null && jComboBox1.getSelectedItem().toString().equals("ALL")){
                    sendtype="ALL";
                    mobilelist=new PreparedSentence(m_App.getSession(), "SELECT C.MOBILE,C.NAME FROM CUSTOMERS C WHERE C.MOBILE IS NOT NULL AND (LENGTH(C.MOBILE)=10 OR LENGTH(C.MOBILE)=13)", null ,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING})).list();
               }else{
                  Facility f=(Facility)jComboBox1.getSelectedItem();
                  sendtype=f.getName();
                  mobilelist=new PreparedSentence(m_App.getSession(), "SELECT C.MOBILE,C.NAME FROM MEMFACILITYUSAGE M JOIN CUSTOMERS C ON M.MEMNO=C.ID AND C.MOBILE IS NOT NULL AND (LENGTH(C.MOBILE)=10 OR LENGTH(C.MOBILE)=13)  WHERE M.ACTIVE=TRUE AND M.FACILITYTYPE=?"
                            , SerializerWriteString.INSTANCE ,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING})).list(f.getid());
                }
               for(Object[] a:mobilelist){
                 if(mobiledata==null)
                  mobiledata=a[1]+" # "+a[0];
                 else
                  mobiledata+=" : "+a[1]+" # "+a[0];
                 /*if(mobiledata==null)
                  mobiledata=String.valueOf(a[0]);
                 else
                  mobiledata+=" : "+a[0];*/
               }
             }else{
                   List<String> mobilelist=new ArrayList();
                  if(jComboBox1.getSelectedItem() != null && jComboBox1.getSelectedItem().toString().equals("ALL")){
                    sendtype="ALL";
                    mobilelist=new PreparedSentence(m_App.getSession(), "SELECT C.MOBILE FROM CUSTOMERS C WHERE C.MOBILE IS NOT NULL AND (LENGTH(C.MOBILE)=10 OR LENGTH(C.MOBILE)=13)", null ,SerializerReadString.INSTANCE).list();
             } else{
                    Facility f=(Facility)jComboBox1.getSelectedItem();
                    sendtype=f.getName();
                    mobilelist=new PreparedSentence(m_App.getSession(), "SELECT C.MOBILE FROM MEMFACILITYUSAGE M JOIN CUSTOMERS C ON M.MEMNO=C.ID AND C.MOBILE IS NOT NULL AND (LENGTH(C.MOBILE)=10 OR LENGTH(C.MOBILE)=13)  WHERE M.ACTIVE=TRUE AND M.FACILITYTYPE=?"
                            , SerializerWriteString.INSTANCE ,SerializerReadString.INSTANCE).list(f.getid());
                  }
                  for(String a:mobilelist){
                    if(mobiledata==null)
                      mobiledata=a;
                    else
                      mobiledata+=" : "+a;
                  }
             }
            }else if(jRadioButton2.isSelected()){
                 sendtype="others";
                 String filename=jTextField1.getText();
                 MapExelData exceldata=new MapExelData(new JFrame(), true);
                 exceldata.setTitle("Data Mapper");
                 if(prefix.contains("$Name$")){
                    withname=true;
                    exceldata.ShowData(filename, withname);
                    mobiledata=exceldata.getLodedData();
                 }else{
                    withname=false;
                    exceldata.ShowData(filename, withname);
                    mobiledata=exceldata.getLodedData();
                 }
            }
           // if(prefix.contains("$Name$")){

          //  }
             if(mobiledata!=null){
             String id=UUID.randomUUID().toString();
             if(sendtype.equals("others"))
                new PreparedSentence(m_App.getSession(), "INSERT INTO MANUALMSG(ID,MESSAGE,SENTDATE,TYPE_,SENTTO,SENTBY) VALUES (?,?,?,?,?,?) "
                       ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.TIMESTAMP,Datas.STRING,Datas.STRING,Datas.STRING})
                       ).exec(new Object[]{id,msg,new Date(),sendtype,mobiledata,m_App.getAppUserView().getUser().getId()});
             else
                 new PreparedSentence(m_App.getSession(), "INSERT INTO MANUALMSG(ID,MESSAGE,SENTDATE,TYPE_) VALUES (?,?,?,?) "
                       ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.TIMESTAMP,Datas.STRING})
                       ).exec(new Object[]{id,msg,new Date(),sendtype});
               new PreparedSentence(m_App.getSession(), "INSERT INTO activemsgtable(ID,Message,SENDTO,WITHNAME,PRIORITY,CNT) VALUES (?,?,?,?,?,?) "
                       ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.BOOLEAN,Datas.INT,Datas.INT})
                       ).exec(new Object[]{id,msg,mobiledata,withname,0,0});
               try {
                ComputePi.setvalue(true);
               } catch (RemoteException ex1) {
                 ex1.printStackTrace();
              }
             }
           // String portname="COM5";
          //  String msgc="+919844198441";
           // List<Object[]> list=new ArrayList<Object[]>();
          //  int count=jTextArea1.getText().length();
          //  System.out.println("Message Length "+count);
           // String s5="9845001729 : 9845029534 : 9845038769 : 9845052554 : 9341228824 : 9343727479 : 9448509120 : 9845286241 : 9342253640 : 9845251208 : 9845177773 : 9844588910 : 9845518327 : 9740083303 : 9886155588 : 9844015431 : 9448796830 : 9900129776 : 9449852608 : 9880010593 : 9740581581 : 9742387491 : 9845191547 : 9880871679 : 9845350443 : 9886452368 : 9849810716 : 9845444245 : 9845054184 : 9844244563 : 9886177088 : 9900583133 : 9845237909 : 9448573268 : 9844084413";
           // String arr[]=s5.split(" : ");
          
          if(mobiledata!=null){
              
          }
          
          //  Object[] obj=new Object[]{"Raga","9844084413"};
          //  list.add(obj);
            //SMSClient s = new SMSClient(0,portname , msgc);
            //  s.sendMessage(list, jTextArea1.getText());
              

           // }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JFileChooser file=new JFileChooser();
        file.setCurrentDirectory(null);
        FileFilter ffilter=new FileFilter() {

            @Override
            public boolean accept(File f) {
               // if(f.get)
                String path=f.getAbsolutePath();
                if(path.toUpperCase().endsWith(".XLS"))
                return true;
                else
                    return false;
            }

            @Override
            public String getDescription() {
                return "Excel Files";
            }
        };
        file.setFileFilter(ffilter);
        if(file.showOpenDialog(new JFrame())==JFileChooser.OPEN_DIALOG){
          jTextField1.setText(file.getSelectedFile().getAbsolutePath());

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton1StateChanged
        // TODO add your handling code here:
        if(jRadioButton1.isSelected()==true){
         jPanel2.setVisible(true);
         jPanel1.setVisible(false);
        }
    }//GEN-LAST:event_jRadioButton1StateChanged

    private void jRadioButton2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton2StateChanged
       if(jRadioButton2.isSelected()==true){
        jPanel1.setVisible(true);
        jPanel2.setVisible(false);
       }
    }//GEN-LAST:event_jRadioButton2StateChanged

    private void jRadioButton7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton7StateChanged
       if(jRadioButton7.isSelected()==true){
         jLabel8.setVisible(true);
         jTextField3.setVisible(true);
         jTextField3.setText(null);
       }else{
         jLabel8.setVisible(false);
         jTextField3.setVisible(false);
       }
    }//GEN-LAST:event_jRadioButton7StateChanged

    private void jRadioButton5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton5StateChanged
        if(jRadioButton5.isSelected()==true)
            prefix="Dear Sir/Mam";
    }//GEN-LAST:event_jRadioButton5StateChanged

    private void jRadioButton4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton4StateChanged
         if(jRadioButton4.isSelected()==true)
            prefix="Dear Mam";
    }//GEN-LAST:event_jRadioButton4StateChanged

    private void jRadioButton6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton6StateChanged
         if(jRadioButton6.isSelected()==true)
            prefix="Dear $Name$";
    }//GEN-LAST:event_jRadioButton6StateChanged

    private void jRadioButton3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton3StateChanged
         if(jRadioButton3.isSelected()==true)
            prefix="Dear Sir";
    }//GEN-LAST:event_jRadioButton3StateChanged

    private void jRadioButton9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton9StateChanged
         if(jRadioButton9.isSelected()==true)
            prefix="Dear Member";
    }//GEN-LAST:event_jRadioButton9StateChanged

    private void jRadioButton8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton8StateChanged
         if(jRadioButton8.isSelected()==true){
            prefix="";
            jLabel2.setText("/142");
         }else
             jLabel2.setText("/125");
    }//GEN-LAST:event_jRadioButton8StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables



}
