package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.forms.DeactivateFacilityForm;
import com.openbravo.pos.ticket.DeactivateInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class DeactivateFacilityAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Helper mem = new Helper();

        DeactivateInfo deinfo = null;


        DeactivateFacilityForm deafform = (DeactivateFacilityForm) form;

        String[] mfuId = deafform.getMfuid();
        String date = deafform.getDate();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date temp = new Date();        
        try {
            temp = df.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Date now=new Date();
        if(now.compareTo(temp)>0)
        {
             ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.invaliddate", "invaliddate");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);

        return mapping.findForward(FAILURE);

        }
        UserInfo uinfo = null;
        uinfo = (UserInfo) request.getSession().getAttribute("user");
        
        deinfo = mem.deactivateFacility(mfuId, temp,uinfo.getMid());


        request.setAttribute("deinfo", deinfo);

        return mapping.findForward(SUCCESS);

    }
}
