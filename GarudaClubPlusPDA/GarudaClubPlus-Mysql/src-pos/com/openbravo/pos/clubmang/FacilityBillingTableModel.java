/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor
 */

package com.openbravo.pos.clubmang;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author swathi
 */
public class FacilityBillingTableModel {

    private List<Facilityline> fac;
    private Double taxtotal = 0.0;
    private Double total = 0.0;
    private final static String[] FACILITYHEADERS = {"Mem No", "User Name", "Startdate", "last bill date", "No Of Period", "Amt", ""};

    private FacilityBillingTableModel() {
    }

    public static FacilityBillingTableModel emptyinstance() {
        FacilityBillingTableModel d = new FacilityBillingTableModel();
        d.fac = new ArrayList<Facilityline>();
        d.taxtotal = 0.0;
        d.total = 0.0;
        // d.flogic=new FacilityLogic();
        return d;
    }

    public static FacilityBillingTableModel loadInstance1(AppView app, Facility fac1, DataLogicFacilities dlfac, DataLogicSales m_dlSales, double taxrate) throws BasicException {
        FacilityBillingTableModel d = new FacilityBillingTableModel();
        d.fac = new ArrayList<Facilityline>();
        d.taxtotal = 0.0;
        d.total = 0.0;
        Periodicity p = dlfac.getPerioicitybyid(fac1.getrperiod());
        DebtTypeTableModel.DebtTypeline dueperiod = dlfac.getDebtTypebyid(fac1.getdueperiod());
        String type = null;
        if (p.gettype().equals("Months")) {
            type = "mm";
        } else if (p.gettype().equals("Years")) {
            type = "yy";
        } else if (p.gettype().equals("Days")) {
            type = "dd";
        } else if (p.gettype().equals("Quaterly")) {
            type = "mm";
        }
        Date date = new Date();
        Date edate = new Date();
        FacilityLogic flogic = new FacilityLogic(dlfac);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date.getTime());
        int billabledate = cal1.get(Calendar.DATE);
        if (p.getdoj() == false) {
            cal1.setTimeInMillis(flogic.getBillableDate(p, billabledate, cal1).getTimeInMillis());
        }
        date.setTime(cal1.getTimeInMillis());
        billabledate = cal1.get(Calendar.DATE);
        flogic.setTemp(date);

