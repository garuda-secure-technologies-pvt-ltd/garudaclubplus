/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AccountReports.java
 *
 * Created on Jun 28, 2011, 11:57:41 AM
 */
package com.openbravo.pos.Accounts;

import com.openbravo.pos.clubmang.*;
import com.openbravo.basic.BasicException;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.util.JRViewer300;
//import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
//import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author swathi
 */
public class AccountReports extends javax.swing.JPanel implements JPanelView, BeanFactoryApp {

    /** Creates new form AccountReports */
    private AppView m_app;
    private JRViewer300 reportviewer = null;
    private DataLogicFacilities dlfac;
    private List<TrailBalanceData> acclist = new ArrayList<TrailBalanceData>();
    private List<TrailBalanceData> sacc = new ArrayList<TrailBalanceData>();
    private List<TrailBalanceData> sacc1 = new ArrayList<TrailBalanceData>();
    private List<TrailBalanceData> sacc2 = new ArrayList<TrailBalanceData>();
    private List<TrailBalanceData> sacc3 = new ArrayList<TrailBalanceData>();
    private JTextComponent editor;
    private ComboBoxValModel accmodel;
    private Date startdate;
    private Date enddate;
    private waitDialog w;

    //private Thread t;
    public AccountReports() {
        initComponents();
    }

    public AccountReports(AppView app) {
        initComponents();
        m_app = app;
    }

    public void init(AppView app) throws BeanFactoryException {
        m_app = app;
        dlfac = (DataLogicFacilities) app.getBean("com.openbravo.pos.clubmang.DataLogicFacilitiesCreate");
        GroupingType.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Daily", "Monthly", "Yearly"}));
        reportviewer = new JRViewer300(null);
        jPanel1.add(reportviewer);
        jPanel1.validate();
        jPanel1.repaint();
        accmodel = new ComboBoxValModel();
        fromdate.setEditable(false);
        toDate.setEditable(false);
    }

    public String getTitle() {
        return null;
    }

