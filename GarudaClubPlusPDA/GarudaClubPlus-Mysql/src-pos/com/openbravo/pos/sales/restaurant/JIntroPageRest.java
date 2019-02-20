/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JIntroPageRest.java
 *
 * Created on Dec 4, 2008, 11:08:50 AM
 */

package com.openbravo.pos.sales.restaurant;
import com.openbravo.pos.ticket.TicketInfo;
import java.util.*;
import java.awt.*;
//import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import com.openbravo.pos.sales.*;
import com.openbravo.pos.forms.*;
import com.openbravo.data.loader.StaticSentence;
//import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.customers.DataLogicCustomers;
//import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.data.gui.ComboBoxValModel;
//import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
//import com.openbravo.data.loader.SerializerReadInteger;
//import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
//import com.openbravo.data.loader.Session;
//import com.openbravo.data.user.DirtyManager;
//import com.openbravo.pos.mant.FloorsInfo;
import java.util.ArrayList;
//import javax.swing.text.TableView;
//import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
/**
 *
 * @author swathi
 */
public class JIntroPageRest extends JTicketsBag {
    private DataLogicReceipts dlReceipts = null;
    private DataLogicSales dlSales = null;
    private DataLogicCustomers dlCustomers = null;
    private BillLogic dlBill;
    private JTicketsBagRestaurant m_restaurantmap;
    private TicketInfo m_oTicket;
     private CustomerInfoExt customer;
     private ComboBoxValModel m_table;
     private ComboBoxValModel m_waiter;
     private ComboBoxValModel m_floor;
     private SentenceList m_tlist;
     private SentenceList m_wlist;
     private SentenceList m_flist;
     Object table,wait;
     private ArrayList<TicketInfo> m_ticketList;
   //  private ArrayList<SharedTicketInfo> sharedticketlist;
     private IntroTableModel m_introtablemodel;
     protected Qticket qTicket;
     private AppView m_App;
     public static boolean dflag=false;
     
    public JIntroPageRest(AppView app, TicketsEditor panelTicket) {
        super(app,panelTicket);
  //      sharedticketlist=new ArrayList<SharedTicketInfo>();
        m_App=app;
        dlSales=(DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlReceipts = (DataLogicReceipts) app.getBean("com.openbravo.pos.sales.DataLogicReceipts");
        dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");

        this.dlBill = (BillLogic) app.getBean("com.openbravo.pos.sales.BillLogic");
        dlBill.setDataLogicSales(dlSales);
        dlBill.setAppView(app);

        qTicket = (Qticket) m_App.getBean("com.openbravo.pos.sales.Qticket");
        qTicket.setDataLogicSales(dlSales);
        qTicket.setAppView(m_App);
        
        loadCurrentSharedTickets();
        
        try {
            m_floor = new ComboBoxValModel(dlSales.getFloorsList().list());
            m_waiter=new ComboBoxValModel(dlSales.getActiveWaiterList(m_App.getAppUserView().getUser().getRole()));
           
        } catch (BasicException e) {
            new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("message.table_waiter_list_failed")).show(this);
            throw new IllegalStateException(e.getMessage(), e);
        }

        initComponents();
        jComboBox3.setModel(m_floor);
        guestlist.setSelectedIndex(-1);
        jComboBox2.setModel(m_waiter);
        jTextField2.setText(null);
        jButton7.setText("C List");
        m_restaurantmap = new JTicketsBagRestaurant(app, this);

        m_introtablemodel = new IntroTableModel(m_ticketList);
   
        jTable1.setModel(m_introtablemodel);

        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(1).setPreferredWidth(140);
        columnModel.getColumn(1).setMaxWidth(160);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(2).setMaxWidth(140);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(100);
         columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(4).setMaxWidth(140);

       

        jComboBox2.setEnabled(true);
        jComboBox3.setEnabled(true);
        //
         jComboBox1.setEnabled(false);

//        jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      //removed table model from here
       //tableselectionmodel();
     //   pageobj=this;

    }

   
    private void loadCurrentSharedTickets() {
        try {
            List<SharedTicketInfo> sti = dlReceipts.getSharedTicketList(m_App.getAppUserView().getUser().getRole());
           // sharedticketlist.addAll(sti);
            m_ticketList = new ArrayList<TicketInfo>(sti.size());
            for (Iterator<SharedTicketInfo> it = sti.iterator(); it.hasNext();) {
                SharedTicketInfo sharedTicketInfo = it.next();
                TicketInfo t = dlReceipts.getSharedTicket(sharedTicketInfo.getId(),m_App.getAppUserView().getUser().getRole());
                if (t != null) {
                    m_ticketList.add(t);
                }
            }
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }
    }

