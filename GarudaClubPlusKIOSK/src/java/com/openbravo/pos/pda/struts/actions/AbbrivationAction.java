

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.Constants;
import com.openbravo.pos.ticket.AbbrivationInfo;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.SessionAware;


public class AbbrivationAction extends ActionSupport implements SessionAware{
    Map session;

    
    
    public String execute() {
    Helper hel = new Helper();

        ArrayList<AbbrivationInfo> abbinfo = new ArrayList<AbbrivationInfo>();

        abbinfo = (ArrayList<AbbrivationInfo>) hel.findAbbrivation();

        
        session.put("users", abbinfo);
        return Constants.SUCCESS;
    }
   

    @Override
    public void setSession(Map s) {
        session = s;
    }

}
