/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.clubmang;


import com.openbravo.pos.Accounts.DataSource2;
import com.openbravo.pos.Accounts.DataSource4;
import com.openbravo.pos.Accounts.DueListDataSource;
import com.openbravo.pos.Accounts.OBSource;
import com.openbravo.pos.CardsRoom.DataSource3;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;


public class DataSourceProvider implements JRDataSourceProvider {
  private List list;
  private JRField[] fields;
  private JRDataSource datasource;
  
    public DataSourceProvider() {
    }
  
	public DataSourceProvider(List list) {
        this.list=list;
	}
    public void setDataSource(DataSourceForMemDebtList datasource){ //praveen:added for printing MemDebtBill
        this.datasource = datasource;
    }
    public void setDataSource(DataSourceForCreditList datasource){ //praveen:added for printing credit list
        this.datasource = datasource;
    }

    public void setDataSource(DataSource1 datasource){
       this.datasource=datasource;
    }
    public void setDataSource(DataSource3 datasource){
       this.datasource=datasource;
    }
     public void setDataSource(DataSource4 datasource){
       this.datasource=datasource;
    }

    public void setDataSource(Datasource2 ds) {
        this.datasource=ds;
    }
    public void setDataSource(OBSource datasource){
       this.datasource=datasource;
    }
    public void setDataSource(DataSource datasource){
       this.datasource=datasource;
    }
    public void setDataSource(DataSource2 datasource){
       this.datasource=datasource;
    }
    public void setDataSource(DueListDataSource datasource){
       this.datasource=datasource;
    }
     public void setDataSource(MemberDataSource datasource){
       this.datasource=datasource;
    }
    public void setDataSource(DataSourceforTrailBalance datasource){
       this.datasource=datasource;
    }
     public void setDataSource(DataSourceForAccountJournal datasource){
       this.datasource=datasource;
    }
      public void setDataSource(DataSourceForAccountContra datasource){
       this.datasource=datasource;
    }
    public JRDataSource create(JasperReport report) throws JRException {
        return datasource;
    }

    public void dispose(JRDataSource dataSource) throws JRException {
            // nothing to dispose
    }
    public void setFields(JRField[] fields){
      this.fields=fields;
    }

	public JRField[] getFields(JasperReport arg0) throws JRException, UnsupportedOperationException {
		/* fields = new JRField[7];
		fields[0] = (JRField)new JRBasicField("SlNo", "slno", java.lang.String.class, "java.lang.String");
		fields[1] = (JRField)new JRBasicField("Mem No", "memno", java.lang.String.class, "java.lang.String");
        fields[2] = (JRField)new JRBasicField("Mem Name", "mname", java.lang.String.class, "java.lang.String");
        fields[3] = (JRField)new JRBasicField("Start Date", "sdate", java.util.Date.class, "java.util.Date");
        fields[4] = (JRField)new JRBasicField("Last Bill Date", "lbilldate", java.util.Date.class, "java.util.Date");
        fields[5] = (JRField)new JRBasicField("Num", "no", java.lang.Integer.class, "java.lang.Integer");
        fields[6] = (JRField)new JRBasicField("Amount", "amount", java.lang.Double.class, "java.lang.Double");*/
		return fields;
	}

	public boolean supportsGetFieldsOperation() {
		// TODO Auto-generated method stub
		return true;
	}
}




