

package com.openbravo.pos.sales;

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.ticket.TicketInfo;
//import java.sql.Timestamp;
//import java.util.Date;
import java.util.UUID;

/**
 *
 * @author adrianromero
 */
public class DataLogicReceipts extends BeanFactoryDataSingle {
    
    private Session s;
    
    /** Creates a new instance of DataLogicReceipts */
    public DataLogicReceipts() {
    }
    
    public void init(Session s){
        this.s = s;
    }
     
    public final TicketInfo getSharedTicket(String Id,String counter) throws BasicException {
        
        if (Id == null) {
            return null; 
        } else {
            Object[]record = (Object[]) new StaticSentence(s
                    , "SELECT CONTENT FROM SHAREDTICKETS WHERE CID = ? AND COUNTER=?"
                    , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING})
                    , new SerializerReadBasic(new Datas[] {Datas.SERIALIZABLE})).find(new Object[]{Id,counter});
            return record == null ? null : (TicketInfo) record[0];
           // return (TicketInfo) record[0];
        }
    } 
    
    public final List<SharedTicketInfo> getSharedTicketList( String counter) throws BasicException {
        
        return (List<SharedTicketInfo>) new StaticSentence(s
                , "SELECT CID, NAME FROM SHAREDTICKETS WHERE COUNTER=? ORDER BY NAME"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(SharedTicketInfo.class)).list(counter);
    }
    
    public final void updateSharedTicket(final String id, final TicketInfo ticket,String counter) throws BasicException {
         
        Object[] values = new Object[] {id, ticket.getCustomer().getSearchkey(), ticket,counter};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE,Datas.STRING};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET NAME = ?, CONTENT = ? WHERE CID = ? AND COUNTER=?"
                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 0,3})).exec(values);

    }
  /* public final void updateLastQtTimeOfSharedTicket(Date date,final String id,String counter) throws BasicException {

        Object[] values = new Object[] {date,id, counter};
        Datas[] datas = new Datas[] {Datas.TIMESTAMP, Datas.STRING,Datas.STRING};
        new PreparedSentence(s
                , "UPDATE SHAREDTICKETS SET LASTQTTIME = ? WHERE CID = ? AND COUNTER = ?"
                , new SerializerWriteBasicExt(datas, new int[] { 0, 1, 2})).exec(values);

    }*/

     public final int checkSharedTicket(final String id, final TicketInfo ticket,final String counter) throws BasicException {

     //   Object[] values = new Object[] {id, ticket.getCustomer().getSearchkey(), ticket,counter};
     //   Datas[] datas = new Datas[] {Datas.STRING, Datas.STRING, Datas.SERIALIZABLE,Datas.STRING};
       int count=0;
       Object[] o= (Object[])new  StaticSentence(s
            , "SELECT ID FROM SHAREDTICKETS WHERE CID=? AND COUNTER=?"

            , new SerializerWriteBasic(new Datas[] {Datas.STRING,Datas.STRING})
            ,new SerializerReadBasic(new Datas[] {Datas.STRING})).find(new Object[]{id,counter});
       if(o!=null)
           return o.length;
       return count;
    }
    
    public final void insertSharedTicket(final String id, final TicketInfo ticket,final String counter) throws BasicException {
        
        Object[] values = new Object[] {UUID.randomUUID().toString(),id, ticket.getCustomer().getSearchkey(), ticket,counter};
        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING, Datas.STRING, Datas.SERIALIZABLE,Datas.STRING};
        
        new PreparedSentence(s
            , "INSERT INTO SHAREDTICKETS (ID,CID, NAME,CONTENT,COUNTER) VALUES (?,?, ?, ?,?)"
            , new SerializerWriteBasicExt(datas, new int[] {0, 1, 2,3,4})).exec(values);
    }
    
    public final void deleteSharedTicket(final String id,final String counter) throws BasicException {

        Object[] values = new Object[] {id, counter};
        Datas[] datas = new Datas[] {Datas.STRING,Datas.STRING};
        new StaticSentence(s
            , "DELETE FROM SHAREDTICKETS WHERE CID = ? AND COUNTER = ?"
            , new SerializerWriteBasicExt(datas,new int[]{0,1})).exec(values);
    }    
}