    private int addNewTicket(TicketInfo t) throws BasicException {
         String id="",name="";
     /*   Session s=LookupUtilityImpl.getInstance(null).getAppView().getSession();
        String id=(String) new StaticSentence(s, "CALL NEXT VALUE FOR SHAREDTICKETSNUM", null, SerializerReadInteger.INSTANCE).find();
        String cid=t.getCustomerId();
        String cust,waiter,table,floor;
        int flag=0,cflag=0;;
       table =  m_table.getSelectedItem().toString();
       waiter= m_waiter.getSelectedItem().toString();
       floor= m_floor.getSelectedItem().toString();
       cust=jTextField1.getText();
       String cust1,waiter1,table1,floor1;
        Object[] obj = (Object [] )  new StaticSentence(m_App.getSession()
                        , "SELECT NAME FROM SHAREDTICKETS WHERE CUSTID=? "
                        , SerializerWriteString.INSTANCE
                ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find( cid);
        if(obj==null )
        {
            cflag=0;
        }
        else
        {
            cflag=1;
        }
        if(cflag==1)
        {
        for(int i=0;i< m_introtablemodel.getRowCount();i++)
        {
        cust1= m_introtablemodel.getValueAt(i, 0).toString();
        waiter1=m_introtablemodel.getValueAt(i, 2).toString();
        table1=m_introtablemodel.getValueAt(i, 1).toString();
        floor1=m_introtablemodel.getValueAt(i, 3).toString();
          if(cflag==1 && waiter.equals(waiter1) && table.equals(table1) && floor.equals(floor1))
          {
            JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.duplicateuser"), AppLocal.getIntString("message.duplicatetitle"), JOptionPane.WARNING_MESSAGE);
            flag=1;
            break;
          }
        }
         // else
              if(cflag==1 )
              {
                  if(JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.customerpresent"), AppLocal.getIntString("message.presenttitle"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                  {
                      flag=0;
                     // break;
                  }
                  else
                  {
                      flag=1;
                  }
              }
      //  }
        }
       if(flag==0)*/
       
        //dlReceipts.insertSharedTicket(t.getCustomerId(), m_oTicket,id);

            String customername;

            if(guestlist.getSelectedIndex()>=0)
            {
                id=t.getCustomerId();
                name=m_oTicket.getCustomer().getName();
                customername=id+"#"+guestlist.getSelectedItem().toString();
                  m_oTicket.getCustomer().setid(customername);
                m_oTicket.getCustomer().setName(name+" : "+guestlist.getSelectedItem().toString());
              
            }
            else
            {
                id=t.getCustomerId();
                name=t.getCustomer().getName();
                customername=t.getCustomerId();
            }
      int count= dlReceipts.checkSharedTicket(customername, m_oTicket, m_App.getAppUserView().getUser().getRole());
        if(count==0)
        {

              dlReceipts.insertSharedTicket(customername, m_oTicket,m_App.getAppUserView().getUser().getRole());
              m_introtablemodel.addRow(t);
          return 1;
        }
        else
        {
           m_oTicket.getCustomer().setid(id);
           m_oTicket.getCustomer().setName(name);
           MessageInf msg = new MessageInf(MessageInf.SGN_CAUTION, AppLocal.getIntString("message.ticketalreadyexists"));
            msg.show(this);
            return 0;
        }
       
    }

