/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BCIMainFrame.java
 *
 * Created on 9 Jun, 2010, 11:28:57 PM
 */
package bci;

import DAO.Dependent;
import DAO.User;
import DBConnection.Session;
import DBConnection.esslVDBConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import serialNumber.serialNumber;
import bci.initForm.JFlowPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;
import javax.swing.JButton;
import bci.initForm.passwordCheck;
import bci.initForm.passwordCreatorDialog;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import rfidCardReaderNew.MainCard;
import bci.initForm.waitDialog;
import javax.swing.JFrame;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import rfidCardReaderNew.CardReader;

public class BCIMainFrame extends javax.swing.JFrame {

    public static passwordCreatorDialog pwordDialog;
    public static passwordCheck pwordCheckDialog;
    public static esslVDBConnection esslv3Connection;
    public static Session eSession;
    public static Dependent selectedDep;
    public static User selectedUser;
    public static serialNumber serialObj;
    public String comboitem = null;
    private ComboBoxValModel dependentModel;
    private ListValModel listModel;
    public String DSCvalue;
    public String fpFolderPath;
    public String vbExePath;
    public static MainCard rfmain;
    public static int CombotimeInt;
    public String dateString;
    public waitDialog wDia;
    public Object object;
    public static String DateCheck;
    private int status=0;
    public String cardfinalval = null;
    public CardReader obj;
    public String deleteDevicestaffcode;
    boolean contetion = false;
    public int tabbedchekval = 0;
    ArrayList<String> Amemlist = new ArrayList();
    DefaultListModel model = new DefaultListModel();
    DefaultListModel deModel = new DefaultListModel();
    DefaultListModel logModel = new DefaultListModel();
    int count = 0;
    //me
    private ComboBoxValModel MemberComboModel;
    private JTextComponent memberComponent;

    public BCIMainFrame() {
        initComponents();
        jScrollPane3.getVerticalScrollBar().setPreferredSize(new Dimension(33, 33));
       // Dimension d = new Dimension(800, 600);
       // this.setPreferredSize(d);
      //  this.setMinimumSize(d);
        jTabbedPane1.setVisible(false);
        MemberComboModel = new ComboBoxValModel();
        jPanel1.remove(jScrollPane1);
        jPanel25.remove(jScrollPane5);
        jPanel30.remove(jScrollPane6);
        jButton14.setVisible(false);
        jList3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTabbedPane1.setUI(new BasicTabbedPaneUI() {

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 32;
            }
        });

        jLabel6.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        String meTextval = jTextField12.getText();
                        if(meTextval.equals("")){
                            JOptionPane.showMessageDialog(null, "Please select member!!");
                        }else{
                        dummy d = new dummy();
                        Thread t = new Thread(d);
                        t.start();
                        wDia = new waitDialog(new JFrame(), true);
                     
                        jButton10.setEnabled(true);
                        Connection memConnecton1 = eSession.getConnection();
                        String UpdateMemberQry = "Update memberMasters set FPCheck = 'True' where MemNumber ='" + jTextField12.getText() + "'";  //
                        PreparedStatement selectMem = memConnecton1.prepareStatement(UpdateMemberQry);
                        selectMem.executeUpdate();
                        Runtime rt = Runtime.getRuntime();
                        Process p = rt.exec(vbExePath);
                        wDia.showDialog();
                        p.waitFor();
                        

                        Connection memConnecton = eSession.getConnection();
                        String selectmemMaster = "select DeviceStaffCode from memberMasters where MemNumber ='" + jTextField12.getText() + "'";
                        PreparedStatement selectPstm = memConnecton.prepareStatement(selectmemMaster);
                        ResultSet memMasterRs = selectPstm.executeQuery();
                        while (memMasterRs.next()) {
                            DSCvalue = memMasterRs.getString(1);
                        }

                        if (DSCvalue == null) {
                            jLabel6.setText("No Fingerprint available!!");
                        } else {
                            BufferedImage myPicture = ImageIO.read(new File(fpFolderPath + "\\" + DSCvalue + ".jpg"));
                            jLabel6.setIcon(new ImageIcon(myPicture));

                        }
                        String UpdateMemberQry1 = "Update memberMasters set FPCheck = 'false' where MemNumber ='" + jTextField12.getText() + "'";  //
                        PreparedStatement Upmempstm = memConnecton1.prepareStatement(UpdateMemberQry1);
                        Upmempstm.executeUpdate();
                        }
                    } catch (Exception ex) {
                        System.out.println("Unable to load VB" + ex);
                    }

                }
            }
        });

        jList3.addKeyListener(new ListBoxListener()); 
        jList3.addMouseListener(new ListBoxMouseListener());
        jList4.addKeyListener(new logBoxListener());
        jList4.addMouseListener(new logBoxMouseListener());
        jTextField12.addKeyListener(new TextBoxListener());
        jTextField16.addKeyListener(new TextLogListener());
        jTextField23.addKeyListener(new TextDeleteListener());
        //Delete member
        jTextField23.addKeyListener(new TextBoxDeleteListener());
        jList5.addKeyListener(new ListBoxDeleteListener());
        jList5.addMouseListener(new ListBoxDeleteMouseListener());
    /*    jTextField19.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                try{
                cardSwipe();
                }catch(Exception exc){
                    exc.printStackTrace();
                }
            }

            public void removeUpdate(DocumentEvent e) {
                jTextField19.setText("");
            }

            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }); */
    }

    public void textboxPressEnter() {
        try {
            String val = getmemberName(jTextField12.getText());
            String cardval = getmemberCard(jTextField12.getText());
            if (val == null) {
                mName.setText("");
                cardNumber.setText("");
                try {
                    BufferedImage myPicture1 = ImageIO.read(new File(fpFolderPath + "\\Garuda.jpg"));
                    jLabel6.setIcon(new ImageIcon(myPicture1));
                } catch (Exception e12) {
                    System.out.println("file not rading" + e12);
                }
                jLabel16.setIcon(new ImageIcon(fpFolderPath + "\\Garuda.bmp"));
                JOptionPane.showMessageDialog(null, "No Record Found!!");
            } else {

                mName.setText(val);
                cardNumber.setText(cardval);
                try {
                    Connection memConnecton = eSession.getConnection();
                    String selectmemMaster = "select DeviceStaffCode from memberMasters where MemNumber ='" + jTextField12.getText() + "'";
                    
                    PreparedStatement selectPstm = memConnecton.prepareStatement(selectmemMaster);
                    ResultSet memMasterRs = selectPstm.executeQuery();
                    while (memMasterRs.next()) {
                        DSCvalue = memMasterRs.getString(1);
                    }

                    if (DSCvalue == null) {
                        jLabel6.setText("No Fingerprint available!!");
                    } else {
                        BufferedImage myPicture = ImageIO.read(new File(fpFolderPath + "\\" + DSCvalue + ".jpg"));
                        jLabel6.setIcon(new ImageIcon(myPicture));
                        jLabel8.setText("Finger print already available, for update click update button..");
                        jLabel8.paintImmediately(jLabel8.getVisibleRect());
                    }

                } catch (Exception ex1) {
                    BufferedImage myPicture;
                    try {
                        myPicture = ImageIO.read(new File(fpFolderPath + "\\garuda.jpg"));
                        jLabel6.setIcon(new ImageIcon(myPicture));

                    } catch (IOException ex) {
                        System.out.println("Cannot able to read file" + ex1);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("text box press enter error!!!" + e);
        }
    }

    public void logtextboxEnter(){
        try{
           String logtextmember = jTextField16.getText();
           Connection memcon = eSession.getConnection();
           String memQry = "Select memName from memberMasters where memnumber = '"+logtextmember+"'";
           PreparedStatement pstm = memcon.prepareStatement(memQry);
           ResultSet rs1 = pstm.executeQuery();
           String logMemberName = null;
            while (rs1.next()) {
                logMemberName = rs1.getString(1);
            }
           jTextField15.setText(logMemberName);
        }catch(Exception e){
            System.out.println("log textbox method error!!"+e);
        }
    }

    public DefaultListModel takeDataFromMemberDB(String memTypeCode, String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType = '" + memTypeCode + "') and memnumber like '" + value + "%' and Active = '1'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }

    public DefaultListModel takeDataFromStaffDB(String memTypeCode, String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType = '" + memTypeCode + "') and memnumber like '" + value + "%' and Active = '1'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }

    public DefaultListModel takeDataFromChildDB(String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType != 'M' and memType != 'S' and memType != 'Z') and memnumber like '" + value + "%' and Active = '1'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }
