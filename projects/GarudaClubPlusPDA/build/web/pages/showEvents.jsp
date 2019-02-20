<%@page  import="com.openbravo.pos.ticket.EventsInfo,java.util.ArrayList,java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib  prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title> <bean:message key="events"/> </title>
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>

        
        <script type="text/javascript">
            document.getElementById(text1).title="New tooltip"
        </script>
    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus"  /><br>
        </center>

        <center>


            <% ArrayList<EventsInfo> eventsinf = (ArrayList<EventsInfo>) request.getAttribute("eventsInfo");
                
            %>





            <%
        String temp = null;
        for (EventsInfo eventinfo : eventsinf) {

            if (temp == null) {
                out.println("<table>");
                out.println("<thead>" + eventinfo.getEdate() + "</thead>");
                out.println("<tr><th>Name</th><th>Description</th><th title='StartTime'>STime</th><th title='EndTime'>ETime</th></tr>");
                out.println("<tr>");
                out.println("<td>" + eventinfo.getEname() + "</td>");
                out.println("<td>" + eventinfo.getDescription() + "</td>");
                out.println("<td>" + eventinfo.getStime() + "</td>");
                out.println("<td>" + eventinfo.getEtime() + "</td>");
                out.println("</tr>");
                temp = eventinfo.getEdate();
            } else if (temp.equals(eventinfo.getEdate())) {
                out.println("<tr>");
                out.println("<td>" + eventinfo.getEname() + "</td>");
                out.println("<td>" + eventinfo.getDescription() + "</td>");
                out.println("<td>" + eventinfo.getStime() + "</td>");
                out.println("<td>" + eventinfo.getEtime() + "</td>");
                out.println("</tr>");
                temp = eventinfo.getEdate();
            } else {
                out.println("</table>");
                out.println("<table>");
                out.println("<thead>" + eventinfo.getEdate() + "</thead>");
                out.println("<tr><th>Name</th><th>Description</th><th title='StartTime'>STime</th><th title='EndTime'>ETime</th></tr>");
                out.println("<tr>");
                out.println("<td>" + eventinfo.getEname() + "</td>");
                out.println("<td>" + eventinfo.getDescription() + "</td>");
                out.println("<td>" + eventinfo.getStime() + "</td>");
                out.println("<td>" + eventinfo.getEtime() + "</td>");
                out.println("</tr>");
                temp = eventinfo.getEdate();
            }
        }
        out.println("</table>");
        if(temp==null)
                 out.println("<p>No Events </p>");
            %>





           <a href='showHome.do'><img src="images/home.png" onmouseover="this.src='images/home_mo.png';" onmouseout="this.src='images/home.png';" alt="Home" /></a>
            <a href="logout.do"><img src="images/logoutm.png" onmouseover="this.src='images/logoutm_mo.png';" onmouseout="this.src='images/logoutm.png';" alt="LogOut"/> </a><br/>

        </center>
    </body>
</html>
