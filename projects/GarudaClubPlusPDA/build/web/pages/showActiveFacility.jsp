<%@page import="com.openbravo.pos.ticket.ActiveFacility,java.util.ArrayList,java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib  uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib  uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
    <link rel="SHORTCUT ICON" href="images/favicon.ico">
    <meta name = "viewport" content = "user-scalable=no, width=device-width">
    <link rel=StyleSheet href="style.css" type='text/css' media=screen>
    
        

</head>
<body>
<center>
    <img src="images/logo.png"  alt="Garuda Club Plus" class="logo" /><br/>

    <logic:messagesPresent >
        <html:messages id="msg">
            <p>
                <strong><center><font color="red" size="-1"><bean:write name="msg" /></font></center></strong>
            </p>
        </html:messages>
    </logic:messagesPresent>

</center>
<br/>
<form name="DeactivateFacilityForm" action="deactivatefacility.do" method="post" >
<center>

    <%ArrayList<ActiveFacility> actfac = (ArrayList<ActiveFacility>) request.getAttribute("activefacility");
        HashMap<String, String> hashmap = (HashMap<String, String>) request.getSession().getAttribute("map1");
int i=0;
        if (actfac.size() == 0) {
            out.println("U are not subscribed any Facilities");
        } else {
    %>
    
    <table border="2">

        <tr><td colspan="4" align="center">Standard Facilities</td></tr>
        <tr><th></th><th title="FacilityName">Fac.Name</th><th title="Renewal Amount">Rn.Amt</th><th title="Status">Status</th></tr>
        <%  for (ActiveFacility a : actfac) {
            if (a.getType().equals("Standard")) {
                a.setMfuId("standard");
        %>
        <tr>
            <td></td>
            <td title="<%=a.getFName()%>"><%=a.getFacilityName()%></td>
            <td><%=a.getRenewalAmount()%></td>
            <td><%=a.getStatus()%> </td>
        </tr>
        <%
            }
            else {
                i=i+1;
                }
        }
        if(i>=1)
        %>

        <tr><td colspan="4" align="center">Opt.Facilities</td></tr>
        <tr><th></th><th title="FacilityName">Fac.Name</th><th title="Renewal Amount">Rn.Amt</th><th title="Status">Status</th></tr>

        <%
        for (ActiveFacility a : actfac) {
            if (a.getType().equals("Optional")) {
                if ("Member".equals(a.getDName())) {
                    a.setDName("");
                }
        %>
        <tr>
            <td><input type="checkbox"  name="mfuid" value="<%=a.getMfuId()%>" ></td>
            <td title="<%=a.getFName()%>"><%=a.getFacilityName()%>,<%=a.getDName()%></td>
            <td><%=a.getRenewalAmount()%></td>
            <%if (a.getStatus().equals("In Usage")) {%>
            <td><%=a.getStatus()%> </td>
            <% } else {
    request.setAttribute("mfuid", a.getMfuId());%>
            <td> <%=a.getStatus()%><a href=cancel.do?id=<%=a.getMfuId()%> ><p><font colour="blue">Cancel</font></p></a> </td>
            <%}%>
        </tr>
        <%
                }
            }
        }
        %>

    </table><br/>

    <table border="0">
        <tr> <td><input type="text" name="date" size="7" maxlength="10"/>
            
        <td><input type="submit" value="Submit" name="submit" /></td></tr>
    </table>
    <table>
        <tr align="left" >(dd/mm/yyyy)</tr>
    </table>


</center>
</form>
<center>
    <a href="showFacilities.do" ><img src="images/backm.png" onmouseover="this.src='images/backm_mo.png';" onmouseout="this.src='images/backm.png';" alt="Back" /></a>
    <a href="subscribedabbrivation.do" ><img src="images/abbriv.png" onmouseover="this.src='images/abbriv_mo.png';" onmouseout="this.src='images/abbriv.png';" alt="Abbriv" /></a>
    <a href="logout.do"><img src="images/logoutm.png" onmouseover="this.src='images/logoutm_mo.png';" onmouseout="this.src='images/logoutm.png';" alt="LogOut"/> </a>
</center>
</body>
</html>
