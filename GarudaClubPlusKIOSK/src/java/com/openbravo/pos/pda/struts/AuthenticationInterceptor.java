/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts;

/**
 *
 * @author swathi
 */
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import java.util.Map;
public class AuthenticationInterceptor implements Interceptor {

    @Override
    public void destroy() {
        System.out.println("Inside Authentication Interceptor destroy()");
    }

    @Override
    public void init() {
        System.out.println("Inside Authentication Interceptor Init()");
    }

    @Override
    public String intercept(ActionInvocation ai) throws Exception {
        System.out.println("Inside Authentication Interceptor intercept()");
        
        /*if(ai.getAction() instanceof EditInformationAction)
		{
			Map session = ai.getInvocationContext().getSession();
			if(session.get("user")!=null)
			{
				return ai.invoke();
			}
			else
			{
				return Constants.LOGIN;
			}
		}
        */
        
        return ai.invoke();
    }
    
}
