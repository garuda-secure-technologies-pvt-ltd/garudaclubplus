

package com.openbravo.pos.sales.shared;


import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.sales.JPanelTicketstd1;
import com.openbravo.pos.sales.SharedTicketInfo;
import com.openbravo.pos.sales.TicketsEditor;
import com.openbravo.pos.sales.simple.JTicketsBagSimple;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

public class JTicketsBagSharedListstd1 extends javax.swing.JDialog {
    
    private String m_sDialogTicket;
     private String m_sDialogTicket1;
    
    /** Creates new form JTicketsBagSharedList */
    private JTicketsBagSharedListstd1(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
       
    }
    /** Creates new form JTicketsBagSharedList */
    private JTicketsBagSharedListstd1(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }  
    
    public String showTicketsList(java.util.List<SharedTicketInfo> atickets) {
        
        
        for (int i = 0; i < atickets.size(); i++) {
            m_jtickets.add(new JButtonTicket(atickets.get(i)));                
        }  
        
        m_sDialogTicket = null;
         m_sDialogTicket1=null;
        setVisible(true);
           
        return m_sDialogTicket;
       
    }  
    public String showTicketsListstd(java.util.List<SharedTicketInfo> atickets) {
        
        
        for (int i = 0; i < atickets.size(); i++) {
            m_jtickets.add(new JButtonTicket(atickets.get(i)));                
        }  
        
        m_sDialogTicket = null;
         m_sDialogTicket1=null;
        setVisible(true);
         return m_sDialogTicket1;    
       
       
    }  
        
    public static JTicketsBagSharedListstd1 newJDialog(JTicketsBagShared ticketsbagshared) {
        
        Window window = getWindow(ticketsbagshared);
        JTicketsBagSharedListstd1 mydialog;
        if (window instanceof Frame) { 
            mydialog = new JTicketsBagSharedListstd1((Frame) window, true);
        } else {
            mydialog = new JTicketsBagSharedListstd1((Dialog) window, true);
        } 
        
        mydialog.initComponents();
        
        mydialog.jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
        
        return mydialog;
    }
     public static JTicketsBagSharedListstd1 newJDialog(JTicketsBagSimple ticketsbagsimple) {
        
        Window window = getWindow(ticketsbagsimple);
        JTicketsBagSharedListstd1 mydialog;
        if (window instanceof Frame) { 
            mydialog = new JTicketsBagSharedListstd1((Frame) window, true);
        } else {
            mydialog = new JTicketsBagSharedListstd1((Dialog) window, true);
        } 
        
        mydialog.initComponents();
        
        mydialog.jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
        
        return mydialog;
    }
      public static JTicketsBagSharedListstd1 newJDialog(JPanelTicketstd1 ticketsbag) {
        
        Window window = getWindow(ticketsbag);
        JTicketsBagSharedListstd1 mydialog;
        if (window instanceof Frame) { 
            mydialog = new JTicketsBagSharedListstd1((Frame) window, true);
        } else {
            mydialog = new JTicketsBagSharedListstd1((Dialog) window, true);
        } 
        
        mydialog.initComponents();
        
        mydialog.jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));
        
        return mydialog;
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

 
   
    
    private class JButtonTicket extends JButton {
        
        private SharedTicketInfo m_Ticket;
        
        public JButtonTicket(SharedTicketInfo ticket){
            
            super();
            
            m_Ticket = ticket;
            setFocusPainted(false);
            setFocusable(false);
            setRequestFocusEnabled(false);
            setMargin(new Insets(8, 14, 8, 14));
            //setFont(new java.awt.Font ("Dialog", 1, 24));    
            //setBackground(new java.awt.Color (220, 220, 220));
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    
                    // Selecciono el ticket
                    m_sDialogTicket = m_Ticket.getId();
                     m_sDialogTicket1=m_Ticket.getName();
                     
                    
                  //   m_jTicketId.setText(obj[1].toString());
                    // y oculto la ventana
                    JTicketsBagSharedListstd1.this.setVisible(false);
                }
            });
            
            setText(ticket.getName());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        m_jtickets = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jButtonCancel = new javax.swing.JButton();

        setTitle(AppLocal.getIntString("caption.tickets")); // NOI18N
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        m_jtickets.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jtickets.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
        jPanel2.add(m_jtickets, java.awt.BorderLayout.NORTH);

        jScrollPane1.setViewportView(jPanel2);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanel3.add(jPanel4);

        m_jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jButtonCancel.setText(AppLocal.getIntString("Button.Close")); // NOI18N
        m_jButtonCancel.setFocusPainted(false);
        m_jButtonCancel.setFocusable(false);
        m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonCancel.setRequestFocusEnabled(false);
        m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonCancelActionPerformed(evt);
            }
        });
        jPanel3.add(m_jButtonCancel);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-340)/2, (screenSize.height-272)/2, 340, 272);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonCancelActionPerformed

        dispose();
        
    }//GEN-LAST:event_m_jButtonCancelActionPerformed
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton m_jButtonCancel;
    private javax.swing.JPanel m_jtickets;
    // End of variables declaration//GEN-END:variables
    
}
