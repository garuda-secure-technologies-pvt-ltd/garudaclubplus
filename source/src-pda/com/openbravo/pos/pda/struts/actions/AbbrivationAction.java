

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.ticket.AbbrivationInfo;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class AbbrivationAction extends org.apache.struts.action.Action{
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";


    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm forn,
            HttpServletRequest request,HttpServletResponse response)
            throws Exception{
        Helper hel = new Helper();

        ArrayList<AbbrivationInfo> abbinfo = new ArrayList<AbbrivationInfo>();

        abbinfo = (ArrayList<AbbrivationInfo>) hel.findAbbrivation();

        request.setAttribute("abbinfo", abbinfo);




        return mapping.findForward(SUCCESS);

    }

}
