package com.openbravo.pos.pda.struts.actions;

import java.util.List;
import java.util.Map;

import org.apache.commons.validator.Constant;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.jdbc.core.JdbcTemplate;

import com.openbravo.pos.pda.dao.JdbcDaoImpl;
import com.openbravo.pos.pda.struts.Constants;
import com.openbravo.pos.pda.struts.beans.Advertisement;
import com.openbravo.pos.pda.struts.beans.CompanyInfo;
import com.openbravo.pos.pda.struts.beans.PastEvents;
import com.openbravo.pos.pda.struts.beans.UpcomingEvents;
import com.openbravo.pos.pda.struts.beans.UserInfo;
import com.opensymphony.xwork2.ActionSupport;

public class LogOutAction extends ActionSupport implements SessionAware {
	
	private Map session;
	private UserInfo bean;
	private CompanyInfo cInfo = null;
    private JdbcTemplate jdbcTemplate;
    private List<UpcomingEvents> upcom;
    private List<PastEvents> events;
    private List<Advertisement> advt;
    private String Validation = "";
    
    public LogOutAction() {
    	JdbcDaoImpl jdbcImp = new JdbcDaoImpl();
        jdbcTemplate = jdbcImp.getJdbcTemplate();
	}
    
    
    @Override
    public String execute() throws Exception {
    	cInfo = (CompanyInfo) session.get("home");
		setUpcom(cInfo.getUpcmngEvts());
		setEvents(cInfo.getPastEvt());
		setAdvt(cInfo.getAdvt());
    	
		session.clear();
		session.put("home", cInfo);
		
    	return Constants.SUCCESS;
    }

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public CompanyInfo getcInfo() {
		return cInfo;
	}

	public void setcInfo(CompanyInfo cInfo) {
		this.cInfo = cInfo;
	}

	public List<UpcomingEvents> getUpcom() {
		return upcom;
	}

	public void setUpcom(List<UpcomingEvents> upcom) {
		this.upcom = upcom;
	}

	public List<PastEvents> getEvents() {
		return events;
	}

	public void setEvents(List<PastEvents> events) {
		this.events = events;
	}

	public List<Advertisement> getAdvt() {
		return advt;
	}

	public void setAdvt(List<Advertisement> advt) {
		this.advt = advt;
	}
    
    

    
}
