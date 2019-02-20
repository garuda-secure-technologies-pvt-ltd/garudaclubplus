

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.util.UUID;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author adrianromero
 */
public class LocationsView extends javax.swing.JPanel implements EditorRecord {
    
    // private DirtyManager m_Dirty = new DirtyManager();    
    private String m_sID;
     private DataLogicSales dlSales;
    private ComboBoxValModel wmodel;
    private boolean stkStatus;
    /** Creates new form LocationsEditor */
    public LocationsView(DirtyManager dirty){
        initComponents();
        dlSales = (DataLogicSales) LookupUtilityImpl.getInstance(null).getAppView().getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        m_jName.getDocument().addDocumentListener(dirty);
        m_jAddress.getDocument().addDocumentListener(dirty);
        jTextField1.getDocument().addDocumentListener(dirty);
        jLabel1.setText("Display Name");
        jLabel2.setText("Name");
        jLabel3.setText("Address");
        jLabel4.setText("Parent");
        jLabel5.setText("Maintain Stock");
        jCheckBox1.setText("Yes");
        jCheckBox1.addActionListener(dirty);
        try {
            List<LocationInfo> list=dlSales.getLocationsList().list();
            list.add(0, null);
            wmodel = new ComboBoxValModel(list);
        } catch (BasicException ex) {
            wmodel=new ComboBoxValModel();
        }
        jComboBox1.setModel(wmodel);
        jComboBox1.addActionListener(dirty);
        writeValueEOF();    
    }
    public void writeValueEOF() {
        m_sID = null;
        m_jName.setText(null);
        m_jAddress.setText(null);
        jTextField1.setText(null);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        jTextField1.setEnabled(false);
        wmodel.setSelectedKey(null);
        jComboBox1.setEnabled(false);
        jCheckBox1.setEnabled(false);
        jCheckBox1.setSelected(false);
        //jComboBox1.sets
    }    
    public void writeValueInsert() {
        try{
           m_sID =  dlSales.getCategoriesbyname(m_jName.getText());
        }
        catch(Exception e){
        }
        m_jName.setText(null);
        m_jAddress.setText(null);
        jTextField1.setEnabled(true);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        wmodel.setSelectedKey(null);
        jComboBox1.setEnabled(true);
        jCheckBox1.setEnabled(true);
        jCheckBox1.setSelected(true);
        stkStatus=true;
    }    
    public void writeValueDelete(Object value) {
        
        Object[] location = (Object[]) value;
        m_sID = Formats.STRING.formatValue(location[0]);
        m_jName.setText(Formats.STRING.formatValue(location[1]));
        m_jAddress.setText(Formats.STRING.formatValue(location[2]));
        jTextField1.setText(Formats.STRING.formatValue(location[3]));
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        jTextField1.setEnabled(false);
        wmodel.setSelectedKey(location[4]);
        jComboBox1.setEnabled(false);
        stkStatus=Boolean.valueOf(String.valueOf(location[5]));
        jCheckBox1.setSelected(stkStatus);
        //jCheckBox1.setSelected(Boolean.valueOf(String.valueOf(location[5])));
        jCheckBox1.setEnabled(false);
    }    
    public void writeValueEdit(Object value) {
        Object[] location = (Object[]) value;
        m_sID = Formats.STRING.formatValue(location[0]);
        m_jName.setText(Formats.STRING.formatValue(location[1]));
        m_jAddress.setText(Formats.STRING.formatValue(location[2]));
        jTextField1.setText(Formats.STRING.formatValue(location[3]));
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        jTextField1.setEnabled(true);
        //if(location[4]!=null)
        wmodel.setSelectedKey(location[4]);
        jComboBox1.setEnabled(true);
        stkStatus=Boolean.valueOf(String.valueOf(location[5]));
        jCheckBox1.setSelected(stkStatus);
        jCheckBox1.setEnabled(true);
    }    
    public Object createValue() throws BasicException {
        Object[] location = new Object[6];
         try{
             AppView app=LookupUtilityImpl.getInstance(null).getAppView();
             if(m_sID==null){
                  m_sID =  dlSales.getCategoriesbyname(m_jName.getText());
                  if(m_sID==null){
                       m_sID=UUID.randomUUID().toString();
                       JOptionPane.showMessageDialog(this, "A Root Product Category will be created with the same name", "Message", JOptionPane.INFORMATION_MESSAGE);
                       new PreparedSentence(app.getSession()
                         , "INSERT INTO CATEGORIES(ID,NAME) VALUES (?,?) "
                         , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{m_sID,m_jName.getText()});
                   }
             }else{
               String name=dlSales.getCategorieNamebyID(m_sID);
               if(!name.equals(m_jName.getText())){
                   new PreparedSentence(app.getSession()
                         , "UPDATE CATEGORIES SET NAME=? WHERE ID=?"
                         , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})).exec(new Object[]{m_jName.getText(),m_sID});
               }
             }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        location[0] = m_sID;
        location[1] = m_jName.getText();
        location[2] = m_jAddress.getText();
        location[3] = jTextField1.getText();
        location[4] = wmodel.getSelectedKey();

