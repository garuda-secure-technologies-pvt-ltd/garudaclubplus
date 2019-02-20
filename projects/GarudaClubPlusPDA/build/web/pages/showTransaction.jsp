<%@page import="com.openbravo.pos.ticket.TransactionInfo,java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib  prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title> <bean:message key="last5trans"/> </title>
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>



    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus"  /><br>
        </center>
        <br/>

        <center>


            <%ArrayList<TransactionInfo> transactions = (ArrayList<TransactionInfo>) request.getAttribute("transInfo");
        String facility = "";
        String ttype = "";
        if(transactions.size()==0)
            out.println("<p>No Transactions</p>");
        else
            {
            %>

            <table >
                <tr><th>Date</th><th>TransNo</th><th>Type</th><th>Amt</th></tr>



                <%
        for (TransactionInfo t : transactions) {
            /*
            if (t.getFacility() == null) {
            facility = "";
            } else {
            facility = t.getFacility();
            }

            if (t.getTranstype().equals("C")) {
            ttype = "Receipt";
            } else if (t.getTranstype().equals("D")) {
            ttype = "Bill";
            }*/

                %>
                <tr>

                    <td align="left"><%=t.getTdate()%></td>
                    <td align="left"><%=t.getReceiptno()%></td>
                    <td align="left"><%=t.getFacility()%></td>
                    <%-- <td><%=facility%>&nbsp;<%=ttype%></td>--%>


                    <td align="right"><%=t.getAmount()%></td>

                </tr>
                <% }}%>

            </table>




            <br/>
            
            <a href='showHome.do'><img src="images/home.png" onmouseover="this.src='images/home_mo.png';" onmouseout="this.src='images/home.png';" alt="Home" /></a>
            <a href="logout.do"><img src="images/logoutm.png" onmouseover="this.src='images/logoutm_mo.png';" onmouseout="this.src='images/logoutm.png';" alt="LogOut"/> </a><br/>


        </center>
    </body>
</html>
