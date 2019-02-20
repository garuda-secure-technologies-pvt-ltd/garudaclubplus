package com.openbravo.pos.sales;


import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteBuilder;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.LookupUtilityImpl;
import com.openbravo.pos.mant.FloorsInfo;
//import com.openbravo.pos.sales.BillInfo;
//import com.openbravo.pos.sales.BillLineInfo;
import com.openbravo.pos.sales.restaurant.CounterTotals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swathi
 */
public class BillLogic extends BeanFactoryDataSingle{
    private Session s;
    private DataLogicSales dlSales;
    private AppView appView;
    private FloorsInfo lb;
    @Override
    public void init(Session s) {
        this.s = s;
    }
    
    public BillLogic() {
        
    }
    
    public void setDataLogicSales(DataLogicSales dls) {
        this.dlSales = dls;
    }

    public void setAppView(AppView app) {
        this.appView = app;
    }

    public AppView getAppView() {
        return appView;
    }
     public final List<BillInfo> getBillList1() throws BasicException {

         return (List<BillInfo>) new StaticSentence(s
                , "SELECT BILL.ID, BILL.CUSTOMER, BILL.WAITER, BILL.PLACE, FLOORS.NAME, BILL.AMOUNT, BILL.CREATEDBY, BILL.CREATEDDATE, BILL.PAID, BILL.RECEIPT FROM BILL,FLOORS WHERE  BILL.FLOOR=FLOORS.ID AND BILL.PAID=FALSE ORDER BY BILL.CREATEDDATE,BILL.ID,BILL.CREATEDBY"
                , null
                , new SerializerReadClass(BillInfo.class)).list();
    }
    public final List<CounterTotals> getPendingBillTotal() throws BasicException {

         return (List<CounterTotals>) new StaticSentence(s
                , "SELECT SUM(BILL.AMOUNT),ROLES.NAME FROM BILL,ROLES,PEOPLE WHERE   BILL.PAID=FALSE AND BILL.CREATEDBY=PEOPLE.NAME AND PEOPLE.ROLE=ROLES.ID GROUP BY ROLES.ID,ROLES.NAME "
                , null
                , new SerializerReadClass(CounterTotals.class)).list();
    }
    public final List<BillInfo> getBillList() throws BasicException {

         return (List<BillInfo>) new StaticSentence(s
                , "SELECT ID, CUSTOMER, WAITER, PLACE, FLOOR, AMOUNT, CREATEDBY, CREATEDDATE, PAID, RECEIPT FROM BILL ORDER BY CREATEDBY,ID,CREATEDDATE"
                , null
                , new SerializerReadClass(BillInfo.class)).list();
    }
    public final BillInfo getBillInfo(String id) throws BasicException {

         return (BillInfo) new StaticSentence(s
                , "SELECT ID, CUSTOMER, WAITER, PLACE, FLOOR, AMOUNT, CREATEDBY, CREATEDDATE, PAID, RECEIPT FROM BILL WHERE ID=? "
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(BillInfo.class)).find(id);
    }
    public final void insertBillItem(BillLineInfo bline,int size) throws BasicException{
        int qty=bline.getMultiply();
        Double rate=bline.getRate();
        Double total=qty * rate;
         Object[] value=new Object[]{UUID.randomUUID().toString(),bline.getParentid(),bline.getProduct().getID(),qty,rate,total};
        // Datas type[]=new Datas{Datas.STRING,};
         new PreparedSentence(s
                    , "INSERT INTO BILLITEM (ID, LINE ,PARENTID, PRODUCT, DMULTIPLY, RATE,TOTAL) VALUES (?, ?, ?, ?, ?, ?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.INT,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.DOUBLE,Datas.DOUBLE})).exec(value);
    }
    public final List<BillLineInfo> getBillLineList(String billId) throws BasicException {

         return (List<BillLineInfo>) new PreparedSentence(s
                 ,"SELECT ID, LINE, PARENTID, PRODUCT, DMULTIPLY, RATE, ATTRIBUTES,TOTAL FROM BILLITEM WHERE PARENTID = ? ORDER BY LINE"
                 , SerializerWriteString.INSTANCE
                 , new SerializerReadClass(BillLineInfo.class)).list(billId);
    }

    /*
     * WARNING: This method should be called in a transaction so that all the SQL operation performed here are atomic
     */
     public final boolean markBillAsPaid(final BillInfo bill) throws BasicException {
        boolean status=true;
        Object[] values = new Object[] {bill.getID(), bill.isPaid(), bill.getReceiptRef()};
        Datas[] datas = new Datas[] {Datas.STRING, Datas.BOOLEAN, Datas.STRING};
        int cnt=new PreparedSentence(s
                , "UPDATE BILL SET PAID = ? ,RECEIPT = ? WHERE ID = ? AND PAID=FALSE"
                , new SerializerWriteBasicExt(datas, new int[] {1, 2, 0})).exec(values);
       if(cnt!=0){
        PreparedSentence l = new PreparedSentence(s
                , "UPDATE BILLITEM SET ATTRIBUTES = ? WHERE ID = ?"
                , SerializerWriteParams.INSTANCE);
         for (final BillLineInfo line : bill.getLines()) {
             l.exec(new DataParams() {

                @Override
                public void writeValues() throws BasicException {
                    try {
                        ByteArrayOutputStream o = new ByteArrayOutputStream();
                        line.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                        setBytes(1, o.toByteArray());
                    } catch (IOException e) {
                        setBytes(1, null);
                    }
                    setString(2, line.getID());
                }
            });
         }
       }else{
            status=false;
          JOptionPane.showMessageDialog(null, "Error Occured...Try again...", null, JOptionPane.WARNING_MESSAGE);
       }
        return status;
    }

   /*  private String getNextBillID(BillInfo bill) throws BasicException {
         String prefix = appView.getProperties().getProperty("sequence.billnumber");
         if (prefix == null) {
             //TODO warn about configuration
             prefix = "A";
         }
         StringBuffer sb = new StringBuffer(prefix);
         //TODO get floor billsl
         String temp = LookupUtilityImpl.getInstance(null).getFloorMap().get(bill.getFloor()).getBillsl();
         sb.append(""+temp);
         return sb.append(""+getlastbillno(bill.getFloor())).toString();
         //return sb.append(dlSales.getNextBillNumberIndex()).toString();
     }*/
 private String getNextBillID(BillInfo bill) throws BasicException {
  String billnum;
        String uname=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
     Object[] obj=(Object[])new  StaticSentence(s
            , "SELECT SEQUENCEDETAIL.BSERIES,SEQUENCEDETAIL.BMAX FROM SEQUENCEDETAIL,PEOPLE P1,ROLES R1,PEOPLE P,ROLES R WHERE SEQUENCEDETAIL.USERNAME=R1.NAME AND R1.ID=P1.ROLE AND P1.NAME=? AND SEQUENCEDETAIL.CATEGORY=R.NAME AND R.ID=P.ROLE AND P.NAME=? AND ACTIVE=TRUE"
            ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
            ,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.DOUBLE})).find(new Object[]{uname,bill.getCreatedBy()});
     if(obj!=null){
         Double max=Double.parseDouble(obj[1].toString());
         max++;
         billnum=obj[0].toString()+ max.intValue();
          new StaticSentence(s
                        , "UPDATE SEQUENCEDETAIL SET BMAX=?  WHERE ACTIVE=TRUE AND USERNAME=(SELECT ROLES.NAME FROM ROLES,PEOPLE WHERE PEOPLE.NAME=? AND ROLES.ID=PEOPLE.ROLE) AND CATEGORY=(SELECT ROLES.NAME FROM ROLES,PEOPLE WHERE PEOPLE.NAME=? AND ROLES.ID=PEOPLE.ROLE) "
                        , new SerializerWriteBasic(new Datas[] {Datas.DOUBLE,Datas.STRING,Datas.STRING }))
                        .exec(new Object[] {max,uname,bill.getCreatedBy()});
          return billnum;
     }else{
         JOptionPane.showMessageDialog(null, "Please Specify the Bill Series", "Cannot Create Bill", JOptionPane.OK_OPTION);
         return "";
     }
 }

     public final String getlastbillno(String floor) throws BasicException {
          lb = (FloorsInfo) new PreparedSentence(s
                 ,"SELECT ID, NAME, BILLSL, LASTBILL FROM FLOORS WHERE ID=?"
                 ,SerializerWriteString.INSTANCE
                 ,new SerializerReadClass(FloorsInfo.class)).find(floor);
        String i = (lb.getLastBillsl());
        updatelastbillno();
        return i;
     }

     public final void updatelastbillno() throws BasicException{
        int x = Integer.parseInt(lb.getLastBillsl());
        x++;
        String temp = ""+x;
        Object[] values = new Object[]{lb.getID(), temp};
        Datas[] data = new Datas[]{Datas.STRING, Datas.STRING};
        new PreparedSentence(s
                ,"UPDATE FLOORS SET LASTBILL = ? WHERE ID = ?"
                ,new SerializerWriteBasicExt(data, new int[] {1, 0})).exec(values);
     }
     private int flag1=0;
     private boolean berror=false;
     public final boolean saveBill(final BillInfo bill, final List<QticketInfo> qtInfos, final Qticket dlQTs,final String type) throws Exception {
        flag1=0;
        berror=false;
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
               final Date date=new Date();
                 AppUser user=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser();
                Object[] obj=(Object[])new StaticSentence(s
                        , "SELECT OPENSALE FROM PEOPLE WHERE ID=?"
                        ,SerializerWriteString.INSTANCE
                        ,new SerializerReadBasic(new Datas[]{Datas.TIMESTAMP})).find(user.getId());
                if(obj!=null){
                  if(obj[0]!=null){
                    Date d=(Date)obj[0];
                     Calendar cal1=Calendar.getInstance();
                     Calendar cal2=Calendar.getInstance();
                     cal1.setTimeInMillis(date.getTime());
                     cal2.setTimeInMillis(d.getTime());
                     if(cal1.before(cal2)){
                       if(JOptionPane.showConfirmDialog(null, "Present Time is less than Open sale Time.Previous Open sale Time is "+d+" .Do you want to override the open sale time ?", "Error-System Time was reset", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                         berror=true;
                       else{
                         new PreparedSentence(s
                                 , "UPDATE PEOPLE SET OPENSALE=? WHERE ID=?"
                                 , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.STRING})).exec(new Object[]{date,user.getId()});
                       }
                     }
                  }
                }
                if(berror==false){
                if (bill.getID() == null) {
                    String bno=getNextBillID(bill);

                    if(bno.equals("")){
                        flag1=1;
                        return false;

                    }
                    bill.setID(bno);
                    //qtl.setParentid(qticket.getId());
                }

                // new ticket
                if(type.equals("Debt")){
                   bill.setPaid(true);
                }
                new PreparedSentence(s
                    , "INSERT INTO BILL (ID, CUSTOMER, PLACE, WAITER, FLOOR, AMOUNT, CREATEDBY, CREATEDDATE, PAID, TAXTOTAL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() {

                    @Override
                    public void writeValues() throws BasicException {
                        setString(1, bill.getID());
                        setString(2, bill.getCustomerId());
                        setString(3, bill.getPlace());
                        setString(4, bill.getWaiter());
                        setString(5,bill.getFloor());
                        setDouble(6, bill.getSubTotal());
                        setString(7, bill.getCreatedBy());
                        setTimestamp(8, bill.getCreatedDate());
                        setBoolean(9, bill.isPaid());
                        setDouble(10, bill.getTax());
                      }
                });

                SentenceExec Billlineinsert = new PreparedSentence(s
                    , "INSERT INTO BILLITEM (ID, LINE ,PARENTID, PRODUCT, DMULTIPLY, RATE, ATTRIBUTES,TOTAL) VALUES (?, ?, ?, ?, ?, ?, ?,?)"
                    , SerializerWriteBuilder.INSTANCE);

                for (BillLineInfo l : bill.getLines()) {
                    if (l.getParentid()==null) {
                        l.setParentid(bill.getID());
                    }
                    Billlineinsert.exec(l);
                }
                System.out.println(bill.getCustomerId());
                int cnt1=0;
                Object[] obj7=(Object[])new StaticSentence(s, "SELECT COUNT(Q.ID) FROM QTITEMS Q,QTICKET QT WHERE QT.CUSTOMER=? AND QT.BILLED=FALSE AND QT.ID=Q.PARENTID"
                        , SerializerWriteString.INSTANCE,new SerializerReadBasic(new Datas[]{Datas.INT})).find(bill.getCustomerId());
                if(obj7!=null && obj7[0]!=null){
                  cnt1=Integer.parseInt(String.valueOf(obj7[0]));
                }
                int cnt2=0;
                for (QticketInfo qtInfo : qtInfos) {
                     cnt2+=qtInfo.getLines().size();
                }
               // if(cnt1!=cnt2){
                //    berror=true;
               //     throw new BasicException("Error Occurred...Please try again");
              //  }
            
                for (QticketInfo qtInfo : qtInfos) {
                    if (!qtInfo.isBilled()) {
                        qtInfo.setBilled(true);
                        qtInfo.setBillref(bill.getID());
                        int cnt=dlQTs.updateQTicket(qtInfo.getId(), qtInfo);
                        if(cnt==0){
                            berror=true;
                            JOptionPane.showMessageDialog(null, "Error Occurred...Please try again", null, JOptionPane.WARNING_MESSAGE);
                           throw new BasicException();
                        }
                    }
                }
                   //final Date date = new Date();
                if(type.equals("Debt")){
                   if (bill.getReceiptRef() == null) {
                    String rno=getNextReceiptID(bill.getCreatedBy());
                    bill.setReceiptRef(rno);
                    if(rno.equals(""))
                    {
                        flag1=1;
                        return false;
                    }
                 }

         new PreparedSentence(s
                    , "INSERT INTO CREDITCONFLIST (ID,  DATENEW, RUSER,CUSTOMER,BILLREF,AMOUNT,WAITER) VALUES (?, ?, ?,?,?,?,?)"
                    , new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.TIMESTAMP,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.STRING})
                    ).exec(new Object[]{bill.getReceiptRef(),date,LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName(),bill.getCustomerId(),bill.getID(),bill.getTotal(),bill.getWaiter()}
                     );
          //bill.setPaid(true);
         // boolean berror1= LookupUtilityImpl.getInstance(null).getDataLogicBill().markBillAsPaid(bill);
          }
                }
               return null;
            }
        };
        t.execute();
        if(flag1==0){
            if(berror==true){
                 JOptionPane.showMessageDialog(null, "Please reset the system time or consult your system admin", "Sorry Cannot Create Bill", JOptionPane.OK_OPTION);
                 return false;
            }
            return true;
        } else
            return false;
     }
     public String getNextReceiptID(String createdby) throws BasicException{
        String receiptnum;
        String uname=LookupUtilityImpl.getInstance(null).getAppView().getAppUserView().getUser().getName();
     Object[] obj=(Object[])new  StaticSentence(s
            , "SELECT SEQUENCEDETAIL.RSERIES,SEQUENCEDETAIL.RMAX FROM SEQUENCEDETAIL,PEOPLE P,PEOPLE P1,ROLES R,ROLES R1 WHERE USERNAME=R1.NAME AND R1.ID=P1.ROLE AND P1.NAME=? AND SEQUENCEDETAIL.CATEGORY=R.NAME AND R.ID=P.ROLE AND P.NAME=? AND ACTIVE=TRUE"
            ,new SerializerWriteBasic(new Datas[]{Datas.STRING,Datas.STRING})
            ,new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.DOUBLE})).find(new Object[]{uname,createdby});
     if(obj!=null){
         Double max=Double.parseDouble(obj[1].toString());
         max++;
         receiptnum=obj[0].toString()+max.intValue();
          new StaticSentence(s
                        , "UPDATE SEQUENCEDETAIL SET RMAX=?  WHERE ACTIVE=TRUE AND USERNAME = (SELECT ROLES.NAME FROM ROLES,PEOPLE WHERE PEOPLE.NAME=? AND ROLES.ID=PEOPLE.ROLE)  AND CATEGORY=(SELECT ROLES.NAME FROM ROLES,PEOPLE WHERE PEOPLE.NAME=? AND ROLES.ID=PEOPLE.ROLE) "
                        , new SerializerWriteBasic(new Datas[] {Datas.DOUBLE,Datas.STRING,Datas.STRING }))
                        .exec(new Object[] {max,uname,createdby});
          return receiptnum;
     }else{
         JOptionPane.showMessageDialog(null, "Please Specify the Receipt Series", "Cannot Create Receipt", JOptionPane.OK_OPTION);
         return null;
     }
    }
    public final void deleteBill(final String id) throws BasicException {

        new StaticSentence(s
            , "DELETE FROM BILL WHERE ID = ?"
            , SerializerWriteString.INSTANCE).exec(id);
    }
    
    

}