//
  public DefaultListModel takeDataFromMemberDB1(String memTypeCode, String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType = '" + memTypeCode + "') and memnumber like '" + value + "%'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }

    public DefaultListModel takeDataFromStaffDB1(String memTypeCode, String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType = '" + memTypeCode + "') and memnumber like '" + value + "%'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }

    public DefaultListModel takeDataFromChildDB1(String value) {
        DefaultListModel lModel = new DefaultListModel();
        try {
            Connection membelistCon = eSession.getConnection();
            String membelistQry = "select memNumber from memberMasters where (memType != 'M' and memType != 'S' and memType != 'Z') and memnumber like '" + value + "%'";
            PreparedStatement membelistPstm = membelistCon.prepareStatement(membelistQry);
            ResultSet membelistRS = membelistPstm.executeQuery();
            while (membelistRS.next()) {
                lModel.addElement(membelistRS.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }
//
    public DefaultListModel takeDataFromLogTable(String memTypeCode) {   
        DefaultListModel lModel = new DefaultListModel();
        ArrayList<String> logArrList = new ArrayList<String>();
        try {  
            Connection membelistCon = eSession.getConnection();
            String membernoQry = "Select deviceStaffcode from memberMasters where memnumber like '" + memTypeCode + "%'";
            PreparedStatement memberPstm = membelistCon.prepareStatement(membernoQry);
            ResultSet memberRs = memberPstm.executeQuery();
            String deviceStaff = null;
            while (memberRs.next()) {
                deviceStaff = memberRs.getString(1);
                Connection logConn = esslv3Connection.getConnection();
                String logedmemQry = "SELECT DeviceStaffId FROM Log where DateTime like '"+ dateString +"%' and devicestaffid = '"+ deviceStaff +"'" ;
                PreparedStatement logPstm = logConn.prepareStatement(logedmemQry);
                ResultSet logRs = logPstm.executeQuery();
                String deviceid = null;
                while (logRs.next()) {
                    deviceid = logRs.getString(1);
                    if (!logArrList.contains(deviceid)){
                        logArrList.add(deviceid);
                        String qry = "Select memNumber from memberMasters where devicestaffcode ='"+deviceid +"'";
                        PreparedStatement memPstm = membelistCon.prepareStatement(qry);
                        ResultSet rs = memPstm.executeQuery();
                        String number = null;
                        while (rs.next()) {
                            number = rs.getString(1);
                            lModel.addElement(number);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Unable to get the vale from DB" + e);
        }
        return lModel;
    }

    public ArrayList<String> getmemberNumber(String memnum) {
        try {
            Connection mConn = eSession.getConnection();
            String memNumber = "Select MemNumber from MemberMasters where memNumber like '" + memnum + "%'";
            PreparedStatement mpstm = mConn.prepareStatement(memNumber);
            ResultSet mRs = mpstm.executeQuery();

            while (mRs.next()) {
                Amemlist.add(mRs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Cannot get memberNumber!!");
        }
        return Amemlist;
    }

    public String getmemberName(String memnum) {
        String mName = null;
        try {
            Connection mConn = eSession.getConnection();
            String memNumber = "Select MemName,cardnumber from MemberMasters where memNumber like '" + memnum + "'";
            PreparedStatement mpstm = mConn.prepareStatement(memNumber);
            ResultSet mRs = mpstm.executeQuery();

            //String MCardnumber = null;
            while (mRs.next()) {
                mName = mRs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("Cannot get memberNumber!!");
        }
        return mName;
    }

    public String getmemberCard(String memCard) {
        String mCard = null;
        try {
            Connection mConn = eSession.getConnection();
            String memNumber = "Select cardnumber from MemberMasters where memNumber like '" + memCard + "'";
            PreparedStatement mpstm = mConn.prepareStatement(memNumber);
            ResultSet mRs = mpstm.executeQuery();
            while (mRs.next()) {
                mCard = mRs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("Cannot get memberNumber!!");
        }
        return mCard;
    }

    public void componentSet() {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            int fwidth = (int) toolkit.getScreenSize().getWidth();
            int fhight = (int) toolkit.getScreenSize().getHeight();
            jPanel17.setSize(new Dimension(fwidth, 120));
            jPanel19.setSize(new Dimension(fwidth, 1024));
            jPanel20.setSize(new Dimension(fwidth, 625));

            int fwidthnew = fwidth / 2;
            fwidthnew = fwidthnew / 4;
            jTabbedPane1.setSize(new Dimension(fwidth - fwidthnew, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Load() throws IOException {
        this.setVisible(true);
        Property property = new Property();
        File file = new File("C:\\GarudaClubNameManagement.properties");
        if (!file.exists()) {
            file.createNewFile();
            property.setClubName("");
            property.storeclubName();
        }
        File file1 = new File("C:\\GarudaClubLogoManagement.properties");
        if (!file1.exists()) {
            file1.createNewFile();
            property.setClubLogo("");
            property.storeclubLogo();
        }
        File file2 = new File("C:\\GarudaFingerprint.properties");
        if (!file2.exists()) {
            file2.createNewFile();
            property.setfingerprintFolder("");
            property.setvbExe("");
            property.storeFingerprintfolder();
        }
        File file3 = new File("GarduaRegistration.properties");
        if (!file3.exists()) {
            file3.createNewFile();
            property.setAdbServer("");
            property.setAdbdbname("");
            property.setAdbport("1433");
            property.setRdbServer("");
            property.setRdbdbname("");
            property.setRdbport("1433");
            property.store();
        }
        //
        Property pro = new Property();
        pro.readfingerprintInfo();
        fpFolderPath = pro.getfingerprintFolder();
        vbExePath = pro.getvbExe();
        property.readclubName();
        property.readclubLogo();
        String clubName = property.getClubName();
        String clubLogo = property.getClubLogo();
        jLabel7.setIcon(new ImageIcon(clubName));
        jLabel7.setHorizontalAlignment(jLabel7.CENTER);
        jLabel3.setIcon(new ImageIcon(clubLogo));        
        //

        Property prop = new Property();
        prop.Read();
        String eServer = prop.getAdbServer();
        String eDBName = prop.getAdbdbname();
        String pno = prop.getAdbport();
        String eDBuser = prop.getDBusername();
        String pass = prop.getDBpassword();
        int essPort = Integer.parseInt(pno);
        try {
            eSession = new Session(eServer, essPort, eDBName, eDBuser,pass);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occured While connecting to " + eDBName, eServer + essPort, JOptionPane.ERROR_MESSAGE);
        }

        String Server = prop.getRdbServer();
        String DBName = prop.getRdbdbname();
        String pno1 = prop.getRdbport();
        essPort = Integer.parseInt(pno);
        try {
            esslv3Connection = new esslVDBConnection(Server, essPort, DBName, eDBuser,pass);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Occured While connecting to " + DBName, Server + essPort, JOptionPane.ERROR_MESSAGE);
        }

        addRoletoCombo();
        addUsertoCombo();
        addUsertoCombo2();
        addCardtoCombo();
        loaddatabaseInformation();
       // deactivateLogDetails();
    }

    public static void main(String args[]) throws IOException {
        try {
            Property prop = new Property();
            File myfile = new File("C:\\Garudalic.properties");
            if (!myfile.exists()) {
                myfile.createNewFile();
                prop.setLicenseKey("0000-0000-0000");
                prop.setSerailNumber("123456789");
                prop.storeLicenseKey();
            }
            prop.readLicence();
            String val = prop.getcountcheckval();
            if (val.equals("False")) {
                serialObj.callSerialNumber();
            }
        } catch (Exception e1) {
            System.out.println("Property read error !!" + e1);
        }
        final BCIMainFrame frame = new BCIMainFrame();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int fwidth = (int) toolkit.getScreenSize().getWidth();
        int fhight = (int) toolkit.getScreenSize().getHeight();
        frame.setVisible(true);
        frame.setSize(fwidth, fhight);
        frame.setLocationRelativeTo(null);
        // frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.componentSet();
        frame.Load();
        frame.listOfUsers();
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            public void run() {
                //methodToCall();   // Create one method for Cardinformation
                metadataInfo();
                frame.deactivateLogDetails();
            }
        };

        int conbovalue = frame.getcombovalues();
        int giveValue = conbovalue * 20 * 1000;
        timer.scheduleAtFixedRate(tt, new Date(), giveValue);
        synchronized (BCIMainFrame.class) {       //testit calss i changed==
            try {
                BCIMainFrame.class.wait();
            } catch (InterruptedException ie) {
            }
        }

    }

    public int getcombovalues() {
        int returnCombo = 0;
        try {
            String combologTime = jComboBox2.getSelectedItem().toString();
            returnCombo = Integer.parseInt(combologTime);
        } catch (Exception e) {
            System.out.println("Con't get combovalue" + e);
        }
        return returnCombo;
    }

    public void DBupdateConnectVB() {

        int answer = JOptionPane.showConfirmDialog(null, "Do you want to overwrite Fingerprint", "Message", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            if (jTextField12.getText() != null) {
                try {
                    Connection memConnecton = eSession.getConnection();
                    String UpdateMemberQry = "Update memberMasters set FPCheck = 'True' where MemNumber ='" + jTextField12.getText() + "'";  //
                    PreparedStatement selectMem = memConnecton.prepareStatement(UpdateMemberQry);
                    selectMem.executeUpdate();

                    String selectmemMaster = "select DeviceStaffCode from memberMasters where MemNumber ='" + jTextField12.getText() + "'";
                    PreparedStatement selectPstm = memConnecton.prepareStatement(selectmemMaster);
                    ResultSet memMasterRs = selectPstm.executeQuery();
                    while (memMasterRs.next()) {
                        DSCvalue = memMasterRs.getString(1);
                    }

                 //   System.out.println(fpFolderPath);
                 //   System.out.println(vbExePath);

                    try {
                        Runtime rt = Runtime.getRuntime();
                        Process p = rt.exec(vbExePath);
                        p.waitFor();

                        if (DSCvalue == null) {
                            jLabel6.setText("No Fingerprint available!!");
                        } else {
                            BufferedImage myPicture = ImageIO.read(new File(fpFolderPath + "\\" + DSCvalue + ".jpg"));
                            jLabel6.setIcon(new ImageIcon(myPicture));
                        }

                    } catch (Exception e) {
                        System.out.println("Cannot able to load VB Exe!!");
                    }
                    String UpdateMemberQry1 = "Update memberMasters set FPCheck = 'False' where MemNumber ='" + jTextField12.getText() + "'";  //
                    PreparedStatement upfalsePstm = memConnecton.prepareStatement(UpdateMemberQry1);
                    upfalsePstm.executeUpdate();

                } catch (Exception e) {
                    System.out.println("Connection filed for fingerprint DB" + e);
                }
            }
        }
    }

    private class ComboBoxValModel extends DefaultComboBoxModel {

        private List list;

        public ComboBoxValModel(List list) {
            super(new Vector(list));
            this.list = list;
        }

        private ComboBoxValModel() {
            this(new ArrayList());
        }

        public Object getDataAt(int index) {
            return list.get(index);
        }
    }

    private class ListValModel extends AbstractListModel {

        private List list;

        public ListValModel(List list) {
            super();
            this.list = list;
        }

        public Object getDataAt(int index) {
            return list.get(index);
        }

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel17 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        m_jPanelContainer = new javax.swing.JPanel();
        m_jPanelLogin = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        m_jLogonName = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        m_txtKeys = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cardNumber = new javax.swing.JTextField();
        mName = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jLabel34 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel41 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jButton37 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        browseButton = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jButton38 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jLabel17 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jTextField16 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jButton23 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton21 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jButton33 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jButton34 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 0)));
        jPanel17.setPreferredSize(new java.awt.Dimension(10, 0));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("                                                   ");
        jPanel17.add(jLabel7, java.awt.BorderLayout.CENTER);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/poweredby1.png"))); // NOI18N
        jPanel17.add(jLabel18, java.awt.BorderLayout.LINE_END);
        jPanel17.add(jLabel3, java.awt.BorderLayout.LINE_START);

        getContentPane().add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.setPreferredSize(new java.awt.Dimension(1024, 700));
        jPanel18.setLayout(new java.awt.BorderLayout());

        m_jPanelContainer.setLayout(new java.awt.BorderLayout());

        m_jPanelLogin.setPreferredSize(new java.awt.Dimension(625, 148));
        m_jPanelLogin.setLayout(new java.awt.BorderLayout());

        jPanel19.setPreferredSize(new java.awt.Dimension(600, 200));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/logo.png"))); // NOI18N
        jLabel29.setText("<html><center><br><br><h3>"+
            "Garuda Biometric & Card Registration system<br></h3>" +
            "</br></br>" +
            "</center>");
        jLabel29.setAlignmentX(0.5F);
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel29.setMaximumSize(new java.awt.Dimension(800, 1024));
        jLabel29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel19.add(jLabel29);

        m_jPanelLogin.add(jPanel19, java.awt.BorderLayout.CENTER);

        m_jLogonName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jLogonName.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(510, 118));
        m_jLogonName.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel22.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
        jPanel21.add(jPanel22, java.awt.BorderLayout.NORTH);

        jPanel23.setLayout(null);

        m_txtKeys.setPreferredSize(new java.awt.Dimension(0, 0));
        m_txtKeys.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_txtKeysKeyTyped(evt);
            }
        });
        jPanel23.add(m_txtKeys);
        m_txtKeys.setBounds(0, 0, 0, 0);

        jPanel21.add(jPanel23, java.awt.BorderLayout.CENTER);

        m_jLogonName.add(jPanel21, java.awt.BorderLayout.LINE_END);

        jPanel20.add(m_jLogonName);

        jButton15.setText("Close");
        jButton15.setPreferredSize(new java.awt.Dimension(80, 35));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel20.add(jButton15);

        m_jPanelLogin.add(jPanel20, java.awt.BorderLayout.SOUTH);

        m_jPanelContainer.add(m_jPanelLogin, java.awt.BorderLayout.CENTER);

        jPanel18.add(m_jPanelContainer, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel18, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(jLabel26, java.awt.BorderLayout.LINE_START);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 0, 51)));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setAlignmentX(0.0F);
        jTabbedPane1.setAlignmentY(0.0F);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1024, 628));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel1.setLayout(null);

        jLabel5.setText("Finger Print");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(60, 230, 80, 14);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setForeground(new java.awt.Color(255, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Double Click here to give fingerprint");
        jLabel6.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 153, 0)));
        jPanel1.add(jLabel6);
        jLabel6.setBounds(170, 230, 240, 230);

        jLabel1.setText("Membership No :");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(60, 80, 110, 14);

        jLabel2.setText("Member Name");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(60, 120, 110, 14);

        jLabel4.setText("Card Number");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(60, 160, 120, 14);
        jPanel1.add(cardNumber);
        cardNumber.setBounds(180, 160, 190, 20);
        jPanel1.add(mName);
        mName.setBounds(180, 120, 190, 20);

        jButton10.setText("Update");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton10);
        jButton10.setBounds(550, 410, 120, 30);
        jPanel1.add(jLabel8);
        jLabel8.setBounds(160, 470, 340, 20);
        jPanel1.add(jTextField12);
        jTextField12.setBounds(180, 80, 190, 20);

        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList3);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(370, 80, 120, 130);

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("  M");
        jPanel1.add(jRadioButton3);
        jRadioButton3.setBounds(510, 80, 50, 23);

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setText("  S");
        jPanel1.add(jRadioButton4);
        jRadioButton4.setBounds(560, 80, 50, 23);

        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("  C");
        jPanel1.add(jRadioButton5);
        jRadioButton5.setBounds(620, 80, 40, 23);
        jPanel1.add(jLabel34);
        jLabel34.setBounds(210, 190, 180, 30);

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton20.setText("Back");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton20);
        jButton20.setBounds(10, 440, 90, 23);

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 0, 102)), "Card Reader"));
        jPanel26.setLayout(null);

        jLabel35.setFont(new java.awt.Font("Tahoma", 2, 11));
        jLabel35.setForeground(new java.awt.Color(102, 102, 255));
        jLabel35.setText("Select the Reader Port");
        jPanel26.add(jLabel35);
        jLabel35.setBounds(30, 30, 130, 14);

        jTextField18.setText("COM1");
        jPanel26.add(jTextField18);
        jTextField18.setBounds(30, 50, 130, 20);

        jButton28.setFont(new java.awt.Font("Tahoma", 2, 11));
        jButton28.setForeground(new java.awt.Color(204, 0, 255));
        jButton28.setText("Connect");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel26.add(jButton28);
        jButton28.setBounds(30, 80, 130, 23);

        jLabel36.setFont(new java.awt.Font("Tahoma", 2, 11));
        jLabel36.setForeground(new java.awt.Color(102, 102, 255));
        jLabel36.setText("CardNumber");
        jPanel26.add(jLabel36);
        jLabel36.setBounds(60, 130, 80, 14);

        jTextField19.setEditable(false);
        jPanel26.add(jTextField19);
        jTextField19.setBounds(30, 150, 130, 20);

        jButton29.setFont(new java.awt.Font("Tahoma", 2, 11));
        jButton29.setForeground(new java.awt.Color(204, 0, 255));
        jButton29.setText("Register Card");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel26.add(jButton29);
        jButton29.setBounds(30, 180, 130, 23);

        jLabel37.setFont(new java.awt.Font("Tahoma", 2, 11));
        jLabel37.setForeground(new java.awt.Color(255, 0, 51));
        jPanel26.add(jLabel37);
        jLabel37.setBounds(40, 110, 110, 20);

        jPanel1.add(jPanel26);
        jPanel26.setBounds(510, 120, 180, 230);

        jButton13.setText("Clear");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13);
        jButton13.setBounds(550, 370, 120, 30);

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setText("staff");
        jPanel1.add(jRadioButton1);
        jRadioButton1.setBounds(670, 80, 60, 23);

        jLabel41.setForeground(new java.awt.Color(255, 0, 51));
        jPanel1.add(jLabel41);
        jLabel41.setBounds(10, 10, 180, 20);

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel49.setForeground(new java.awt.Color(0, 51, 255));
        jLabel49.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel1.add(jLabel49);
        jLabel49.setBounds(110, 30, 540, 30);

        jButton37.setText("Remove Card");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton37);
        jButton37.setBounds(550, 450, 120, 30);

        jTabbedPane1.addTab("Registration", new javax.swing.ImageIcon(getClass().getResource("/bci/images/user_add.png")), jPanel1); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel7.setLayout(null);
        jPanel7.add(jTextField1);
        jTextField1.setBounds(290, 150, 300, 20);

        jLabel16.setText("Select Excel file");
        jPanel7.add(jLabel16);
        jLabel16.setBounds(120, 150, 160, 20);

        jButton4.setText("Browse");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4);
        jButton4.setBounds(620, 150, 100, 23);

        browseButton.setText("Upload Data");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        jPanel7.add(browseButton);
        browseButton.setBounds(540, 210, 180, 23);

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton22.setText("Back");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton22);
        jButton22.setBounds(10, 430, 90, 23);

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel50.setForeground(new java.awt.Color(0, 51, 255));
        jLabel50.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel7.add(jLabel50);
        jLabel50.setBounds(110, 30, 540, 30);

        jLabel42.setForeground(new java.awt.Color(255, 0, 51));
        jPanel7.add(jLabel42);
        jLabel42.setBounds(10, 10, 220, 20);

        jLabel63.setText("Example File Format");
        jPanel7.add(jLabel63);
        jLabel63.setBounds(140, 100, 120, 20);

        jButton38.setText("File Format");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton38);
        jButton38.setBounds(300, 100, 100, 23);

        jTabbedPane1.addTab("Import Data", new javax.swing.ImageIcon(getClass().getResource("/bci/images/ark216.png")), jPanel7); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel8.setLayout(null);

        jScrollPane4.setViewportView(jList2);

        jPanel8.add(jScrollPane4);
        jScrollPane4.setBounds(120, 100, 350, 180);

        jLabel17.setText("login Details");
        jPanel8.add(jLabel17);
        jLabel17.setBounds(120, 70, 110, 14);

        jButton7.setText("View Logins");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton7);
        jButton7.setBounds(120, 300, 130, 23);

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton3);
        jButton3.setBounds(310, 300, 120, 23);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Deactivate User"));
        jPanel25.setLayout(null);

        jLabel31.setText("Member Number");
        jPanel25.add(jLabel31);
        jLabel31.setBounds(40, 30, 100, 14);

        jLabel33.setText("Member Name");
        jPanel25.add(jLabel33);
        jLabel33.setBounds(40, 70, 110, 14);

        jTextField15.setEditable(false);
        jPanel25.add(jTextField15);
        jTextField15.setBounds(180, 70, 220, 20);

        jButton18.setText("Deactivate");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel25.add(jButton18);
        jButton18.setBounds(420, 70, 100, 23);
        jPanel25.add(jTextField16);
        jTextField16.setBounds(180, 30, 220, 20);

        jScrollPane5.setViewportView(jList4);

        jPanel25.add(jScrollPane5);
        jScrollPane5.setBounds(180, 50, 220, 90);

        jPanel8.add(jPanel25);
        jPanel25.setBounds(120, 330, 540, 150);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton23.setText("Back");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton23);
        jButton23.setBounds(10, 430, 90, 23);

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel51.setForeground(new java.awt.Color(0, 51, 255));
        jLabel51.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel8.add(jLabel51);
        jLabel51.setBounds(110, 30, 540, 30);

        jLabel43.setForeground(new java.awt.Color(255, 0, 51));
        jPanel8.add(jLabel43);
        jLabel43.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Login Details", new javax.swing.ImageIcon(getClass().getResource("/bci/images/launch.png")), jPanel8); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel10.setLayout(null);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Create New User"));
        jPanel14.setLayout(null);

        jLabel22.setText("User Name");
        jPanel14.add(jLabel22);
        jLabel22.setBounds(100, 20, 70, 14);
        jPanel14.add(jTextField11);
        jTextField11.setBounds(190, 20, 240, 20);

        jButton11.setText("CREATE");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel14.add(jButton11);
        jButton11.setBounds(460, 50, 90, 23);

        jLabel24.setText(" UserRole");
        jPanel14.add(jLabel24);
        jLabel24.setBounds(100, 50, 80, 14);

        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        jPanel14.add(jComboBox4);
        jComboBox4.setBounds(190, 50, 240, 20);

        jPanel10.add(jPanel14);
        jPanel14.setBounds(40, 60, 610, 90);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Delete User"));
        jPanel16.setLayout(null);

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jPanel16.add(jComboBox3);
        jComboBox3.setBounds(190, 30, 240, 20);

        jLabel23.setText(" UserName");
        jPanel16.add(jLabel23);
        jLabel23.setBounds(100, 30, 80, 20);

        jButton12.setText("REMOVE");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton12);
        jButton12.setBounds(470, 30, 90, 23);

        jPanel10.add(jPanel16);
        jPanel16.setBounds(40, 150, 610, 70);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Change User Password"));
        jPanel24.setLayout(null);

        jLabel27.setText("Select User");
        jPanel24.add(jLabel27);
        jLabel27.setBounds(100, 30, 70, 14);

        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        jPanel24.add(jComboBox5);
        jComboBox5.setBounds(200, 30, 250, 20);

        jLabel28.setText("Enter the New password");
        jPanel24.add(jLabel28);
        jLabel28.setBounds(100, 60, 150, 14);
        jPanel24.add(jTextField13);
        jTextField13.setBounds(260, 60, 190, 20);

        jButton16.setText("SAVE");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel24.add(jButton16);
        jButton16.setBounds(480, 60, 80, 23);

        jPanel10.add(jPanel24);
        jPanel24.setBounds(40, 220, 610, 100);

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton25.setText("Back");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton25);
        jButton25.setBounds(10, 420, 90, 23);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Card Registration Settings"));
        jPanel15.setLayout(null);

        jLabel25.setText("Card Number");
        jPanel15.add(jLabel25);
        jLabel25.setBounds(90, 20, 100, 14);

        jPanel15.add(jComboBox1);
        jComboBox1.setBounds(220, 20, 180, 20);

        jLabel32.setText("Enter New Number");
        jPanel15.add(jLabel32);
        jLabel32.setBounds(90, 50, 120, 20);
        jPanel15.add(jTextField17);
        jTextField17.setBounds(220, 50, 180, 20);

        jButton27.setText("ADD NEW NUMBER");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton27);
        jButton27.setBounds(420, 50, 150, 23);

        jPanel10.add(jPanel15);
        jPanel15.setBounds(40, 320, 610, 90);

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel52.setForeground(new java.awt.Color(0, 51, 255));
        jLabel52.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel10.add(jLabel52);
        jLabel52.setBounds(110, 30, 540, 30);

        jLabel44.setForeground(new java.awt.Color(255, 0, 51));
        jPanel10.add(jLabel44);
        jLabel44.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Users Settings", new javax.swing.ImageIcon(getClass().getResource("/bci/images/lightbulb.png")), jPanel10); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel2.setLayout(null);
        jPanel2.add(jPanel3);
        jPanel3.setBounds(30, 80, 10, 10);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration DB Settings"));
        jPanel5.setLayout(null);
        jPanel5.add(jTextField2);
        jTextField2.setBounds(190, 30, 280, 20);

        jLabel9.setText("Server IP");
        jPanel5.add(jLabel9);
        jLabel9.setBounds(50, 30, 100, 14);

        jLabel10.setText("Port No");
        jPanel5.add(jLabel10);
        jLabel10.setBounds(50, 60, 100, 14);
        jPanel5.add(jTextField3);
        jTextField3.setBounds(190, 60, 280, 20);

        jLabel11.setText("DB Name");
        jPanel5.add(jLabel11);
        jLabel11.setBounds(50, 90, 100, 14);
        jPanel5.add(jTextField4);
        jTextField4.setBounds(190, 90, 280, 20);

        jPanel2.add(jPanel5);
        jPanel5.setBounds(10, 70, 630, 130);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Access DB Settings"));
        jPanel6.setLayout(null);
        jPanel6.add(jTextField5);
        jTextField5.setBounds(190, 30, 280, 20);

        jLabel12.setText("Server IP");
        jPanel6.add(jLabel12);
        jLabel12.setBounds(50, 30, 100, 14);

        jLabel13.setText("Port No");
        jPanel6.add(jLabel13);
        jLabel13.setBounds(50, 60, 100, 14);
        jPanel6.add(jTextField6);
        jTextField6.setBounds(190, 60, 280, 20);

        jLabel14.setText("DB Name");
        jPanel6.add(jLabel14);
        jLabel14.setBounds(50, 90, 100, 14);
        jPanel6.add(jTextField7);
        jTextField7.setBounds(190, 90, 280, 20);

        jPanel2.add(jPanel6);
        jPanel6.setBounds(10, 200, 630, 130);

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(533, 440, 100, 23);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Device Log Details Settings"));
        jPanel9.setLayout(null);

        jLabel15.setText("Login Time");
        jPanel9.add(jLabel15);
        jLabel15.setBounds(40, 40, 110, 14);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        jPanel9.add(jComboBox2);
        jComboBox2.setBounds(180, 40, 280, 20);

        jPanel2.add(jPanel9);
        jPanel9.setBounds(10, 330, 630, 100);
        jPanel9.getAccessibleContext().setAccessibleName("Log Details Settings");

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton21.setText("Back");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton21);
        jButton21.setBounds(10, 440, 90, 23);

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel53.setForeground(new java.awt.Color(0, 51, 255));
        jLabel53.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel2.add(jLabel53);
        jLabel53.setBounds(110, 30, 540, 30);

        jLabel45.setForeground(new java.awt.Color(255, 0, 51));
        jPanel2.add(jLabel45);
        jLabel45.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("DataBase Settings", new javax.swing.ImageIcon(getClass().getResource("/bci/images/database.png")), jPanel2); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel4.setLayout(null);

        jButton9.setText("Send Data to Device");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton9);
        jButton9.setBounds(240, 330, 180, 23);

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton24.setText("Back");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton24);
        jButton24.setBounds(10, 430, 90, 23);

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel54.setForeground(new java.awt.Color(0, 51, 255));
        jLabel54.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel4.add(jLabel54);
        jLabel54.setBounds(110, 30, 540, 30);

        jLabel46.setForeground(new java.awt.Color(255, 0, 51));
        jPanel4.add(jLabel46);
        jLabel46.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Synchronize Device", new javax.swing.ImageIcon(getClass().getResource("/bci/images/drive.png")), jPanel4); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel11.setLayout(null);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Change Name & Logo"));
        jPanel12.setLayout(null);

        jLabel19.setText("Name");
        jPanel12.add(jLabel19);
        jLabel19.setBounds(130, 40, 60, 14);
        jPanel12.add(jTextField8);
        jTextField8.setBounds(200, 40, 310, 20);

        jLabel20.setText("Logo");
        jPanel12.add(jLabel20);
        jLabel20.setBounds(130, 80, 60, 14);
        jPanel12.add(jTextField9);
        jTextField9.setBounds(200, 80, 310, 20);

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton1);
        jButton1.setBounds(530, 80, 80, 23);

        jButton6.setText("SAVE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton6);
        jButton6.setBounds(420, 120, 90, 23);

        jButton5.setText("Browse");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton5);
        jButton5.setBounds(530, 40, 80, 23);

        jPanel11.add(jPanel12);
        jPanel12.setBounds(10, 80, 650, 160);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Finger print & .net installer"));
        jPanel13.setLayout(null);

        jLabel21.setText("select the folder to save the Fingrprint");
        jPanel13.add(jLabel21);
        jLabel21.setBounds(70, 40, 210, 14);
        jPanel13.add(jTextField10);
        jTextField10.setBounds(290, 40, 270, 20);

        jLabel30.setText("Select the .net exe");
        jPanel13.add(jLabel30);
        jLabel30.setBounds(130, 80, 150, 14);
        jPanel13.add(jTextField14);
        jTextField14.setBounds(290, 80, 270, 20);

        jButton8.setText("...");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton8);
        jButton8.setBounds(580, 40, 30, 23);

        jButton17.setText("...");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton17);
        jButton17.setBounds(580, 80, 30, 23);

        jButton19.setText("SAVE");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel13.add(jButton19);
        jButton19.setBounds(480, 110, 80, 23);

        jPanel11.add(jPanel13);
        jPanel13.setBounds(10, 250, 650, 140);

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton26.setText("Back");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton26);
        jButton26.setBounds(10, 430, 90, 23);

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel55.setForeground(new java.awt.Color(0, 51, 255));
        jLabel55.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel11.add(jLabel55);
        jLabel55.setBounds(110, 30, 540, 30);

        jLabel47.setForeground(new java.awt.Color(255, 0, 51));
        jPanel11.add(jLabel47);
        jLabel47.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Installation Settings", new javax.swing.ImageIcon(getClass().getResource("/bci/images/lock_go.png")), jPanel11); // NOI18N

        jPanel27.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel27.setLayout(null);

        jPanel29.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 0, 204)));
        jPanel29.setLayout(null);

        jLabel38.setText("MEMBER NAME ");
        jPanel29.add(jLabel38);
        jLabel38.setBounds(50, 80, 110, 20);

        jLabel39.setText("MEMBER NUMBER");
        jPanel29.add(jLabel39);
        jLabel39.setBounds(40, 130, 120, 20);

        jTextField20.setEditable(false);
        jPanel29.add(jTextField20);
        jTextField20.setBounds(180, 80, 180, 20);

        jTextField21.setEditable(false);
        jPanel29.add(jTextField21);
        jTextField21.setBounds(180, 130, 180, 20);

        jLabel40.setText("CARD NUMBER");
        jPanel29.add(jLabel40);
        jLabel40.setBounds(40, 180, 100, 20);

        jTextField22.setEditable(false);
        jPanel29.add(jTextField22);
        jTextField22.setBounds(180, 180, 180, 20);

        jButton30.setText("SEARCH");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel29.add(jButton30);
        jButton30.setBounds(250, 250, 110, 30);

        jButton32.setText("CLEAR");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel29.add(jButton32);
        jButton32.setBounds(130, 250, 110, 30);

        jPanel27.add(jPanel29);
        jPanel29.setBounds(140, 70, 380, 350);

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton31.setText("Back");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton31);
        jButton31.setBounds(10, 430, 90, 23);

        jLabel48.setForeground(new java.awt.Color(255, 0, 51));
        jPanel27.add(jLabel48);
        jLabel48.setBounds(30, 480, 250, 30);

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel56.setForeground(new java.awt.Color(0, 51, 255));
        jLabel56.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel27.add(jLabel56);
        jLabel56.setBounds(110, 30, 580, 30);

        jLabel57.setForeground(new java.awt.Color(255, 0, 51));
        jPanel27.add(jLabel57);
        jLabel57.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Card varification", new javax.swing.ImageIcon(getClass().getResource("/bci/images/mime.png")), jPanel27); // NOI18N

        jPanel28.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 255)));
        jPanel28.setLayout(null);

        jPanel30.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 51)));
        jPanel30.setLayout(null);

        jLabel58.setText("Member Number");
        jPanel30.add(jLabel58);
        jLabel58.setBounds(30, 100, 100, 20);
        jPanel30.add(jTextField23);
        jTextField23.setBounds(140, 100, 180, 20);

        jLabel59.setText("Member Name");
        jPanel30.add(jLabel59);
        jLabel59.setBounds(30, 140, 90, 14);

        jTextField24.setEditable(false);
        jPanel30.add(jTextField24);
        jTextField24.setBounds(140, 140, 180, 20);

        jButton33.setText("DEACTIVATE");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton33);
        jButton33.setBounds(160, 220, 120, 30);

        jScrollPane6.setViewportView(jList5);

        jPanel30.add(jScrollPane6);
        jScrollPane6.setBounds(320, 100, 120, 100);

        buttonGroup3.add(jRadioButton2);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Member");
        jPanel30.add(jRadioButton2);
        jRadioButton2.setBounds(80, 30, 80, 23);

        buttonGroup3.add(jRadioButton6);
        jRadioButton6.setText("Spouse");
        jPanel30.add(jRadioButton6);
        jRadioButton6.setBounds(160, 30, 70, 23);

        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setText("Children");
        jPanel30.add(jRadioButton7);
        jRadioButton7.setBounds(240, 30, 80, 23);

        buttonGroup3.add(jRadioButton8);
        jRadioButton8.setText("Staff");
        jPanel30.add(jRadioButton8);
        jRadioButton8.setBounds(320, 30, 70, 23);

        jButton35.setText("CLEAR");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton35);
        jButton35.setBounds(40, 220, 110, 30);

        jButton36.setText("ACTIVATE");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton36);
        jButton36.setBounds(290, 220, 110, 30);

        jLabel62.setText("Status");
        jPanel30.add(jLabel62);
        jLabel62.setBounds(30, 180, 90, 20);

        jTextField25.setEditable(false);
        jPanel30.add(jTextField25);
        jTextField25.setBounds(140, 180, 180, 20);

        jPanel28.add(jPanel30);
        jPanel30.setBounds(120, 110, 450, 270);

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/redo16.png"))); // NOI18N
        jButton34.setText("Back");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        jPanel28.add(jButton34);
        jButton34.setBounds(10, 430, 90, 23);

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 20));
        jLabel60.setForeground(new java.awt.Color(0, 51, 255));
        jLabel60.setText("FINGERPRINT & CARD REGISTRATION SYSTEM");
        jPanel28.add(jLabel60);
        jLabel60.setBounds(110, 30, 540, 30);

        jLabel61.setForeground(new java.awt.Color(255, 0, 51));
        jPanel28.add(jLabel61);
        jLabel61.setBounds(10, 10, 220, 20);

        jTabbedPane1.addTab("Delete Member", new javax.swing.ImageIcon(getClass().getResource("/bci/images/editdelete.png")), jPanel28); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.PAGE_END);
        jTabbedPane1.getAccessibleContext().setAccessibleName("Registration");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bci/images/exit.png"))); // NOI18N
        jButton14.setText("CLOSE");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton14, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        //       if(jTabbedPane1.getSelectedIndex()==2){
        //           listModel=new ListValModel(ThreadSafeList.getList());
        //           jList1.setModel(listModel);
        //       }
}//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            model.clear();
            jList2.setModel(model);
            String systemCurrentDate = null;
            SimpleDateFormat Dformat = new SimpleDateFormat("yyMMdd");
            Date date = new Date();
            systemCurrentDate = Dformat.format(date);
            try {
                FileInputStream fstream = new FileInputStream("logs\\"+systemCurrentDate+"LG.txt");
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    model.addElement(strLine);
                }
                in.close();
            } catch (Exception e1) {
                System.err.println("Error: " + e1);
            }
            jList2.setModel(model);
            jList2.setVisible(true);
        } catch (Exception e) {
            System.out.println("DB Connection Error " + e);
        }
}//GEN-LAST:event_jButton7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Property prop = new Property();
        prop.setAdbServer(jTextField2.getText());
        prop.setAdbport(jTextField3.getText());
        prop.setAdbdbname(jTextField4.getText());
        prop.setRdbServer(jTextField5.getText());
        prop.setRdbport(jTextField6.getText());
        prop.setRdbdbname(jTextField7.getText());
        try {
            prop.store();
            JOptionPane.showMessageDialog(null, "Saved!!");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error Occured While Storing The File");
        }
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        model.clear();
        jList2.setModel(model);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            DBupdateConnectVB();
        } catch (Exception e1) {
            System.out.println("Update failed!!" + e1);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("C:\\Program Files\\eSSL\\eTimeTrack\\eTimeTrack\\DataSyncronizer.exe");
        } catch (Exception e) {
            System.out.println("Updated Database with Finger Print!!");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            JFileChooser fChooser = new JFileChooser();
            fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fChooser.setCurrentDirectory(null);
            FileNameExtensionFilter filefilter = new FileNameExtensionFilter("Png file", "png");
            fChooser.setFileFilter(filefilter);
            fChooser.showOpenDialog(null);
            jTextField9.setText(fChooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Logo file browse error!!" + e);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            String companyName = jTextField8.getText();
            String companyLogo = jTextField9.getText();
            Property property = new Property();

            if (!companyName.equals("")) {
                property.setClubName(companyName);
                property.storeclubName();
            }
            if (!companyLogo.equals("")) {
                property.setClubLogo(companyLogo);
                property.storeclubLogo();
            }
            if (companyName.equals("") && companyLogo.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill atleast one filed!!");
            } else {
                JOptionPane.showMessageDialog(null, "Saved!!");
            }
        } catch (Exception e) {
            System.out.println("Name and logo stting error" + e);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            String usernameText = jTextField11.getText();
            String roleCombo = jComboBox4.getSelectedItem().toString();
            if (!usernameText.equals("")) {
                if (!roleCombo.equals("Select Role..")) {
                    Connection userCreateCon = eSession.getConnection();
                    String userCreateQry = "Insert into userInfo (Id,Name,role,password) values (?,?,?,?)";
                    PreparedStatement userCreatePstm = userCreateCon.prepareStatement(userCreateQry);
                    userCreatePstm.setString(1, usernameText);
                    userCreatePstm.setString(2, usernameText);
                    userCreatePstm.setString(3, roleCombo);
                    userCreatePstm.setString(4, "");
                    userCreatePstm.executeUpdate();
                    JOptionPane.showMessageDialog(null, "User Created successfully,Changes will applied the next time the application restarts");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select Role!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please fill the fields!!");
            }
        } catch (Exception e) {
            System.out.println("User Creation Error" + e);
        }
}//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            String removeUser = jComboBox3.getSelectedItem().toString();
            if (!removeUser.equals("Select User..")) {
                Connection userCreateCon = eSession.getConnection();
                String userCreateQry = "Delete from userInfo where name = ?";
                PreparedStatement userCreatePstm = userCreateCon.prepareStatement(userCreateQry);
                userCreatePstm.setString(1, removeUser);
                userCreatePstm.executeUpdate();
                JOptionPane.showMessageDialog(null, "User Deleted successfully,Changes will applied the next time the application restarts");
            } else {
                JOptionPane.showMessageDialog(null, "Please select user !!");
            }
        } catch (Exception e) {
            System.out.println("user remove error!!" + e);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        JOptionPane.showMessageDialog(null, " log details are writing into a file !!!!plz click ok and  wait for few minutes !!");
        /*try {

        //updateETimeTrackV3();
        //InsertIntoStaffBiometricDetsils();

        } catch (SQLException ex) {
        ex.printStackTrace();
        }*/
        writeToFile();
        System.out.println("written to target file");
        System.exit(0);
}//GEN-LAST:event_jButton15ActionPerformed

