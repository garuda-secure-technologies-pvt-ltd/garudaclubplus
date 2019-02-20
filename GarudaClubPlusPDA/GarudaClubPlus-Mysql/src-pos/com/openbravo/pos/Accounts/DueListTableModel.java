
package com.openbravo.pos.Accounts;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
//import com.openbravo.format.Formats;
import com.openbravo.format.Formats;
import com.openbravo.pos.clubmang.DataLogicFacilities;
import com.openbravo.pos.clubmang.Facility;
//import com.openbravo.pos.clubmang.FacilityInfo;
//import com.openbravo.pos.clubmang.Periodicity;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author swathi
 */
public class DueListTableModel {

   private List<DueListTableModel.ReportLine> data;
   //private int columncnt=0;
   private List<Facility> flist;
   private List<String> hlist=new ArrayList<String>();
   public static DueListTableModel loadEmptyInstance(){
       DueListTableModel d=new DueListTableModel();
       d.data=new ArrayList<DueListTableModel.ReportLine>();
       return d;
   }
   public static DueListTableModel loadData(AppView app,List<Facility> flist,String memtypeid,Date date,Date overduedate,DataLogicFacilities dlfac) throws BasicException{
       DueListTableModel d=new DueListTableModel();
       String query=null;
       String querytemp=null;
       String queryOthers=null;


       d.data=new ArrayList<DueListTableModel.ReportLine>();
       d.hlist.add("Searchkey");
       d.hlist.add("Name");
       d.hlist.add("CTotal");
       String querytemp1=null,query1=null;
       d.flist=flist;
        if(memtypeid==null) //praveen:active was ambiguous so changed to active1 in below statements
            query="SELECT C.ID as cid,C.NAME as cname,C.SEARCHKEY as ckey,F.NAME as fname,F.ID as fid,M.CURRENTDEBT as currdebt, MF.LBILLDATE as lbilldate,MF.SDATE as sdate,MF.ACTIVE as active1,MF.STATUS_ as status,0.0 as overdue,0.0 as due,c.account as acc FROM CUSTOMERS C join  MEMDEBTTABLE M on c.id=m.memid join facility F on M.FACILITY=f.id JOIN MEMFACILITYUSAGE MF ON M.MEMID=MF.MEMNO AND F.id=MF.FACILITYTYPE   ";
       else{
            query="SELECT C.ID as cid,C.NAME as cname,C.SEARCHKEY as ckey,F.NAME as fname,F.ID as fid,M.CURRENTDEBT as currdebt, MF.LBILLDATE as lbilldate,MF.SDATE as sdate,MF.ACTIVE as active1,MF.STATUS_ as status,0.0 as overdue,0.0 as due,c.account as acc   FROM Facility f join  MEMDEBTTABLE M on F.ID=M.FACILITY JOIN MEMFACILITYUSAGE MF ON M.MEMID=MF.MEMNO AND F.id=MF.FACILITYTYPE JOIN CUSTOMERS C ON M.MEMID=C.ID JOIN MEMTYPE M1 ON C.MEMTYPE=M1.ID ";
            querytemp=" WHERE M1.ID= '" +memtypeid+ "'";
       }
       int barfacflag=0;
       for(Facility f:flist){
           d.hlist.add("Facility");
           if(f.getid().equals("1")){
               barfacflag=1;
               if(flist.size()==1)
                   query=null;
               if(memtypeid==null)
                  query1="select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,sum(overdue) as overdue,sum(due) as due,acc from( "+
                         " select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,overdue, due,acc from( select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,sum(a.balanceamount) as overdue,0.0 as due,c.account as acc from customers c, accountjournal a where c.account=a.accountid and a.adjusted=false  and a.transref='1' and a.date< ? and a.transtype='D'  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc) as duebill where overdue>0 "+
                         " union all  select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status, overdue, due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,0.0 as overdue,sum(a.balanceamount) as due,c.account as acc from customers c, accountjournal a where c.account=a.accountid and a.adjusted=false  and a.transref='1' and a.date>= ?  and a.date< ?  and a.transtype='D'  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc )as billdue where due>0 ) as duebill group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc order by ckey ";
               else       //praveen:changed active to active1
                   query1="select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,sum(overdue) as overdue,sum(due) as due,acc from( "+
                         " select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue as overdue,due as due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active1,null as status,sum(a.balanceamount) as overdue,0.0 as due,c.account as acc from customers c, accountjournal a,memtype m where c.account=a.accountid and a.adjusted=false  and a.transref='1' and a.date< ? and a.transtype='D' and c.memtype=m.id  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active1,status,acc) as billdue where overdue>0 "+
                         " union all  select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue as overdue,due as due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active1,null as status,0.0 as overdue,sum(a.balanceamount) as due,c.account as acc from customers c, accountjournal a,memtype m where c.account=a.accountid and a.adjusted=false  and a.transref='1' and a.date>= ?  and a.date< ?  and a.transtype='D' and c.memtype=m.id  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active1,status,acc)as billdue where due>0) as duebill group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active1,status,acc order by ckey ";

               d.hlist.add("Due Amount");
               d.hlist.add("Over Due Amount");
            
           }
           else if(f.getid().equals("2")){
               barfacflag=2;
               if(flist.size()==1)
                   query=null;
               if(memtypeid==null)
                query1="select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,sum(overdue) as overdue,sum(due) as due,acc from( "+
                         " select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,overdue, due,acc from( select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,sum(a.balanceamount) as overdue,0.0 as due,c.account as acc from customers c, accountjournal a where c.account=a.accountid and a.adjusted=false  and a.transref='2' and a.date< ? and a.transtype='D'  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc) as billdue where overdue>0 "+
                         " union all  select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status, overdue, due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,0.0 as overdue,sum(a.balanceamount) as due,c.account as acc from customers c, accountjournal a where c.account=a.accountid and a.adjusted=false  and a.transref='2' and a.date>= ?  and a.date< ?  and a.transtype='D'  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc ) as billdue where due>0 ) as duebill group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc order by ckey ";
               else
                   query1="select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,sum(overdue) as overdue,sum(due) as due,acc from( "+
                         " select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,overdue as overdue,due as due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,sum(a.balanceamount) as overdue,0.0 as due,c.account as acc from customers c, accountjournal a,memtype m where c.account=a.accountid and a.adjusted=false  and a.transref='2' and a.date< ? and a.transtype='D' and c.memtype=m.id  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc) as billdue  where overdue>0 "+
                         " union all  select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,overdue as overdue,due as due,acc from(select c.id as cid,c.name as cname,c.searchkey as ckey,null as fname,null as fid,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,0.0 as overdue,sum(a.balanceamount) as due,c.account as acc from customers c, accountjournal a,memtype m where c.account=a.accountid and a.adjusted=false  and a.transref='2' and a.date>= ?  and a.date< ?  and a.transtype='D' and c.memtype=m.id  group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc)as billdue where due>0) as duebill group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc order by ckey ";

               d.hlist.add("Due Amount");
               d.hlist.add("Over Due Amount");
            
           }
           else if(!f.getid().equals("others")){
               if(querytemp==null){
                if(querytemp1==null)
                 querytemp1=" WHERE ( M.FACILITY= '"+f.getid()+"'";
                else
                 querytemp1+= " OR M.FACILITY= '"+f.getid()+"'";
            }
               else{
                if(querytemp1==null)
                 querytemp1=" AND ( M.FACILITY= '"+f.getid()+"'";
                else
                 querytemp1+= " OR M.FACILITY= '"+f.getid()+"'";
            }
            d.hlist.add("Due Amount");
            d.hlist.add("Status");
           }else{
                  queryOthers="select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active,status,overdue as overdue,due as due,acc from(SELECT C.ID AS CID,C.NAME AS CNAME,C.SEARCHKEY AS CKEY,NULL AS FNAME,NULL AS FID,null as currdebt,null as lbilldate,null as sdate,null as active,null as status,0.0 as overdue,sum(a.balanceamount) as due,c.account as acc FROM CUSTOMERS C,ACCOUNTJOURNAL A,FACILITY F where C.ACCOUNT=A.ACCOUNTID AND A.DATE<? AND A.TRANSTYPE='D' AND A.TRANSREF != F.ID Group by cid,ckey,cname,fname,fid,currdebt,lbilldate,sdate,active,status,acc order by ckey)as billdue  WHERE DUE>0";
               }
       }
       if(query==null)
           query=query1;
       else{
       if(querytemp!=null || querytemp1!=null){
           if(querytemp==null)
               query+=querytemp1+" )";
           else
               query+=querytemp+querytemp1+" )";
       }
       if(query1!=null)
       query+=" union all "+query1;
       }
       if(queryOthers!=null){
            query+=" union all "+queryOthers;
       }
       List<Object[]> obj=new ArrayList<Object[]>();
       if(barfacflag==0){
           if(queryOthers==null)  //praveen:added alias name wr ever required and changed active to active1
             obj=new PreparedSentence(app.getSession(),"select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C' where currdebt >0   group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , null, new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list();
           else
               obj=new PreparedSentence(app.getSession(),"select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C' where currdebt >0  group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list(new Object[]{date});
       }else if(barfacflag==1){
          if(queryOthers==null)
              obj=new PreparedSentence(app.getSession(),"select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C'  group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list(new Object[]{overduedate,overduedate,date});
          else
              obj=new PreparedSentence(app.getSession(),"select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C'  group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list(new Object[]{overduedate,overduedate,date,date});
       }
       else if(barfacflag==2){
          if(queryOthers==null)
              obj=new PreparedSentence(app.getSession(),"(select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C'  group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list(new Object[]{overduedate,overduedate,date});
          else
              obj=new PreparedSentence(app.getSession(),"(select cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc,sum(a.balanceamount) from( "+query+" ) as billdue left join accountjournal a on a.accountid=acc and a.transtype='C'  group by cid,cname,ckey,fname,fid,currdebt,lbilldate,sdate,active1,status,overdue,due,acc order by 3,4"
               , new SerializerWriteBasic(new Datas[]{Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.TIMESTAMP}), new SerializerReadBasic(new Datas[]{Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.STRING,Datas.DOUBLE,Datas.TIMESTAMP,Datas.TIMESTAMP,Datas.BOOLEAN,Datas.INT,Datas.DOUBLE,Datas.DOUBLE,Datas.STRING,Datas.DOUBLE})).list(new Object[]{overduedate,overduedate,date,date});
       }
       

       List<ReportLine> rlist=new ArrayList<ReportLine>();
       String memid=null,memname=null,memno=null,facentry=null;
       double camt=0;
       List<FacilityLine> tlist=new ArrayList<FacilityLine>();
       //tlist.addAll(flist);
       for(Object[] objtemp:obj){

           /*if(memid!=null && !memid.equals(String.valueOf(objtemp[0]))){
               for(int k=0;k<flist.size();k++){
                  int flag=0;
                  Facility f=flist.get(k);

                  for(FacilityLine fl:tlist){
                     if(f.getid().equals(fl.getFacilityid())){
                         flag=1;
                         break;
                     }
                  }
                  if(flag!=1){
                      FacilityLine f1=new FacilityLine(f.getName(), f.getid(), 0.0, 0.0, "",null);
                      tlist.add(k,f1);
                  }
               }
               ReportLine r=new ReportLine(memid, memname, memno, tlist,camt);
               d.data.add(r);
               tlist=new ArrayList<FacilityLine>();
           }*/

           memid=String.valueOf(objtemp[0]);
           memno=String.valueOf(objtemp[2]);
           memname=String.valueOf(objtemp[1]);
           facentry=String.valueOf(objtemp[4]);
           if(objtemp[13]!=null)
           camt=Double.parseDouble(String.valueOf(objtemp[13]));
           if(objtemp[4]!=null && !facentry.equals("1")){
             Date lbilldate=null;
             if(objtemp[6]!=null){
                   lbilldate=(Date)objtemp[6];
             }else{
                   lbilldate=(Date)objtemp[7];
             }
             boolean status=Boolean.parseBoolean(String.valueOf(objtemp[8]));
             String billstatus=null;
             int no=0;
             if(status==true){
                 Calendar callbill=Calendar.getInstance();
                 callbill.setTimeInMillis(lbilldate.getTime());
                 callbill.set(Calendar.HOUR_OF_DAY, 00);
                 callbill.set(Calendar.MINUTE, 00);
                 callbill.set(Calendar.SECOND, 00);
                 callbill.set(Calendar.MILLISECOND, 00);
                 Calendar calpdate=Calendar.getInstance();
                 calpdate.setTimeInMillis(date.getTime());
                // Facility f=
                 calpdate.set(Calendar.DATE, calpdate.getMaximum(Calendar.DATE));
                 calpdate.set(Calendar.HOUR_OF_DAY, 00);
                 calpdate.set(Calendar.MINUTE, 00);
                 calpdate.set(Calendar.SECOND, 00);
                 calpdate.set(Calendar.MILLISECOND, 00);
                 if(calpdate.after(callbill)){
                     billstatus="short ("+Formats.DATE.formatValue(lbilldate)+")";
                 }else if(calpdate.before(callbill))
                     billstatus="Excess ("+Formats.DATE.formatValue(lbilldate)+")";
               }
               FacilityLine f=new FacilityLine(String.valueOf(objtemp[3]), facentry, Double.parseDouble(objtemp[5].toString()),0.0 , billstatus,Formats.DATE.formatValue(lbilldate));
               tlist.add(f);
               
             }else if(barfacflag==2){
                if(objtemp[11]!=null || objtemp[10]!=null  ){
                FacilityLine f=new FacilityLine("Restaurant", "2", Double.parseDouble(objtemp[11].toString()),Double.parseDouble(objtemp[10].toString()) , null,null);
                tlist.add(f);
               }
             }
             else if(barfacflag==1)
             {
               if(objtemp[11]!=null || objtemp[10]!=null  ){
               FacilityLine f=new FacilityLine("Bar", "1", Double.parseDouble(objtemp[11].toString()),Double.parseDouble(objtemp[10].toString()) , null,null);
               tlist.add(f);
               
             }
             }
           if(memid!=null && memid.equals(String.valueOf(objtemp[0]))){
               for(int k=0;k<flist.size();k++){
                  int flag=0;
                  Facility f=flist.get(k);

                  for(FacilityLine fl:tlist){
                     if(f.getid().equals(fl.getFacilityid())){
                         flag=1;
                         break;
                     }
                  }
                  if(flag!=1){
                      FacilityLine f1=new FacilityLine(f.getName(), f.getid(), 0.0, 0.0, "",null);
                      tlist.add(k,f1);
                  }
               }
               ReportLine r=new ReportLine(memid, memname, memno, tlist,camt);
               d.data.add(r);
               tlist=new ArrayList<FacilityLine>();
           }
       }


       return d;
   }
   public List<DueListTableModel.ReportLine> getReportList1(){
      return data;
   }
   public AbstractTableModel getTableModel(){
     return new AbstractTableModel() {
            int i=2,j=0;
            public int getRowCount() {
                return data.size();
            }

            public int getColumnCount() {
                return hlist.size();
            }
            @Override
             public String getColumnName(int column) {
                return AppLocal.getIntString(hlist.get(column));
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                ReportLine r=data.get(rowIndex);
                int temp=0;
                switch(columnIndex){
                    case 0  : return r.getSearchkey();
                    case 1  : return r.getName();
                    case 2  : return Formats.ConvertDoubleToString(r.getCreditAmt());
                    default : temp=(columnIndex-3);
                              if(temp!=0){
                                   temp=temp/3;
                              }
                              FacilityLine f=r.getFacilityList().get(temp);
                              if( ((temp)*3+3)==columnIndex) {
                                      return f.getFacilityName();
                             }else if(f.getFacilityName().equals(f.getFacilityName())){
                                  if( ((temp)*3+4)==columnIndex) {
                                      return Formats.ConvertDoubleToString(f.getDueAmount());
                                  }else if( ((temp)*3+5)==columnIndex) {
                                      if(f.getRemark()!=null )
                                          return f.getRemark();
                                      else if(f.getOverDueAmount()>0){
                                          return Formats.ConvertDoubleToString(f.getOverDueAmount());
                                      }else
                                          return null;
                                  }
                                  else return null;
                              }else return null;
                }
            }
        };
   }
   public static class ReportLine {
     private String name;
     private String searchkey;
     private String id;
     private List<FacilityLine> faclist;
     private double camt;

     public ReportLine(String id,String name, String searchkey, List<FacilityLine> faclist,double camt) {
            this.name = name;
            this.id=id;
            this.searchkey = searchkey;
            this.faclist = faclist;
            this.camt=camt;
     }
     public String getName(){
       return name;
     }
     public String getSearchkey(){
         return searchkey;
     }
     public List<FacilityLine> getFacilityList(){
       return faclist;
     }
     public void addFacility(FacilityLine f){
        faclist.add(f);
     }
     public double getCreditAmt(){
      return camt;
     }
   }
   public static class FacilityLine{
     private String facid;
     private String facilityname;
     private Double dueamount;
     private Double overdueamount;
     private String remark;
     private Object lbillperiod;//last billed upto
    // private int no;

     public FacilityLine(String facilityname,String facid,Double dueamount,Double overdueamount,String remark,Object lbillperiod){
         this.facilityname=facilityname;
         this.facid=facid;
         this.dueamount=dueamount;
         this.overdueamount=overdueamount;
         this.remark=remark;
         this.lbillperiod=lbillperiod;
         //this.no=no;
     }
     public Object getLastBillPeriod(){
         return lbillperiod;
     }
     public String getFacilityid(){
         return facid;
     }
     public String getFacilityName(){
       return facilityname;
     }
     public Double getDueAmount(){
       return dueamount;
     }
     public Double getOverDueAmount(){
       return overdueamount;
     }
     public String getRemark(){
        return remark;
     }
   }
}
