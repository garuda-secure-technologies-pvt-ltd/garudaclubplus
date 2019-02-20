/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * KIOSKSettings.java
 *
 * Created on 25-Mar-2013, 11:43:25
 */
package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author swathi
 */
public class KIOSKSettings extends javax.swing.JPanel implements JPanelView,BeanFactoryApp, ActionListener  {
    
    private AppView m_App;
    JFileChooser chooser;
   String choosertitle;
   File srcLogo = null;
   
    
    
    /** Creates new form KIOSKSettings */
    public KIOSKSettings() {
        initComponents();
        initCom();
        groupButton();
        groupButton1();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ClubName = new javax.swing.JTextField();
        ClubAddress = new javax.swing.JTextField();
        ClubWebsite = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        LogoPath = new javax.swing.JButton();
        POnePath = new javax.swing.JButton();
        PTwoPath = new javax.swing.JButton();
        PThreePath = new javax.swing.JButton();
        LPD = new javax.swing.JTextField();
        POP = new javax.swing.JTextField();
        PTP = new javax.swing.JTextField();
        PTHP = new javax.swing.JTextField();
        NLPath = new javax.swing.JButton();
        NLP = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        FRAYes = new javax.swing.JRadioButton();
        FRANo = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        FRDYes = new javax.swing.JRadioButton();
        FRDNo = new javax.swing.JRadioButton();
        Save = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        selectLogo = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        jFileChooser1.setName("jFileChooser1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText("Club Name");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("Address");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("Website");
        jLabel3.setName("jLabel3"); // NOI18N

        ClubName.setName("ClubName"); // NOI18N

        ClubAddress.setName("ClubAddress"); // NOI18N

        ClubWebsite.setName("ClubWebsite"); // NOI18N

        jLabel4.setText("Club Logo Path");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText("Panel One Path");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText("Panel Two Path");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText("Panel Three Path");
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText("New Letter Path");
        jLabel8.setName("jLabel8"); // NOI18N

        LogoPath.setText("Select Path to Store Club  Logo");
        LogoPath.setName("LogoPath"); // NOI18N
        LogoPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoPathActionPerformed(evt);
            }
        });

        POnePath.setText("Path to Store Panel One Img");
        POnePath.setName("POnePath"); // NOI18N

        PTwoPath.setText("Path to Store Panel Two Img");
        PTwoPath.setName("PTwoPath"); // NOI18N

        PThreePath.setText("Path to Store Panel Three Img");
        PThreePath.setName("PThreePath"); // NOI18N

        LPD.setName("LPD"); // NOI18N

        POP.setName("POP"); // NOI18N

        PTP.setName("PTP"); // NOI18N

        PTHP.setName("PTHP"); // NOI18N

        NLPath.setText("Path to Store News Letter");
        NLPath.setName("NLPath"); // NOI18N

        NLP.setName("NLP"); // NOI18N

        jLabel9.setText("Activate Fecility Req");
        jLabel9.setName("jLabel9"); // NOI18N

        FRAYes.setText("Yes");
        FRAYes.setName("FRAYes"); // NOI18N

        FRANo.setText("No");
        FRANo.setName("FRANo"); // NOI18N

        jLabel10.setText("Deactivate Fecility Req");
        jLabel10.setName("jLabel10"); // NOI18N

        FRDYes.setText("Yes");
        FRDYes.setName("FRDYes"); // NOI18N

        FRDNo.setText("No");
        FRDNo.setName("FRDNo"); // NOI18N

        Save.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Save.setText("Save");
        Save.setName("Save"); // NOI18N
        Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveActionPerformed(evt);
            }
        });

        jLabel11.setText("Ex: http://www.garudasecuretech.com");
        jLabel11.setName("jLabel11"); // NOI18N

        jLabel12.setText("Select Club Logo");
        jLabel12.setName("jLabel12"); // NOI18N

        selectLogo.setText("Select CLub Logo");
        selectLogo.setName("selectLogo"); // NOI18N
        selectLogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectLogoActionPerformed(evt);
            }
        });

        Cancel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Cancel.setText("Cancel");
        Cancel.setName("Cancel"); // NOI18N
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        jLabel13.setText("img Folder");
        jLabel13.setName("jLabel13"); // NOI18N

        jLabel14.setText("upcoming Folder");
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel16.setText("photos Folder");
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setText("adver Folder");
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel15.setText("Note: All the Above Folders should be with in GarudaClubPlus main project Folder.");
        jLabel15.setName("jLabel15"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ClubName, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ClubAddress, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ClubWebsite, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(NLPath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PThreePath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PTwoPath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(POnePath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(LogoPath, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11)
                            .addComponent(PTHP)
                            .addComponent(PTP)
                            .addComponent(NLP)
                            .addComponent(POP)
                            .addComponent(LPD, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(FRAYes)
                                    .addGap(143, 143, 143))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(FRANo)
                                    .addGap(76, 76, 76)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(FRDYes)
                                .addGap(18, 18, 18)
                                .addComponent(FRDNo)
                                .addGap(86, 86, 86))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(selectLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Save, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel15))))
                .addContainerGap(540, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(ClubName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ClubAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ClubWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel11))))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(LogoPath)
                    .addComponent(LPD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(POnePath)
                    .addComponent(POP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(PTwoPath)
                    .addComponent(PTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(PThreePath)
                    .addComponent(PTHP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(NLPath)
                    .addComponent(NLP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(selectLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FRAYes)
                            .addComponent(FRANo)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(FRDYes)
                            .addComponent(FRDNo)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Save)
                            .addComponent(Cancel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        LPD.setEditable(false);
        POP.setEditable(false);
        PTP.setEditable(false);
        PTHP.setEditable(false);
        NLP.setEditable(false);
        FRAYes.setSelected(true);
        FRDYes.setSelected(true);

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 591, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveActionPerformed
       String filename = null;
       boolean activate = true;
       boolean deactivate = true;
      
     if(ClubName.getText()!=null && !ClubName.getText().equals("") && ClubAddress.getText()!=null && !ClubAddress.getText().equals(""))
     {
         if(LPD.getText()!=null && !LPD.getText().equals("") && POP.getText()!=null && !POP.getText().equals("") && PTP.getText()!=null && !PTP.getText().equals("") && PTHP.getText()!=null && !PTHP.getText().equals("") && NLP.getText()!=null && !NLP.getText().equals("") )
         {
                try {
                    if(srcLogo!=null)
                    {
                        filename = UUID.randomUUID().toString()+srcLogo.getAbsoluteFile().getName();
                    }
                    
                    if(FRANo.isSelected())
                    {
                        activate = false;
                    }
                    
                    if(FRDNo.isSelected())
                    {
                        deactivate = false;
                    }
                    
                    Object [] val = new Object [] {UUID.randomUUID().toString(), ClubName.getText(), ClubAddress.getText(), ClubWebsite.getText(), LPD.getText(), POP.getText(), PTP.getText(), PTHP.getText(), NLP.getText(), filename, activate, deactivate, m_App.getAppUserView().getUser().getName(), new Date(), m_App.getProperties().getHost(), true};
                    
                   int num =  new PreparedSentence(m_App.getSession(), "UPDATE CLUBDETAILSKIOSK SET ACTIVE = FALSE, LASTMODIFIEDBY =? , LASTMODIFIEDDATE=?, LASTMODIFIEDHOST=? WHERE ACTIVE = TRUE", new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{m_App.getAppUserView().getUser().getName(), new Date(), m_App.getProperties().getHost()});
                    
                    int num2 =  new PreparedSentence(m_App.getSession()
                                   , "INSERT INTO CLUBDETAILSKIOSK (ID, CLUBNAME, ADDRESS, WEBSITE, CLUBLOGOPATH, PANELONE , PANELTWO, PANELTHREE,NEWSLETTERPATH, CLUBLOGO, ACTIVATEFECILITYREQ, DEACTIVATEFECILITYREQ, LASTMODIFIEDBY, LASTMODIFIEDDATE, LASTMODIFIEDHOST, ACTIVE ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                                   , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING ,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.BOOLEAN})
                                   ).exec(val);
                                 
                    
                    JOptionPane.showMessageDialog(null, "Update Success", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    if(srcLogo!=null & num2==1)
                    {
                           File sourceF = srcLogo;
                           File destFile = new File(LPD.getText()+ filename);
                           try {
                               FileUtils.copyFile(sourceF,destFile);
                               
                               JOptionPane.showMessageDialog(null, "File Copied to Target Path", "Success", JOptionPane.INFORMATION_MESSAGE);
                           } catch (IOException ex) {
                               Logger.getLogger(KIOSKSettings.class.getName()).log(Level.SEVERE, null, ex);
                               JOptionPane.showMessageDialog(null, "Unable to Load File Logo Image to Server..!!!");
                           }
                    }
                } catch (BasicException ex) {
                    Logger.getLogger(KIOSKSettings.class.getName()).log(Level.SEVERE, null, ex);
                }
             reset();
         }
         else
         {
             JOptionPane.showMessageDialog(null, "Select All the paths Which is mentioned above..!!!", "Select Path", JOptionPane.ERROR_MESSAGE);
         }
         
         
     }
       
    }//GEN-LAST:event_SaveActionPerformed

    private void selectLogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectLogoActionPerformed
        
        
         JFileChooser fileChooser = new JFileChooser();
         fileChooser.addChoosableFileFilter(new ExtensionsFilter(LocalRes.getIntString("label.imagefiles"), "png", "gif", "jpg", "jpeg", "bmp"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
             try {
                 File sourceFile1 = fileChooser.getSelectedFile();
                 srcLogo = sourceFile1;
                 BufferedImage in = ImageIO.read(sourceFile1);
                 BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
                 ImageIcon ii = new ImageIcon(in);
                 selectLogo.setIcon(ii);
             } catch (IOException ex) {
                 Logger.getLogger(KIOSKSettings.class.getName()).log(Level.SEVERE, null, ex);
             }
                
        }
        
    }//GEN-LAST:event_selectLogoActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        reset();
    }//GEN-LAST:event_CancelActionPerformed

    private void LogoPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoPathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LogoPathActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JTextField ClubAddress;
    private javax.swing.JTextField ClubName;
    private javax.swing.JTextField ClubWebsite;
    private javax.swing.JRadioButton FRANo;
    private javax.swing.JRadioButton FRAYes;
    private javax.swing.JRadioButton FRDNo;
    private javax.swing.JRadioButton FRDYes;
    private javax.swing.JTextField LPD;
    private javax.swing.JButton LogoPath;
    private javax.swing.JTextField NLP;
    private javax.swing.JButton NLPath;
    private javax.swing.JTextField POP;
    private javax.swing.JButton POnePath;
    private javax.swing.JTextField PTHP;
    private javax.swing.JTextField PTP;
    private javax.swing.JButton PThreePath;
    private javax.swing.JButton PTwoPath;
    private javax.swing.JButton Save;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton selectLogo;
    // End of variables declaration//GEN-END:variables

    public String getTitle() {
        return "KIOSK";
    }

    public void activate() throws BasicException {
        
        Object [] obj = (Object[]) new StaticSentence(m_App.getSession(), "SELECT id, CLUBNAME, ADDRESS, CLUBLOGO, WEBSITE, PANELONE, PANELTWO, PANELTHREE, ACTIVATEFECILITYREQ, DEACTIVATEFECILITYREQ, NEWSLETTERPATH, NEWSLETTER, CLUBLOGOPATH FROM clubdetailskiosk WHERE ACTIVE =TRUE",SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.BOOLEAN, Datas.BOOLEAN, Datas.STRING,Datas.STRING,Datas.STRING })).find();
       
        if(obj!=null)
        {
            ClubName.setText(obj[1].toString());
            ClubAddress.setText(obj[2].toString());
            LPD.setText(obj[12].toString());
            ClubWebsite.setText(obj[4].toString());
            POP.setText(obj[5].toString());
            PTP.setText(obj[6].toString());
            PTHP.setText(obj[7].toString());
            NLP.setText(obj[10].toString());
            boolean b;
            b = (Boolean) obj[8];
            if(b)
            {
                FRAYes.setSelected(true);
                FRANo.setSelected(false);
            }
            else
            {
                FRAYes.setSelected(false);
                FRANo.setSelected(true);
            }
            
            b = (Boolean) obj[9];
            if(b)
            {
                FRDYes.setSelected(true);
                FRDNo.setSelected(false);
            }
            else
            {
                FRDYes.setSelected(false);
                FRDNo.setSelected(true);
            }
            
            File sourceFile1 = new File(obj[12].toString()+obj[3].toString());
                if(sourceFile1!=null)
                {
                 BufferedImage in;
            try {
                in = ImageIO.read(sourceFile1);
                BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
                 ImageIcon ii = new ImageIcon(in);
                 selectLogo.setIcon(ii);
            } catch (IOException ex) {
                Logger.getLogger(KIOSKSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
                 
            
        }
        
        
        
    }

    public boolean deactivate() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }

    public void init(AppView app) throws BeanFactoryException {
        this.m_App = app;
    }

    public Object getBean() {
        return this;
    }
    
    
    public void initCom()
    {
        LogoPath.addActionListener(this);
        POnePath.addActionListener(this);
        PTwoPath.addActionListener(this);
        PThreePath.addActionListener(this);
        NLPath.addActionListener(this);
        reset();
        
    }

    public void actionPerformed(ActionEvent e) {
        
        int result;
        
    chooser = new JFileChooser(); 
    chooser.setCurrentDirectory(new java.io.File("."));
    
    chooser.setDialogTitle(choosertitle);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    
    //
    // disable the "All files" option.
    //
    chooser.setAcceptAllFileFilterUsed(false);
    //    
        String curDire =  chooser.getCurrentDirectory().getAbsolutePath();
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
        
        if(e.getSource().equals(LogoPath))
        {
            String fn = chooser.getSelectedFile().getAbsolutePath();
            fn = fn.replace(curDire, "");
            while (fn.charAt(0)=='\\' || fn.charAt(0)=='/' ) {                
                fn = fn.substring(1);
            }
           fn = fn.replace("\\","//");
          
            System.out.println(fn);
           LPD.setText(fn+"//");
           LPD.setVisible(true);
        }
        else if(e.getSource().equals(POnePath))
        {
            String fn = chooser.getSelectedFile().getAbsolutePath();
            fn = fn.replace(curDire, "");
            
           while (fn.charAt(0)=='\\' || fn.charAt(0)=='/' ) {                
                fn = fn.substring(1);
            }
           fn = fn.replace("\\","//");
          
            System.out.println(fn);
           POP.setText(fn+"//");
            POP.setVisible(true);
        }
        else if(e.getSource().equals(PTwoPath))
        {
            String fn = chooser.getSelectedFile().getAbsolutePath();
            fn = fn.replace(curDire, "");
           
           while (fn.charAt(0)=='\\' || fn.charAt(0)=='/' ) {                
                fn = fn.substring(1);
            }
           fn = fn.replace("\\","//");
          
            System.out.println(fn);
           PTP.setText(fn+"//");
            PTP.setVisible(true);
        }
        else if(e.getSource().equals(PThreePath))
        {
            String fn = chooser.getSelectedFile().getAbsolutePath();
            fn = fn.replace(curDire, "");
            
            while (fn.charAt(0)=='\\' || fn.charAt(0)=='/' ) {                
                fn = fn.substring(1);
            }
           fn = fn.replace("\\","//");
          
            System.out.println(fn);
           PTHP.setText(fn+"//");
            PTHP.setVisible(true);
        }
        else if(e.getSource().equals(NLPath))
        {
            String fn = chooser.getSelectedFile().getAbsolutePath();
           
            fn = fn.replace("\\", "//");
           // fn = fn.replace("//","");
           NLP.setText(fn+"//");
            NLP.setVisible(true);
        }
      
      }
    else {
      System.out.println("No Selection ");
      }
        
    }
    
    private void groupButton() {
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(FRAYes);
        bg1.add(FRANo);
    }
     
    
    private void groupButton1() {
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(FRDYes);
        bg1.add(FRDNo);
    }
    
    public void reset()
    {
        ClubName.setText(null);
        ClubAddress.setText(null);
        ClubWebsite.setText(null);
        LPD.setText(null);
        LPD.setVisible(false);
        POP.setText(null);
        POP.setVisible(false);
        PTP.setText(null);
        PTP.setVisible(false);
        PTHP.setText(null);
        PTHP.setVisible(false);
        NLP.setText(null);
        NLP.setVisible(false);
        selectLogo.setIcon(null);
    }
    
    private static class ExtensionsFilter extends FileFilter {
        
        private String message;
        private String[] extensions;
        
        public ExtensionsFilter(String message, String... extensions) {
            this.message = message;
            this.extensions = extensions;            
        }
        
        public boolean accept(java.io.File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                String sFileName = f.getName();
                int ipos = sFileName.lastIndexOf('.');
                if (ipos >= 0) {
                    String sExt = sFileName.substring(ipos + 1);
                    for(String s : extensions) {
                        if (s.equalsIgnoreCase(sExt)) {
                            return true;
                        }
                    }
                }                        
                return false;
            }   
        }
        
        public String getDescription() {
            return message;
        }      
    }
    
}
