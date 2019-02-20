<%@ page  import="com.openbravo.pos.ticket.OtherFacility,com.openbravo.pos.pda.struts.forms.ActivateFacilityForm, java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib  uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib  uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>
        <title>OtherFacilitis</title>

    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus" class="logo" /><br/>

            <logic:messagesPresent >
                <html:messages id="msg">
                    <p>
                        <strong><center><font color="red" size="-1"><bean:write name="msg" /></font></center></strong>
                    </p>
                </html:messages>
            </logic:messagesPresent>

        </center>
        <br/>
        <form name="ActivateFacilityForm" action="activatefacility.do" method="post">
            <center>

                <%ArrayList<OtherFacility> othfac = (ArrayList<OtherFacility>) request.getAttribute("otherfacility");

                %>
                <table border="2" >
                    <tr><th></th><th>Fac.Name</th><th title="Joining Amount">JnAt</th><th title="Renewal Amount">Rn.Amt</th></tr>



                    <% ActivateFacilityForm afform = null;
                     
                        for (OtherFacility o : othfac) {
                            
                        %>
                    <tr>
                        <td><input type="checkbox"  name="fid" value="<%=o.getFId()%>" ></td>


                        <td title="<%=o.getFNmae()%>"><%=o.getFacilityName()%></td>

                        <td><%=o.getJoiningAmt()%></td>
                        <td><%=o.getRenewalAmt()%></td>

                    </tr>
                    <% }%>

                </table>              

                <table border="0">
                    <tr> <td><input type="text" name="date" size="7" maxlength="10"/></td>
                    <td><input type="submit" value="activate" name="submit"></td></tr>
                </table>
                <table>
                    <tr align="left" >(dd/mm/yyyy)</tr>
                </table>
            </center>

        </form>
        <center>
            <a href="back1.do" ><img src="images/backm.png" onmouseover="this.src='images/backm_mo.png';" onmouseout="this.src='images/backm.png';" alt="Back" /></a>
            <a href="subscribedabbrivation.do" ><img src="images/abbriv.png" onmouseover="this.src='images/abbriv_mo.png';" onmouseout="this.src='images/abbriv.png';" alt="Abbriv" /></a>
            <a href="logout.do"><img src="images/logoutm.png" onmouseover="this.src='images/logoutm_mo.png';" onmouseout="this.src='images/logoutm.png';" alt="LogOut"/> </a>
        </center>
    </body>
</html>