    private void refreshlists() {
        try {
        //    m_table = new ComboBoxValModel(dlSales.getPlacesList().list());
            m_waiter=new ComboBoxValModel(dlSales.getActiveWaiterList(m_App.getAppUserView().getUser().getRole()));
            m_floor = new ComboBoxValModel(dlSales.getFloorsList().list());
            guestlist.setSelectedIndex(-1);
            loadCurrentSharedTickets();
        } catch (BasicException e) {
            new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("message.table_waiter_list_failed")).show(this);
            throw new IllegalStateException(e.getMessage(), e);
        }

         jComboBox3.setModel(m_floor);
        // jComboBox1.setModel(m_table);
         jComboBox2.setModel(m_waiter);

    }

    public void activate()
    {
        refreshlists();
        loadCurrentSharedTickets();
      // guestlist.setSelectedIndex(-1);
          m_introtablemodel = new IntroTableModel(m_ticketList);
        jTable1.setModel(m_introtablemodel);


        TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(1).setMaxWidth(140);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(2).setMaxWidth(140);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(100);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(120);
        jTextField1.setText(null);

       // jComboBox1.setSelectedIndex(-1);
        jComboBox2.setSelectedIndex(-1);
        jComboBox3.setSelectedIndex(0);
        m_panelticket.setActiveTicket(null, null);
        m_restaurantmap.activate();
        jTextField2.setText(null);
     //   cust=null;
        //  jTextField2.setCursor(this.getCursor());
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                jTextField2.requestFocus();
            }
        });
     //   jTextField2.requestFocus();

    }

    public boolean deactivate()
    {
       
        return true;
    }

    private boolean validateDeleteTicket() throws BasicException {
        List<QticketInfo> qts = LookupUtilityImpl.getInstance(null).getDataLogicQT().getPendingQTicketList(m_oTicket.getCustomerId());
        return qts.isEmpty();
    }

    public boolean deleteTicket()
    {
        boolean resultok = false;
        try {
            if (validateDeleteTicket()) {
                LookupUtilityImpl.getInstance(null).getDataLogicReceipts().deleteSharedTicket(m_oTicket.getCustomerId(),m_App.getAppUserView().getUser().getRole());
                resultok = true;
            } else {
                new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.pendingqt")).show(this);
            }
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }
        return resultok;
    }

    protected JComponent getBagComponent() {
        return m_restaurantmap;
    }
    protected JComponent getNullComponent() {
        return this;
    }

