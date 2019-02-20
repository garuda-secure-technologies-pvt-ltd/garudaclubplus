

package com.openbravo.pos.payment;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;

public class PaymentInfoTicket extends PaymentInfo implements SerializableRead  {
    
    private double m_dTicket;
    private String m_sName;
    private ChequeDetails cd;
    
    /** Creates a new instance of PaymentInfoCash */
    public PaymentInfoTicket(double dTicket, String sName) {
        m_sName = sName;
        m_dTicket = dTicket;
    }

    public PaymentInfoTicket(double dTicket, String sname, ChequeDetails cd) {
        m_sName = sname;
        m_dTicket = dTicket;
        this.cd = cd;
    }
    
    public PaymentInfoTicket() {
        m_sName = null;
        m_dTicket = 0.0;
        cd = null;
     }
    
    public void readValues(DataRead dr) throws BasicException {
        m_sName = dr.getString(1);
        m_dTicket = dr.getDouble(2).doubleValue();
    }
    
    public PaymentInfo copyPayment(){
        return new PaymentInfoTicket(m_dTicket, m_sName);
    }

    public ChequeDetails getChequeDetails() {
        return cd;
    }
    public String getName() {
        return m_sName;
    }   
    public double getTotal() {
        return m_dTicket;
    }    
    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dTicket));
    }       
    
    // Especificas
    public String printPaperTotal() {
        // En una devolucion hay que cambiar el signo al total
        return Formats.CURRENCY.formatValue(new Double(-m_dTicket));
    }          
}
