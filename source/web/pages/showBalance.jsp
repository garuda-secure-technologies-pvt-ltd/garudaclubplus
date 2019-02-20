<%@page  import="com.openbravo.pos.ticket.BalanceInfo,com.openbravo.pos.ticket.MinimumUsageInfo,java.util.ArrayList" %>
<%@taglib  prefix="bean" uri="/WEB-INF/struts-bean.tld" %>

<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title><bean:message key="balance" /></title>
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>


    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus"  /><br>
        </center>

        <center>

            <div >
                <%BalanceInfo binfo = (BalanceInfo) request.getSession().getAttribute("binfo");

                %>
                <p>Welcome ${binfo.name},<br>
                Mem.No: ${binfo.searchKey}</p>
                <%String cORd = null;
        if (binfo.getBal() > 0) {
            cORd = "CREDIT";
        } else {
            cORd = "DEBIT";
        }
                %>
                <strong>Balance:Rs.<% out.print(Math.abs((float) binfo.getBal()));%>&nbsp;<%=cORd%> </strong>

            </div>

            <br/>
            <a href='showHome.do'><img src="images/home.png" onmouseover="this.src='images/home_mo.png';" onmouseout="this.src='images/home.png';" alt="Home" /></a>
            <a href='showTrans.do'><img src="images/transactions.png" onmouseover="this.src='images/transactions_mo.png';" onmouseout="this.src='images/transactions.png';" alt="Transactions" /></a>
            
        </center>
    </body>
</html>
