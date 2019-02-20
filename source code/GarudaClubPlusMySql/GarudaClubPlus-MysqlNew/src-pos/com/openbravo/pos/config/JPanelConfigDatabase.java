

package com.openbravo.pos.config;

import com.openbravo.data.user.DirtyManager;
import java.awt.Component;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.util.AltEncrypter;
import com.openbravo.pos.util.DirectoryEvent;

/**
 *
 * @author adrianromero
 */
public class JPanelConfigDatabase extends javax.swing.JPanel implements PanelConfig {
    
    private DirtyManager dirty = new DirtyManager();
    
    /** Creates new form JPanelConfigDatabase */
    public JPanelConfigDatabase() {
        
        initComponents();
        
        jtxtDbDriverLib.getDocument().addDocumentListener(dirty);
        jtxtDbDriver.getDocument().addDocumentListener(dirty);
        jtxtDbURL.getDocument().addDocumentListener(dirty);
        jtxtDbPassword.getDocument().addDocumentListener(dirty);
        jtxtDbUser.getDocument().addDocumentListener(dirty);
         
        jbtnDbDriverLib.addActionListener(new DirectoryEvent(jtxtDbDriverLib));
    }
    
    public boolean hasChanged() {
        return dirty.isDirty();
    }
    
    public Component getConfigComponent() {
        return this;
    }
   
    public void loadProperties(AppConfig config) {
        
        jtxtDbDriverLib.setText(config.getProperty("db.driverlib"));
        jtxtDbDriver.setText(config.getProperty("db.driver"));
        jtxtDbURL.setText(config.getProperty("db.URL"));
        
        String sDBUser = config.getProperty("db.user");
        String sDBPassword = config.getProperty("db.password");        
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            // La clave esta encriptada.
            AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }        
        jtxtDbUser.setText(sDBUser);
        jtxtDbPassword.setText(sDBPassword);   
        
        dirty.setDirty(false);
    }
   
    public void saveProperties(AppConfig config) {
        
        config.setProperty("db.driverlib", jtxtDbDriverLib.getText());
        config.setProperty("db.driver", jtxtDbDriver.getText());
        config.setProperty("db.URL", jtxtDbURL.getText());
        config.setProperty("db.user", jtxtDbUser.getText());
        AltEncrypter cypher = new AltEncrypter("cypherkey" + jtxtDbUser.getText());       
        config.setProperty("db.password", "crypt:" + cypher.encrypt(new String(jtxtDbPassword.getPassword())));

        dirty.setDirty(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxtDbPassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtxtDbDriverLib = new javax.swing.JTextField();
        jbtnDbDriverLib = new javax.swing.JButton();
        jtxtDbDriver = new javax.swing.JTextField();
        jtxtDbURL = new javax.swing.JTextField();
        jtxtDbUser = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(680, 190));
        setLayout(null);
        add(jtxtDbPassword);
        jtxtDbPassword.setBounds(150, 160, 180, 20);

        jLabel4.setText(AppLocal.getIntString("Label.DbPassword")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 160, 130, 14);

        jLabel3.setText(AppLocal.getIntString("Label.DbUser")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 130, 130, 14);

        jLabel2.setText(AppLocal.getIntString("Label.DbURL")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 100, 130, 14);

        jLabel1.setText(AppLocal.getIntString("Label.DbDriver")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 70, 130, 14);

        jLabel17.setText(AppLocal.getIntString("label.dbdriverlib")); // NOI18N
        add(jLabel17);
        jLabel17.setBounds(20, 40, 130, 14);

        jLabel8.setText(AppLocal.getIntString("Label.Database")); // NOI18N
        jLabel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        add(jLabel8);
        jLabel8.setBounds(20, 10, 660, 15);
        add(jtxtDbDriverLib);
        jtxtDbDriverLib.setBounds(150, 40, 340, 20);

        jbtnDbDriverLib.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        add(jbtnDbDriverLib);
        jbtnDbDriverLib.setBounds(500, 40, 49, 25);
        add(jtxtDbDriver);
        jtxtDbDriver.setBounds(150, 70, 180, 20);
        add(jtxtDbURL);
        jtxtDbURL.setBounds(150, 100, 340, 20);
        add(jtxtDbUser);
        jtxtDbUser.setBounds(150, 130, 180, 20);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton jbtnDbDriverLib;
    private javax.swing.JTextField jtxtDbDriver;
    private javax.swing.JTextField jtxtDbDriverLib;
    private javax.swing.JPasswordField jtxtDbPassword;
    private javax.swing.JTextField jtxtDbURL;
    private javax.swing.JTextField jtxtDbUser;
    // End of variables declaration//GEN-END:variables
    
}
