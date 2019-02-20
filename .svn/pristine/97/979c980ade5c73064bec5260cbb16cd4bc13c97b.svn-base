

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.forms.LoginForm;
import com.openbravo.pos.ticket.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author jaroslawwozniak
 */
public class LoginAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";
    private final static String RESTPASS = "restpass";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        LoginForm inputForm = (LoginForm) form;
        Helper m=new Helper();
        String login = inputForm.getLogin();
        String password = inputForm.getPassword();
        UserInfo user = m.findUser(login, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if(!user.isPassrest())
            {
                return mapping.findForward(RESTPASS);
            }
            else
            return mapping.findForward(SUCCESS);
        }
        ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.loginfailed", "login/password");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);

        return mapping.findForward(FAILURE);

    }
}