/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * dueListTable.java
 *
 * Created on 20-Oct-2009, 17:10:46
 */

package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.pos.Accounts.DueListTableModel.ReportLine;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.DataSourceProvider;
import com.openbravo.pos.clubmang.Facility;
import com.openbravo.pos.clubmang.JasperReportNew;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.sales.JPanelTicket;
import com.openbravo.pos.sms.SMSgeneralDBSettings;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author swathi
 */
public class dueListTable extends javax.swing.JDialog {

    /** Creates new form dueListTable */
    private List<Facility> rflist;
    private DueListTableModel dmodel;
    private AppView m_App;
    private Date date;
    private String header;
    private String secretary;
    private String treasurer;
    private DataLogicFacilities dlfac;
    private String getDueAmount;
    private double  DueAmount=0.0,OverDue=0.0;
    private SMSgeneralDBSettings smsDBsettings;
    
    DecimalFormat decimalFormat=new DecimalFormat("0.00");
    private Object obj;
    public dueListTable(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        AppView app = LookupUtilityImpl.getInstance(null).getAppView();
        smsDBsettings = (SMSgeneralDBSettings) app.getBean("com.openbravo.pos.sms.SMSgeneralDBSettings");
        
        jLabel1.setText("DueList");
        jLabel2.setText("Please see Cr.Avbl column,Adjust credit and regenerate report if requiered. If ok tick yes to print it");
        jLabel3.setText("No Of Facilities to be displayed in a report page");
        jButton1.setText("Generate Report");
        jCheckBox1.setText("Yes");
        jButton1.setEnabled(false);
        sendSms_Btn.setVisible(false);
        sendSmsCheck.setVisible(false);
        jSpinner1.setModel(new SpinnerNumberModel(1, 1, 3, 1));
    }


    protected dueListTable(Dialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public dueListTable(Frame parent,AppView app,Date d,String header,String sec,String tre) {
        this(parent, false);
        this.m_App=app;
        this.date=d;
        this.header=header;
        this.secretary=sec;
        this.treasurer=tre;
    }

    public dueListTable(Dialog parent,AppView app,Date d,String header,String sec,String tre) {
        this(parent, false);
        this.m_App=app;
        this.date=d;
        this.header=header;
        this.secretary=sec;
        this.treasurer=tre;
    }

    protected static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }


    public static dueListTable getDialog(Component parent,AppView app,Date d,String header,String sec,String tre,String fid) {

        Window window = getWindow(parent);

        dueListTable ct;

        if (window instanceof Frame) {
            ct = new dueListTable((Frame) window,app,d,header,sec,tre);
        } else {
            ct = new dueListTable((Dialog) window,app,d,header,sec,tre);
        }

        return ct;
    }

    public dueListTable(java.awt.Frame parent, boolean modal,Date d,String header,String sec,String tre) {
        super(parent, modal);
        initComponents();
        jSpinner1.setModel(new SpinnerNumberModel(1, 1, 3, 1));
        jLabel1.setText("Due List");
        jLabel2.setText("Please see Cr.Avbl column,Adjust credit and regenerate report if requiered. If ok tick yes to print it");
        jLabel3.setText("No Of Facilities to be displayed in a report page");
        jButton1.setText("Generate Report");
        
        jCheckBox1.setText("Yes");
        jButton1.setEnabled(false);
    }

