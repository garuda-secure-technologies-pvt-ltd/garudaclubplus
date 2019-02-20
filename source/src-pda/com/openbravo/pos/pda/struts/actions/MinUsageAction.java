/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.Helper;
import com.openbravo.pos.ticket.BillPeriods;
import com.openbravo.pos.ticket.MinUsageLogic;
import com.openbravo.pos.ticket.MinimumUsageInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author user
 */
public class MinUsageAction extends org.apache.struts.action.Action {

    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo uinfo = null;
        uinfo = (UserInfo) request.getSession().getAttribute("user");
        BillPeriods bp = null;
        double usage, limit;
        String msg="";

        Helper m = new Helper();
        List<MinimumUsageInfo> muinfos = new ArrayList<MinimumUsageInfo>();
        List<MinimumUsageInfo> muinfosTemp = new ArrayList<MinimumUsageInfo>();
        muinfos = (ArrayList<MinimumUsageInfo>) m.findMinUsage(uinfo.getMid());
        muinfosTemp = muinfos;
        MinUsageLogic mulogic = new MinUsageLogic();
        /*  for (MinimumUsageInfo muinfo : muinfos) {
        usage = 0.0;
        if (uinfo.getJoindate() != null || muinfo.getLBilldate() != null) {
        bp = mulogic.findBillPeriods(uinfo.getJoindate(), muinfo);
        usage = m.findUsage(bp, uinfo.getMid());

        limit = muinfo.getLimit();
        muinfo.setLimit(limit);
        muinfo.setUsage(usage);
        }
        }*/
        for (int i = 0; i < muinfos.size(); i++) {
            MinimumUsageInfo muinfo = muinfos.get(i);
            usage = 0.0;
            if (uinfo.getJoindate() != null || muinfo.getLBilldate() != null) {
                bp = mulogic.findBillPeriods(uinfo.getJoindate(), muinfo);
                usage = m.findUsage(bp, uinfo.getMid());

                limit = muinfo.getLimit();
                muinfo.setLimit(limit);
                muinfo.setUsage(usage);
            }
            else
            {
                String mname=muinfo.getMname();
                muinfosTemp.remove(i);
               
                msg=msg+mname+" ";

            }
        }
        if(!"".equals(msg))
        {
             msg="Unable to process your request for "+msg+" minusages";
        }
        request.setAttribute("msg",msg);
        request.getSession().setAttribute("muinfo", muinfosTemp);
        return mapping.findForward(SUCCESS);
    }
}
