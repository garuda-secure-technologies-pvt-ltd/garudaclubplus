/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.sms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.util.ICallBack;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Akash
 */
public class SMSgeneralDBSettings extends BeanFactoryDataSingle
{
    private Session session;
    public static final String SMS_QT_ID =  "sms-101";
    public static final String SMS_BILL_ID =  "sms-102";
    public static final String SMS_ACCOUNT_ID =  "sms-103";
    public static final String SMS_GUEST_ID =  "sms-104";
    public static final String SMS_SHARED_TKT_ID =  "sms-105";
    
    public static final String SMS_QT_NAME =  "Send SMS while creatig QT ? ";
    public static final String SMS_BILL_NAME =  "Send SMS while creatig Bill ?";
    public static final String SMS_ACCOUNT_NAME =  "Send SMS while creatig Account ?";
    public static final String SMS_GUEST_NAME =  "Send SMS while creatig Guest Charges ?";
    public static final String SMS_SHARED_TKT_NAME =  "Send SMS while creatig Shared Ticket ?";
    
    
    public static final String MESSAGE_QT_KEY =  "General message for QT";
    public static final String MESSAGE_BILL_KEY =  "General message for BILL";
    public static final String MESSAGE_ACCOUNT_KEY =  "General message for Account";
    public static final String MESSAGE_GUEST_KEY =  "General message for Guest charges";
    public static final String MESSAGE_SHARED_TKT_KEY =  "General message for Shared ticket";
    
    // GENERIC IDS FOR MESSAGE MASTER
    public static final String MESSAGE_QT_ID =  "sms-201";
    public static final String MESSAGE_BILL_ID =  "sms-202";
    public static final String MESSAGE_ACCOUNT_ID =  "sms-203";
    public static final String MESSAGE_GUEST_ID =  "sms-204";
    public static final String MESSAGE_SHARED_TKT_ID =  "sms-205";
    
    public static final String SMS_QT_KEY = " ###QTNO### ";
    public static final String SMS_BILL_KEY = " ###BILLNO### ";
    public static final String SMS_DTM_KEY = " ###DTM### ";
    public static final String SMS_FACILITY_KEY = " ###FCLTNO### ";
    public static final String SMS_ACCOUNT_KEY = " ###ACTNO### ";
    public static final String SMS_GUEST_KEY = " ###GUESTCHRGNO### ";
    
    
    @Override
    public void init(Session s) 
    {
        this.session = s;
    }
    
    
    
    public boolean getSMSvalue(String id)
    {
        Object[] obj;
        try 
        {
            obj = (Object[]) new StaticSentence(session, "SELECT VALUE FROM GENERALTABLE WHERE  ID=? ", SerializerWriteString.INSTANCE , new SerializerReadBasic(new Datas[]{Datas.INT}) ).find(id);
            if((obj != null) && obj[0] != null  && (int)obj[0] == 1) 
            {
                return true;
            }
            else 
                return false;
        } 
        catch (BasicException ex) 
        {
            Logger.getLogger(SMSgeneralDBSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public String getMessage(String id)
    {
        Object[] obj;
        try 
        {
            obj = (Object[]) new StaticSentence(session, "SELECT VALUE FROM GENERALTABLE WHERE  ID=? ", SerializerWriteString.INSTANCE , new SerializerReadBasic(new Datas[]{Datas.STRING}) ).find(id);
            if((obj != null) && obj[0] != null) 
            {
                return obj[0].toString();
            }
        } 
        catch (BasicException ex) 
        {
            Logger.getLogger(SMSgeneralDBSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
   
    
    public void setSMSflag(final String id ,final String name,final String value,final String massageID,final  String smsName,final String messageValue, final ICallBack iCallback)
    {
        try 
        {
            Transaction t = new Transaction(session) 
            {
            //  boolean error=false;
                public Object transact() throws BasicException {

                // query to store if need to send SMS or not
                if( new PreparedSentence(session
                        , "UPDATE GENERALTABLE SET VALUE=? WHERE ID=?"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING}))
                        .exec(new Object[]{value,id}) <= 0)
                {

                    new PreparedSentence(session
                            , "INSERT INTO GENERALTABLE(ID,NAME,VALUE) VALUES(?,?,?)"
                            , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING}))
                            .exec(new Object[]{id,name,value});

                }

                // query to store SMS string
                if( new PreparedSentence(session
                        , "UPDATE GENERALTABLE SET VALUE=? WHERE ID=?"
                        , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING}))
                        .exec(new Object[]{messageValue,massageID}) <= 0)
                {

                    new PreparedSentence(session
                            , "INSERT INTO GENERALTABLE(ID,NAME,VALUE) VALUES(?,?,?)"
                            , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING}))
                            .exec(new Object[]{massageID,smsName,messageValue});

                }
                iCallback.onSuccess();
                return null;

                }
            };
            t.execute();
        }
        catch (BasicException ex) 
        {
            iCallback.onError();
            Logger.getLogger(SMSgeneralDBSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
