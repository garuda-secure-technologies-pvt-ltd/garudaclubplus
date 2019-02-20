package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.ticket.OtherFacility;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import com.openbravo.pos.ticket.UserInfo;
import java.util.HashMap;

public class OtherFacilityAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Helper mem = new Helper();
        UserInfo uinfo = null;
        uinfo = (UserInfo) request.getSession().getAttribute("user");

        ArrayList<OtherFacility> otherfacility = new ArrayList<OtherFacility>();

        otherfacility = (ArrayList<OtherFacility>) mem.findOtherFacility(uinfo.getSearchkey());

        request.setAttribute("otherfacility", otherfacility);

        HashMap<String, String> map = new HashMap<String, String>();

        for (OtherFacility ofac : otherfacility) {
            map.put(ofac.getFId(), ofac.getFacilityName());
        }


        request.getSession().setAttribute("map", map);



        return mapping.findForward(SUCCESS);
    }
}