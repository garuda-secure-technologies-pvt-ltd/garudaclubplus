/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.ticket.BalanceInfo;
import com.openbravo.pos.ticket.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author user

public class BalanceAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserInfo uinfo = null;
        uinfo = (UserInfo) request.getSession().getAttribute("user");
        Helper m = new Helper();
        BalanceInfo binfo = m.findBalance(uinfo.getAccid());
        binfo.setSearchKey(uinfo.getSearchkey());
        binfo.setAccid(uinfo.getAccid());
        binfo.setName(uinfo.getName());
        request.getSession().setAttribute("binfo", binfo);
        return mapping.findForward(SUCCESS);
    }
}
*/