/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bci;

import DBConnection.Session;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;

/**
 *
 * @author swathi
 */
public class memberNumber {

    public static Session memSession;
    ArrayList<String> Amemlist = new ArrayList();
    public ArrayList<String> getmemberNumber(String memnum){
        try{
            Connection mConn = memSession.getConnection();
            String memNumber = "Select MemNumber from MemberMasters where memNumber like '"+memnum+"%'";
            PreparedStatement mpstm = mConn.prepareStatement(memNumber);
            ResultSet mRs = mpstm.executeQuery();
            
            while (mRs.next()) {
                Amemlist.add(mRs.getString(1));
            }
        }catch(Exception e){
            System.out.println("Cannot get memberNumber!!");
        }
        return Amemlist;
    }

  /*  public final ArrayList<String> getSubaccounts1(String accid) throws BasicException{
            return new StaticSentence(s
                           , "SELECT A.ID,A.NAME,A.SEARCHKEY,A.PARENT FROM ACCOUNTMASTER A WHERE A.PARENT=? AND A.LEVEL='S' AND A.OPERANDS IS NULL ORDER BY A.NAME"
                           , SerializerWriteString.INSTANCE
                           , new SerializerReadClass(AccountMaster.class)).list(accid);
        }  */

public String getmemberName(String memnum){
      String mName = null;
        try{
            Connection mConn = memSession.getConnection();
            String memNumber = "Select MemName,cardnumber from MemberMasters where memNumber like '"+memnum+"'";
            PreparedStatement mpstm = mConn.prepareStatement(memNumber);
            ResultSet mRs = mpstm.executeQuery();

            //String MCardnumber = null;
            while (mRs.next()) {
                mName = mRs.getString(1);
            }
        }catch(Exception e){
            System.out.println("Cannot get memberNumber!!");
        }
        return mName;
    }


}
