/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.format.Formats;
import com.openbravo.pos.clubmang.GeneralSettingInfo;
import com.openbravo.pos.clubmang.GeneralSettingsAccounting;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.LookupUtilityImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author swathi
 */
public class CloseFinancialYear {

    public static void close(AppView app) throws BasicException {

        // get start and end date  of financial year
        Map<String, GeneralSettingInfo> settings = LookupUtilityImpl.getInstance(null).getGeneralSettingsMap();
        Date sdate = (Date) Formats.DATE.parseValue(settings.get("Datestart").getValue());
        Date edate = (Date) Formats.DATE.parseValue(settings.get("Dateend").getValue());
        Date osdate = (Date) Formats.DATE.parseValue(settings.get("Datestart").getValue());
        Date oedate = (Date) Formats.DATE.parseValue(settings.get("Dateend").getValue());
        Calendar ocal = Calendar.getInstance();
        ocal.setTimeInMillis(osdate.getTime());
        Calendar ocal1 = Calendar.getInstance();
        ocal1.setTimeInMillis(oedate.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(sdate.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(cal.getTimeInMillis());


        if (ocal1.get(Calendar.MONTH) == cal1.get(Calendar.MONTH)) {
            cal1.set(Calendar.DATE, ocal1.get(Calendar.DATE));
            cal1.add(Calendar.DATE, 1);
        } else {
            cal1.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            cal1.add(Calendar.DATE, 1);
        }
        sdate.setTime(cal.getTimeInMillis());
        edate.setTime(cal1.getTimeInMillis());
        TransferAndUpdate(app, cal, cal1, ocal1, sdate, edate);

    }

     public static void TransferAndUpdate(AppView app, Calendar cal, Calendar cal1, Calendar ocal1, Date sdate, Date edate) throws BasicException {
     TransferAndUpdate( app.getSession(),  cal,  cal1,  ocal1,  sdate,  edate);
     }

    public static void TransferAndUpdate(Session session, Calendar cal, Calendar cal1, Calendar ocal1, Date sdate, Date edate) throws BasicException {
        updateAjunadjusted(session);
        while (!cal.after(ocal1)) {
            String name = "AJ_" + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK).toUpperCase() + cal.get(Calendar.YEAR);
            int count = 0;
            //to check if entries for this period exist in the live AJ table
            Object[] countobj = (Object[]) new PreparedSentence(session, "SELECT COUNT(*) FROM ACCOUNTJOURNAL WHERE DATE>=? AND DATE<?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.INT})).find(new Object[]{sdate, edate});
            if (countobj != null && countobj[0] != null) {
                count = Integer.parseInt(countobj[0].toString());
            }
            if (count > 0) {
                try {
                    //to check if the table exist
                    new PreparedSentence(session, "SELECT COUNT(*) FROM " + name, null, new SerializerReadBasic(new Datas[]{Datas.INT})).find();
                    TransferDataToRespectiveTables(name, session, sdate, edate);
                } catch (Exception e) {
                    //e.printStackTrace();
                    new PreparedSentence(session, "CREATE TABLE " + name + "(ID VARCHAR(255),MEMID VARCHAR(255) ,DATE TIMESTAMP NOT NULL,TRANSREF VARCHAR(255) NOT NULL,"+
                                      "TRANSNO VARCHAR(255) NOT NULL,AMOUNT DOUBLE NOT NULL,DUEDATE TIMESTAMP,CLEARDATE TIMESTAMP ,BALANCEAMOUNT DOUBLE,"+
                                      "ADJUSTED BOOLEAN ,CREATEDBY VARCHAR(255) NOT NULL,COUNTER VARCHAR(255) NOT NULL,NARRATION VARCHAR(255) NOT NULL,"+
                                       "ACCOUNTID VARCHAR(255) NOT NULL,TID VARCHAR(255) NOT NULL,DATEOFENTRY TIMESTAMP ,TRANSTYPE VARCHAR(255),"+
                                      "PAYMENTREF VARCHAR(255),DEACTDATE TIMESTAMP,DEACTBY VARCHAR(255),ACTIVE BOOLEAN,DEACTREF VARCHAR(255),PRIMARY KEY(ID))", null).exec();
                    //CREATE  INDEX  ACC_INX_1 ON ACCOUNTJOURNAL (ACCOUNTID,DATE)
                    new PreparedSentence(session, "CREATE  INDEX  ACC_INX_" + name + " ON " + name + " (ACCOUNTID,DATE)", null).exec();
                    TransferDataToRespectiveTables(name, session, sdate, edate);
                     e.printStackTrace();

                }
                cal1.add(Calendar.DATE, -1);

                Date edatetemp = new Date(cal1.getTimeInMillis());
                System.out.println(edatetemp.toString()+" , " +edatetemp);
                new PreparedSentence(session, "DELETE FROM AJPERIODTOTALS WHERE EDATE=?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP})).exec(new Object[]{edatetemp});
                new PreparedSentence(session, "INSERT INTO AJPERIODTOTALS (ID,ACCOUNTID,EDATE,DEBIT,CREDIT,CURDEBIT,CURCREDIT) SELECT CONCAT(A.ID,?),A.ID,?,(SELECT COALESCE(SUM(A1.AMOUNT),0.0) FROM " + name + " A1  WHERE A1.ACCOUNTID=A.ID AND A1.TRANSTYPE='D' AND A1.ACTIVE=TRUE ) " +
                        ",(SELECT COALESCE(SUM(A1.AMOUNT),0.0) FROM " + name + " A1 WHERE A1.ACCOUNTID=A.ID AND A1.TRANSTYPE='C'  AND A1.ACTIVE=TRUE ),0.0,0.0  FROM ACCOUNTMASTER A", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.TIMESTAMP})).exec(new Object[]{edatetemp.toString(), edatetemp});

            }
            cal.setTimeInMillis(edate.getTime());
            //cal.add(Calendar.DATE, 1);
            sdate.setTime(cal.getTimeInMillis());
            cal1.setTime(cal.getTime());

            cal1.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            cal1.add(Calendar.DATE, 1);
            //  }
            edate.setTime(cal1.getTimeInMillis());

        }
    }

    private static void TransferDataToRespectiveTables(String tablename, Session session, Date sdate, Date edate) throws BasicException {
        new PreparedSentence(session, "DELETE FROM " + tablename + " WHERE DATE >= ? AND DATE < ?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP})).exec(new Object[]{sdate, edate});
        new PreparedSentence(session, "INSERT INTO " + tablename + "  SELECT * FROM ACCOUNTJOURNAL A1  WHERE A1.DATE >= ? AND A1.DATE < ? ", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP})).exec(new Object[]{sdate, edate});
        new PreparedSentence(session, "DELETE FROM ACCOUNTJOURNAL WHERE DATE >= ? AND DATE < ?", new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP, Datas.TIMESTAMP})).exec(new Object[]{sdate, edate});

    }

    private static void updateAJPeriodTotals(double debit, double credit, Session session, String acc, Date edate) throws BasicException {
        // Formats.ConvertDoubleToString(debit)
        if (new PreparedSentence(session, "UPDATE AJPERIODTOTALS SET DEBIT=?,CREDIT=? WHERE ACCOUNTID=? AND EDATE=?", new SerializerWriteBasic(new Datas[]{Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.TIMESTAMP})).exec(new Object[]{Formats.ApproxTo2Decimals(debit), Formats.ApproxTo2Decimals(credit), acc, edate}) <= 0) {
            new PreparedSentence(session, "INSERT INTO AJPERIODTOTALS (ID,ACCOUNTID,EDATE,DEBIT,CREDIT,CURDEBIT,CURCREDIT) VALUES (?,?,?,?,?,?,?) ", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE})).exec(new Object[]{UUID.randomUUID().toString(), acc, edate, Formats.ApproxTo2Decimals(debit), Formats.ApproxTo2Decimals(credit), 0.0, 0.0});

        }
    }

    private static void createnewrow(Session session, String acc, double dtotal, double ctotal, Date edate) throws BasicException {

        new PreparedSentence(session, "INSERT INTO AJPERIODTOTALS (ID,ACCOUNTID,EDATE,DEBIT,CREDIT) VALUES (?,?,?,?,?) ", new SerializerWriteBasic(new Datas[]{Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE, Datas.DOUBLE})).exec(new Object[]{UUID.randomUUID().toString(), acc, edate, dtotal, ctotal});

    }

    private static void updateAjunadjusted(Session session){
       try{
       new PreparedSentence(session, "DELETE FROM AJUNADJUSTED " , null).exec();
       } catch(Exception e){
         e.printStackTrace();
       }

    }
}