        if(stkStatus==true && stkStatus!=jCheckBox1.isSelected()){
            AppView m_App=LookupUtilityImpl.getInstance(null).getAppView();
            Object[] obj=(Object[])new PreparedSentence(m_App.getSession(), "SELECT SUM(STK),SUM(PUR) FROM (SELECT COUNT(S.ID) AS STK,0.0 AS PUR FROM PRODUCTS P JOIN PDT_PRCAT PR  ON P.ID=PR.ID AND PR.CATEGORY=? JOIN STOCKDIARY S ON  S.PRODUCT=P.ID OR S.PRODUCT1=P.ID union All "+
                                         " SELECT 0.0  AS STK,COUNT(P.ID) AS PUR FROM PRODUCTS P JOIN PDT_PRCAT PR  ON P.ID=PR.ID AND PR.CATEGORY=? JOIN PURCHASEJOURNAL PJ ON PJ.ITEM=P.ID)",new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                                         , new SerializerReadBasic(new Datas[]{Datas.INT,Datas.INT})).find(new Object[]{m_sID,m_sID});
            if(obj!=null ){
              int cnt1=Integer.valueOf(String.valueOf(obj[0]));
              int cnt=Integer.valueOf(String.valueOf(obj[1]));
              if(cnt>0 || cnt1>0){
                    location[5]=stkStatus;
                    JOptionPane.showMessageDialog(null, "Sorry Maintain stock cannot be set to false", "Entry exists in purchasejournal or stockdiary", JOptionPane.WARNING_MESSAGE);
              }else
                    location[5] = jCheckBox1.isSelected();
            }else
            location[5] = jCheckBox1.isSelected();
        }else
        location[5] = jCheckBox1.isSelected();
        m_sID=null;
        return location;
    }    
    
    public Component getComponent() {
        return this;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jAddress = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setLayout(null);

        jLabel2.setText("null");
        add(jLabel2);
        jLabel2.setBounds(20, 20, 100, 14);
        add(m_jName);
        m_jName.setBounds(130, 20, 230, 20);

        jLabel3.setText("null");
        add(jLabel3);
        jLabel3.setBounds(20, 50, 100, 14);
        add(m_jAddress);
        m_jAddress.setBounds(130, 50, 230, 20);

        jLabel1.setText("jLabel1");
        add(jLabel1);
        jLabel1.setBounds(20, 80, 100, 14);

        jTextField1.setText("jTextField1");
        add(jTextField1);
        jTextField1.setBounds(130, 80, 230, 20);

        jLabel4.setText("jLabel4");
        add(jLabel4);
        jLabel4.setBounds(20, 110, 100, 14);

        add(jComboBox1);
        jComboBox1.setBounds(130, 110, 230, 20);

        jLabel5.setText("jLabel5");
        add(jLabel5);
        jLabel5.setBounds(20, 140, 100, 14);

        jCheckBox1.setText("jCheckBox1");
        add(jCheckBox1);
        jCheckBox1.setBounds(130, 140, 81, 23);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField m_jAddress;
    private javax.swing.JTextField m_jName;
    // End of variables declaration//GEN-END:variables
    
}