//    private void setActiveCustomer(CustomerInfo customer)  {
//        this.customer = customer;
//        m_oTicket = new TicketInfo();
//        try {
//            dlReceipts.insertSharedTicket(customer.getId(), m_oTicket);
//        } catch (BasicException e) {
//            new MessageInf(e).show(JIntroPageRest.this); // Glup. But It was empty.
//        }
//        m_panelticket.setActiveTicket(m_oTicket, customer.getName());
//
//    }

    private void refreshModel() {
        loadCurrentSharedTickets();
        ((IntroTableModel) jTable1.getModel()).refresh(m_ticketList);
    }
    
    private void updateCurrentSharedTicket() throws BasicException {
        if (m_oTicket != null) {
            dlReceipts.updateSharedTicket(m_oTicket.getCustomerId(), m_oTicket,m_App.getAppUserView().getUser().getRole());
        }
    }

    public void newTicket() {
        try {
            //The ticket might be modified from ReceiptSplit
            m_oTicket = m_panelticket.getActiveTicket();
            updateCurrentSharedTicket();

            activate();

           // printState();
            m_panelticket.setActiveTicket(null, null);
            refreshModel();
        } catch (BasicException e) {
            new MessageInf(e).show(this);
        }
    }


    private void setActiveChoice(CustomerInfoExt ac_cust,String ac_place,String ac_waiter, String ac_floor) {
        if (ac_cust == null || ac_place == null || ac_waiter == null || ac_floor == null) {
            new MessageInf(MessageInf.SGN_CAUTION, AppLocal.getIntString("message.chooseallticketvalues")).show(this);
            return;
        }
        try {
            m_oTicket = new TicketInfo();
            // String customername;
        /*    if(guestlist.getSelectedIndex()>=0)
            {

                ac_cust.setid(ac_cust.getId()+"#"+guestlist.getSelectedItem().toString());
                ac_cust.setName(ac_cust.getName()+" : "+guestlist.getSelectedItem().toString());
            }*/

            m_oTicket.setCustomer(ac_cust);
            m_oTicket.setPlace(dlSales.getPlaceByID(ac_place));
            m_oTicket.setWaiter(dlSales.getWaiterByID(ac_waiter));
            m_oTicket.setFloor(dlSales.getFloorByID(ac_floor));
            m_oTicket.setUser(m_App.getAppUserView().getUser().getUserInfo());
           int flag= addNewTicket(m_oTicket);
           if(flag==1)
            m_panelticket.setActiveTicket(m_oTicket, ac_place);
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_CAUTION, AppLocal.getIntString("message.ticketalreadyexists"), e);
            msg.show(this);
        }


    }

    private void displayBillList() {

        BillList billList = BillList.getDialog(this, dlSales, dlBill, customer);
        billList.showDialog();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this metho
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 =  new javax.swing.JTable(){public Component prepareRenderer(TableCellRenderer renderer,
            int rowIndex, int vColIndex) {
            Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));

                if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
                    jc.setBackground(Color.lightGray);
                }
                else {
                    jc.setBackground(Color.white);
                }
                if(isCellSelected(rowIndex, vColIndex))
                jc.setBackground(Color.cyan);
            }
            return c;
        }};
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        guestlist = new javax.swing.JComboBox();

        jLabel1.setText("Customer"); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        jButton1.setToolTipText("Member list");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Table"); // NOI18N

        jLabel3.setText("Waiter"); // NOI18N

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        jButton4.setText("Add New"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Times New Roman", 0, 13));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Customer", "Table", "Waiter", "Floorl"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAlignmentX(5.0F);
        jTable1.setRowHeight(18);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel4.setForeground(new java.awt.Color(0, 153, 0));
        jLabel4.setText("Existing Customers"); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        jButton2.setText("Select"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/vcard.png"))); // NOI18N
        jButton3.setToolTipText("Bill List");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/reload.png"))); // NOI18N
        jButton5.setToolTipText("Refresh");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Bill");
        jButton6.setToolTipText("Bill");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton6.getAccessibleContext().setAccessibleName("");
        jButton6.getAccessibleContext().setAccessibleDescription(null);

        jLabel5.setText("Floor");

        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jLabel6.setText("Mem No:");

        jLabel7.setText("Guest");

        guestlist.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Guest 1", "Guest 2", "Guest 3", "Guest 4", "Guest 5" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addComponent(jComboBox3, 0, 116, Short.MAX_VALUE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                                .addGap(8, 8, 8)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2))
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox2, 0, 163, Short.MAX_VALUE))
                                    .addComponent(guestlist, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                            .addComponent(jButton4))
                        .addGap(63, 63, 63))
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 403, Short.MAX_VALUE)
                                .addComponent(jButton2)))
                        .addGap(72, 72, 72)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guestlist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addGap(14, 14, 14)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(30, 30, 30))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(366, Short.MAX_VALUE))
        );

        jButton1.getAccessibleContext().setAccessibleDescription(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        //finder.search(m_oTicket.getCustomer());
        finder.setVisible(true);
        CustomerInfo customerInfo = finder.getSelectedCustomer();
        if (customerInfo != null) {
            try {
                customer = dlSales.loadCustomerExt(customerInfo.getId());
                jTextField1.setText(customerInfo.toString());
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
                msg.show(this);
            }
        }
         
//        try {
//            m_oTicket.setCustomer(finder.getSelectedCustomer() == null
//                  ? null
//                    : dlSales.loadCustomerExt(finder.getSelectedCustomer().getId()));
//        } catch (BasicException e) {
//            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), e);
//            msg.show(this);
//        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      /* String custid=jTextField2.getText();
       String custoid;
        if(customer == null && custid != null)
        {
            try{
            Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID FROM CUSTOMERS WHERE SEARCHKEY = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(custid);
            if(obj==null)
            {
            }
            else
            {
                custoid=obj[0].toString();
                customer = dlSales.loadCustomerExt(custoid);
            }
            }
          catch(Exception e)
          {
          }
        }*/
        setActiveChoice(customer, (String) m_table.getSelectedKey(), (String) m_waiter.getSelectedKey(), (String) m_floor.getSelectedKey());
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int i = jTable1.getSelectedRow();
        if (i >= 0 && i < m_ticketList.size()) {
            m_oTicket = m_ticketList.get(i);
            m_panelticket.setActiveTicket(m_oTicket, m_oTicket.getPlace().getName());
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.ticketnotselected"));
            msg.show(this);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        
        dflag=true;
        displayBillList();
        dflag=false;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        refreshModel();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed
//  public String cust;
    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        // TODO add your handling code here:
       // String cust=null;
         String custoid;

         if(evt.getKeyText(evt.getKeyCode()).equals("Enter")){
             String cust=jTextField2.getText();
              try{
            Object[] obj=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID,NAME FROM CUSTOMERS WHERE SEARCHKEY = ?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING,Datas.STRING})).find(cust.toUpperCase());
            if(obj==null)
            {
                JOptionPane.showMessageDialog(this,AppLocal.getIntString("message.custnotpresent"), AppLocal.getIntString("message.custtitle"), JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                custoid=obj[0].toString();
                customer = dlSales.loadCustomerExt(custoid);
                  jTextField1.setText(obj[1].toString());
            }
            }
          catch(Exception e)
          {
              e.printStackTrace();
          }
         }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()>1)
        {
          int i = jTable1.getSelectedRow();
        if (i >= 0 && i < m_ticketList.size()) {
            m_oTicket = m_ticketList.get(i);
            m_panelticket.setActiveTicket(m_oTicket, m_oTicket.getPlace().getName());
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.ticketnotselected"));
            msg.show(this);
        }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
           String temp=m_floor.getSelectedItem().toString();
           String floor;
       try{
       if(temp!=null)
       {
          // try{
            Object[] obj1=(Object[])   new StaticSentence(m_App.getSession()
                        , "SELECT ID FROM FLOORS WHERE NAME=?"
                       ,SerializerWriteString.INSTANCE
                       ,new SerializerReadBasic(new Datas[] { Datas.STRING})).find(temp);

        if(obj1== null)
            floor="";
        else{
           floor=obj1[0].toString();
        }
            m_table = new ComboBoxValModel(dlSales.getPlaceList(floor));
            jComboBox1.setModel(m_table);
             jComboBox1.setSelectedIndex(0);
              jComboBox1.setEnabled(true);
          // }
       }
       }
       catch(Exception e)
       {
          e.printStackTrace();
       }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
          QTList qtList = QTList.getDialog(this, m_App, dlSales, qTicket);
         try{
            int row= jTable1.getSelectedRow();
            String id;
             int i = jTable1.getSelectedRow();
            if (i >= 0 && i < m_ticketList.size()) {
            m_oTicket = m_ticketList.get(i);
            }

      /*     String name= m_introtablemodel.getValueAt(row, 0).toString();
           String arr[]=name.split(" - ");
           name=arr[1];
           String searchkey=arr[0];
           
           Object[] obj = (Object [] )  new StaticSentence(m_App.getSession()
                        , "SELECT CID FROM SHAREDTICKETS WHERE NAME = ? AND COUNTER=? "
                        ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
                ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find(new Object[]{ searchkey,m_App.getAppUserView().getUser().getRole()});
     if(obj != null && obj[0]!=null)*/
     {
        // id=obj[0].toString();
           id=m_oTicket.getCustomerId();
          CustomerInfo cinfo=new CustomerInfo(id);
        //  cinfo.setName(name);
          cinfo.setName(m_oTicket.getCustomer().getName());
         // cinfo.setSearchkey(searchkey);
          cinfo.setSearchkey(m_oTicket.getCustomer().getSearchkey());
          boolean flag=qtList.setCustomer(cinfo);
          if(flag==true){
           qtList.refreshModel();
           qtList.displayBill();
          }
          // qtList.reloadPendingQTList(true);
     }
   //  else
   //      id="";


         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        DebtBillList dbillList = DebtBillList.getDialog(this, dlSales, m_App,false);
        dbillList.showDialog();
    }//GEN-LAST:event_jButton7ActionPerformed
    
    /**
    * @param args the command line arguments
    */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox guestlist;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
