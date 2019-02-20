

package com.openbravo.pos.payment;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.util.RoundUtils;

public class JPaymentCheque extends javax.swing.JPanel implements JPaymentInterface {
    
    private JPaymentNotifier m_notifier;
    private ChequeDetails cd;
    private double m_dPaid;
    private double m_dTotal;
    private ComboBoxValModel bank;
    
    /** Creates new form JPaymentCash */
    public JPaymentCheque(JPaymentNotifier notifier) {
        
        m_notifier = notifier;
        
        initComponents();  
        
        m_jTendered.addPropertyChangeListener("Edition", new RecalculateState());
        m_jTendered.addEditorKeys(m_jKeys);
    }
    
    public void activate(CustomerInfoExt customerext, double dTotal) {
        
        m_dTotal = dTotal;
        
        
        m_jTendered.reset();
        m_jTendered.activate();
        
        printState();
        
    }
    public PaymentInfo executePayment() {
        ChequeDetails cd1 = new ChequeDetails();
        //cd1.setChequeno(jTextChequeNo.getText());
        cd1.setChequeno(jTextChequeNo.getText());
        cd1.setholder(LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getId());
        if(jTextBank.getText()== null || jTextBank.getText().length()<=0) {
            cd1.setBank(jComboBox1.getSelectedItem().toString());
        }
        else {
            cd1.setBank(jTextBank.getText());
        }
        cd1.setAmount(m_dPaid);
        return new PaymentInfoTicket(m_dPaid, "cheque", cd1);
    }
    public Component getComponent() {
        return this;
    }

    private void printState() {
        
        try {
            m_dPaid = m_jTendered.getValue();
        } catch (BasicException e){
            m_dPaid = m_dTotal;
        }   

        m_jMoneyEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dPaid)));
        DataLogicSales dlSales = LookupUtilityImpl.getInstance(null).getDataLogicSales();
        try {

            bank = new ComboBoxValModel(dlSales.getBankList().list());
            jComboBox1.setModel(bank);
        } catch(BasicException e) {
            new MessageInf(e).show(this);
        }
        int iCompare = RoundUtils.compare(m_dPaid, m_dTotal);
        
        // if iCompare > 0 then the payment is not valid
        m_notifier.setStatus(m_dPaid > 0.0 && iCompare <= 0, iCompare == 0);
    }
    
    private class RecalculateState implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            printState();
        }
    }     
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel3 = new javax.swing.JPanel();
        m_jTendered = new com.openbravo.editor.JEditorCurrencyPositive();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        m_jMoneyEuros = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextChequeNo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextBank = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(m_jKeys);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(m_jTendered, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel2.add(jPanel1, java.awt.BorderLayout.NORTH);

        add(jPanel2, java.awt.BorderLayout.EAST);

        jPanel4.setLayout(null);

        jLabel8.setText(AppLocal.getIntString("Label.InputCash")); // NOI18N
        jPanel4.add(jLabel8);
        jLabel8.setBounds(20, 20, 100, 14);

        m_jMoneyEuros.setBackground(new java.awt.Color(153, 153, 255));
        m_jMoneyEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jMoneyEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jMoneyEuros.setOpaque(true);
        m_jMoneyEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        jPanel4.add(m_jMoneyEuros);
        m_jMoneyEuros.setBounds(120, 20, 150, 25);

        jLabel1.setText("Cheque No");
        jPanel4.add(jLabel1);
        jLabel1.setBounds(20, 70, 80, 14);
        jPanel4.add(jTextChequeNo);
        jTextChequeNo.setBounds(120, 70, 150, 20);

        jLabel2.setText("Bank");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(20, 120, 60, 14);
        jPanel4.add(jTextBank);
        jTextBank.setBounds(120, 120, 150, 20);

        jComboBox1.setEditable(true);
        jPanel4.add(jComboBox1);
        jComboBox1.setBounds(120, 160, 150, 20);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextBank;
    private javax.swing.JTextField jTextChequeNo;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JLabel m_jMoneyEuros;
    private com.openbravo.editor.JEditorCurrencyPositive m_jTendered;
    // End of variables declaration//GEN-END:variables
    
}
