/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bci;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Ratan
 */
public class UploadData {

    public String sysDate = null;

    public boolean load(String filename, ArrayList al) {
        // List<CustomerInfoExt2> cusinfo=new ArrayList<CustomerInfoExt2>();
        boolean result = false;
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filename));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            int count = wb.getNumberOfSheets();
            for (int j = 0; j < count; j++) {
                HSSFSheet sheet = wb.getSheetAt(j);
                HSSFRow row;

                HSSFCell cell;
                //HSSFCell cell1;
                //HSSFCell cell2;

                int rows; // No of rows
                rows = sheet.getPhysicalNumberOfRows();

                if (rows != 0) {
                    int cols = 0; // No of columns
                    int tmp = 0;

                    int nfcount = sheet.getRow(0).getPhysicalNumberOfCells();
                    int ffcount = (Integer) al.get(0);
                    String memno=sheet.getRow(0).getCell((short)0).getRichStringCellValue().getString();
                    String memtype=sheet.getRow(0).getCell((short)1).getRichStringCellValue().getString();
                    String memname=sheet.getRow(0).getCell((short)3).getRichStringCellValue().getString();
                    String gender=sheet.getRow(0).getCell((short)4).getRichStringCellValue().getString();
                    String memcat=sheet.getRow(0).getCell((short)5).getRichStringCellValue().getString();

                    String mno=(String)al.get(1);
                    String mtype=(String)al.get(2);
                    String mname=(String)al.get(3);
                    String mgender=(String)al.get(4);
                    String mcat=(String)al.get(5);


                    // This trick ensures that we get the data properly even if it doesn't start from first few rows
                    for (int i = 0; i < 10 || i < rows; i++) {
                        row = sheet.getRow(i);
                        if (row != null) {
                            tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                            if (tmp > cols) {
                                cols = tmp;
                            }
                        }
                    }
                    //Session ses=new Session(filename, tmp, filename);
                    if (ffcount == nfcount && mno.equals(memno) && mtype.equals(memtype) && mname.equals(memname) && mgender.equals(gender) && mcat.endsWith(memcat) ) {
                        for (int r = 1; r < rows; r++) {
                            //    for (int r = 1; r < 20; r++) {
                            row = sheet.getRow(r);
                            String memNum = null;
                            String memName = null;
                            String memType = null;
                            String Gender = null;
                            String memCategory = null;
                            if (row != null) {
                                //  for(int c = 0; c < cols; c++) {
                                cell = row.getCell((short) 0);
                                if (cell != null) {
                                    memNum = cell.getRichStringCellValue().getString();
                                }
                                cell = row.getCell((short) 1);
                                if (cell != null) {
                                    memName = cell.getRichStringCellValue().getString();
                                }
                                cell = row.getCell((short) 3);
                                if (cell != null) {
                                    memType = cell.getRichStringCellValue().getString();
                                }
                                cell = row.getCell((short) 4);
                                if (cell != null) {
                                    Gender = cell.getRichStringCellValue().getString();
                                }
                                cell = row.getCell((short) 5);
                                if (cell != null && cell.getRichStringCellValue().length()==1) {
                                    memCategory = cell.getRichStringCellValue().getString();
                                }
                                int flag = 0;
                                if ((memName != null && memNum != null && memType != null && Gender != null)) {
                                    result=true;
                                    System.out.println(memNum + "" + memName + " " + memType + "" + Gender + "" + memCategory);
                                    String testStr = memNum + " - " + memCategory;
                                    Connection memCon = BCIMainFrame.eSession.getConnection();
                                    String qry = "Select memname from membermasters where memnumber=?";
                                    PreparedStatement memPstm = memCon.prepareStatement(qry);
                                    memPstm.setString(1, memNum);
                                    ResultSet memRs = memPstm.executeQuery();
                                    ArrayList<String> alist = new ArrayList<String>();
//                                    while (memRs.next()) {
//                                        alist.add(memRs.getString(1));
//                                    }
                                    if (!memRs.next()) {
                                        newRegistratios(memNum, memName, memType, Gender, memCategory);
                                    } else {
                                        System.out.println(memNum + "-Alredyregisterd!!");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Error in File Content ");
                                    result = false;
                                }
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect File Format ");
                    }
                }

            }

        } catch (Exception ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error Occured While Loading");
            result = false;
        }
        return result;
    }

    public void newRegistratios(String memNum, String memName, String memType, String Gender, String memCategory) {

        try {
            Connection conn = BCIMainFrame.eSession.getConnection();
            Connection esslVConn = BCIMainFrame.esslv3Connection.getConnection();

            SimpleDateFormat Dformat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss.mmm");
            Date date = new Date();
            sysDate = Dformat.format(date);



            PreparedStatement pstm = esslVConn.prepareStatement("SELECT Name FROM Department");

            ResultSet categoryRs = pstm.executeQuery();
            ArrayList<String> CategoryName = new ArrayList();
            while (categoryRs.next()) {
                CategoryName.add(categoryRs.getString(1));
            }
            categoryRs.close();
            if (!CategoryName.contains(memName)) {

                String deptInQry = "INSERT INTO Department ([NAME],createdById,CreatedDate,ModifiedById,ModifiedDate) VALUES (?,1,?,1,?)";
                PreparedStatement esslpstmDptInsert = esslVConn.prepareStatement(deptInQry);
                esslpstmDptInsert.setString(1, memName);
                esslpstmDptInsert.setString(2, sysDate);
                esslpstmDptInsert.setString(3, sysDate);
                esslpstmDptInsert.executeUpdate();

            }

            // V3 insert
            PreparedStatement esslVSecectDpt = esslVConn.prepareStatement("SELECT ID FROM DEPARTMENT WHERE NAME='" + memName + "'");
            ResultSet dptIDRs = esslVSecectDpt.executeQuery();
            String dptID = null;
            while (dptIDRs.next()) {
                dptID = dptIDRs.getString(1);

            }

            String[] EmpcodeArr = memNum.split("-");
            String staffOne = EmpcodeArr[0];
            String staffTwo = EmpcodeArr[1];
            char EmpGenderCh = Gender.charAt(0);
            String EmpGenderChStr = Character.toString(EmpGenderCh);

            // Device staffcode modification
            StringBuffer staffCodeBufferOne = new StringBuffer(staffOne);
            StringBuffer staffCodeBufferTwo = new StringBuffer(staffTwo);
            StringBuffer staffCodeCatch = new StringBuffer();
            staffCodeBufferOne.insert(0, memCategory);


            if (staffCodeBufferOne.charAt(0) == 'M') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "1");
            } else if (staffCodeBufferOne.charAt(0) == 'S') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "2");
            } else if (staffCodeBufferOne.charAt(0) == 'A') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "3");
            } else if (staffCodeBufferOne.charAt(0) == 'B') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "4");
            } else if (staffCodeBufferOne.charAt(0) == 'C') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "5");
            } else if (staffCodeBufferOne.charAt(0) == 'D') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "6");
            } else if (staffCodeBufferOne.charAt(0) == 'E') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "7");
            } else if (staffCodeBufferOne.charAt(0) == 'F') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "8");
            } else if (staffCodeBufferOne.charAt(0) == 'G') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "9");
            } else if (staffCodeBufferOne.charAt(0) == 'H') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "10");
            } else if (staffCodeBufferOne.charAt(0) == 'I') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "11");
            } else if (staffCodeBufferOne.charAt(0) == 'J') {
                staffCodeCatch.append(memberCodeCreate(staffOne, staffCodeBufferTwo));
                staffCodeBufferTwo.delete(0, staffCodeBufferTwo.length());
                staffCodeBufferTwo.append(staffCodeCatch);
                staffCodeBufferTwo.insert(0, "12");
            }

            System.out.println(staffCodeBufferTwo);
            String DeviceStaffCode = staffCodeBufferTwo.toString();

            String essVInsertStaff = "INSERT INTO STAFF (Name,departmentid,staffcode,staffcode1,staffcode2,devicecode,gender,doj,doc,createdDate,ModifiedDate,stafflatestpunchtime,CreatedById,ModifiedById,specialHolidayGroupId,Designation) values (?,?,?,?,?,?,?,?,?,?,?,?,2,2,2,?)";
            PreparedStatement esslVpstSt = esslVConn.prepareStatement(essVInsertStaff);
            esslVpstSt.setString(1, memType);
            esslVpstSt.setString(2, dptID);  //int
            esslVpstSt.setString(3, memNum + " - " + memCategory);
            esslVpstSt.setString(4, staffOne);
            esslVpstSt.setString(5, DeviceStaffCode); //int
            esslVpstSt.setString(6, DeviceStaffCode); //int
            esslVpstSt.setString(7, EmpGenderChStr);
            esslVpstSt.setString(8, sysDate);
            esslVpstSt.setString(9, sysDate);
            esslVpstSt.setString(10, sysDate);
            esslVpstSt.setString(11, sysDate);
            esslVpstSt.setString(12, sysDate);
            esslVpstSt.setString(13, "Member");
            esslVpstSt.executeUpdate();

            String essVInsertBio = "INSERT INTO StaffBiometricDetails (DeviceStaffCode,privilege,password,Fp1,companyId) values (?,0,'0',?,1)";
            PreparedStatement esslVBioPstm = esslVConn.prepareStatement(essVInsertBio);
            esslVBioPstm.setString(1, DeviceStaffCode);
            esslVBioPstm.setString(2, "");
            esslVBioPstm.executeUpdate();


            //FingerPrint DataBase Connection

            String esslStaffId = "Select id from staff where DeviceCode ='" + staffCodeBufferTwo + "'";
            PreparedStatement esslStaffidPstm = esslVConn.prepareStatement(esslStaffId);
            ResultSet esslStaffIdRs = esslStaffidPstm.executeQuery();
            String esslStaffIdName = null;

            while (esslStaffIdRs.next()) {
                esslStaffIdName = esslStaffIdRs.getString(1);
            }
            esslStaffIdRs.close();

            PreparedStatement FpPstm = conn.prepareStatement("Insert Into MemberMasters (MemNumber,MemName,DeviceStaffCode,Fp1,FpImage,CardNumberMain,CardNumber,V3ID,FPcheck,memType,Active) Values (?,?,?,?,?,?,?,?,?,?,?)");
            FpPstm.setString(1, memNum + " - " + memCategory);
            FpPstm.setString(2, memType);
            FpPstm.setString(3, DeviceStaffCode);
            FpPstm.setString(4, "");
            FpPstm.setString(5, "");
            FpPstm.setString(6, "");
            FpPstm.setInt(7, 0);
            FpPstm.setString(8, esslStaffIdName);
            FpPstm.setString(9, "False");
            FpPstm.setString(10, memCategory);
            FpPstm.setString(11, "1");
            FpPstm.executeUpdate();

        } catch (Exception ex) {
            System.out.println("unable to connect Database!!" + ex);
        }

    }

    public StringBuffer memberCodeCreate(String staffOne, StringBuffer staffCodeBufferTwo) {
        try {
            if (staffOne.length() == 1) {
                if (staffOne.startsWith("A")) {
                    staffCodeBufferTwo.insert(0, "11");
                } else if (staffOne.startsWith("B")) {
                    staffCodeBufferTwo.insert(0, "12");
                } else if (staffOne.startsWith("C")) {
                    staffCodeBufferTwo.insert(0, "13");
                } else if (staffOne.startsWith("D")) {
                    staffCodeBufferTwo.insert(0, "14");
                } else if (staffOne.startsWith("E")) {
                    staffCodeBufferTwo.insert(0, "15");
                } else if (staffOne.startsWith("F")) {
                    staffCodeBufferTwo.insert(0, "16");
                } else if (staffOne.startsWith("G")) {
                    staffCodeBufferTwo.insert(0, "17");
                } else if (staffOne.startsWith("H")) {
                    staffCodeBufferTwo.insert(0, "18");
                } else if (staffOne.startsWith("I")) {
                    staffCodeBufferTwo.insert(0, "19");
                } else if (staffOne.startsWith("J")) {
                    staffCodeBufferTwo.insert(0, "20");
                } else if (staffOne.startsWith("K")) {
                    staffCodeBufferTwo.insert(0, "21");
                } else if (staffOne.startsWith("L")) {
                    staffCodeBufferTwo.insert(0, "22");
                } else if (staffOne.startsWith("M")) {
                    staffCodeBufferTwo.insert(0, "23");
                } else if (staffOne.startsWith("N")) {
                    staffCodeBufferTwo.insert(0, "24");
                } else if (staffOne.startsWith("O")) {
                    staffCodeBufferTwo.insert(0, "25");
                } else if (staffOne.startsWith("P")) {
                    staffCodeBufferTwo.insert(0, "26");
                } else if (staffOne.startsWith("Q")) {
                    staffCodeBufferTwo.insert(0, "27");
                } else if (staffOne.startsWith("R")) {
                    staffCodeBufferTwo.insert(0, "28");
                } else if (staffOne.startsWith("S")) {
                    staffCodeBufferTwo.insert(0, "29");
                } else if (staffOne.startsWith("T")) {
                    staffCodeBufferTwo.insert(0, "30");
                } else if (staffOne.startsWith("U")) {
                    staffCodeBufferTwo.insert(0, "31");
                } else if (staffOne.startsWith("V")) {
                    staffCodeBufferTwo.insert(0, "32");
                } else if (staffOne.startsWith("W")) {
                    staffCodeBufferTwo.insert(0, "33");
                } else if (staffOne.startsWith("X")) {
                    staffCodeBufferTwo.insert(0, "34");
                } else if (staffOne.startsWith("Y")) {
                    staffCodeBufferTwo.insert(0, "35");
                } else {
                    staffCodeBufferTwo.insert(0, "36");
                }
            } else {
                staffCodeBufferTwo.insert(0, "1113");
            }
        } catch (Exception e) {
            System.out.println("mamber code create error!!" + e);
        }
        return staffCodeBufferTwo;
    }
}
