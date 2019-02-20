

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.pda.struts.Constants;
import com.openbravo.pos.pda.struts.forms.LoginForm;
import com.openbravo.pos.ticket.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.interceptor.SessionAware;



public class LoginAction extends ActionSupport implements SessionAware {

    
    Map session;
    UserInfo user1;

    public UserInfo getUser1() {
        return user1;
    }

    public void setUser1(UserInfo user1) {
        this.user1 = user1;
    }

   
//    @Override
//    public ActionForward execute(ActionMapping mapping, ActionForm form,
//            HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//
//        LoginForm inputForm = (LoginForm) form;
//        Helper m=new Helper();
//        String login = inputForm.getLogin();
//        String password = inputForm.getPassword();
//        UserInfo user = m.findUser(login, password);
//        if (user != null) {
//            HttpSession session = request.getSession();
//            session.setAttribute("user", user);
//            if(!user.isPassrest())
//            {
//                return mapping.findForward(RESTPASS);
//            }
//            else
//            return mapping.findForward(SUCCESS);
//        }
//        ActionMessages errors = new ActionErrors();
//        ActionMessage msg = new ActionMessage("errors.loginfailed", "login/password");
//        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
//        saveErrors(request, errors);
//
//        return mapping.findForward(FAILURE);
//
//    }

    public String execute() {
        Helper m=new Helper();
        String login = user1.getMid();
        String password = user1.getPassword();
        UserInfo user = m.findUser(login, password);
        if (user != null) {
            
            session.put("user", user);
            if(!user.isPassrest())
            {
                return Constants.RESTPASS;
            }
            else
            return Constants.SUCCESS;
        }
        addActionError(getText("errors.loginfailed"));
        return Constants.FAILURE;
    }
    
    @Override
    public void setSession(Map map) {
        session = map;
    }
}