package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.forms.ActivateFacilityForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.openbravo.pos.ticket.ActivateInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
public class ActivateFacilityAction extends org.apache.struts.action.Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        Helper mem = new Helper();
        //String st[]=null;
        ActivateInfo atinfo = new ActivateInfo();
        /*if(request.getParameter("fid")!=null){
        st=request.getParameterValues("fid");
        }*/

/*
        ActivateFacilityForm afform =(ActivateFacilityForm) form;
        String[] fId = afform.getFid();
        String date = afform.getDate();
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
        ActionMessage msg1 = new ActionMessage("errors.invaliddate", "invaliddate");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg1);
        saveErrors(request, errors);

        return mapping.findForward(FAILURE);

        }
        UserInfo usinfo = (UserInfo) request.getSession().getAttribute("user");
        

     atinfo = mem.activateFacility(fId, temp,usinfo.getMid());

        request.setAttribute("atinfo", atinfo);
        
        return mapping.findForward(SUCCESS);
    }
}
*/