        edate.setTime(flogic.calculateEndDate(date, p, billabledate, 1, new Date()).getTime());
        Date duedate = flogic.getDueDate(dueperiod, flogic.getTemp());
        List<FacilityBillingTableModel.Facilityline> memlist = new ArrayList<Facilityline>();
        List list = dlfac.getMembersToBeBilled1(edate, fac1.getid(), type);
        if (list != null) {
            memlist = list;
        }
        for (FacilityBillingTableModel.Facilityline line : memlist) {
            if (line.getno() > 0) {
                if (taxrate > 0) {
                    double total = line.getamt();
                    double taxvalue = Math.floor(total * taxrate);
                    d.taxtotal += taxvalue;
                    d.total += total;
                    line.setamt(dlfac.roundTwoDecimals(total + taxvalue));
                    line.setDueDate(duedate);
                    line.setbillabledate(edate);
                    d.fac.add(line);
                } else {
                    d.total += line.getamt();
                    line.setDueDate(duedate);
                    d.fac.add(line);
                }
            }
        }
        return d;
    }

    public static FacilityBillingTableModel loadInstance(AppView app, Facility fac1, DataLogicFacilities dlfac) throws BasicException {
        FacilityBillingTableModel d = new FacilityBillingTableModel();

        Periodicity p = dlfac.getPerioicitybyid(fac1.getrperiod());
        temp = 0;
        if (p.gettype().equals("Months")) {
            d.fac = bill("mm", p, dlfac, fac1);
        } else if (p.gettype().equals("Years")) {
            d.fac = bill("yy", p, dlfac, fac1);
        } else if (p.gettype().equals("Days")) {
            d.fac = bill("dd", p, dlfac, fac1);
        } else if (p.gettype().equals("Quaterly")) {
            d.fac = bill("quater", p, dlfac, fac1);
        }
        return d;
    }

    private static List<Facilityline> bill(String type, Periodicity p, DataLogicFacilities dlfac, Facility fac) throws BasicException {
        Date d = new Date();
        Calendar calnow = Calendar.getInstance();
        calnow.setTimeInMillis(d.getTime());
        calnow.set(Calendar.HOUR_OF_DAY, 00);
        calnow.set(Calendar.MINUTE, 00);
        calnow.set(Calendar.SECOND, 00);
        calnow.set(Calendar.MILLISECOND, 00);
        d.setTime(calnow.getTimeInMillis());
        Timestamp dnow = new Timestamp(d.getTime());

        List<Facilityline> fac1;
        int billabledate = calnow.get(Calendar.DATE);
        Object pdate = (Object) p.getdate();
        if (type.equals("yy") || type.equals("mm")) {
            if (pdate != null) {
                if (pdate.equals("Last Day")) {
                    billabledate = calnow.getActualMaximum(Calendar.DATE);
                } else {
                    if (p.getdoj() == false) {
                        billabledate = Integer.parseInt(pdate.toString());
                        if (billabledate == 29 || billabledate == 30) {
                            int dmax = calnow.getActualMaximum(Calendar.DATE);
                            if (dmax == 29 || dmax == 30) {
                                billabledate = dmax;
                            }
                        }
                    }
                }
            }
            if (type.equals("yy")) {
                if (p.getmonth() != -1) {
                    calnow.set(Calendar.MONTH, p.getmonth());
                }
            }
            calnow.set(Calendar.DATE, billabledate);
        }
        Date bdate = new Date();
        if (type.equals("quater")) {
            //  calnow.add(Calendar.DATE,p.getno());
            int month = calnow.get(Calendar.MONTH);
            int bdatetemp = 1;
            int flag = 0;
            int bmonth = 0;
            if (p.getqbtype() == true) {
                bdatetemp = 1;
                if (month <= 3 && month >= 1) {
                    bmonth = 1;

                } else if (month <= 6 && month >= 3) {
                    bmonth = 3;

                } else if (month <= 9 && month >= 6) {
                    bmonth = 6;

                } else if (month <= 12 && month >= 9) {
                    bmonth = 9;

                }
            } else if (p.getqbtype() == false) {
                if (month <= 3 && month >= 1) {
                    bdatetemp = 31;
                    if (month == 3 && calnow.get(Calendar.DATE) == 31) {
                        bmonth = 3;
                    } else {
                        bmonth = 12;
                        calnow.set(Calendar.YEAR, (calnow.get(Calendar.YEAR) - 1));
                    }
                } else if (month <= 6 && month >= 3) {
                    bdatetemp = 30;
                    if (month == 6 && calnow.get(Calendar.DATE) == 30) {
                        bmonth = 6;
                    } else {
                        bmonth = 3;
                    }
                } else if (month <= 9 && month >= 6) {
                    bdatetemp = 30;
                    if (month == 9 && calnow.get(Calendar.DATE) == 30) {
                        bmonth = 9;
                    } else {
                        bmonth = 6;
                    }
                } else if (month <= 12 && month >= 9) {
                    bdatetemp = 31;
                    if (month == 9 && calnow.get(Calendar.DATE) == 31) {
                        bmonth = 9;
                    } else { 
                        bmonth = 9;
                    }
                }
            }
            calnow.set(Calendar.DATE, bdatetemp);
            calnow.set(Calendar.MONTH, bmonth);
            bdate.setTime(calnow.getTimeInMillis());
            fac1 = dlfac.getMembersToBeBilled("mm", bdate, (p.getno() - 1), fac.getid());


            for (FacilityBillingTableModel.Facilityline f : fac1) {
                f.setbillabledate(new Timestamp(bdate.getTime()));
                int ntemp = f.getno() / p.getno();
                if (ntemp >= 0) {
                    f.setno(ntemp);
                }
            }
        } else {
            Date tdate = new Date();
            tdate.setTime(calnow.getTimeInMillis());
            //calnow.add(Calendar.DATE, -1);
            bdate.setTime(calnow.getTimeInMillis());
            fac1 = dlfac.getMembersToBeBilled(type, tdate, (p.getno() - 1), fac.getid());
            if (type.equals("mm")) {
                Calendar calnew = GregorianCalendar.getInstance();
                for (FacilityBillingTableModel.Facilityline f : fac1) {
                    f.setbillabledate(new Timestamp(bdate.getTime()));
                    //  if(f.getno()>=p.getno()){
                    if (f.getlbilldate() == null) {
                        calnew.setTimeInMillis(f.getsdate().getTime());
                        //if(calnow.get(Calendar.DATE) > calnew.get(Calendar.DATE))
                        //   f.setno(f.getno()+1);
                        float ntemp = (float) f.getno() / (float) p.getno();
                        if (ntemp >= 0) {
                            f.setno((int) Math.round(ntemp));
                            f.setamt(f.getno() * f.getramt());
                        }
                    } else {
                        calnew.setTimeInMillis(f.getlbilldate().getTime());
                        //  if(calnow.get(Calendar.DATE) > calnew.get(Calendar.DATE))
                        //      f.setno(f.getno()+1);
                        float ntemp = (float) f.getno() / (float) p.getno();
                        if (ntemp > 0) {

                            f.setno((int) Math.round(ntemp));
                            f.setamt(f.getno() * f.getramt());
                        }
                    }
                }
            }
        }
        if (type.equals("dd")) {
            for (FacilityBillingTableModel.Facilityline f : fac1) {
                f.setbillabledate(new Timestamp(bdate.getTime()));
                if (f.getno() >= p.getno()) {
                    int ntemp = f.getno() / p.getno();
                    if (ntemp >= 0) {
                        f.setamt(f.getramt() * ntemp);
                        f.setno(ntemp);

                    }
                }
            }
        }
        //  if(billabledate==calnow.get(Calendar.DATE)){

        dnow.setTime(bdate.getTime());
        if (p.getdoj() == false) {
            fac1 = calculateamt(p, fac1, dnow, dlfac);
        } else {
            fac1 = calculatefordoj(p, fac1, dnow, type);
        }
        //   }
        if (fac1.size() <= 0) {
            fac1 = new ArrayList<Facilityline>();
        }
        return fac1;
    }

    private static List<Facilityline> calculatefordoj(Periodicity p, List<Facilityline> fac1, Timestamp tnow, String type) {
        // Timestamp tnow=new Timestamp((long)00);
        // tnow.setTime((long)00);
        int i = 0;
        List facilitytemp = new ArrayList();
        for (FacilityBillingTableModel.Facilityline f : fac1) {
            Timestamp temp1 = new Timestamp(tnow.getTime());
            if (f.getlbilldate() == null) {
                temp1.setTime(f.getsdate().getTime());
                f.setbillabledate(temp1);
            } else {
                temp1.setTime(f.getlbilldate().getTime());
                f.setbillabledate(temp1);
            }
            Timestamp d = new Timestamp(tnow.getTime());
            do {
                Calendar caltemp1 = GregorianCalendar.getInstance();
                caltemp1.setTimeInMillis(temp1.getTime());
                if (type.equals("mm")) {
                    caltemp1.add(Calendar.MONTH, p.getno());
                }
                if (type.equals("yy")) {
                    caltemp1.add(Calendar.YEAR, p.getno());
                }
                //=(Timestamp)cal.getTimeInMillis();
                d.setTime(f.getbillabledate().getTime());
                temp1.setTime(caltemp1.getTimeInMillis());

                f.setbillabledate(temp1);
                //  if(temp1.before(tnow) || temp1.equals(tnow)){
                i++;
            //    }else
            //       facilitytemp.add(f);
            //fac1.remove(f);
            } while (temp1.before(tnow) || temp1.equals(tnow));
            f.setbillabledate(d);
            f.setno(i - 1);
            f.setamt(f.getramt() * (i - 1));
            if (i == 1 || f.getamt() <= 0) {
                facilitytemp.add(f);
            }
        }

        for (Object f : facilitytemp) {
            fac1.remove(f);
        }
        return fac1;
    }

    private static List<Facilityline> calculateamt(Periodicity p, List<Facilityline> fac1, Timestamp tnow, DataLogicFacilities dlfac) {
        //Timestamp tnow=new Timestamp((long)00);
        if (p.getaccurate() == true) {
            // Timestamp temp=new Timestamp(dnow.getTime());calnow
            List<FacilityBillingTableModel.Facilityline> fdelete = new ArrayList<FacilityBillingTableModel.Facilityline>();
            for (FacilityBillingTableModel.Facilityline f : fac1) {
                // Timestamp temp1;
                if (f.getlbilldate() == null) {
                    // temp1=f.getsdate();

                    Calendar caltemp1 = Calendar.getInstance();
                    Calendar caltemp2 = Calendar.getInstance();
                    caltemp1.setTimeInMillis(f.getsdate().getTime());
                    caltemp2.setTimeInMillis(tnow.getTime());
                    caltemp2.set(Calendar.DATE, caltemp1.get(Calendar.DATE));
                    //  Timestamp temp2=caltemp2.getTimeInMillis()
                    long difMil1 = caltemp2.getTimeInMillis() - caltemp1.getTimeInMillis();
                    int days1 = (int) ((difMil1 + 12 * 3600000L) / (24 * 3600000L));
                    caltemp1.set(Calendar.DATE, caltemp2.get(Calendar.DATE));
                    long difMil = tnow.getTime() - caltemp1.getTimeInMillis();
                    int days = (int) ((difMil + 12 * 3600000L) / (24 * 3600000L));
                    f.setamt(dlfac.roundTwoDecimals(Math.round(f.getamt() * days / days1)));

                }
                if (f.getamt() <= 0) {
                    fdelete.add(f);
                }
            }
            for (Facilityline f : fdelete) {
                fac1.remove(f);
            }
        }
        return fac1;
    }
    /*   private void calculateperiod(Periodicity p,DataLogicFacilities dlfac){
    if(p.gettype().equals("Monthly")){
    if(p.getdoj()==false){
    return 1;
    dlfac
    }else{
    p.getdate();
    }
    }
    }

    private void calculatenextbilldate(Facility facility,Object sdatetime,DataLogicFacilities dlfac){
    int uperiod=0;
    int rperiod=0;
    try{
    String rid=facility.getrperiod();
    if(rid!=null){
    Periodicity pr=dlfac.getPerioicitybyid(rid);
    calculateperiod(pr,dlfac);
    }
    String uid=facility.getuperiod();
    if(uid!=null){
    Periodicity pu=dlfac.getPerioicitybyid(facility.getuperiod());
    }
    }
    catch(Exception e){
    }
    }*/

    public List<Facilityline> getfacilityline() {
        return fac;
    }

    public double getTaxtotal() {
        return taxtotal;
    }

    public double getTotal() {
        return total;
    }

    public AbstractTableModel getTableModel() {
        return new AbstractTableModel() {

            @Override
            public String getColumnName(int column) {
                return AppLocal.getIntString(FACILITYHEADERS[column]);
            }

            public int getRowCount() {
                return fac.size();
            }

            public int getColumnCount() {

                return FACILITYHEADERS.length;
            }
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

            public Object getValueAt(int row, int column) {
                Facilityline l = fac.get(row);

                switch (column) {

                    case 0:
                        return l.getmno();
                    case 1:
                        return l.getmname();
                    case 2:
                        return Formats.DATE.formatValue(l.getsdate());
                    case 3:
                        return Formats.DATE.formatValue(l.getlbilldate());
                    case 4:
                        return l.getno();
                    case 5:
                        return l.getamt();
                    case 6:
                        return l.getbillit();
                    case 7:
                        return l.getbillabledate();
                    case 8:
                        return l.getid();
                    case 9:
                        return l.getaccid();//due period
                    case 10:
                        return l.getcustomeraccount();
                    case 11:
                        return l.getmemid();
                    case 12:
                        return l.getUserID();
                    case 13:
                        return l.getDueDate();
                    case 14:
                        return l.getMobile();
                    default:
                        return null;
                }
            }
        };
    }
    private static int temp = 0;

    public static class Facilityline implements SerializableRead {

        private String id;
        private String mno;
        private String mname;
        //   private String fname;
        private Double ramt;
        private Timestamp sdate;
        private Timestamp lbdate;
        //   private String createdby;
        private Double amt = 0.0;
        private int no;
        private Timestamp billabledate;
        private String accid;
        private Boolean billit = true;
        private String caccount;
        private String memid;
        private String username;
        private String userid;
        private int slno = 0;
        private double tax = 0.0;
        private double total = 0.0;
        private Date duedate;
        private String mobile;

        public void readValues(DataRead dr) throws BasicException {
            slno = ++temp;
            id = dr.getString(1);
            mno = dr.getString(2);
            mname = dr.getString(3);
            ramt = dr.getDouble(4);
            sdate = dr.getTimestamp(5);
            lbdate = dr.getTimestamp(6);
            no = dr.getInt(7);
            accid = dr.getString(8);
            caccount = dr.getString(9);
            memid = dr.getString(10);
            username = dr.getString(11);
            userid = dr.getString(12);
            mobile = dr.getString(13);
            duedate = new Date();
            billabledate = new Timestamp(new Date().getTime());
            amt = ramt * no;
            tax = 0.0;
            total = 0.0;
        }

        public String getMobile() {
            return mobile;
        }

        public Date getDueDate() {
            return duedate;
        }

        public void setDueDate(Date date) {
            duedate.setTime(date.getTime());
        }

        public String getUserName() {
            return username;
        }

        public String getUserID() {
            return userid;
        }

        public double getTax() {
            return tax;
        }

        public double getTotal() {
            return total;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getmemid() {
            return memid;
        }

        public String getcustomeraccount() {
            return caccount;
        }

        public Boolean getbillit() {
            return billit;
        }

        public String getaccid() {
            return accid;
        }

        public Double getamt() {
            return amt;
        }

        public Timestamp getbillabledate() {
            return billabledate;
        }

        public void setbillabledate(Date date) {
            billabledate.setTime(date.getTime());
        }

        public Double getramt() {
            return ramt;
        }

        public int getno() {
            return no;
        }

        public void setno(int nno) {
            no = nno;
        }

        public void setamt(Double amount) {
            amt = amount;
        }

        public String getid() {
            return id;
        }

        public String getmno() {
            return mno;
        }

        public Timestamp getsdate() {
            return sdate;
        }

        public Timestamp getlbilldate() {
            return lbdate;
        }

        public String getmname() {
            if (username == null) {
                return mname;
            } else {
                return username;
            }
        }

        public String getSlNo() {
            return String.valueOf(slno);
        }
    }
}
