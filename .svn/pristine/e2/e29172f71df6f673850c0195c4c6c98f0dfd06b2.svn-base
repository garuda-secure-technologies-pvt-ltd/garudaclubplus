
package com.openbravo.pos.adminUtils;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.JComponent;
import javax.swing.JOptionPane;


public class MemMinUsageInsert extends javax.swing.JPanel implements JPanelView, BeanFactoryApp {

   private AppView m_App;
    public MemMinUsageInsert() {
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        jButton1.setText("InsertMemMinUsage");
        jButton1.setName("jButton1"); // NOI18N
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
                .addGap(54, 54, 54)
                .addComponent(jButton1)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jButton1)
                .addContainerGap(249, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            Date d=new Date();
            if (JOptionPane.showConfirmDialog(this, "Do you want insert members into memminusage","Confirm Insertion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
            List<MemberInfo> minfo = getMembersOfMinUsage();
            insertMinimumUsageForSpecificMember(d, minfo);
             JOptionPane.showMessageDialog(null, "Members are successfully inserted.");
            }

        } catch (BasicException ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed
    public void insertMinimumUsageForSpecificMember(Date date1,  List<MemberInfo> members) throws BasicException {
        
        for (MemberInfo m : members) {
            Object[] value1 = new Object[]{UUID.randomUUID().toString(), true, m.getMid(), "7d231f12-dcc0-42da-9562-f96e3b96f344", date1,"Garuda", date1, 0};
            new PreparedSentence(m_App.getSession(), "INSERT INTO MEMMINUSAGE(ID, ACTIVE, MEMNO, USAGETYPE, SDATE, CREATEDBY, CRDATE,STATUS) VALUES (?,?,?,?,?,?,?,?)", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.INT})).exec(value1);
        }
    }
    public List<MemberInfo> getMembersOfMinUsage() throws BasicException {
        List<MemberInfo> mulist=new PreparedSentence(m_App.getSession(), "select  c.id from customers c,minusage m where c.id not in (select memno from memminusage ) and m.members like concat(concat('%',c.memtype),'%') and c.visible=true and m.id='7d231f12-dcc0-42da-9562-f96e3b96f344' order by searchkey", null, new SerializerReadClass(MemberInfo.class)).list();
        return mulist;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    public String getTitle() {
        return null;
    }

    public void activate() throws BasicException {
        
    }

    public boolean deactivate() {
       return true;
    }

    public JComponent getComponent() {
         return this;
    }

    public void init(AppView app) throws BeanFactoryException {
        m_App = app;
    }

    public Object getBean() {
        return this;
    }

}
