package com.openbravo.pos.pda.bo;

import com.openbravo.pos.pda.dao.AbbrivationDAO; 
import com.openbravo.pos.pda.dao.ActivateFacilityDAO;
import com.openbravo.pos.pda.dao.ActiveFacilityDAO;
import com.openbravo.pos.pda.dao.BalanceDAO;
import com.openbravo.pos.pda.dao.CancelRequestDAO;
import com.openbravo.pos.pda.dao.DeactivateFacilityDAO;
import com.openbravo.pos.pda.dao.EventsDAO;
import com.openbravo.pos.pda.dao.LoginDAO;
import com.openbravo.pos.pda.dao.MinUsageDAO;
import com.openbravo.pos.pda.dao.OtherFacilityDAO;
import com.openbravo.pos.pda.dao.PasswordResetDAO;
import com.openbravo.pos.pda.dao.TransactionDAO; 
import com.openbravo.pos.ticket.AbbrivationInfo;
import com.openbravo.pos.ticket.ActivateInfo;
import com.openbravo.pos.ticket.ActiveFacility;
import com.openbravo.pos.ticket.BalanceInfo;
import com.openbravo.pos.ticket.BillPeriods;
import com.openbravo.pos.ticket.DeactivateInfo;
import com.openbravo.pos.ticket.EventsInfo;
import com.openbravo.pos.ticket.MinimumUsageInfo;
import com.openbravo.pos.ticket.OtherFacility;
import com.openbravo.pos.ticket.TransactionInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.util.Date;
import java.util.List;

public class Helper {

    private LoginDAO login;
    private BalanceDAO balance;
    private TransactionDAO trans;
    private MinUsageDAO mudao;
    private EventsDAO edao;
    private ActiveFacilityDAO actfaci;
    private OtherFacilityDAO othfaci;
    private DeactivateFacilityDAO deatfac;
    private ActivateFacilityDAO actfac;
    private AbbrivationDAO abbri;
    private CancelRequestDAO calre;
    private PasswordResetDAO prdao;

    public void changePassword(String memid, String memname, String newPass) {
        prdao = new PasswordResetDAO();
        prdao.changePassword(memid, memname, newPass);
    }

    public UserInfo findUser(String aLogin, String password) {
        login = new LoginDAO();

        return login.findUser(aLogin, password);
    }

    public BalanceInfo findBalance(String accid) {
        balance = new BalanceDAO();

        return balance.findbalance(accid);
    }

    public double findUsage(BillPeriods bp, String mid) {
        mudao = new MinUsageDAO();
        return mudao.findUsage(bp, mid);
    }

    public List<MinimumUsageInfo> findMinUsage(String mid) {
        mudao = new MinUsageDAO();
        return mudao.findMinUsage(mid);

    }

    public List<MinimumUsageInfo> findMinUsageHsql(String mid) {
        mudao = new MinUsageDAO();
        return mudao.findMinUsageHsql(mid);

    }

    public List<TransactionInfo> findLastTransactions(String accid) {
        trans = new TransactionDAO();
        return trans.findLastTransactions(accid);
    }

    public List<EventsInfo> findEvents() {
        edao = new EventsDAO();
        return edao.findEvents();
    }

    public DeactivateInfo deactivateFacility(String[] mfuId, Date date, String cid) {

        deatfac = new DeactivateFacilityDAO();

        return deatfac.deactivateFacility(mfuId, date, cid);
    }
    public DeactivateInfo deactivateFacilityHsql(String[] mfuId, Date date, String cid) {

        deatfac = new DeactivateFacilityDAO();

        return deatfac.deactivateFacilityHsql(mfuId, date, cid);
    }

    public ActivateInfo activateFacility(String[] fId, Date date, String memid) {

        actfac = new ActivateFacilityDAO();

        return actfac.activateFacility(fId, date, memid);

    }
    public ActivateInfo activateFacilityHsql(String[] fId, Date date, String memid) {

        actfac = new ActivateFacilityDAO();

        return actfac.activateFacilityHsql(fId, date, memid);

    }

    public List<ActiveFacility> findActiveFacility(String memId) {
        actfaci = new ActiveFacilityDAO();

        return (List<ActiveFacility>) actfaci.findActiveFacility(memId);
    }
    public List<ActiveFacility> findActiveFacilityHsql(String memId) {
        actfaci = new ActiveFacilityDAO();

        return (List<ActiveFacility>) actfaci.findActiveFacilityHsql(memId);
    }

    public List<OtherFacility> findOtherFacility(String memId) {
        othfaci = new OtherFacilityDAO();

        return (List<OtherFacility>) othfaci.findOtherFacility(memId);
    }
    public List<OtherFacility> findOtherFacilityHsql(String memId) {
        othfaci = new OtherFacilityDAO();

        return (List<OtherFacility>) othfaci.findOtherFacilityHsql(memId);
    }

    public List<AbbrivationInfo> findAbbrivation() {
        abbri = new AbbrivationDAO();

        return (List<AbbrivationInfo>) abbri.findAbbrivation();

    }
    public List<AbbrivationInfo> findAbbrivationHsql() {
        abbri = new AbbrivationDAO();

        return (List<AbbrivationInfo>) abbri.findAbbrivationHsql();

    }


    public int cancelRequestInfo(String mfuid) {
        calre = new CancelRequestDAO();     
       return calre.deleteRequestedFacility(mfuid);
        }

    public int cancelRequestInfoHsql(String mfuid) {
        calre = new CancelRequestDAO();
       return calre.deleteRequestedFacilityHsql(mfuid);
        }

    }


