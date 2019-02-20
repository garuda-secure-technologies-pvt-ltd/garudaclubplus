/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.forms.PasswordResetForm;
import com.openbravo.pos.pda.util.StringUtils;
import com.openbravo.pos.ticket.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PasswordResetAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        UserInfo uinfo = null;
        uinfo = (UserInfo) request.getSession().getAttribute("user");
        PasswordResetForm prForm = (PasswordResetForm) form;
        String newPass = prForm.getNewPass();
        String confirmPass = prForm.getConfirmPass();
        String oldPass = StringUtils.hashString(prForm.getOldPass());

        ActionMessages errors = new ActionErrors();
        Helper m = new Helper();
        if (oldPass.equals(uinfo.getPassword())) {
            if (newPass.equals(confirmPass)) {
                m.changePassword(uinfo.getMid(), uinfo.getSearchkey(), newPass);
                return mapping.findForward(SUCCESS);
            }


            ActionMessage msg = new ActionMessage("errors.passnotsame", "New Password & Confirm Password should be same");
            errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
            saveErrors(request, errors);
        } else {
            ActionMessage msg = new ActionMessage("errors.passincorrect", "Old password is incorrect");
            errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
            saveErrors(request, errors);
        }

        return mapping.findForward(FAILURE);
    }
}
*/