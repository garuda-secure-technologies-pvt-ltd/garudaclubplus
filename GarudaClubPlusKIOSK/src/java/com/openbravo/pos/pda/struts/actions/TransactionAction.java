/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.ticket.TransactionInfo;
import com.openbravo.pos.ticket.UserInfo;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 
public class TransactionAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Helper m = new Helper();
        UserInfo a = (UserInfo) request.getSession().getAttribute("user");
        String acc = a.getAccid();
        List tinfo = new ArrayList<TransactionInfo>();


        tinfo = (ArrayList<TransactionInfo>) m.findLastTransactions(acc);
        request.getSession().setAttribute("trinfo", tinfo);
        request.setAttribute("transInfo", tinfo);



        return mapping.findForward(SUCCESS);
    }
}
*/