    public void activate() throws BasicException {
        account.setModel(accmodel);
        acclist = new ArrayList<TrailBalanceData>();
        editor = (JTextComponent) account.getEditor().getEditorComponent();
        editor.setText(null);
        editor.addKeyListener(new Comboboxlistenner());
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

    public void loadReport(String from, String to, AccountMaster accm, String filename, String reporttype, boolean display) {
        try {
            Date sdate = new Date();
            try {
                sdate = getDate(from);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date edate = getSecondDate(to);
            startdate = sdate;
            enddate = edate;
            LookupUtilityImpl.getInstance(null).reloadGeneralSettingsMap();
            Map<String, GeneralSettingInfo> gs = LookupUtilityImpl.getInstance(null).getGeneralSettingsMap();
            GeneralSettingInfo sinfo = gs.get("Datestart");
            GeneralSettingInfo einfo = gs.get("Dateend");
            Date fsdate = (Date) Formats.DATE.parseValue(sinfo.getValue());
            Date fedate = (Date) Formats.DATE.parseValue(einfo.getValue());
            Calendar fscal = Calendar.getInstance();//financial year sdate
            fscal.setTime(fsdate);
            Calendar fecal = Calendar.getInstance();//financial year edate
            fecal.setTime(fedate);
            Calendar rscal = Calendar.getInstance();//report start date
            Calendar rscal1 = Calendar.getInstance();
            Calendar recal = Calendar.getInstance();//report end date
            Calendar recal1 = Calendar.getInstance();//report end date
            rscal.setTimeInMillis(sdate.getTime());
            recal.setTimeInMillis(edate.getTime());
            rscal1.setTimeInMillis(rscal.getTimeInMillis());
            rscal1.add(Calendar.DATE, -1);
            recal1.setTimeInMillis(recal.getTimeInMillis());
            recal1.add(Calendar.DATE, -1);


            if (reporttype.equals("Ledger Individual")) {
                List<AccountsList> ledgerlist = new ArrayList<AccountsList>();
                String grouptype = GroupingType.getSelectedItem().toString();
                /*   if(account.getSelectedIndex()==0 ||account.getSelectedIndex()==-1){
                ledgerlist=createLedgerReport(sdate,edate,grouptype);
                }else{
                AccountMaster accm=(AccountMaster)account.getSelectedItem();
                ledgerlist=createLedgerReportforAccount(sdate,edate,grouptype,accm.getid());
                }*/
                //  m_app.getSession().getConnection().getMetaData().getCatalogs().get;
                List<String> list = new ArrayList<String>();
                ResultSet rs = m_app.getSession().getConnection().getMetaData().getTables(null, null, "AJ%", null);
                while (rs.next()) {
                    list.add(rs.getString("TABLE_NAME"));
                }
                //AccountMaster accm=(AccountMaster)account.getSelectedItem();
                double dtemp = 0, ctemp = 0;
                //calculate OB
                double crtemp = 0, drtemp = 0, temp = 0;
                if (!rscal.after(fscal)) {
                    if (rscal1.get(Calendar.DATE) != rscal1.getActualMaximum(Calendar.DATE)) {
                        Object[] obj2 = (Object[]) new StaticSentence(m_app.getSession(), "SELECT SUM(A.DEBIT-A.CREDIT) FROM AJPERIODTOTALS A WHERE A.ACCOUNTID=? AND A.EDATE < ? ", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE})).find(new Object[]{accm.getid(), sdate});
                        temp = Double.parseDouble(obj2[0].toString());
                    } else {
                        rscal1.set(Calendar.DATE, rscal1.getActualMinimum(Calendar.DATE));
                        Date stemp = new Date(rscal1.getTimeInMillis());

                        Object[] obj2 = (Object[]) new StaticSentence(m_app.getSession(), "SELECT (SELECT SUM(A.DEBIT-A.CREDIT) FROM AJPERIODTOTALS A WHERE A.ACCOUNTID=? AND A.EDATE < ? ),(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=? AND DATE>=? AND DATE < ?  AND TRANSTYPE='C' AND ACTIVE=TRUE)" + " ,(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=? AND DATE>=? AND DATE < ?  AND TRANSTYPE='D' AND ACTIVE=TRUE) FROM APPLICATIONS WHERE ID=?   ", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE})).find(new Object[]{accm.getid(), sdate, accm.getid(), sdate, stemp, accm.getid(), sdate, stemp, AppLocal.APP_ID});
                        if (obj2[1] != null) {
                            crtemp = Double.parseDouble(obj2[1].toString());
                        }
                        if (obj2[2] != null) {
                            drtemp = Double.parseDouble(obj2[2].toString());
                        }
                        if (obj2[0] != null) {
                            temp = Double.parseDouble(obj2[0].toString());
                        }
                        temp = temp + drtemp - crtemp;
                    }
                } else {
                    Object[] obj2 = (Object[]) new StaticSentence(m_app.getSession(), "SELECT (SELECT SUM(A.DEBIT-A.CREDIT) FROM AJPERIODTOTALS A WHERE A.ACCOUNTID=? AND A.EDATE < ? ),(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=? AND DATE>=? AND DATE < ?  AND TRANSTYPE='C' AND ACTIVE=TRUE)" + " ,(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=? AND DATE>=? AND DATE < ?  AND TRANSTYPE='D' AND ACTIVE=TRUE) FROM APPLICATIONS WHERE ID=?   ", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING}), new SerializerReadBasic(new Datas[]{Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE})).find(new Object[]{accm.getid(), fsdate, accm.getid(), fsdate, sdate, accm.getid(), fsdate, sdate, AppLocal.APP_ID});
                    if (obj2 != null) {

                        if (obj2[1] != null) {
                            crtemp = Double.parseDouble(obj2[1].toString());
                        }
                        if (obj2[2] != null) {
                            drtemp = Double.parseDouble(obj2[2].toString());
                        }
                        if (obj2[0] != null) {
                            temp = Double.parseDouble(obj2[0].toString());
                        }
                        temp = temp + drtemp - crtemp;
                    }
                }
                if (temp >= 0) {
                    dtemp = temp;
                } else {
                    ctemp = temp;
                }
                //get data
                ledgerlist = loadReport(rscal, recal, fscal, fecal, accm.getid(), list, "");
                String groupTypeVal = "";
                String groupingtype = GroupingType.getSelectedItem().toString();
                DateFormat df2 = new SimpleDateFormat("MMM,yyyy");
                DateFormat df3 = new SimpleDateFormat("yyyy");
                DateFormat df1 = new SimpleDateFormat("dd MMM,yyyy");
                for (AccountsList acc : ledgerlist) {
                    if ("Daily".equals(grouptype)) {
                        groupTypeVal = df1.format(acc.getDate());
                    } else if ("Monthly".equals(grouptype)) {
                        groupTypeVal = df2.format(acc.getDate());
                    } else if ("Yearly".equals(grouptype)) {
                        groupTypeVal = df3.format(acc.getDate());
                    }                    
                    acc.setType(groupTypeVal);
                }
                // List<ReportData> ledgerlist=createLedgerReport(sdate,edate,grouptype);
                Map reportparam = new HashMap();
                reportparam.put("Narrationreq", jCheckBox1.isSelected());
                reportparam.put("startdate", sdate);
                reportparam.put("enddate", edate);
                reportparam.put("grouptype", GroupingType.getSelectedItem().toString());

                // Sanjeev : added an extra parameter for grouptype
                //reportparam.put("grouptype", GroupingType.getSelectedItem().toString());
                if (accm != null) {
                    reportparam.put("aname", accm.getName());
                }

                reportparam.put("dobalance", dtemp);
                if (ctemp != 0) {
                    reportparam.put("cobalance", ctemp * -1);
                } else {
                    reportparam.put("cobalance", ctemp);
                }
                boolean flag = false;
                //if(rtemp!=0)
                flag = true;

                reportparam.put("dop", (Object) sdate);

                reportparam.put("flag", flag);
                DataSourceProvider data1 = new DataSourceProvider(ledgerlist);

                DataSource1 ds = new DataSource1(ledgerlist, edate, sdate);
                data1.setDataSource(ds);
                JasperPrint jp = JasperReportNew.runReport(m_app, "./reports/com/openbravo/reports/LedgerReport.jrxml", reportparam, false, data1, display, filename);
                //JasperPrint jp=JasperReportNew.runReport(m_app, "C:\\Documents and Settings\\swathi\\Desktop\\GarudaNew/reports/com/openbravo/reports/LedgerReport.jrxml", reportparam, false,data1,true);

                reportviewer.loadJasperPrint(jp);
            } else if (reportType.getSelectedItem() == "Ledger All") {
                acclist = new ArrayList<TrailBalanceData>();
                Date d = new Date();
                Date d1 = (Date) Formats.TIMESTAMP.parseValue(toDate.getText());
                String[] temp = d.toString().split(" ");
                String[] temp1 = d1.toString().split(" ");
                if (temp[0].equals(temp1[0])) {
                    acclist = getPresentStatement();
                } else if (fscal.equals(recal)) {
                    acclist = getTrailBalanceofPrevFYear();
                } else {
                    acclist = getStatement(fscal, fecal, recal, recal1);
                }
                DataSourceProvider data1 = new DataSourceProvider(acclist);
                DataSourceforTrailBalance ds = new DataSourceforTrailBalance(acclist);
                data1.setDataSource(ds);
                Map reportparam = new HashMap();
                JasperPrint jp = JasperReportNew.runReport(m_app, "./reports/com/openbravo/reports/TrailBalanceNew.jrxml", reportparam, false, data1, display, filename);
                reportviewer.loadJasperPrint(jp);
            }
            w.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
            w.hideDialog();
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
            msg.show(this);
        }
    }

    public class Comboboxlistenner extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            try {
                //!e.getKeyText(e.getKeyCode()).equals("Enter")
                String text = editor.getText();
                if (isAlpha(String.valueOf(e.getKeyChar())) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//|| !e.getKeyText(e.getKeyCode()).equals("Backspace")){
                    accmodel = new ComboBoxValModel(dlfac.getsubAccounts1(text.toUpperCase()));
                    account.setModel(accmodel);
                    editor.setText(text);
                    account.showPopup();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (accmodel.getSize() <= 0) {
                        editor.setText(null);
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean deactivate() {
        return true;
    }

    public JComponent getComponent() {
        return this;
    }

    public Object getBean() {
        return this;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /* private List<ReportData> createLedgerReport(Date sDate,Date eDate,String grouptype) throws BasicException{

    List<ReportData> ledgerlist=new StaticSentence(m_app.getSession()
    , " SELECT  (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS DOPENBALANCE, "+
    " (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='C' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS COPENBALANCE, "+
    " A2.AMOUNT,A2.TRANSTYPE,A2.TRANSNO,A2.TRANSREF,AM1.NAME,A.TRANSTYPE AS MAINTRANSTYPE,AM.NAME AS MNAME,A.DATE,A2.NARRATION ,"+
    "(SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS DCLOSEBALANCE, "+
    " (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='C' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS CCLOSEBALANCE "+
    " FROM ACCOUNTJOURNAL A,ACCOUNTJOURNAL A2,ACCOUNTMASTER AM,ACCOUNTMASTER AM1 WHERE A.TID=A2.TID  AND A.ACCOUNTID=AM.ID AND A2.ACCOUNTID=AM1.ID AND A.ACCOUNTID!=A2.ACCOUNTID AND A2.DATE >? AND A2.DATE<? AND A.DATE >? AND A.DATE<? ORDER BY AM.NAME,A.DATE "
    , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadClass(ReportData.class)).list(new Object[]{sDate,sDate,eDate,eDate,sDate,eDate,sDate,eDate});
    return ledgerlist;
    }*/
    private List<AccountsList> createLedgerReport1(Date sDate, Date eDate, String grouptype) throws BasicException {

        List<AccountsList> ledgerlist = new StaticSentence(m_app.getSession(), " SELECT  A.ID,A.NAME ,(SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ID AND A1.DATE < ? GROUP BY A1.ACCOUNTID)AS DOPENBALANCE, " +
                " (SELECT SUM(A2.AMOUNT) FROM ACCOUNTJOURNAL A2 WHERE A2.TRANSTYPE='C' AND A2.ACCOUNTID=A.ID AND A2.DATE < ? GROUP BY A2.ACCOUNTID)AS COPENBALANCE ," +
                "(SELECT SUM(A3.AMOUNT) FROM ACCOUNTJOURNAL A3 WHERE A3.TRANSTYPE='D' AND A3.ACCOUNTID=A.ID AND A3.DATE <= ? GROUP BY A3.ACCOUNTID)AS DCLOSEBALANCE, " +
                " (SELECT SUM(A4.AMOUNT) FROM ACCOUNTJOURNAL A4 WHERE A4.TRANSTYPE='C' AND A4.ACCOUNTID=A.ID AND A4.DATE <= ? GROUP BY A4.ACCOUNTID)AS CCLOSEBALANCE " +
                " FROM ACCOUNTMASTER A ORDER BY A.NAME ", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.TIMESTAMP}), new SerializerReadClass(AccountsList.class)).list(new Object[]{sDate, sDate, eDate, eDate});
        return ledgerlist;
    }
    /*    private List<AccountsList> createLedgerReportforAccount1(Date sDate,Date eDate,String grouptype,String accid) throws BasicException{

    List<AccountsList> ledgerlist=new StaticSentence(m_app.getSession()
    , " SELECT  A.ID,A.NAME ,(SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ID AND A1.DATE < ? GROUP BY A1.ACCOUNTID)AS DOPENBALANCE, "+
    " (SELECT SUM(A2.AMOUNT) FROM ACCOUNTJOURNAL A2 WHERE A2.TRANSTYPE='C' AND A2.ACCOUNTID=A.ID AND A2.DATE < ? GROUP BY A2.ACCOUNTID)AS COPENBALANCE ,"+
    "(SELECT SUM(A3.AMOUNT) FROM ACCOUNTJOURNAL A3 WHERE A3.TRANSTYPE='D' AND A3.ACCOUNTID=A.ID AND A3.DATE <= ? GROUP BY A3.ACCOUNTID)AS DCLOSEBALANCE, "+
    " (SELECT SUM(A4.AMOUNT) FROM ACCOUNTJOURNAL A4 WHERE A4.TRANSTYPE='C' AND A4.ACCOUNTID=A.ID AND A4.DATE <= ? GROUP BY A4.ACCOUNTID)AS CCLOSEBALANCE "+
    " FROM ACCOUNTMASTER A WHERE A.ID=?  "
    , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING})
    , new SerializerReadClass(AccountsList.class)).list(new Object[]{sDate,sDate,eDate,eDate,accid});
    return ledgerlist;
    }*/
    /*    private List<AccountsList> createLedgerReportforAccount1(Date sDate,Date eDate,String grouptype,String accid) throws BasicException{

    List<AccountsList> ledgerlist=new StaticSentence(m_app.getSession()
    , "select distinct a.tid,(SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS DOPENBALANCE, "+
    " (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='C' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS COPENBALANCE, "+
    " a.date,a.amount,a.transtype,a.transno,a.transref,a.narration,a1.transtype,am.name "+
    "from accountjournal a,accountjournal a1,accountmaster am where a.accountid=? and a1.accountid=am.id and a1.accountid != a.accountid and a.tid=a1.tid and a.transtype!=a1.transtype  and a.date>=? and a.date<=?   order by a.date,a.tid"
    , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadClass(AccountsList.class)).list(new Object[]{sDate,sDate,accid,sDate,eDate});
    return ledgerlist;
    }*///f73138a6-06b7-4032-ac05-af176c6f0bd6

    private List<AccountsList> createLedgerReportforAccount1(Date sDate, Date eDate, String grouptype, String accid) throws BasicException {
        List<AccountsList> datalist = new ArrayList<AccountsList>();
        List<AccountsList> ledgerlist = new StaticSentence(m_app.getSession(), "select  a.tid, " +
                " a.date,a.amount,a.transtype,a.transno,a.transref,a.narration,a1.transtype,a.id " +
                "from accountjournal a join accountjournal a1 on  a.tid=a1.tid and a1.active=true and a.accountid != a1.accountid  and a.transtype!=a1.transtype join accountmaster am on a1.accountid=am.id  where a.accountid=? and a.date>=? and a.date<=? and a.active=true as accr order by a.date,a.tid", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP}), new SerializerReadClass(AccountsList.class)).list(new Object[]{accid, sDate, eDate});
        int i = 0, j = 0;
        int size = ledgerlist.size();
        while (i < size) {
            AccountsList line = ledgerlist.get(i);
            if (i + 1 != size) {
                int flag = 0;
                for (j = i + 1; j < size; j++) {
                    AccountsList line1 = ledgerlist.get(j);

                    if (line.getTid().equals(line1.getTid()) && flag == 0 && line.getID().equals(line1.getID())) {
                        datalist.add(line);
                        flag = 1;
                        i++;
                    } else if ((!line.getTid().equals(line1.getTid()) && flag == 1) || (!line.getID().equals(line1.getID()) && flag == 1)) {
                        i++;
                        break;
                    } else if (line.getTid().equals(line1.getTid()) && flag == 1 && line.getID().equals(line1.getID())) {
                        i++;
                    } else if ((!line.getTid().equals(line1.getTid()) && flag == 0) || (!line.getID().equals(line1.getID()) && flag == 0)) {
                        datalist.add(line);
                        i++;
                        break;
                    }
                }
            } else {
                if (ledgerlist.size() > 0) {
                    datalist.add(line);
                    i++;
                }
            }
        }
        return datalist;
    }
    /*  private List<ReportData> createLedgerReportforAccount(Date sDate,Date eDate,String grouptype,String accid) throws BasicException{

    List<ReportData> ledgerlist=new StaticSentence(m_app.getSession()
    , " SELECT  (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS DOPENBALANCE, "+
    " (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='C' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS COPENBALANCE, "+
    " A2.AMOUNT,A2.TRANSTYPE,A2.TRANSNO,A2.TRANSREF,AM1.NAME,A.TRANSTYPE AS MAINTRANSTYPE,AM.NAME AS MNAME,A.DATE,A2.NARRATION ,"+
    "(SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='D' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS DCLOSEBALANCE, "+
    " (SELECT SUM(A1.AMOUNT) FROM ACCOUNTJOURNAL A1 WHERE A1.TRANSTYPE='C' AND A1.ACCOUNTID=A.ACCOUNTID AND A1.DATE < ?)AS CCLOSEBALANCE "+
    " FROM ACCOUNTJOURNAL A,ACCOUNTJOURNAL A2,ACCOUNTMASTER AM,ACCOUNTMASTER AM1 WHERE A.TID=A2.TID  AND A.ACCOUNTID=AM.ID AND A2.ACCOUNTID=AM1.ID AND A.ACCOUNTID!=A2.ACCOUNTID AND A.ACCOUNTID=? AND A2.DATE >? AND A2.DATE<? AND A.DATE >? AND A.DATE<? ORDER BY AM.NAME,A.DATE "
    , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.STRING,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP})
    , new SerializerReadClass(ReportData.class)).list(new Object[]{sDate,sDate,eDate,eDate,accid,sDate,eDate,sDate,eDate});
    return ledgerlist;
    }*/

    private void getsubaccounts(TrailBalanceData main) throws BasicException {
        List<TrailBalanceData> acclistsubacc = new StaticSentence(m_app.getSession(), " SELECT  DISTINCT AM2.ID,AM2.NAME, AM2.SEARCHKEY,(SELECT SUM(A1.AMOUNT)  FROM ACCOUNTJOURNAL A1 WHERE A1.ACCOUNTID=AM2.ID AND A1.TRANSTYPE='C'AND A1.ACTIVE=TRUE  GROUP BY A1.ACCOUNTID)  ,(SELECT SUM(A1.AMOUNT)  FROM ACCOUNTJOURNAL A1 WHERE A1.ACCOUNTID=AM2.ID AND A1.TRANSTYPE='D' AND A1.ACTIVE=TRUE   GROUP BY A1.ACCOUNTID) ,REPLACE(AM2.LEVEL_,5),AM2.LEVEL_,AM2.SUMMARY" +
                " FROM ACCOUNTMASTER AM2 " +
                " WHERE  AM2.LEVEL_='S' AND AM2.PARENT=? " +
                //  " GROUP BY AM2.ID,AM2.NAME, AM2.SEARCHKEY,AM2.LEVEL_,AM2.SUMMARY,AM2.OPENINGBALANCE"+
                " ORDER BY AM2.NAME ", new SerializerWriteBasic(new Datas[]{Datas.STRING}), new SerializerReadClass(TrailBalanceData.class)).list(new Object[]{main.getSearchkey()});
        //Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP // startdate,enddate,startdate,enddate, //AND A1.DATE>=? AND A1.DATE<=? //AND A1.DATE>=? AND A1.DATE<=?
        if (acclistsubacc.size() > 0) {

            double ctotal = 0.0;
            double dtotal = 0.0;
            int i = 0;
            for (TrailBalanceData temp : acclistsubacc) {
                Double amt = temp.getcredit() - temp.getdebit();
                if (amt > 0) {

                    ctotal += amt;
                    acclistsubacc.get(i).setcredits(amt);
                    acclistsubacc.get(i).setdebits(0.0);
                } else {
                    dtotal += (amt * -1);
                    acclistsubacc.get(i).setdebits(amt * -1);
                    acclistsubacc.get(i).setcredits(0.0);
                }
                i++;
            }
            sacc.addAll(acclistsubacc);
            main.setdebit(dtotal);
            main.setcredit(ctotal);

            if (!acclist.contains(main)) {
                sacc.add(0, main);
            } else {
                int index = acclist.indexOf(main);
                acclist.set(index, main);
            }
            sacc1.addAll(sacc);
            sacc.clear();
        }
    }

    private void breakdown(TrailBalanceData main) throws BasicException {
        List<TrailBalanceData> acclistbreak = new StaticSentence(m_app.getSession(), "  SELECT  AM2.ID,AM2.NAME,AM2.SEARCHKEY, AM2.OPENINGBALANCE,AM2.OPENINGBALANCE,REPLACE(AM2.LEVEL_,3),AM2.LEVEL_,AM2.SUMMARY " +
                " FROM ACCOUNTMASTER AM2 " +
                " WHERE  AM2.LEVEL_='D'  AND AM2.PARENT=?" +
                " ORDER BY AM2.NAME  ", SerializerWriteString.INSTANCE, new SerializerReadClass(TrailBalanceData.class)).list(main.getSearchkey());
        // breakdown();
        for (TrailBalanceData breakd : acclistbreak) {
            acclist.add(breakd);
            getsubaccounts(breakd);
            List<TrailBalanceData> acclistbreak1 = new StaticSentence(m_app.getSession(), "  SELECT  AM2.ID,AM2.NAME,AM2.SEARCHKEY, AM2.OPENINGBALANCE,AM2.OPENINGBALANCE,REPLACE(AM2.LEVEL_,3),AM2.LEVEL_,AM2.SUMMARY " +
                    " FROM ACCOUNTMASTER AM2 " +
                    " WHERE  AM2.LEVEL_='D' AND AM2.PARENT=?" +
                    " ORDER BY AM2.NAME  ", SerializerWriteString.INSTANCE, new SerializerReadClass(TrailBalanceData.class)).list(breakd.getSearchkey());

            for (TrailBalanceData breakd1 : acclistbreak1) {
                breakdown(breakd1);

            }
        }
        getsubaccounts(main);
    }

    private void elements(TrailBalanceData ele) throws BasicException {
        List<TrailBalanceData> acclistele1 = new StaticSentence(m_app.getSession(), "  SELECT  AM2.ID,AM2.NAME,AM2.SEARCHKEY, AM2.OPENINGBALANCE,AM2.OPENINGBALANCE,CASE WHEN AM2.PARENT IS NULL THEN REPLACE(AM2.LEVEL_,1) ELSE REPLACE(AM2.LEVEL_,2) END,AM2.LEVEL_,AM2.SUMMARY " +
                " FROM ACCOUNTMASTER AM2 " +
                " WHERE  AM2.LEVEL_='E' AND AM2.PARENT=?" +
                " ORDER BY AM2.NAME  ", SerializerWriteString.INSTANCE, new SerializerReadClass(TrailBalanceData.class)).list(ele.getSearchkey());

        for (TrailBalanceData ele1 : acclistele1) {
            acclist.add(ele1);
            getsubaccounts(ele1);
            if (sacc1.size() > 0) {
                if (!acclist.contains(ele1) && !sacc2.contains(ele1)) {
                    sacc2.add(ele1);
                } else {
                    if (acclist.contains(ele1)) {
                        int index = acclist.indexOf(ele1);
                        acclist.set(index, ele1);
                    } else if (sacc2.contains(ele1)) {
                        int index = sacc2.indexOf(ele1);
                        sacc2.set(index, ele1);
                    }
                }
                sacc2.addAll(sacc1);

            }
            // acclist.addAll(sacc2);
            sacc.clear();
            sacc1.clear();

            acclist.addAll(sacc2);
            sacc2.clear();
            ///call the same func
            elements(ele1);
            ele.setdebit(ele1.getdebit());
            ele.setcredit(ele1.getcredit());
            int index = acclist.indexOf(ele);
            acclist.set(index, ele);

        }

        List<TrailBalanceData> acclistmain = new StaticSentence(m_app.getSession(), " SELECT  AM2.ID,AM2.NAME, AM2.SEARCHKEY,(SELECT SUM(A1.AMOUNT)  FROM ACCOUNTJOURNAL A1 WHERE A1.ACCOUNTID=AM2.ID AND A1.TRANSTYPE='C' AND A1.ACTIVE=TRUE   GROUP BY A1.ACCOUNTID ) ,(SELECT SUM(A1.AMOUNT)  FROM ACCOUNTJOURNAL A1 WHERE A1.ACCOUNTID=AM2.ID AND A1.TRANSTYPE='D' AND A1.ACTIVE=TRUE   GROUP BY A1.ACCOUNTID) ,REPLACE(AM2.LEVEL_,5),AM2.LEVEL_,AM2.SUMMARY" +
                " FROM ACCOUNTMASTER AM2 " +
                " WHERE  AM2.LEVEL_='C' AND AM2.PARENT=? " +
                " GROUP BY AM2.ID,AM2.NAME, AM2.SEARCHKEY,AM2.LEVEL_,AM2.SUMMARY,AM2.OPENINGBALANCE" +
                " ORDER BY AM2.NAME  ", SerializerWriteString.INSTANCE, new SerializerReadClass(TrailBalanceData.class)).list(ele.getSearchkey());

        //  acclistmain.addAll(acclistmain1);
        //  int i=0;
        for (TrailBalanceData main : acclistmain) {

            if (main.getSummary() == false) {
                Double amt = main.getcredit() - main.getdebit();
                int i = acclistmain.indexOf(main);
                if (amt > 0) {
                    acclistmain.get(i).setcredits(amt);
                    acclistmain.get(i).setdebits(0.0);
                } else {

                    acclistmain.get(i).setdebits(amt * -1);
                    acclistmain.get(i).setcredits(0.0);
                }
            }
            //    i++;
            acclist.add(main);
            breakdown(main);
            // if(main.getdebit()!=null)
            ele.setdebit(main.getdebit());
            // if(main.getcredit()!=null)
            ele.setcredit(main.getcredit());
            //  if(ele.getdebit()!=null && ele.getcredit()!=null){
            //  if(ele.getcredit()>0.0 || ele.getdebit()>0.0){

            if (sacc1.size() > 0) {
                if (!acclist.contains(ele) && !sacc2.contains(ele)) {
                    sacc2.add(ele);
                } else {
                    if (acclist.contains(ele)) {
                        int index = acclist.indexOf(ele);
                        acclist.set(index, ele);
                    } else if (sacc2.contains(ele)) {
                        int index = sacc2.indexOf(ele);
                        sacc2.set(index, ele);
                    }
                }
                sacc2.addAll(sacc1);

            }
            // acclist.addAll(sacc2);
            sacc.clear();
            sacc1.clear();

            acclist.addAll(sacc2);
            sacc2.clear();
        //  }
        //  }
        //   }
        }


    }

    private void createTrailBalanceReport() throws BasicException {
        List<TrailBalanceData> acclistele = new StaticSentence(m_app.getSession(), " SELECT  AM2.ID,AM2.NAME, AM2.SEARCHKEY, AM2.OPENINGBALANCE,AM2.OPENINGBALANCE,CASE WHEN AM2.PARENT IS NULL THEN REPLACE(AM2.LEVEL_,1) ELSE REPLACE(AM2.LEVEL_,2) END ,AM2.LEVEL_,AM2.SUMMARY " +
                " FROM ACCOUNTMASTER AM2 " +
                " WHERE  AM2.LEVEL_='E'AND AM2.PARENT IS NULL " +
                " ORDER BY AM2.NAME ", null, new SerializerReadClass(TrailBalanceData.class)).list();
        for (TrailBalanceData ele : acclistele) {
            acclist.add(ele);
            getsubaccounts(ele);
            if (sacc1.size() > 0) {
                if (!acclist.contains(ele) && !sacc2.contains(ele)) {
                    sacc2.add(ele);
                } else {
                    if (acclist.contains(ele)) {
                        int index = acclist.indexOf(ele);
                        acclist.set(index, ele);
                    } else if (sacc2.contains(ele)) {
                        int index = sacc2.indexOf(ele);
                        sacc2.set(index, ele);
                    }
                }
                sacc2.addAll(sacc1);

            }
            // acclist.addAll(sacc2);
            sacc.clear();
            sacc1.clear();

            acclist.addAll(sacc2);
            sacc2.clear();
            elements(ele);
        }
    /*  if(sacc2.size()>0){
    acclist.addAll(sacc2);
    sacc2.clear();
    }*/
    }

    private List<TrailBalanceData> createTrailBalanceReport1(String sent) throws BasicException {
        //  gfhf

        List<TrailBalanceData> acclistele = new StaticSentence(m_app.getSession(), " SELECT  NAME,SEARCHKEY, CAMOUNT,DAMOUNT,PARENT,LEVEL_,SUMMARY " +
                " FROM ( " + sent +
                ") as ba ORDER BY 5,1 ", null, new SerializerReadClass(TrailBalanceData.class)).list();
        List<TrailBalanceData> list = new ArrayList<TrailBalanceData>();

        list.addAll(acclistele);
        Map<String, TrailBalanceData> tacclist = new HashMap<String, TrailBalanceData>();
        int i = 0;
        for (TrailBalanceData t : acclistele) {
            if (t.getParent() != null) {
                if (t.getcredit() > 0 || t.getdebit() > 0) {
                    int flag = 0;
                    do {
                        TrailBalanceData t1 = tacclist.get(t.getParent());//parent
                        tacclist.remove(t1);
                        int pindex = 0, cnt = 0, nindex = 0;
                        if (flag == 0) {
                            list.remove(t);
                            pindex = list.indexOf(t1);
                            if (t1.getcount() == 0) {
                                cnt = 1;
                            } else {
                                cnt = t1.getcount() + 1;
                            }
                            nindex = pindex + cnt;
                        } else {
                            nindex = list.indexOf(t);
                            list.remove(t);
                            pindex = list.indexOf(t1);
                            cnt = t1.getcount();
                        }
                        flag = 1;
                        if (t.getcredit() > 0) {
                            t1.setcredit(t.getcredit());
                        } else if (t.getdebit() > 0) {
                            t1.setdebit(t.getdebit());
                        }
                        t1.setCount(cnt);
                        list.remove(pindex);
                        list.add(pindex, t1);
                        list.add(nindex, t);
                        tacclist.put(t.getSearchkey(), t);
                        tacclist.put(t1.getSearchkey(), t1);
                        t = new TrailBalanceData();
                        t = t1;
                    } while (t.getParent() != null);

                } else {
                    TrailBalanceData t1 = tacclist.get(t.getParent());//parent
                    tacclist.remove(t1);
                    //  String te=t1.getName();
                    //  String te1=t.getName();
                    list.remove(t);
                    int pindex = list.indexOf(t1);
                    int cnt = t1.getcount() + 1;
                    t1.setCount(cnt);
                    list.remove(pindex);
                    list.add(pindex, t1);
                    list.add(pindex + cnt, t);
                    tacclist.put(t.getSearchkey(), t);
                    tacclist.put(t1.getSearchkey(), t1);
                }
            //    tacclist.put(t.getSearchkey(), t);
            } else {
                tacclist.put(t.getSearchkey(), t);
            }
            t = new TrailBalanceData();
        }

        return list;
    }

    private List<TrailBalanceData> getPresentStatement() throws BasicException {
        String statement = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,(T.OBCREDIT+T.CURCREDIT) AS CAMOUNT,T.OBDEBIT+T.CURDEBIT AS DAMOUNT,AM.PARENT,AM.LEVEL_,AM.SUMMARY " + " FROM ACCOUNTMASTER AM LEFT OUTER JOIN TRAILBALANCE T ON AM.ID=T.ACCOUNTID ORDER BY AM.SEARCHKEY";
        return createTrailBalanceReport1(statement);
    }

    private List<TrailBalanceData> getTrailBalanceofPrevFYear() throws BasicException {
        String statement = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,T.OBCREDIT AS CAMOUNT,T.OBDEBIT AS DAMOUNT,AM.PARENT,AM.LEVEL_,AM.SUMMARY " + " FROM ACCOUNTMASTER AM LEFT OUTER JOIN TRAILBALANCE T ON AM.ID=T.ACCOUNTID ORDER BY AM.SEARCHKEY";
        return createTrailBalanceReport1(statement);
    }

    public static class TrailBalanceData implements SerializableRead {

        private String name;
        private Double credit;
        private Double debit;
        //  private String id;
        private String Searchkey;
        private String parent;
        private String level1;
        private boolean Summary;
        private int count;

        public void readValues(DataRead dr) throws BasicException {
            // id=dr.getString(1);
            name = dr.getString(1);
            Searchkey = dr.getString(2);
            try {
                credit = dr.getDouble(3);
                if (credit == null) {
                    credit = 0.0;
                }
                debit = dr.getDouble(4);
                if (debit == null) {
                    debit = 0.0;
                }
                double temp = credit - debit;
                if (temp < 0) {
                    debit = temp * -1;
                    credit = 0.0;
                } else {
                    credit = temp;
                    debit = 0.0;
                }
                parent = dr.getString(5);
                level1 = dr.getString(6);
                Summary = dr.getBoolean(7);
                count = 0;
            } catch (Exception e) {
            }
        }

        public int getcount() {
            return count;
        }

        public void setCount(int cnt) {
            count = cnt;
        }

        public Boolean getSummary() {
            return Summary;
        }

        public String getLevel1() {
            return level1;
        }

        public String getSearchkey() {
            return Searchkey;
        }

        public Double getcredit() {
            if (credit == null) {
                return 0.0;
            } else {
                return credit;
            }
        }

        public Double getdebit() {
            if (debit == null) {
                return 0.0;
            } else {
                return debit;
            }
        }

        public void setcredit(Double amt) {
            if (credit == null) {
                credit = amt;
            } else {
                credit += amt;
            }
        }

        public void setcredits(Double amt) {
            credit = amt;
        }

        public void setdebits(Double amt) {
            debit = amt;
        }

        public void setdebit(Double amt) {

            if (debit == null) {
                debit = amt;
            } else {
                debit += amt;
            }
        }

        public String getName() {
            return name;
        }

        public String getParent() {
            return parent;
        }
    }

    public static class AccountsList implements SerializableRead {
        //  private Double dobalance;
        //  private Double cobalance;

        private Timestamp date;
        private Double amt;
        private String transtype;
        private String transno;
        private String transref;
        private String narration;
        private String transtype1;
        private String name;
        private String tid;
        private String id;
        private String paymentref;
        private String createdby;
        private String type;
        // private String transno;
        // Formats.DOUBLE.f
        // private List<ReportsData> data;

        public void readValues(DataRead dr) throws BasicException {
            tid = dr.getString(1);
            date = dr.getTimestamp(2);
            amt = dr.getDouble(3);
            transtype = dr.getString(4);
            transno = dr.getString(5);
            transref = dr.getString(6);
            narration = dr.getString(7);
            transtype1 = dr.getString(8);
            name = dr.getString(10);
            id = dr.getString(9);
            paymentref = dr.getString(11);
            createdby = dr.getString(12);
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPaymentRef() {
            return paymentref;
        }

        public String getCreatedBy() {
            return createdby;
        }

        public String getTid() {
            return tid;
        }

        public Timestamp getDate() {
            return date;
        }
        //   public Double getDobalance(){
        //      return dobalance;
        //    }
        //    public Double getCobalance(){
        //     return cobalance;
        //    }

        public String getID() {
            return id;
        }

        public Double getAmt() {
            return amt;
        }

        public String getTranstype() {
            return transtype;
        }

        public String getTransref() {
            FacilityInfo temp = LookupUtilityImpl.getInstance(null).getFacilityMap().get(transref);
            if (temp != null) {
                transref = temp.getName();
            }
            return transref;
        }

        public String getNarration() {
            return narration;
        }

        public String gettranstype1() {
            return transtype1;
        }

        public String getAccountName() {
            return name;
        }

        public void setAccountName(String name) {
            this.name = name;
        }

        public String getTransno() {
            return transno;
        }
    }

    /*   public static class AccountsList implements SerializableRead{
    private Double dopenbal;
    private Double copenbal;
    private String accountid;
    private String name;
    private Double dclosebal;
    private Double cclosebal;
    // private List<ReportsData> data;
    public void readValues(DataRead dr) throws BasicException
    {
    accountid=dr.getString(1);
    dopenbal=dr.getDouble(3);
    copenbal=dr.getDouble(4);
    name=dr.getString(2);
    dclosebal=dr.getDouble(5);
    cclosebal=dr.getDouble(6);
    // data=dr.getDataField()
    }
    public Double getDopenbal(){
    if(dopenbal==null)
    return new Double(0.0);
    else
    return dopenbal;
    }
    public Double getCopenbal(){
    if(copenbal==null)
    return new Double(0.0);
    else
    return copenbal;
    }
    public Double getDclosebal(){
    if(dclosebal==null)
    return new Double(0.0);
    else
    return dclosebal;
    }
    public Double getCclosebal(){
    if(cclosebal==null)
    return new Double(0.0);
    else
    return cclosebal;
    }
    public String getAccountid(){
    return accountid;
    }
    public String getAccountName(){
    return name;
    }
    }*/
    public static class ReportData implements SerializableRead {

        private Double dopeningbal;
        private Double copeningbal;
        private Double amount;
        private String transtype;
        private String transno;
        private String transref;
        private String name;
        private String mtranstype;
        private String mname;
        private Timestamp date;
        private String narration;
        private Double dclosebal;
        private Double cclosebal;
//   private List<AccountReports.ReportData> temp;

        public void readValues(DataRead dr) throws BasicException {
            dopeningbal = dr.getDouble(1);
            copeningbal = dr.getDouble(2);
            amount = dr.getDouble(3);
            transtype = dr.getString(4);
            transno = dr.getString(5);
            transref = dr.getString(6);
            name = dr.getString(7);
            mtranstype = dr.getString(8);
            mname = dr.getString(9);
            date = dr.getTimestamp(10);
            narration = dr.getString(11);
            dclosebal = dr.getDouble(12);
            cclosebal = dr.getDouble(13);
        /*    amount=dr.getDouble(1);
        transtype=dr.getString(2);
        transno=dr.getString(3);
        transref=dr.getString(4);
        name=dr.getString(5);
        mtranstype=dr.getString(6);
        mname=dr.getString(7);
        date=dr.getTimestamp(8);
        narration=dr.getString(9);*/
        //  temp=new ArrayList<AccountReports.ReportData>();
        }

        public Double getdclosebal() {
            return dclosebal;
        }

        public Double getcclosebal() {
            return cclosebal;
        }

        public Timestamp getdate() {
            return date;
        }

        public String getname() {
            return name;
        }

        public String getmname() {
            return mname;
        }

        public Double getDopeningbal() {
            return dopeningbal;
        }

        public Double getcopeningbal() {
            return copeningbal;
        }

        public String gettranstype() {
            return transtype;
        }

        public String getmtranstype() {
            return mtranstype;
        }

        public String gettransref() {
            return transref;
        }

        public Double getamount() {
            return amount;
        }

        public String gettransno() {
            return transno;
        }

        public String getNarration() {
            return narration;
        }
        /*  public List<AccountReports.ReportData> getlistdata(){
        // List<ReportData> list=new ArrayList<ReportData>();
        // list.add(this);
        // temp.add(this);
        return temp;
        }*/
    }

    private Date getDate(String date) throws BasicException {
        Date d = (Date) Formats.TIMESTAMP.parseValue(date);
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        // cal.set(Calendar.AM_PM, Calendar.PM);
        d.setTime(cal.getTimeInMillis());
        return d;
    }

    private Date getSecondDate(String date) throws BasicException {
        Date d = (Date) Formats.TIMESTAMP.parseValue(date);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        // cal.set(Calendar.AM_PM, Calendar.PM);
        d.setTime(cal.getTimeInMillis());
        return d;
    }
    //a.tid in (select a.tid from accountjournal a  join accountmaster am on a.accountid=am.id and  (am.parent='1.1.1' or am.parent='1.1.2') where  a.transref!='Contra' and a.transref !='Cash handover' and a.transref !='Cheque handover')

    public List loadReport(Calendar rscal, Calendar recal, Calendar fscal, Calendar fecal, String accid, List<String> list, String addl) throws BasicException {

        List<AccountsList> datalist = new ArrayList<AccountsList>();
        //Calendar rscalorg=new Date(rscal.getTimeInMillis());
        // Date redate=new Date(recal.getTimeInMillis());
        //  Date fsdate=new Date(fscal.getTimeInMillis());
        //  Date fedate=new Date(fecal.getTimeInMillis());
        //  select   am.name,am.id,sum(a1.amount),a1.transtype
        //             from  aj_apr2009 a join aj_apr2009 a1 on  a.tid=a1.tid and a1.active=true and a.id != a1.id  and a.transtype!=a1.transtype join accountmaster am on am.id=a1.accountid  where a.accountid like 'ce449227%'  and a.active=true and a1.active=true group by am.id,am.name,a1.transtype
        List<Date> values = new ArrayList<Date>();
        String sentence = null;
        if (rscal.before(fscal)) {
            //and a.accountid != a1.accountid  and a.transtype!=a1.transtype
            while (rscal.before(fscal) && rscal.before(recal)) {
                String name = "AJ_" + rscal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + rscal.get(Calendar.YEAR);
                boolean flag = list.contains(name);
                if (flag) {
                    String sent = "select  a.tid as tid, " +
                            " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby" +
                            "from " + name + " a join " + name + " a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype  and a.id != a1.id   join accountmaster am on am.id=a1.accountid  where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true ";
                    if (sentence == null) {
                        sentence = sent;
                    } else {
                        sentence = sentence + " union all " + sent;
                    }
                    // values.add(accid);
                    Date d = new Date(rscal.getTimeInMillis());
                    values.add(d);

                }
                rscal.set(Calendar.DATE, rscal.getActualMaximum(Calendar.DATE));
                if (!rscal.before(fscal)) {
                    rscal.set(Calendar.DATE, fscal.get(Calendar.DATE));
                }
                if (!rscal.before(recal)) {
                    rscal.set(Calendar.DATE, recal.get(Calendar.DATE));
                }
                Date d = new Date(rscal.getTimeInMillis());
                if (flag) {
                    values.add(d);
                }
                rscal.add(Calendar.DATE, 1);
            }
            //case 1
            if (!recal.after(fscal)) {//report end date is before or equals financial year start date
            }//case 2
            else if (recal.after(fscal) && !recal.after(fecal)) {
                //report end date is between financial year start date and financial years end date
                String sent = "select  a.tid as tid, " +
                        " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                        "from accountjournal a join accountjournal a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype   and a.id != a1.id   join accountmaster am on am.id=a1.accountid where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true ";
                if (sentence == null) {
                    sentence = sent;
                } else {
                    sentence = sentence + " union all " + sent;
                }
                Date d = new Date(fscal.getTimeInMillis());

                values.add(d);
                d = new Date(recal.getTimeInMillis());
                values.add(d);

            }//case 3
            else if (rscal.before(fecal)) {
                //report end date is after financial years end date
                String sent = "select  a.tid as tid, " +
                        " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                        "from accountjournal a join accountjournal a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype  and a.id != a1.id  join accountmaster am on am.id=a1.accountid where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true ";
                if (sentence == null) {
                    sentence = sent;
                } else {
                    sentence = sentence + " union all " + sent;
                }
                Date d = new Date(fscal.getTimeInMillis());
                values.add(d);
                d = new Date(fecal.getTimeInMillis());
                values.add(d);
                while (fecal.before(recal)) {
                    String name = "AJ_" + fecal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + fecal.get(Calendar.YEAR);
                    boolean flag = list.contains(name);
                    if (flag) {
                        String sent1 = "select  a.tid as tid, " +
                                " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                                "from " + name + " a join " + name + " a1 on  a.tid=a1.tid and a1.active=true   and a.transtype!=a1.transtype   and a.id != a1.id   join accountmaster am on am.id=a1.accountid  where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true  order by a.date,a.tid";
                        if (sentence == null) {
                            sentence = sent1;
                        } else {
                            sentence = sentence + " union all " + sent1;
                        }
                        //values.add(accid);
                        d = new Date(fecal.getTimeInMillis());
                        values.add(d);

                    }
                    fecal.set(Calendar.DATE, fecal.getActualMaximum(Calendar.DATE));
                    if (!fecal.before(recal)) {
                        fecal.set(Calendar.DATE, recal.get(Calendar.DATE));
                    }

                    d = new Date(fecal.getTimeInMillis());
                    if (flag) {
                        values.add(d);
                    }
                    fecal.add(Calendar.DATE, 1);
                }
            }
        } else {
            if (!recal.after(fecal) && !rscal.before(fscal)) {//if requested period is in between fy start date and fy end date
                String sent = "select  a.tid as tid, " +
                        " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                        "from accountjournal a join accountjournal a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype  and a.id != a1.id   join accountmaster am on am.id=a1.accountid  where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true  ";
                if (sentence == null) {
                    sentence = sent;
                } else {
                    sentence = sentence + " union all " + sent;
                }
                Date d = new Date(rscal.getTimeInMillis());
                values.add(d);
                d = new Date(recal.getTimeInMillis());
                values.add(d);
            } else if (rscal.before(fecal) && !recal.before(fecal)) {
                String sent = "select  a.tid as tid, " +
                        " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                        "from accountjournal a join accountjournal a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype   and a.id != a1.id   join accountmaster am on am.id=a1.accountid  where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true  ";
                if (sentence == null) {
                    sentence = sent;
                } else {
                    sentence = sentence + " union all " + sent;
                }
                Date d = new Date(rscal.getTimeInMillis());
                values.add(d);
                d = new Date(fecal.getTimeInMillis());
                values.add(d);
                while (!recal.before(fecal)) {
                    String name = "AJ_" + fecal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + fecal.get(Calendar.YEAR);
                    boolean flag = list.contains(name);
                    if (flag) {
                        sent = "select  a.tid as tid, " +
                                " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id,am.name as name,a.paymentref as pref,a.createdby as cby " +
                                "from " + name + " a join " + name + " a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype  and a.id != a1.id   join accountmaster am on am.id=a1.accountid where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true ";
                        if (sentence == null) {
                            sentence = sent;
                        } else {
                            sentence = sentence + " union all " + sent;
                        }
                        d = new Date(fecal.getTimeInMillis());
                        values.add(d);

                    }
                    fecal.set(Calendar.DATE, fecal.getActualMaximum(Calendar.DATE));
                    if (!fecal.before(recal)) {
                        fecal.set(Calendar.DATE, recal.get(Calendar.DATE));
                    }
                    d = new Date(fecal.getTimeInMillis());
                    if (flag) {
                        values.add(d);
                    }
                    fecal.add(Calendar.DATE, 1);
                }
            } else if (!rscal.before(fecal)) {
                while (!rscal.after(recal)) {
                    String name = "AJ_" + rscal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + rscal.get(Calendar.YEAR);
                    boolean flag = list.contains(name);
                    if (flag) {
                        String sent = "select  a.tid as tid, " +
                                " a.date as date,a.amount as amount,a.transtype as transtype,a.transno as transno,a.transref as transref,a.narration as narration,a1.transtype as transtype1,a.id as id ,am.name as name,a.paymentref as pref,a.createdby as cby " +
                                "from " + name + " a join " + name + " a1 on  a.tid=a1.tid and a1.active=true  and a.transtype!=a1.transtype   and a.id != a1.id   join accountmaster am on am.id=a1.accountid  where a.accountid=? and a.date>=? and a.date<=? " + addl + " and a.active=true and a1.active=true ";
                        if (sentence == null) {
                            sentence = sent;
                        } else {
                            sentence = sentence + " union all " + sent;
                        }
                        Date d = new Date(rscal.getTimeInMillis());
                        values.add(d);

                    }
                    rscal.set(Calendar.DATE, rscal.getActualMaximum(Calendar.DATE));
                    if (!rscal.before(recal)) {
                        rscal.set(Calendar.DATE, recal.get(Calendar.DATE));
                    }
                    Date d = new Date(rscal.getTimeInMillis());
                    if (flag) {
                        values.add(d);
                    }
                    rscal.add(Calendar.DATE, 1);
                }
            }


        }
        Object[] params = new Object[values.size() + values.size() / 2];
        Datas[] data = new Datas[values.size() + values.size() / 2];
        int k = 0;
        for (int i = 0; i < values.size(); i++) {
            data[k] = Datas.STRING;
            params[k++] = accid;
            data[k] = Datas.TIMESTAMP;
            params[k++] = values.get(i++);
            data[k] = Datas.TIMESTAMP;
            params[k++] = values.get(i);
        }
        List<AccountsList> ledgerlist = new StaticSentence(m_app.getSession(), "SELECT TID,DATE,AMOUNT,TRANSTYPE,TRANSNO,TRANSREF,NARRATION,TRANSTYPE1,ID,NAME,pref,cby FROM ( " + sentence + ") AS ACCRP ORDER BY 2,1", new SerializerWriteBasic(data), new SerializerReadClass(AccountsList.class)).list(params);
        int i = 0, j = 0, s = 0;
        int size = ledgerlist.size();
        List<String> idlist = new ArrayList<String>();


        for (AccountsList acc : ledgerlist) {
            if (!idlist.contains(acc.getID())) {
                datalist.add(acc);
                idlist.add(acc.getID());
            }
        }
        /*   while(i<size){
        AccountsList line=ledgerlist.get(i);
        if(i+1!=size){
        s=1;
        int flag=0;
        for(j=i+1;j<size;j++){
        AccountsList line1=ledgerlist.get(j);
        if(!line.getTid().equals(line1.getTid()) && flag==0 ){
        datalist.add(line);
        flag=1;
        i++;
        break;
        }else
        if(line.getTid().equals(line1.getTid()) && flag==0 && !line.getID().equals(line1.getID())){
        datalist.add(line);
        flag=1;
        i++;
        // break;
        //if(line.getTid().equals(line1.getTid()) && flag==1 && !line.getID().equals(line1.getID()))
        }else {
        i++;
        break;
        }

        }
        }else{
        if(ledgerlist.size()>0 && s==0){
        datalist.add(line);
        i++;
        }else{
        AccountsList line1=ledgerlist.get(i-1);
        if(!line.getTid().equals(line1.getTid())){
        datalist.add(line);
        i++;
        break;
        }else if(line.getTid().equals(line1.getTid()) && line.getID().equals(line1.getID())){
        datalist.add(line);
        i++;
        }else {
        i++;
        break;
        }
        }
        }
        }*/
        return datalist;
    }

    private List<TrailBalanceData> getStatement(Calendar fscal, Calendar fecal, Calendar recal, Calendar recal1) throws BasicException {
        String statement = null, sent = null;
        /*" SELECT  AM2.ID,AM2.NAME, AM2.SEARCHKEY, (SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND TRANSTYPE='C' AND ACTIVE=TRUE),(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND TRANSTYPE='D' AND ACTIVE=TRUE),AM2.PARENT ,AM2.LEVEL_,AM2.SUMMARY "+
        " FROM ACCOUNTMASTER AM2 "+
        " ORDER BY AM2.PARENT,AM2.NAME "
         * */
        List<Date> list = new ArrayList<Date>();
        if (!recal.after(fscal)) {
            Date d = new Date(recal.getTimeInMillis());
            sent = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,A.CREDIT AS CAMOUNT,A.DEBIT AS DAMOUNT,AM.PARENT AS PARENT ,AM.LEVEL_ AS LEVEL_,AM.SUMMARY AS SUMMARY FROM ACCOUNTMASTER AM LEFT OUTER JOIN AJPERIODTOTALS A ON AM.ID=A.ACCOUNTID AND A.EDATE< ?";
            if (statement == null) {
                statement = sent;
            } else {
                statement = statement + " union all " + sent;
            }
            list.add(d);
            if (recal1.get(Calendar.DATE) != recal1.getActualMaximum(Calendar.DATE)) {
                String name = "AJ_" + recal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + recal.get(Calendar.YEAR);
                String sent1 = " SELECT  AM2.NAME AS NAME,AM2.SEARCHKEY AS SEARCHKEY, (SELECT SUM(AMOUNT) FROM " + name + " WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='C' AND ACTIVE=TRUE) AS CAMOUNT,(SELECT SUM(AMOUNT) FROM " + name + " WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='D' AND ACTIVE=TRUE) AS DAMOUNT,AM2.PARENT AS PARENT ,AM2.LEVEL_ AS LEVEL_,AM2.SUMMARY AS SUMMARY " +
                        " FROM ACCOUNTMASTER AM2 " +
                        " ORDER BY AM2.PARENT,AM2.NAME ";
                if (statement == null) {
                    statement = sent1;
                } else {
                    statement = statement + " union all " + sent1;
                }
                //d=new Date(recal);
                list.add(d);
                list.add(d);
            }
        } else if (recal.after(fscal) && !recal.after(fscal)) {
            Date d = new Date(fscal.getTimeInMillis());
            sent = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,A.OBCREDIT AS CAMOUNT,A.OBDEBIT AS DAMOUNT,AM.PARENT AS PERANT,AM.LEVEL_ AS LEVEL_,AM.SUMMARY AS SUMMARY FROM ACCOUNTMASTER AM LEFT OUTER JOIN TRAILBALANCE A ON AM.ID=A.ACCOUNTID ";
            if (statement == null) {
                statement = sent;
            } else {
                statement = statement + " union all " + sent;
            }
            // list.add(d);
            String sent1 = " SELECT  AM2.NAME AS NAME,AM2.SEARCHKEY AS SEARCHKEY, (SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='C' AND ACTIVE=TRUE) AS CAMOUNT,(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='D' AND ACTIVE=TRUE) AS DAMOUNT,AM2.PARENT AS PARENT,AM2.LEVEL_ AS LEVEL_,AM2.SUMMARY AS SUMMARY " +
                    " FROM ACCOUNTMASTER AM2 " +
                    " ORDER BY AM2.PARENT,AM2.NAME ";
            Date d1 = new Date(recal.getTimeInMillis());
            if (statement == null) {
                statement = sent1;
            } else {
                statement = statement + " union all " + sent1;
            }
            list.add(d1);
            list.add(d1);
        } else if (recal.after(fecal)) {
            //Date d=new Date(fscal.getTimeInMillis());
            sent = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,A.OBCREDIT+A.CURCREDIT AS CAMOUNT,A.OBDEBIT+A.CURDEBIT AS DAMOUNT,AM.PARENT AS PARENT,AM.LEVEL_ AS LEVEL_,AM.SUMMARY AS SUMMARY FROM ACCOUNTMASTER AM LEFT OUTER JOIN AJPERIODTOTALS A ON AM.ID=A.ACCOUNTID ";
            if (statement == null) {
                statement = sent;
            } else {
                statement = statement + " union all " + sent;
            }
            //  list.add(d);
          /* String sent1=" SELECT  AM2.NAME AS NAME,AM2.SEARCHKEY AS SEARCHKEY, (SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='C' AND ACTIVE=TRUE) AS CAMOUNT,(SELECT SUM(AMOUNT) FROM ACCOUNTJOURNAL WHERE ACCOUNTID=AM2.ID AND DATE<? AND TRANSTYPE='D' AND ACTIVE=TRUE) AS DAMOUNT,AM2.PARENT AS PARENT,AM2.LEVEL_ AS LEVEL_,AM2.SUMMARY AS SUMMARY "+
            " FROM ACCOUNTMASTER AM2 "+
            " ORDER BY AM2.PARENT,AM2.NAME ";
            Date d3=new Date(recal.getTimeInMillis());
            if(statement==null)
            statement=sent1;
            else
            statement=statement+" union all "+sent1;
            list.add(d3);
            list.add(d3);*/
            //while(){
            sent = "SELECT AM.NAME AS NAME,AM.SEARCHKEY AS SEARCHKEY,A.CREDIT AS CAMOUNT,A.DEBIT AS DAMOUNT,AM.PARENT AS PARENT ,AM.LEVEL_ AS LEVEL_,AM.SUMMARY AS SUMMARY FROM ACCOUNTMASTER AM LEFT OUTER JOIN AJPERIODTOTALS A ON AM.ID=A.ACCOUNTID AND A.EDATE > ? AND A.EDATE< ?";
            if (statement == null) {
                statement = sent;
            } else {
                statement = statement + " union all " + sent;
            }
            Date d2 = new Date(fecal.getTimeInMillis());
            list.add(d2);
            Date d1 = new Date(recal.getTimeInMillis());
            list.add(d1);
        //}
        }
        //   fjg
        //return statement;
        return createTrailBalanceReport1(statement);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        reportType = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        GroupingType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fromdate = new javax.swing.JTextField();
        toDate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        account = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setText("Report Type");

        reportType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ledger Individual", "Ledger All" }));
        reportType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                reportTypeItemStateChanged(evt);
            }
        });

        jLabel2.setText("Grouping Type");

        jLabel3.setText("From Date");

        jLabel4.setText("To Date");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        jButton3.setText("Execute");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel5.setText("Account");

        account.setEditable(true);

        jCheckBox1.setText("Yes");

        jLabel6.setText("Narration");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(GroupingType, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(reportType, javax.swing.GroupLayout.Alignment.LEADING, 0, 209, Short.MAX_VALUE)
                            .addComponent(account, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fromdate, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(toDate, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(reportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(fromdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(GroupingType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(toDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(account, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jCheckBox1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (fromdate.getText().isEmpty() == true && toDate.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(null, "Please Enter both start an end date");

        } else if (fromdate.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(null, "Please Enter Start Date");
        } else if (toDate.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(null, "Please Enter End Date");
        } else if (fromdate.getText().isEmpty() == false && toDate.getText().isEmpty() == false) {
            w = new waitDialog(new JFrame(), true);
            int h = w.getSize().height;
            int w1 = w.getSize().width;
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension scrnsize = toolkit.getScreenSize();
            w.setLocation(scrnsize.width / 2 - w1, scrnsize.height / 2 - h);

            Thread t = new Thread(
                    new Runnable() {

                        public void run() {
                            try {
                                AccountMaster accm = (AccountMaster) account.getSelectedItem();
                                String reporttype = String.valueOf(reportType.getSelectedItem());
                                loadReport(fromdate.getText(), toDate.getText(), accm, null, reporttype, true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            //JOptionPane.showMessageDialog(JRViewer300.this, getBundleString("error.printing"));
                            }
                        }
                    });
            t.start();
            w.showDialog("Please wait.Loading Report...");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private JRField[] getFields() throws JRException, UnsupportedOperationException {
        JRField[] fields = new JRField[11];
        fields[0] = (JRField) new JRBasicField("DOPENINGBAL", "dopeningbal", java.lang.Double.class, "java.lang.Double");
        fields[1] = (JRField) new JRBasicField("COPENINGBAL", "copeningbal", java.lang.Double.class, "java.lang.Double");
        fields[2] = (JRField) new JRBasicField("AMOUNT", "amount", java.lang.Double.class, "java.lang.Double");
        fields[3] = (JRField) new JRBasicField("TRANSTYPE", "transtype", java.lang.String.class, "java.lang.String");
        fields[4] = (JRField) new JRBasicField("TRANSNO", "transno", java.lang.String.class, "java.lang.String");
        fields[5] = (JRField) new JRBasicField("TRANSREF", "transref", java.lang.String.class, "java.lang.String");
        fields[6] = (JRField) new JRBasicField("Name", "name", java.lang.String.class, "java.lang.String");
        fields[7] = (JRField) new JRBasicField("MTRANSTYPE", "mtranstype", java.lang.String.class, "java.lang.String");
        fields[8] = (JRField) new JRBasicField("Mem Name", "mname", java.lang.String.class, "java.lang.String");
        fields[9] = (JRField) new JRBasicField("Date", "date", java.util.Date.class, "java.util.Date");
        fields[10] = (JRField) new JRBasicField("NARRATION", "narration", java.lang.String.class, "java.lang.String");
        return fields;
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Date date;
        try {
            date = (Date) Formats.DATE.parseValue(fromdate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendar(this, date);
        if (date != null) {
            fromdate.setText(Formats.DATE.formatValue(date));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Date date;
        try {
            date = (Date) Formats.DATE.parseValue(toDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendar(this, date);
        if (date != null) {
            toDate.setText(Formats.DATE.formatValue(date));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void reportTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_reportTypeItemStateChanged
        // TODO add your handling code here:
        if (reportType.getSelectedIndex() == 1) {
            account.setVisible(false);
            jLabel5.setVisible(false);
        //jButton1.setEnabled(false);
        //fromdate.setText(null);
        } else {
            account.setVisible(true);
            jLabel5.setVisible(true);
        }
    //Sanjeev:commentedabove 2 lines and below else block
        /*else
    {
    jButton1.setEnabled(true);}*/
    }//GEN-LAST:event_reportTypeItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox GroupingType;
    private javax.swing.JComboBox account;
    private javax.swing.JTextField fromdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox reportType;
    private javax.swing.JTextField toDate;
    // End of variables declaration//GEN-END:variables
}
