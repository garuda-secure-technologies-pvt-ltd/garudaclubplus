/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.clubmang;

import com.openbravo.pos.Accounts.AccountReports;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.LookupUtilityImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author swathi
 */
public class DataSource1 implements JRDataSource{
     private int m_nIdx;
	private List<AccountReports.AccountsList> v;
    private Date edate;
    private  Date sdate;

	public DataSource1() {
		this (new ArrayList<AccountReports.ReportData>());
	}

	public DataSource1(List<AccountReports.ReportData> v) {
		m_nIdx = -1;
	//	this.v = v;
	}

    public DataSource1(List<AccountReports.AccountsList> v,Date date,Date date1) {
		m_nIdx = -1;
		this.v = v;
        edate=date;
        sdate=date1;
	}
  

    public Object getFieldValue(JRField field) throws JRException {
          Object o = null;

		String sName = field.getName();

		AccountReports.AccountsList curr = v.get(m_nIdx);

		if (curr== null)
			return null;
		if (sName.equals("amount"))
			o = curr.getAmt();
        else if (sName.equals("transtype"))
			o = curr.getTranstype();
        else if (sName.equals("transref"))
			o = curr.getTransref();
        else if (sName.equals("narration"))
			o = curr.getNarration();
        else if (sName.equals("name"))
			o = curr.getAccountName();
        else if (sName.equals("transtype1"))
			o = curr.gettranstype1();
        else if(sName.equals("date"))
            o = curr.getDate();
        else if(sName.equals("transno"))
            o = curr.getTransno();
        else if(sName.equals("type"))
            o = curr.getType();
		return o;
      
    }


	public boolean next() throws JRException {
		m_nIdx++;
		return (m_nIdx < v.size());
	}
}