    public void showDialog(DueListTableModel dmodel,List<Facility> rflist,AppView m_App){
       this.rflist=rflist;
       this.m_App=m_App;
       jTable1.setAutoscrolls(true);
       if(rflist.size()>1)
           jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
       else
           jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
       jTable1.setModel(dmodel.getTableModel());
       this.dmodel=dmodel;
            int columncnt=jTable1.getColumnModel().getColumnCount();
            if(columncnt>0){
              jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
              jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
              jTable1.getColumnModel().getColumn(1).setMaxWidth(120);
              jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
              for(int k=2;k<columncnt;k++){
               if((k-2)%3==0){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(100);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(150);
                //jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
               }else if((k-2)%3==1){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
                //jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_ALL_COLUMNS);
               } else if((k-2)%3==2){
                jTable1.getColumnModel().getColumn(k).setMaxWidth(60);
                jTable1.getColumnModel().getColumn(k).setPreferredWidth(60);
               // jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
               }
              }
            }
      setVisible(true);
   }
    
  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        sendSms_Btn = new javax.swing.JButton();
        sendSmsCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("jLabel1");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("jLabel2");

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText("jLabel3");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        sendSms_Btn.setText("Send SMS");
        sendSms_Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendSms_BtnActionPerformed(evt);
            }
        });

        sendSmsCheck.setText("Do You want to send SMS to all Members ");
        sendSmsCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sendSmsCheckItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 658, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 246, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(layout.createSequentialGroup()
                            .add(sendSmsCheck)
                            .add(62, 62, 62)
                            .add(sendSms_Btn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 370, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 127, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 570, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jCheckBox1)))))
                .addContainerGap(459, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(11, 11, 11)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jCheckBox1))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jSpinner1)
                        .add(jButton1))
                    .add(jLabel3))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sendSms_Btn)
                    .add(sendSmsCheck))
                .add(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         int n=Integer.parseInt(jSpinner1.getValue().toString());
        if(n>0 && n<4 ){
            Map reportparam=new HashMap();
            reportparam.put("companyName",m_App.getSession().getCompanyName());
            reportparam.put("companyAddress",m_App.getSession().getCompanyAddress());

            reportparam.put("date", date);
            reportparam.put("secretary", secretary);
            reportparam.put("treasurer", treasurer);
            reportparam.put("header", header);
            reportparam.put("total", dmodel.getReportList1().size());

            String path=null;
            //if(rflist.get(0).getid().equals("1")){

           // }
           /* if(n==1)
                    path="C:\\Documents and Settings\\swathi\\Desktop\\GarudaNew\\reports\\com\\openbravo\\reports/DueReport2.jrxml";
               else  if(n==2)
                    path="C:\\Documents and Settings\\swathi\\Desktop\\GarudaNew\\reports\\com\\openbravo\\reports/DueReport1.jrxml";
               else if(n==3)
                    path="C:\\Documents and Settings\\swathi\\Desktop\\GarudaNew\\reports\\com\\openbravo\\reports/DueReport.jrxml";
            */
            int cnt=rflist.size();
            int temp=1,m=n;
           // JasperReportNew.runReport(m_App,  "./reports/com/openbravo/reports/DueReport.jrxml", reportparam, false, data1, true);
            //C:\Documents and Settings\swathi\Desktop\GarudaNew\reports\com\openbravo\reports
           // while(cnt>0){//temp<=cnt
               DataSourceProvider data1=new DataSourceProvider(dmodel.getReportList1());
               DueListDataSource ds=new DueListDataSource(dmodel.getReportList1());

               data1.setDataSource(ds);
               ds.setTemp(temp);
               //if(m==1){
//                   if(rflist.get(cnt-1).getName().equals("Bar"))
                       path="./reports/com/openbravo/reports/DueReportBar.jrxml";
//                   else
//                    path="./reports/com/openbravo/reports/DueReport2.jrxml";
//               }else  if(m==2)
//                    path="./reports/com/openbravo/reports/DueReport1.jrxml";
//               else if(m==3)
//                    path="./reports/com/openbravo/reports/DueReport.jrxml";
                JasperReportNew.runReport(m_App,  path, reportparam, false, data1, true,null);
                cnt=cnt-m;
                temp=temp+m;
                if(cnt<m)
                    m=cnt;


               /* if(temp==cnt)
                    break;
                temp+=n;
                m=n;
                while(temp>cnt){
                    temp--;
                    m--;
                }
                if(temp==cnt)
                    m--;*/
            }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
    
        if(jCheckBox1.isSelected()){
            jButton1.setEnabled(jCheckBox1.isSelected());
            jButton1.setEnabled(jCheckBox1.isSelected());
        
            boolean sendSMSwhileBill =  smsDBsettings.getSMSvalue(SMSgeneralDBSettings.SMS_DUE_LIST_ID);
            if(sendSMSwhileBill) {
                sendSmsCheck.setVisible(true);
                sendSmsCheck.setSelected(false);
            }
            else{
               sendSmsCheck.setVisible(false);
               sendSms_Btn.setVisible(false);
            }
             
        }
        else{
             jButton1.setEnabled(false);
             sendSmsCheck.setVisible(false);
             sendSms_Btn.setVisible(false);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void sendSms_BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendSms_BtnActionPerformed
 
    boolean sendSMSwhileBill =  smsDBsettings.getSMSvalue(SMSgeneralDBSettings.SMS_DUE_LIST_ID);
    if(sendSMSwhileBill) {
        String mobilelist = null;
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        // formatting
        System.out.println(format.format(new Date()));
     
        String sendtype=null;
        boolean withname=false;
        UUID.randomUUID().toString();
        String fid;
    
      
        List<ReportLine> list1 = dmodel.getReportList1();

        for (ReportLine reportLine : list1) {
            System.out.println(reportLine.getSearchkey()); 
            List<DueListTableModel.FacilityLine> faclist=reportLine.getFacilityList();

            try {
                mobilelist=(String)new PreparedSentence(m_App.getSession(), "SELECT C.MOBILE,c.searchkey FROM CUSTOMERS C WHERE C.MOBILE IS NOT NULL AND (LENGTH(C.MOBILE)=10 OR LENGTH(C.MOBILE)=13) and c.searchkey=? order by searchkey", SerializerWriteString.INSTANCE ,SerializerReadString.INSTANCE).find(reportLine.getSearchkey());
            } catch (BasicException ex) {
                Logger.getLogger(dueListTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (DueListTableModel.FacilityLine facilityLine : faclist) {
                if(mobilelist!=null)
                {
                    double due=facilityLine.getDueAmount();
                    double overdue=facilityLine.getOverDueAmount();
                    double Total=due+ overdue;
                    String memberNo = reportLine.getSearchkey();
                    String memberName = reportLine.getName();
                    String FacilityName = facilityLine.getFacilityName();

                    String msg="Dear Member,Bal for ALL facility as on "+format.format(new Date())+ " for due on "+format.format(date)+ " is "+decimalFormat.format(due)+ " and overdue is "+decimalFormat.format(overdue)+" total "+ decimalFormat.format(Total)+" plz pay at earliest.Ignore if paid";




                    String smsString = smsDBsettings.getMessage(SMSgeneralDBSettings.SMS_DUE_LIST_ID);
             
             
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_DUE_DATE_KEY, format.format(date));
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_DTM_KEY , format.format(new Date()));
                    AppView m_App = LookupUtilityImpl.getInstance(null).getAppView();
                    String x = m_App.getAppUserView().getUser().getRole();
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_ROLE_KEY, LookupUtilityImpl.getInstance(null).getRoleMap().get(x).toString()); 
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_TOT_AMOUNT_KEY, decimalFormat.format(due));
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_OVER_DUE_AMOUNT_KEY, decimalFormat.format(overdue));
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_DUE_PLUS_OVER_DUE_AMOUNT_KEY, decimalFormat.format(Total));
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NAME_KEY, memberName);
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_MEMBER_NO_KEY, memberNo);
                    smsString = smsString.replace(SMSgeneralDBSettings.SMS_FACILITY_KEY, FacilityName);
                    
                    
                 
                       
                    smsDBsettings.insertSMStoActiveMsgTable(smsString, mobilelist, memberNo);
                    Logger.getLogger(JPanelTicket.class.getName()).log(Level.INFO,  "SMS sent successfully : "+smsString);   
                }
            }
          
        }
            
    }
    
    }//GEN-LAST:event_sendSms_BtnActionPerformed

    private void sendSmsCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sendSmsCheckItemStateChanged
        if(sendSmsCheck.isSelected()){
            sendSms_Btn.setVisible(true);
            
        }
        else{
            sendSms_Btn.setVisible(false);
            
        }
    }//GEN-LAST:event_sendSmsCheckItemStateChanged
    

    
    /**
    * @param args the command line arguments
    */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox sendSmsCheck;
    private javax.swing.JButton sendSms_Btn;
    // End of variables declaration//GEN-END:variables
     
     
}
