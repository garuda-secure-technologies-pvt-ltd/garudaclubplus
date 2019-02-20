<%@page import="com.openbravo.pos.ticket.AbbrivationInfo,java.util.ArrayList" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib  uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib  uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>
        <title>Abbrivation</title>
    </head>
    <body>
        <center>

            <img src="images/logo.png"  alt="Garuda Club Plus" class="logo" /><br/><br/>
            <% ArrayList<AbbrivationInfo> abbInfo = (ArrayList<AbbrivationInfo>) request.getAttribute("abbinfo");
            %>
            <table border="2">

                <tr><th>ShortForms</th><th>Abbrivations</th></tr>
                <% for (AbbrivationInfo a : abbInfo) {
                %>
                <tr>
                    <td><%=a.getSmsForm()%></td>
                    <td><%=a.getFName()%></td>
                </tr>
                <%
        }

                %>
                <tr><td>Fac.Name</td><td>Facility Name</td></tr>
                <tr><td>JnAt</td><td>Joining Amount</td></tr>
                <tr><td>Rn.Amt</td><td>Renewal Amount</td></tr>
            </table><br/>
   
            <a href="showActiveFacility.do"><img src="images/backm.png" onmouseover="this.src='images/backm_mo.png';" onmouseout="this.src='images/backm.png';" alt="Back" /></a>
        </center>
    </body>
</html>