//praveen added to update cardnumber in etimetrackv3
    public void updateETimeTrackV3() throws SQLException{
        ArrayList al=new ArrayList();
        HashMap<String,String> map=new HashMap<String, String>();
        Connection updatecon=eSession.getConnection();
        Connection updatecon1=esslv3Connection.getConnection();
        String sqlStr1="select devicestaffcode,cardnumber from membermasters ";
        String sqlStr2="update staffbiometricdetails set cardnumber=? where devicestaffcode=?  ";
         String sqlStr3="select devicestaffcode from staffbiometricdetails order by devicestaffcode";
        PreparedStatement ps1=updatecon.prepareStatement(sqlStr1);       
        ResultSet rs=ps1.executeQuery();
        System.out.println(rs.getFetchSize());
        while(rs.next()){            
            map.put(rs.getString(1),rs.getString(2));
             }
        System.out.println(map);
        PreparedStatement ps3=updatecon1.prepareStatement(sqlStr3);
        ResultSet rs3=ps3.executeQuery();
        while(rs3.next()){
            al.add(rs3.getString(1));       
        }     
        int n=0,i;
        for(i=0;i<map.size();i++)
       {
            PreparedStatement ps2=updatecon1.prepareStatement(sqlStr2);
            ps2.setString(1, map.get(al.get(i)));
            ps2.setString(2,(String) al.get(i));
            System.out.println((String) al.get(i));            
            System.out.println(map.get(al.get(i)));
            int m=ps2.executeUpdate();
            if(m==1){
                n++;
            }
            else{
                System.out.println("its not updated to this"+(String) al.get(i)+"number");
            }          
       }
        System.out.println(n);
        JOptionPane.showMessageDialog(this, " updated successfully !!");
    }


    public void InsertIntoStaffBiometricDetsils() throws SQLException{

        ArrayList a=new ArrayList();
        ArrayList a1=new ArrayList();
        //ArrayList a2=new ArrayList();
        HashMap<String,String> map=new HashMap<String, String>();
        Connection con=esslv3Connection.getConnection();
        Connection con1=null;
        PreparedStatement ps2=null;
        String sqlStr1="select name,devicecode from staff where devicecode not in(select devicestaffcode from staffbiometricdetails) order by devicecode";
        PreparedStatement ps=con.prepareStatement(sqlStr1);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            a.add(rs.getString(2));
            a1.add(rs.getString(1));
            map.put(rs.getString(2),rs.getString(1));
        }
        System.out.println(a.size());

        
        //PreparedStatement ps2=con.prepareStatement(sqlStr);
        int n=0;

        for(int i=0;i<map.size();i++){
            con1=esslv3Connection.getConnection();
            String sqlStr2="insert into staffbiometricdetails(devicestaffcode,staffname,companyid) values(?,?,?)" ;

            ps2 =con1.prepareStatement(sqlStr2);
            ps2.setString(1,(String) a.get(i));
            ps2.setString(2, (String)map.get(a.get(i)));
            System.out.println((String) a.get(i));
            System.out.println((String) a1.get(i));
            System.out.println(map.get(a.get(i)));
            ps2.setInt(3, 1);
          ps2.executeUpdate();
            n++;
        }
        System.out.println(n);
        JOptionPane.showMessageDialog(this, " inserted successfully !!");
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try { //startMainpanel();
            JFileChooser fChooser = new JFileChooser();
            fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fChooser.setCurrentDirectory(null);
            FileNameExtensionFilter filefilter = new FileNameExtensionFilter("Png file", "png");
            fChooser.setFileFilter(filefilter);
            fChooser.showOpenDialog(null);
            jTextField8.setText(fChooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Logo file browse error!!" + e);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        try {
            String combo5Val = jComboBox5.getSelectedItem().toString();
            Connection userInfoConn = eSession.getConnection();
            String userInfoQry = "select password from userInfo where name = '" + combo5Val + "'";
            PreparedStatement userInfoPstm = userInfoConn.prepareStatement(userInfoQry);
            ResultSet userInfoRs = userInfoPstm.executeQuery();
            String passworval = null;
            while (userInfoRs.next()) {
                passworval = userInfoRs.getString(1);
            }
            byte[] passval = Base64.decode(passworval);
            String password = new String(passval);

            jTextField13.setText(password);
        } catch (Exception e) {
            System.out.println("Combobox selection error!!" + e);
        }
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        try {
            String como5val = jComboBox5.getSelectedItem().toString();
            String textfieldval = jTextField13.getText();
            byte[] passArr = textfieldval.getBytes();
            String convertedPassword = Base64.encode(passArr);

            if (!textfieldval.equals("")) {
                Connection userInfoConn = eSession.getConnection();
                String userInfoQry = "Update userInfo set password = ? where name = ?";
                PreparedStatement userInfopstm = userInfoConn.prepareStatement(userInfoQry);
                userInfopstm.setString(1, convertedPassword);
                userInfopstm.setString(2, como5val);
                userInfopstm.executeUpdate();
                JOptionPane.showMessageDialog(null, "Password Modified!!");
            } else {
                JOptionPane.showMessageDialog(null, "Password field is Blank!!");
            }
        } catch (Exception e) {
            System.out.println("Password save button error!!" + e);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            JFileChooser fChooser = new JFileChooser();
            fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fChooser.setCurrentDirectory(null);
            FileNameExtensionFilter filefilter = new FileNameExtensionFilter("Png file", "png");
            fChooser.setFileFilter(filefilter);
            fChooser.showOpenDialog(null);
            jTextField10.setText(fChooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Fingerprint folder browse error!!" + e);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
            JFileChooser fChooser = new JFileChooser();
            fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fChooser.setCurrentDirectory(null);
            FileNameExtensionFilter filefilter = new FileNameExtensionFilter("exe file", "exe");
            fChooser.setFileFilter(filefilter);
            fChooser.showOpenDialog(null);
            jTextField14.setText(fChooser.getSelectedFile().getAbsolutePath());
        } catch (Exception e) {
            System.out.println("exe browse error!!" + e);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        try {
            String folderpath = jTextField10.getText();
            String vbexe1 = jTextField14.getText();
            if (!folderpath.equals("") && !vbexe1.equals("")) {
                Property propert = new Property();
                propert.setfingerprintFolder(folderpath);
                propert.setvbExe(vbexe1);
                propert.storeFingerprintfolder();
            } else {
                JOptionPane.showMessageDialog(null, "please select the path!!");
            }
            JOptionPane.showMessageDialog(null, "Saved!!");
        } catch (Exception e) {
            System.out.println("finger print save button error" + e);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try{
            String textfieldItem = jTextField16.getText();
            if(!textfieldItem.equals("")){
               int answer = JOptionPane.showConfirmDialog(null, "Do you want to Deactivate member!!", "Message", JOptionPane.YES_NO_OPTION);
               if (answer == JOptionPane.YES_OPTION) {

                      Connection logConn = esslv3Connection.getConnection();
                       String staffQry = "Select DeviceCode from staff where staffCode = '"+ textfieldItem +"'";
                       PreparedStatement pstm = logConn.prepareStatement(staffQry);
                       ResultSet rs = pstm.executeQuery();
                       String DeviceCode1 = null;
                       while (rs.next()) {
                           DeviceCode1 = rs.getString(1);
                       }
                       String logStatusQry = "update log set status = '1' where DeviceStaffid = ? and DateTime like '"+ dateString +"%'";
                       PreparedStatement logpstm = logConn.prepareStatement(logStatusQry);
                       logpstm.setString(1, DeviceCode1);
                       logpstm.executeUpdate();
                       JOptionPane.showMessageDialog(null, "Deactivated!!!");
               }
            }else{
                JOptionPane.showMessageDialog(null, "Please select one member!!");
            }
        }catch(Exception e){
            System.out.println("Deactivate button error!!"+e);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void m_txtKeysKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_txtKeysKeyTyped

}//GEN-LAST:event_m_txtKeysKeyTyped

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        startMainpanel(tabbedchekval);
}//GEN-LAST:event_jButton14ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        try{
            String textValue = jTextField17.getText();
            if(!textValue.equals("")){
               Connection roleConn = eSession.getConnection();
               String roleQry = "Insert Into cardnumberTable (cardnumber) values (?)";
               PreparedStatement rolePstm = roleConn.prepareStatement(roleQry);
               rolePstm.setString(1, textValue);
               rolePstm.executeUpdate();
               JOptionPane.showMessageDialog(null, "Number Added,Modification is valid from next time!!");
            }else{
               JOptionPane.showMessageDialog(null, "Please give Number!!");
            }
        }catch(Exception e){
            System.out.println("CardNumber Combo error"+e);
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        try {
            CardReader cr = new CardReader(jTextField18.getText().toUpperCase(), jTextField19,jTextField22);
            cr.ConfigurePort();
            status=1;
            obj = cr;
            jLabel37.setText("Connected!!");
            jPanel26.repaint();
        } catch (Exception ex) {
           JOptionPane.showMessageDialog(null, "Sorry failed to connect to the reader");

        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        try{
            String memNo = jTextField12.getText();
            String memName = mName.getText();
            String cardno = jTextField19.getText().trim();
            if(memNo.equals("") && memName.equals("")){
                JOptionPane.showMessageDialog(null, "please give the member Number & member Name!!");
            }else{
                if(!cardno.equals("")){
                    //check cardnumber already there..
            Connection memCon = eSession.getConnection();
            ArrayList<String> cardNumberCheck = new ArrayList<String>();
            String checkQry = "select cardnumberMain from MemberMasters";
            PreparedStatement checkCardpstm = memCon.prepareStatement(checkQry);
            ResultSet cardCheckRs = checkCardpstm.executeQuery();
                    while (cardCheckRs.next()) {
                        cardNumberCheck.add(cardCheckRs.getString(1));
                    }
            if(cardNumberCheck.contains(cardno)){
                JOptionPane.showMessageDialog(null, "Card Already Registered!!");
            }else{
                //check memeber already registered!!
                String alredycardQry = "select cardnumberMain from MemberMasters where memNumber = '"+memNo+"'";
                PreparedStatement alredycardPstm = memCon.prepareStatement(alredycardQry);
                ResultSet alredycardRs = alredycardPstm.executeQuery();
                String alredycardNo = null;
                while (alredycardRs.next()) {
                    alredycardNo = alredycardRs.getString(1);
                }
                System.out.println(alredycardNo);
                if(!alredycardNo.equals("")){
                    int answer = JOptionPane.showConfirmDialog(null, "Card Already Registered,Do you want to overwrite!!!", "Message", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                          int cardNumber1 = Integer.parseInt(cardno);
                          int cNo = Integer.parseInt(jComboBox1.getSelectedItem().toString());
                          cardNumber1 = cardNumber1 - cNo ;
                          cardfinalval = Integer.toString(cardNumber1);


                          String memQry = "update MemberMasters set cardnumber = ?,cardnumberMain = ? where memNumber = ?";
                          PreparedStatement memPstm = memCon.prepareStatement(memQry);
                          memPstm.setString(1, cardfinalval);
                          memPstm.setString(2, cardno);
                          memPstm.setString(3, memNo);
                          memPstm.executeUpdate();
                          //essl
                          Connection esslCardCon = esslv3Connection.getConnection();
                          String staffQry = "select DeviceCode from staff where staffCode = '"+ memNo +"'";
                          PreparedStatement staffPstm = esslCardCon.prepareStatement(staffQry);
                          ResultSet staffRs = staffPstm.executeQuery();
                          String DVCode = null;
                          while (staffRs.next()) {
                                DVCode = staffRs.getString(1);
                           }
                          String esslCardQry = "update StaffBiometricDetails set CardNumber = ? where DeviceStaffCode = ? ";
                          PreparedStatement esslCardpstm = esslCardCon.prepareStatement(esslCardQry);
                          esslCardpstm.setString(1, cardfinalval);
                          esslCardpstm.setString(2, DVCode);
                          esslCardpstm.executeUpdate();
                          JOptionPane.showMessageDialog(null, "Card Registered Successfully!!!");
                          cardNumber.setText(cardno);
                            }
                            }else{
            int cardNumber1 = Integer.parseInt(cardno);
            int cNo = Integer.parseInt(jComboBox1.getSelectedItem().toString());
            cardNumber1 = cardNumber1 - cNo ;
            cardfinalval = Integer.toString(cardNumber1);

            
            String memQry = "update MemberMasters set cardnumber = ?,cardnumberMain = ? where memNumber = ?";
            PreparedStatement memPstm = memCon.prepareStatement(memQry);
            memPstm.setString(1, cardfinalval);
            memPstm.setString(2, cardno);
            memPstm.setString(3, memNo);
            memPstm.executeUpdate();
            //essl
            Connection esslCardCon = esslv3Connection.getConnection();
            String staffQry = "select DeviceCode from staff where staffCode = '"+ memNo +"'";
            PreparedStatement staffPstm = esslCardCon.prepareStatement(staffQry);
            ResultSet staffRs = staffPstm.executeQuery();
            String DVCode = null;
            while (staffRs.next()) {
                DVCode = staffRs.getString(1);
            }
            String esslCardQry = "update StaffBiometricDetails set CardNumber = ? where DeviceStaffCode = ? ";
            PreparedStatement esslCardpstm = esslCardCon.prepareStatement(esslCardQry);
            esslCardpstm.setString(1, cardfinalval);
            esslCardpstm.setString(2, DVCode);
            esslCardpstm.executeUpdate();
            JOptionPane.showMessageDialog(null, "Card Registered Successfully!!!");
            cardNumber.setText(cardno);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Please give card Number!!");
            }
            }
            
        }catch(Exception e){
            System.out.println("Card Registration Error!!"+e);
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try{
            jTextField12.setText("");
            mName.setText("");
            cardNumber.setText("");
            jTextField19.setText("");
            jLabel6.setText("Double click here to give fingerprint");
            jPanel1.remove(jScrollPane1);
            jPanel1.revalidate();
            jPanel1.repaint();
        }catch(Exception e){
            System.out.println("Clear Button Error!!"+e);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
      try{
          String cardno = jTextField22.getText();
          if(!cardno.equals("")){
              ArrayList<String> arrList = new ArrayList<String>();
              Connection con = eSession.getConnection();
              String cardRegfind = "Select cardNumber from MemberMasters";
              PreparedStatement cardPstm = con.prepareStatement(cardRegfind);
              ResultSet cardRs = cardPstm.executeQuery();
              while (cardRs.next()) {
                  arrList.add(cardRs.getString(1));        
              }
              StringBuffer stbf = new StringBuffer(cardno);
              int k  = stbf.indexOf("0");
              stbf.deleteCharAt(k);
              int k1  = stbf.indexOf("0");
              stbf.deleteCharAt(k1);
              
              if(arrList.contains(stbf.toString())){
                  String cardQry = "Select memNumber,MemName from MemberMasters where cardNumberMain =?" ;
                  PreparedStatement cardOnepstm = con.prepareStatement(cardQry);
                  cardOnepstm.setString(1, cardno);
                  ResultSet cardOneRs = cardOnepstm.executeQuery();
                  String mNumber = null;
                  String mName = null;
                  while (cardOneRs.next()) {
                      mNumber = cardOneRs.getString(1);
                      mName = cardOneRs.getString(2);
                  }
                  jTextField20.setText(mName);
                  jTextField21.setText(mNumber);
              }else{
                  JOptionPane.showMessageDialog(null, "No Record found,for this card!!");
              }

             // PreparedStatement cardPstm = con.prepareStatement(cardno);
          }else{
              JOptionPane.showMessageDialog(null, "Please give the CardNumber!!");
          }
      }catch(Exception e){
          System.out.println("Search Button Error!!"+e);
      }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
       jTextField20.setText("");
       jTextField21.setText("");
       jTextField22.setText("");
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        startMainpanel(tabbedchekval);
        backmethodclear();
}//GEN-LAST:event_jButton22ActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        if (jTextField1.getText().length() > 0) {
            try {
                String memno=null;
                String memname=null;
                String memtype=null;
                String gender=null;
                String memcat=null;
                ArrayList al=new ArrayList();
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("fileformat/FingrPrintRegistration-FileFormat.xls"));
                HSSFWorkbook wb = new HSSFWorkbook(fs);
                HSSFSheet sheet = wb.getSheetAt(0);
                HSSFRow row = sheet.getRow(0);
                int c = row.getPhysicalNumberOfCells();
                al.add(c);
                HSSFCell cell = null;
                cell = row.getCell((short) 0);
                if (cell != null) {
                    memno = cell.getRichStringCellValue().getString();
                    al.add(memno);
                    System.out.println(memno);
                }
                cell = row.getCell((short) 1);
                if (cell != null) {
                     memtype = cell.getRichStringCellValue().getString();
                     al.add((memtype));
                    System.out.println(memtype);
                }
                cell = row.getCell((short) 3);
                if (cell != null) {
                     memname = cell.getRichStringCellValue().getString();
                     al.add(memname);
                    System.out.println(memname);
                }
                cell = row.getCell((short) 4);
                if (cell != null) {
                     gender = cell.getRichStringCellValue().getString();
                     al.add(gender);
                    System.out.println(gender);
                }
                cell = row.getCell((short) 5);
                if (cell != null) {
                     memcat = cell.getRichStringCellValue().getString();
                     al.add(memcat);
                    System.out.println(memcat);
                }
                UploadData udata = new UploadData();
                boolean result=udata.load(jTextField1.getText(),al);
                if(result){
                JOptionPane.showMessageDialog(null, "Uploaded successfully");
                }
                else{
                    JOptionPane.showMessageDialog(null, "File Not Uploaded");
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
}//GEN-LAST:event_browseButtonActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser fChooser = new JFileChooser();
        fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fChooser.setCurrentDirectory(null);
        fChooser.showOpenDialog(null);
        jTextField1.setText(fChooser.getSelectedFile().getAbsolutePath());
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        startMainpanel(tabbedchekval);
        backmethodclear();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        jTextField23.setText("");
        jTextField24.setText("");
        jTextField25.setText("");
        jPanel30.remove(jScrollPane6);
        jPanel30.repaint();
        jPanel30.revalidate();
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        try{
           if(!jTextField23.getText().equals("") && !jTextField24.getText().equals("") && !jTextField25.getText().equals("")){
              if(jTextField25.getText().equals("Activate")){
                 int answer = JOptionPane.showConfirmDialog(null, "Do you want to Deactivate this Member!!", "Message", JOptionPane.YES_NO_OPTION);
                 if (answer == JOptionPane.YES_OPTION) {
                     Connection co = eSession.getConnection();
                     String AcivateQry = "Update memberMasters set Active =? where memnumber = ? and memName = ?";
                     PreparedStatement pstm = co.prepareStatement(AcivateQry);
                     pstm.setString(1, "0");
                     pstm.setString(2, jTextField23.getText());
                     pstm.setString(3, jTextField24.getText());
                     pstm.executeUpdate();
                     JOptionPane.showMessageDialog(null, "Member Deactivated Successfully!!!!");
                 }
              }else{
                  JOptionPane.showMessageDialog(null, "Member Already Deactive!!");
              }
           }else{
               JOptionPane.showMessageDialog(null, "Please Select One Member!!");
           }
        }catch(Exception e){
            e.printStackTrace();
        }

        /*     if(jTextField23.getText().equals("") && jTextField24.getText().equals("")){
          JOptionPane.showMessageDialog(null, "Please select One Member!!");
      }else{
        int answer = JOptionPane.showConfirmDialog(null, "Do you want to Delete this Member!!", "Message", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try{
               Connection conne = eSession.getConnection();
               String delQry = "Delete from membermasters where memnumber = ? and devicestaffcode = ?";
               PreparedStatement preStmt = conne.prepareStatement(delQry);
               preStmt.setString(1, jTextField23.getText());
               preStmt.setString(2, deleteDevicestaffcode);
               preStmt.executeUpdate();
               //-------essl-----------
               Connection esConn = esslv3Connection.getConnection();
               String delesslQry = "Delete from staff where staffcode = ? and devicecode = ?";
               PreparedStatement espreStmt = esConn.prepareStatement(delesslQry);
               espreStmt.setString(1, jTextField23.getText());
               espreStmt.setString(2, deleteDevicestaffcode);
               espreStmt.executeUpdate();

               String delessl1Qry = "Delete from StaffBiometricDetails where devicestaffcode = ?";
               PreparedStatement espreStmt1 = esConn.prepareStatement(delessl1Qry);
               espreStmt1.setString(1, deleteDevicestaffcode);
               espreStmt1.executeUpdate();

               JOptionPane.showMessageDialog(null, "Member Deleted successfully!!");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
      } */
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        try{
           if(!jTextField23.getText().equals("") && !jTextField24.getText().equals("") && !jTextField25.getText().equals("")){
              if(jTextField25.getText().equals("Deactivate")){
                 int answer = JOptionPane.showConfirmDialog(null, "Do you want to Activate this Member!!", "Message", JOptionPane.YES_NO_OPTION);
                 if (answer == JOptionPane.YES_OPTION) {
                     Connection co = eSession.getConnection();
                     String AcivateQry = "Update memberMasters set Active =? where memnumber = ? and memName = ?";
                     PreparedStatement pstm = co.prepareStatement(AcivateQry);
                     pstm.setString(1, "1");
                     pstm.setString(2, jTextField23.getText());
                     pstm.setString(3, jTextField24.getText());
                     pstm.executeUpdate();
                     JOptionPane.showMessageDialog(null, "Member Activated Successfully!!!!");
                 }
              }else{
                  JOptionPane.showMessageDialog(null, "Member Already Active!!");
              }
           }else{
               JOptionPane.showMessageDialog(null, "Please Select One Member!!");
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
       try{
        if(!jTextField12.getText().equals("") && !mName.getText().equals("")){
            //if(!cardNumber.getText().equals("")){
              int answer = JOptionPane.showConfirmDialog(null, "Do you wnat to remove Card number","message",JOptionPane.YES_NO_OPTION);
              if(answer == JOptionPane.YES_OPTION){
                Connection clearCon = eSession.getConnection();
                String clearqry = "Update membermasters set cardnumberMain = ?,cardnumber =? where memnumber = ? and memname = ?";
                PreparedStatement clearpstm = clearCon.prepareStatement(clearqry);
                clearpstm.setString(1, "");
                clearpstm.setString(2, "");
                clearpstm.setString(3, jTextField12.getText().toString());
                clearpstm.setString(4, mName.getText().toString());
                clearpstm.executeUpdate();

                
                String devicecodeqry = "select devicestaffcode from membermasters where memnumber =? and memname =?";
                PreparedStatement clearpstmV = clearCon.prepareStatement(devicecodeqry);
                clearpstmV.setString(1, jTextField12.getText().toString());
                clearpstmV.setString(2, mName.getText().toString());
                ResultSet clearrs = clearpstmV.executeQuery();
                String devCode = null;
                  while (clearrs.next()) {
                      devCode = clearrs.getString(1);
                  }

                Connection clearConV = esslv3Connection.getConnection();
                String clearqry1 = "Update StaffBiometricDetails set cardnumber = ? where devicestaffcode =?";
                PreparedStatement clearpstmV1 = clearConV.prepareStatement(clearqry1);
                clearpstmV1.setString(1, "");
                clearpstmV1.setString(2, devCode);
                clearpstmV1.executeUpdate();
                    cardNumber.setText("");
              }
          //  }else{
           //     JOptionPane.showMessageDialog(null, "No card Registered");
           // }
        }else{
            JOptionPane.showMessageDialog(null, "Please select the member!!");
        }
       }catch(Exception e){
           System.out.println("Clear button error!!");
           e.printStackTrace();
       }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        try {
            // TODO add your handling code here:
            

            Runtime rt=Runtime.getRuntime();
            rt.exec("cmd.exe /C start excel.exe /r fileformat/FingrPrintRegistration-FileFormat.xls");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
    }//GEN-LAST:event_jButton38ActionPerformed
    public static void metadataInfo() {
        Boolean datbseCahnge = false;
        DateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat Dformat2 = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        DateCheck = Dformat.format(date);
        
        String systemCurrentDate=Dformat2.format(date);

        try {
            File logfile = new File("logs\\"+systemCurrentDate+"LG.txt");
            if(logfile.exists()){
               String a = "";
               FileOutputStream fos = new FileOutputStream(logfile,false);
               fos.write(a.getBytes());
               fos.close();
            }
            if (!logfile.exists()) {
                logfile.createNewFile();
                System.out.println("File created!!");
            }
            RandomAccessFile rfile = new RandomAccessFile(logfile, "rw");
            rfile.seek(logfile.length());

            Connection connection = esslv3Connection.getConnection();
            PreparedStatement pStm = null;
            String AttendencelogQuery = "SELECT DeviceStaffId , DateTime ,status FROM Log order by DateTime desc";
            pStm = connection.prepareStatement(AttendencelogQuery);
            ResultSet AttendenceRS = pStm.executeQuery();
            String EmployeeidDB = null;
            String InTimeDB = null;
            String logStatus = null;
            while (AttendenceRS.next()) {
                EmployeeidDB = AttendenceRS.getString(1);
                String memebrIdget = "select memnumber from MemberMasters where DeviceStaffCode ='" + EmployeeidDB + "'";
                Connection connection1 = eSession.getConnection();
                PreparedStatement mpstm = connection1.prepareStatement(memebrIdget);
                ResultSet logmemRS = mpstm.executeQuery();
                String mnumberlog = null;
                while (logmemRS.next()) {
                    mnumberlog = logmemRS.getString(1);
                }

                InTimeDB = AttendenceRS.getString(2);
                logStatus = AttendenceRS.getString(3);
                String DateOnly[] = InTimeDB.split(" ");
                String DateOnlyOne = DateOnly[0];
                String DateOnlyTwo = DateOnly[1];

                if (DateCheck.equals(DateOnlyOne)) {
                   // System.out.println(InTimeDB);
                    rfile.writeBytes(mnumberlog.trim() + " - \t");
                    rfile.writeBytes(InTimeDB.trim() + "\t ");
                    if (logStatus.equals("0")) {
                        rfile.writeBytes(" - Active" + "\r\n");
                    } else {
                        rfile.writeBytes(" - Deactive" + "\r\n");
                    }
                    boolean flag = true;
                }
            }
            rfile.close();
        } catch (Exception e) {
            System.out.println("Cannot find database " + e);
            e.printStackTrace();
        }
    }


    public static void writeToFile() {
     Boolean datbseCahnge = false;
    DateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat Dformat2 = new SimpleDateFormat("yyMMdd");
    Date date = new Date();
    DateCheck = Dformat.format(date);
    //java.sql.Date  DateCheck1 = new java.sql.Date(new Date().getTime());
    String systemCurrentDate=Dformat2.format(date);

    try {
    File logfile = new File("logs\\"+systemCurrentDate+"LG.txt");
    if(logfile.exists()){
    String a = "";
    FileOutputStream fos = new FileOutputStream(logfile,false);
    fos.write(a.getBytes());
    fos.close();
    }
    if (!logfile.exists()) {
     logfile.createNewFile();
     System.out.println("File created!!");
     }
    RandomAccessFile rfile = new RandomAccessFile(logfile, "rw");
    rfile.seek(logfile.length());

    Connection connection = esslv3Connection.getConnection();
    PreparedStatement pStm = null;
    String AttendencelogQuery = "SELECT DeviceStaffId , DateTime ,status FROM Log where DateTime  like '"+DateCheck+"%' order by DateTime";
    pStm = connection.prepareStatement(AttendencelogQuery);
    ResultSet AttendenceRS = pStm.executeQuery();
    String EmployeeidDB = null;
    String InTimeDB = null;
    String logStatus = null;
    while (AttendenceRS.next()) {
    EmployeeidDB = AttendenceRS.getString(1);
    String memebrIdget = "select memnumber from MemberMasters where DeviceStaffCode ='" + EmployeeidDB + "'";
    Connection connection1 = eSession.getConnection();
    PreparedStatement mpstm = connection1.prepareStatement(memebrIdget);
    ResultSet logmemRS = mpstm.executeQuery();
    String mnumberlog = null;
    while (logmemRS.next()) {
    mnumberlog = logmemRS.getString(1);
    }

    InTimeDB = AttendenceRS.getString(2);
    logStatus = AttendenceRS.getString(3);
    String DateOnly[] = InTimeDB.split(" ");
    String DateOnlyOne = DateOnly[0];
    String DateOnlyTwo = DateOnly[1];

    if (DateCheck.equals(DateOnlyOne)) {
    System.out.println(InTimeDB);
    rfile.writeBytes(mnumberlog.trim() + " - \t");
    rfile.writeBytes(InTimeDB.trim() + "\t ");
    if (logStatus.equals("0")) {
    rfile.writeBytes(" - Active" + "\r\n");
    } else {
    rfile.writeBytes(" - Deactive" + "\r\n");
    }
     boolean flag = true;
    }
    }

    rfile.close();
    } catch (Exception e) {
    System.out.println("Cannot find database " + e);
    }
    }

    public void deactivateLogDetails() {   // to call the log table and member master table... //date wise
        try {
            DefaultComboBoxModel memberModel = new DefaultComboBoxModel();
            memberModel.removeAllElements();
            memberModel.addElement("Select Member Number..");
            Date date = new Date();
            StringBuffer dateBuffer = new StringBuffer(date.toString());
            int firstspace = dateBuffer.indexOf(" ");
            dateBuffer.delete(0, firstspace + 1);
            int secSpace = dateBuffer.indexOf(" ", 5);
            dateBuffer.delete(secSpace, dateBuffer.length());
            dateString = new String(dateBuffer);

            Connection connection = esslv3Connection.getConnection();
            PreparedStatement pStm = null;
            String AttendencelogQuery = "SELECT DeviceStaffId , DateTime FROM Log where DateTime like '"+ dateBuffer +"%'";
            pStm = connection.prepareStatement(AttendencelogQuery);
            ResultSet AttendenceRS = pStm.executeQuery();
            String EmployeeidDB = null;
            String InTimeDB = null;
            while (AttendenceRS.next()) {
                EmployeeidDB = AttendenceRS.getString(1);
                InTimeDB = AttendenceRS.getString(2);
                String memebrIdget = "select memnumber from MemberMasters where DeviceStaffCode ='" + EmployeeidDB + "'";
                Connection connection1 = eSession.getConnection();
                PreparedStatement mpstm = connection1.prepareStatement(memebrIdget);
                ResultSet logmemRS = mpstm.executeQuery();
                String mnumberlog = null;
                while (logmemRS.next()) {
                    mnumberlog = logmemRS.getString(1);
                    memberModel.addElement(mnumberlog +" / " +InTimeDB);
                }
            }
//           jComboBox6.setModel(memberModel);  //g12
        } catch (Exception e) {
            System.out.println("Swipe log error" + e);
        }
    }

        public class TextBoxListener extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            jPanel1.add(jScrollPane1);
            try {
                String text = jTextField12.getText();
                if (isAlpha(String.valueOf(e.getKeyChar())) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//|| !e.getKeyText(e.getKeyCode()).equals("Backspace")){
                    // Radiobutton if condition
                    if (jRadioButton3.isSelected() == true) {
                        deModel = takeDataFromMemberDB("M", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton4.isSelected() == true) {
                        deModel = takeDataFromMemberDB("S", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton5.isSelected() == true) {
                        deModel = takeDataFromChildDB(text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton1.isSelected() == true) {
                        deModel = takeDataFromStaffDB("Z", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    }
                } 
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        private boolean isAlpha(String s) {
            s = s.toUpperCase();
            for (int i = 0; i < s.length(); i++) {
                int c = (int) s.charAt(i);
                if ((c < 65 || c > 90) && (c < 47 || c > 58)) {
                    return false;
                }
            }
            return true;
        }
    }

public class TextBoxDeleteListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            jPanel30.add(jScrollPane6);
            try {
                String text = jTextField23.getText();
                if (isAlpha(String.valueOf(e.getKeyChar())) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//|| !e.getKeyText(e.getKeyCode()).equals("Backspace")){
                    // Radiobutton if condition
                    if (jRadioButton2.isSelected() == true) {
                        deModel = takeDataFromMemberDB1("M", text);
                        jPanel30.repaint();
                        jList5.setModel(deModel);
                    } else if (jRadioButton6.isSelected() == true) {
                        deModel = takeDataFromMemberDB1("S", text);
                        jPanel30.repaint();
                        jList5.setModel(deModel);
                    } else if (jRadioButton7.isSelected() == true) {
                        deModel = takeDataFromChildDB1(text);
                        jPanel30.repaint();
                        jList5.setModel(deModel);
                    } else if (jRadioButton8.isSelected() == true) {
                        deModel = takeDataFromStaffDB1("Z", text);
                        jPanel30.repaint();
                        jList5.setModel(deModel);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        private boolean isAlpha(String s) {
            s = s.toUpperCase();
            for (int i = 0; i < s.length(); i++) {
                int c = (int) s.charAt(i);
                if ((c < 65 || c > 90) && (c < 47 || c > 58)) {
                    return false;
                }
            }
            return true;
        }
    }

    public class TextLogListener extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            jPanel25.add(jScrollPane5);
            try {
                String text = jTextField16.getText();
                if (isAlpha(String.valueOf(e.getKeyChar())) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//|| !e.getKeyText(e.getKeyCode()).equals("Backspace")){
                    logModel = takeDataFromLogTable(text);
                    jList4.setModel(logModel);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        private boolean isAlpha(String s) {
            s = s.toUpperCase();
            for (int i = 0; i < s.length(); i++) {
                int c = (int) s.charAt(i);
                if ((c < 65 || c > 90) && (c < 47 || c > 58)) {
                    return false;
                }
            }
            return true;
        }
    }
    public class TextDeleteListener extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            jPanel30.add(jScrollPane6);
                        try {
                String text = jTextField23.getText();
                if (isAlpha(String.valueOf(e.getKeyChar())) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//|| !e.getKeyText(e.getKeyCode()).equals("Backspace")){
                    // Radiobutton if condition
                    if (jRadioButton2.isSelected() == true) {
                        deModel = takeDataFromMemberDB("M", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton6.isSelected() == true) {
                        deModel = takeDataFromMemberDB("S", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton7.isSelected() == true) {
                        deModel = takeDataFromChildDB(text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    } else if (jRadioButton8.isSelected() == true) {
                        deModel = takeDataFromStaffDB("Z", text);
                        jPanel1.repaint();
                        jList3.setModel(deModel);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        private boolean isAlpha(String s) {
            s = s.toUpperCase();
            for (int i = 0; i < s.length(); i++) {
                int c = (int) s.charAt(i);
                if ((c < 65 || c > 90) && (c < 47 || c > 58)) {
                    return false;
                }
            }
            return true;
        }
    }
    public class ListBoxListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            try {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextField12.setText(jList3.getSelectedValue().toString());
                    deModel.clear();
                    jPanel1.remove(jScrollPane1);
                    jPanel1.revalidate();
                    jPanel1.repaint();
                    textboxPressEnter();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    public class ListBoxMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            try {
                if(e.getClickCount() == 2){
                    jTextField12.setText(jList3.getSelectedValue().toString());
                    deModel.clear();
                    jPanel1.remove(jScrollPane1);
                    jPanel1.revalidate();
                    jPanel1.repaint();
                    textboxPressEnter();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

   public class ListBoxDeleteListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            try {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextField23.setText(jList5.getSelectedValue().toString());
                    deModel.clear();
                    jPanel30.remove(jScrollPane6);
                    jPanel30.revalidate();
                    jPanel30.repaint();
                    textboxDeleteMember();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    public class ListBoxDeleteMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            try {
                if(e.getClickCount() == 2){
                    jTextField23.setText(jList5.getSelectedValue().toString());
                    deModel.clear();
                    jPanel30.remove(jScrollPane6);
                    jPanel30.revalidate();
                    jPanel30.repaint();
                    textboxDeleteMember();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    
 public class logBoxListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            try {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jTextField16.setText(jList4.getSelectedValue().toString());
                    logModel.clear();
                    jPanel25.remove(jScrollPane5);
                    jPanel25.revalidate();
                    jPanel25.repaint();
                    logtextboxEnter();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
 public class logBoxMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            try {
                if (e.getClickCount() == 2) {
                    jTextField16.setText(jList4.getSelectedValue().toString());
                    logModel.clear();
                    jPanel25.remove(jScrollPane5);
                    jPanel25.revalidate();
                    jPanel25.repaint();
                    logtextboxEnter();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
 /*public class excelaccessListrener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            try {
                if (e.getClickCount() == 1) {
                    String currentdir = System.getProperty("user.dir");
                    Runtime rt = Runtime.getRuntime();
                    String test = "excel.exe /r F:\\VB\\bcimodif\\BCI\\GARUDA.xls";
                    System.out.println(test);
                    Process p = rt.exec(test);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }  */

    public void addRoletoCombo() {
        try {
            DefaultComboBoxModel roleModel = new DefaultComboBoxModel();
            roleModel.addElement("Select Role..");
            Connection roleConn = eSession.getConnection();
            String roleQry = "select name from role";
            PreparedStatement rolePstm = roleConn.prepareStatement(roleQry);
            ResultSet roleRs = rolePstm.executeQuery();
            String roleName = null;
            while (roleRs.next()) {
                roleName = roleRs.getString(1);
                if (!roleName.equals("Installer")) {
                    roleModel.addElement(roleName);
                }
            }
            jComboBox4.setModel(roleModel);

        } catch (Exception e) {
            System.out.println("Role Combobox error" + e);
        }
    }

    public void addUsertoCombo() {
        try {
            DefaultComboBoxModel userModel = new DefaultComboBoxModel();
            userModel.removeAllElements();
            userModel.addElement("Select User..");
            Connection roleConn = eSession.getConnection();
            String roleQry = "select name from userinfo";
            PreparedStatement rolePstm = roleConn.prepareStatement(roleQry);
            ResultSet roleRs = rolePstm.executeQuery();
            String roleName = null;
            while (roleRs.next()) {
                roleName = roleRs.getString(1);
                if (!roleName.equals("Installer")) {
                    userModel.addElement(roleName);
                }
            }
            jComboBox3.setModel(userModel);

        } catch (Exception e) {
            System.out.println("User Combobox error" + e);
        }
    }

    public void addUsertoCombo2() {
        try {
            DefaultComboBoxModel userModel2 = new DefaultComboBoxModel();
            userModel2.removeAllElements();
            userModel2.addElement("Select User..");
            Connection roleConn = eSession.getConnection();
            String roleQry = "select name from userinfo";
            PreparedStatement rolePstm = roleConn.prepareStatement(roleQry);
            ResultSet roleRs = rolePstm.executeQuery();
            String roleName = null;
            while (roleRs.next()) {
                roleName = roleRs.getString(1);
                if (!roleName.equals("Installer")) {
                    userModel2.addElement(roleName);
                }
            }
            jComboBox5.setModel(userModel2);
        } catch (Exception e) {
            System.out.println("User Combobox error" + e);
        }
    }

    public void loaddatabaseInformation() {
        try {
           Property prop = new Property();
           prop.Read();
           jTextField2.setText(prop.getAdbServer());
           jTextField3.setText(prop.getAdbport());
           jTextField4.setText(prop.getAdbdbname());
           jTextField5.setText(prop.getRdbServer());
           jTextField6.setText(prop.getRdbport());
           jTextField7.setText(prop.getRdbdbname());
        } catch (Exception e) {
            System.out.println("load database information error" + e);
        }
    }
    public void addCardtoCombo() {
        try {
            DefaultComboBoxModel userModel2 = new DefaultComboBoxModel();
            userModel2.removeAllElements();
            userModel2.addElement(0);
            Connection roleConn = eSession.getConnection();
            String roleQry = "select cardnumber from cardnumberTable";
            PreparedStatement rolePstm = roleConn.prepareStatement(roleQry);
            ResultSet roleRs = rolePstm.executeQuery();
            String roleName = null;
            while (roleRs.next()) {
                roleName = roleRs.getString(1);
                    userModel2.addElement(roleName);
            }
            jComboBox1.setModel(userModel2);
        } catch (Exception e) {
            System.out.println("User Combobox error" + e);
        }
    }

    // first panel modification
    public void listOfUsers() {

        try {
            JButton btn = null;
            String buttonName = null;
            jScrollPane3.getViewport().setView(null);
            JFlowPanel userList = new JFlowPanel();
            userList.applyComponentOrientation(getComponentOrientation());

            //
            final JButton btnInstaller = new JButton("Installer");
            btnInstaller.applyComponentOrientation(getComponentOrientation());
            btnInstaller.setFocusPainted(false);
            btnInstaller.setFocusable(false);
            btnInstaller.setRequestFocusEnabled(false);
            btnInstaller.setHorizontalAlignment(SwingConstants.CENTER);
            btnInstaller.setMaximumSize(new Dimension(150, 50));
            btnInstaller.setPreferredSize(new Dimension(150, 50));
            btnInstaller.setMinimumSize(new Dimension(150, 50));
            userList.add(btnInstaller);
            // Administrator Button.
            final JButton btnAdmin = new JButton("Administrator");
            btnAdmin.applyComponentOrientation(getComponentOrientation());
            btnAdmin.setFocusPainted(false);
            btnAdmin.setFocusable(false);
            btnAdmin.setRequestFocusEnabled(false);
            btnAdmin.setHorizontalAlignment(SwingConstants.CENTER);
            btnAdmin.setMaximumSize(new Dimension(150, 50));
            btnAdmin.setPreferredSize(new Dimension(150, 50));
            btnAdmin.setMinimumSize(new Dimension(150, 50));
            userList.add(btnAdmin);
            // Enroller Button.
            final JButton btnEnroller = new JButton("Enroller");
            btnEnroller.applyComponentOrientation(getComponentOrientation());
            btnEnroller.setFocusPainted(false);
            btnEnroller.setFocusable(false);
            btnEnroller.setRequestFocusEnabled(false);
            btnEnroller.setHorizontalAlignment(SwingConstants.CENTER);
            btnEnroller.setMaximumSize(new Dimension(150, 50));
            btnEnroller.setPreferredSize(new Dimension(150, 50));
            btnEnroller.setMinimumSize(new Dimension(150, 50));
            userList.add(btnEnroller);

            // To take the user names from user Table
            try {
                ArrayList<JButton> arr = new ArrayList<JButton>();
                Connection userNameCon = eSession.getConnection();
                String userNameQry = "select name from userinfo";
                PreparedStatement userNamePstm = userNameCon.prepareStatement(userNameQry);
                ResultSet userNameRs = userNamePstm.executeQuery();
                String userNamefromDB = null;
                while (userNameRs.next()) {
                    userNamefromDB = userNameRs.getString(1);
                    if (!userNamefromDB.equals("Installer") && !userNamefromDB.equals("Administrator") && !userNamefromDB.equals("Enroller")) {

                        btn = new JButton(userNamefromDB);
                        btn.setName(userNamefromDB);
                        btn.applyComponentOrientation(getComponentOrientation());
                        btn.setFocusPainted(false);
                        btn.setFocusable(false);
                        btn.setRequestFocusEnabled(false);
                        btn.setHorizontalAlignment(SwingConstants.CENTER);
                        btn.setMaximumSize(new Dimension(150, 50));
                        btn.setPreferredSize(new Dimension(150, 50));
                        btn.setMinimumSize(new Dimension(150, 50));
                        userList.add(btn);
                        arr.add(btn);
                        final String buttonValue = userNamefromDB;
                        btn.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                try {
                                    passwordCheck(buttonValue);
                                } catch (Exception e1) {
                                    System.out.println("password check Calling errror" + e1);
                                }
                            }
                        });
                    }
                }

            } catch (Exception e1) {
                System.out.println("User Table connection problem " + e1);
            }

            jScrollPane3.getViewport().setView(userList);

            btnInstaller.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        passwordCheck(btnInstaller.getText());
                    } catch (Exception e1) {
                        System.out.println("password check Calling errror" + e1);
                    }
                }
            });
            btnAdmin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        passwordCheck(btnAdmin.getText());
                    } catch (Exception e1) {
                        System.out.println("password check Calling errror" + e1);
                    }
                }
            });
            btnEnroller.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        passwordCheck(btnEnroller.getText());
                    } catch (Exception e1) {
                        System.out.println("password check Calling errror" + e1);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("list of user Error" + e);
        }

    }

    public void passwordCheck(String buttonName) {
        try {
            Boolean passwordChecking = false;
            Connection passwordCon = eSession.getConnection();
            String passwordQry = "SELECT NAME,PASSWORD FROM USERINFO WHERE NAME = '" + buttonName + "'";
            PreparedStatement passwordPst = passwordCon.prepareStatement(passwordQry);
            ResultSet passwordRs = passwordPst.executeQuery();
            String Uname = null;
            String pword = null;
            while (passwordRs.next()) {
                passwordChecking = true;
                Uname = passwordRs.getString(1);
                pword = passwordRs.getString(2);
                if (Uname.equals(buttonName)) {
                    if (pword.equals("")) {
                        pwordDialog.dialogmethod(buttonName);
                    } else {
                        pwordCheckDialog.getPasswordCheck(buttonName, this);
                    }
                }
            }
            if (!passwordChecking) {
                pwordDialog.dialogmethod(buttonName);
            }
        } catch (Exception e) {
            System.out.println("password check error!!" + e);
        }
        
    }

    public void startMainpanel(int countval) {

        if(countval == 1){
        jTabbedPane1.add(jPanel11);
        jTabbedPane1.revalidate();
        jTabbedPane1.repaint();
        }else if(countval == 2){
        jTabbedPane1.add(jPanel11);
        jTabbedPane1.add(jPanel2);
        jTabbedPane1.add(jPanel7);
        jTabbedPane1.add(jPanel10);
        jTabbedPane1.add(jPanel28);
        jTabbedPane1.revalidate();
        jTabbedPane1.repaint();
        }
        getContentPane().remove(jTabbedPane1);
        getContentPane().remove(jButton14);
        getContentPane().add(jPanel18);
        getContentPane().validate();
        getContentPane().repaint();
        jTabbedPane1.setVisible(true);
    }

    public void installerMethod(String logname) {

        tabbedchekval = 0;
        lognameDisplay(logname);
        getContentPane().remove(jPanel18);
        getContentPane().add(jTabbedPane1);
        getContentPane().add(jButton14);
        getContentPane().validate();
        getContentPane().repaint();
        jTabbedPane1.setVisible(true);
       // jButton14.setVisible(true); 
    }

    public void adminMethod(String logname) {

        tabbedchekval = 1;
        lognameDisplay(logname);
        getContentPane().remove(jPanel18);
        getContentPane().add(jTabbedPane1);
        getContentPane().add(jButton14);
        getContentPane().validate();
        getContentPane().repaint();
        jTabbedPane1.setVisible(true);
     //   jButton14.setVisible(true);
        jTabbedPane1.remove(jPanel11);
        jTabbedPane1.revalidate();
        jTabbedPane1.repaint();
    }

    public void EnrollerMethod(String logname) {

        tabbedchekval = 2;
        lognameDisplay(logname);
        getContentPane().remove(jPanel18);
        getContentPane().add(jTabbedPane1);
       getContentPane().add(jButton14);
        getContentPane().validate();
        getContentPane().repaint();
        jTabbedPane1.setVisible(true);
     //   jButton14.setVisible(true);
        jTabbedPane1.remove(jPanel11);
        jTabbedPane1.remove(jPanel2);
        jTabbedPane1.remove(jPanel7);
        jTabbedPane1.remove(jPanel10);
        jTabbedPane1.remove(jPanel28);
        jTabbedPane1.revalidate();
        jTabbedPane1.repaint();
    }

    public void lognameDisplay(String logname){
        try{
            jLabel41.setText("Loged On : "+logname);
            jLabel42.setText("Loged On : "+logname);
            jLabel43.setText("Loged On : "+logname);
            jLabel44.setText("Loged On : "+logname);
            jLabel45.setText("Loged On : "+logname);
            jLabel46.setText("Loged On : "+logname);
            jLabel47.setText("Loged On : "+logname);
            jLabel48.setText("Loged On : "+logname);
            jLabel57.setText("Loged On : "+logname);
            jLabel61.setText("Loged On : "+logname);
        }catch(Exception e){
            System.out.println("Log name Dispaly Error!!"+e);
        }
    }

    public class dummy implements Runnable{
    public void run() {
        try{
            Thread t = Thread.currentThread();
            t.sleep(2000);
            wDia.hideDialog();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}



public void textboxDeleteMember(){
    try{
        String memtextfield = jTextField23.getText();
        if(!memtextfield.equals("")){
            Connection connection = eSession.getConnection();
            String deleteqry = "Select memname,devicestaffcode,Active from membermasters where memnumber ='"+memtextfield+"'";
            PreparedStatement PreparedStmt = connection.prepareStatement(deleteqry);
            ResultSet resultset = PreparedStmt.executeQuery();
            String mname = null;
            String activeinfo = null;
            while (resultset.next()) {
                mname = resultset.getString(1);
                deleteDevicestaffcode = resultset.getString(2);
                activeinfo = resultset.getString(3);
            }
            jTextField24.setText(mname);
            if(activeinfo.equals("1")){
                jTextField25.setText("Activate");
            }else{
                jTextField25.setText("Deactivate");
            }
        }
    }catch(Exception e){
        System.out.println("Delete member method error!!");
        e.printStackTrace();
    }
}
public void backmethodclear(){

    jTextField25.setText("");
    jTextField24.setText("");
    jTextField23.setText("");
    jTextField21.setText("");
    jTextField22.setText("");
    jTextField23.setText("");
    jTextField11.setText("");
    jTextField13.setText("");
    jTextField17.setText("");
    jTextField16.setText("");
    jTextField15.setText("");
    jTextField1.setText("");
    cardNumber.setText("");
    mName.setText("");
    jTextField19.setText("");

}
 /*   public void cardSwipe() throws IOException{
        System.out.println("method called!!!");
        try{
            String cardNo = jTextField19.getText();
            File file = new File("C:\\Garuda.txt");
            if(file.exists()){
               String a = "";
               FileOutputStream fos = new FileOutputStream(file,false);
               fos.write(a.getBytes());
               fos.close();
            }
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Swipe File created!!");
              }
            RandomAccessFile rfile = new RandomAccessFile(file, "rw");
            rfile.seek(file.length());
            if(!cardNo.equals("")){
                Connection cardCon = eSession.getConnection();
                String qry1 = "select cardNumberMain from  MemberMasters";
                PreparedStatement pstm = cardCon.prepareStatement(qry1);
                ResultSet rs = pstm.executeQuery();
                ArrayList<String> a1list = new ArrayList<String>();
                while(rs.next()){
                    a1list.add(rs.getString(1));
                }

                if(a1list.contains(cardNo)){
               String qry = "Select cardNumber,Memnumber from MemberMasters where cardNumberMain = '"+cardNo+"'";
               PreparedStatement cardpstm = cardCon.prepareStatement(qry);
               ResultSet cardRs =  cardpstm.executeQuery();

                String cardNumber = null;
                String memNumber = null;
                while (cardRs.next()) {
                    cardNumber = cardRs.getString(1);
                    memNumber = cardRs.getString(2);
                    if(cardNumber.equals("0")){
                        rfile.writeBytes("Unregistered Card!! \r\n");
                    }else{
                    rfile.writeBytes(cardNumber.trim() + "    - ");
                    rfile.writeBytes(memNumber.trim());
                    rfile.close();
                    }
                }

            }else{
                 rfile.writeBytes("Unregistered Card!! \r\n");
                 rfile.close();
            }
            }
        }catch(Exception e){
            System.out.println("Card swipe error!!"+e);
        }
    } */

    //

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JTextField cardNumber;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JList jList5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField mName;
    private javax.swing.JPanel m_jLogonName;
    private javax.swing.JPanel m_jPanelContainer;
    private javax.swing.JPanel m_jPanelLogin;
    private javax.swing.JTextField m_txtKeys;
    // End of variables declaration//GEN-END:variables
}
