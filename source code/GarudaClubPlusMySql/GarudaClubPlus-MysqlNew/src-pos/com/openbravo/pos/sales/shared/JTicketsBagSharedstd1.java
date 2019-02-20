
package com.openbravo.pos.sales.shared;

import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import javax.swing.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.forms.*; 

public class JTicketsBagSharedstd1 extends JTicketsBagstd1 {
    
    private String m_sCurrentTicket = null;
    private DataLogicReceiptsstd1 dlReceipts = null;
    private AppView m_App;
    
    /** Creates new form JTicketsBagShared */
    public JTicketsBagSharedstd1(AppView app, TicketsEditor panelticket) {
       
        super(app, panelticket);
        
        dlReceipts = (DataLogicReceiptsstd1) app.getBean("com.openbravo.pos.sales.DataLogicReceiptsstd1");
         m_App=app;
        initComponents();
    }
    
    public void activate() {
        
        // precondicion es que no tenemos ticket activado ni ticket en el panel
        
        m_sCurrentTicket = null;
        selectValidTicket();     
        
        // Authorization
        m_jDelTicket.setEnabled(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
       
        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }
    
    public boolean deactivate() {
        
          // precondicion es que tenemos ticket activado aqui y ticket en el panel 
    String ticketBag = m_App.getProperties().getProperty("machine.ticketsbag");
        if (ticketBag.equals("restaurant")) {
          saveCurrentTicket();
        
        m_sCurrentTicket = null;
        m_panelticket.setActiveTicket(null, null);       
        
        return true;
    
        } else if (ticketBag.equals("standard")) {
          
        m_sCurrentTicket = null;
        m_panelticket.setActiveTicket(null, null);  
      
        
        
        }
        saveCurrentTicket();
        
        m_sCurrentTicket = null;
        m_panelticket.setActiveTicket(null, null);       
        
        return true;
        
        // postcondicion es que no tenemos ticket activado ni ticket en el panel
    }
        
    public boolean deleteTicket() {
        m_sCurrentTicket = null;
        return selectValidTicket();
    }
    
    protected JComponent getBagComponent() {
        return this;
    }
    
    protected JComponent getNullComponent() {
        return new JPanel();
    }
   
    private void saveCurrentTicket() {
        
        // save current ticket, if exists,
        if (m_sCurrentTicket != null) {
            try {

                dlReceipts.insertSharedTicket(m_sCurrentTicket, m_panelticket.getActiveTicket(),m_App.getAppUserView().getUser().getRole(),null,DataConstants.SALES);
            } catch (BasicException e) {
                new MessageInf(e).show(this);
            }  
        }    
    }
    
    private void setActiveTicket(String id) throws BasicException{
          
        // BEGIN TRANSACTION
        TicketInfo ticket = dlReceipts.getSharedTicket(id,m_App.getAppUserView().getUser().getRole(),DataConstants.SALES);
        if (ticket == null)  {
            // Does not exists ???
            throw new BasicException(AppLocal.getIntString("message.noticket"));
        } else {
            dlReceipts.deleteSharedTicket(id,m_App.getAppUserView().getUser().getRole());
            m_sCurrentTicket = id;
            m_panelticket.setActiveTicket(ticket, null);
        } 
        // END TRANSACTION                 
    }
    
    private boolean selectValidTicket() {
        boolean resultok = false;
        try {
            List<SharedTicketInfo> l = dlReceipts.getSharedTicketList(m_App.getAppUserView().getUser().getRole(),DataConstants.SALES);
            if (l.size() == 0) {
                newTicket();
            } else {
                setActiveTicket(l.get(0).getId());
            }
            resultok = true;
        } catch (BasicException e) {
            new MessageInf(e).show(this);
            newTicket();
        }
        return resultok;
    }    
    
    private void newTicket() {      
        
        saveCurrentTicket();

        TicketInfo ticket = new TicketInfo();    
        m_sCurrentTicket = UUID.randomUUID().toString(); // m_fmtid.format(ticket.getId());
        m_panelticket.setActiveTicket(ticket, null);      
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jNewTicket = new javax.swing.JButton();
        m_jDelTicket = new javax.swing.JButton();
        m_jListTickets = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        m_jNewTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        m_jNewTicket.setFocusPainted(false);
        m_jNewTicket.setFocusable(false);
        m_jNewTicket.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jNewTicket.setRequestFocusEnabled(false);
        m_jNewTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jNewTicketActionPerformed(evt);
            }
        });
        jPanel1.add(m_jNewTicket);

        m_jDelTicket.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editdelete.png"))); // NOI18N
        m_jDelTicket.setFocusPainted(false);
        m_jDelTicket.setFocusable(false);
        m_jDelTicket.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelTicket.setRequestFocusEnabled(false);
        m_jDelTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDelTicketActionPerformed(evt);
            }
        });
        jPanel1.add(m_jDelTicket);

        m_jListTickets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/unsortedList.png"))); // NOI18N
        m_jListTickets.setFocusPainted(false);
        m_jListTickets.setFocusable(false);
        m_jListTickets.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jListTickets.setRequestFocusEnabled(false);
        m_jListTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jListTicketsActionPerformed(evt);
            }
        });
        jPanel1.add(m_jListTickets);

        add(jPanel1, java.awt.BorderLayout.WEST);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jListTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jListTicketsActionPerformed

        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
               try {
                    List<SharedTicketInfo> l = dlReceipts.getSharedTicketList(m_App.getAppUserView().getUser().getRole(),DataConstants.SALES);

                  //  JTicketsBagSharedListstd1 listDialog = JTicketsBagSharedListstd1.newJDialog(JTicketsBagSharedstd1.this);
                    //String id = listDialog.showTicketsList(l); 

                   // if (id != null) {
                     //   saveCurrentTicket();
                       // setActiveTicket(id); 
                    //}
                } catch (BasicException e) {
                    new MessageInf(e).show(JTicketsBagSharedstd1.this);
                    newTicket();
                }                    
            }
        });
        
    }//GEN-LAST:event_m_jListTicketsActionPerformed

    private void m_jDelTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDelTicketActionPerformed
        
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannadelete"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            deleteTicket();
        }
        
    }//GEN-LAST:event_m_jDelTicketActionPerformed

    private void m_jNewTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jNewTicketActionPerformed

        newTicket();
        
    }//GEN-LAST:event_m_jNewTicketActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton m_jDelTicket;
    private javax.swing.JButton m_jListTickets;
    private javax.swing.JButton m_jNewTicket;
    // End of variables declaration//GEN-END:variables
    
}
