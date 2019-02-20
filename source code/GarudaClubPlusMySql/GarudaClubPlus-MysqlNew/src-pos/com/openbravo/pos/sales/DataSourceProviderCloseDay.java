

package com.openbravo.pos.sales;

import com.openbravo.pos.panels.ConsumableBillReprintTableModel;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class DataSourceProviderCloseDay implements JRDataSource{
    
    
    
    private int m_nIdx;
    private List<ConsumableBillReprintTableModel.CloseDayInfo> v;
    
    public boolean next() throws JRException {
       m_nIdx++;
       return (m_nIdx < v.size());
    }
    
    
     DataSourceProviderCloseDay(List<ConsumableBillReprintTableModel.CloseDayInfo> list) {
        m_nIdx = -1;
	v = list;
    }
    
    
     
     
      public Object getFieldValue(JRField jrf) throws JRException {
       
        
        Object o = null;
       
         String sName = jrf.getName();
       
         ConsumableBillReprintTableModel.CloseDayInfo curr = v.get(m_nIdx);
       
         if (curr== null)
	   return null;
            
            
          if(sName.equals("ProdName"))
                o = curr.getDateStart();
          
           else if (sName.equals("SRNO"))  
              o = curr.getSequenceNo()+"";
          
          
          else if (sName.equals("qty"))  
              o = curr.getDateEnd();
         
          else if (sName.equals("Rate"))  
              o = curr.getCreatedBy();
          
          
          
           else if (sName.equals("Amount"))  {
             o = curr.getAmount(); 
           }
          
         
         
          
         
          
          // heading Part 
          
          
           else if (sName.equals("SeqNo"))  
              o = "Seq No";
           else if (sName.equals("Heading1"))  
              o = "Date Start";
          else if (sName.equals("Heading2"))  
              o = "Date end";
           else if (sName.equals("Heading3"))  
              o = "Created By";
          else if (sName.equals("AmountTitle"))  
              o = "Amount";
         
          
          
          
         return o;
        
    }
       
     
    
}
