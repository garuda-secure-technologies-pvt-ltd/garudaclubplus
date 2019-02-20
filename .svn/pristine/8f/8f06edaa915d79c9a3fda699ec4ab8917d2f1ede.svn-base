<%@page import="com.openbravo.pos.ticket.DeactivateInfo,java.util.HashMap,java.util.ArrayList"  %>
<%@taglib  uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib  uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib  uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>
        <title>DeactivateFacility</title>
    </head>
    <body>
        <center>
            
            <img src="images/logo.png" alt="Garuda Club Plus" class="logo" /><br/><br/>

            <%DeactivateInfo dinfo = (DeactivateInfo) request.getAttribute("deinfo");

        String[] msges = dinfo.getMsg();
        String[] succ = dinfo.getSucc();
        

        HashMap<String, String> map1 = (HashMap<String, String>) request.getSession().getAttribute("map1");


        /*if (stan != null && stan.length>0) {
        out.println("<font color='red'>sorry!!!!<br/>");
        for (String s : stan) {
        if (s != null) {
        out.println(map1.get(s) + "<br/>");
        }
        }
        out.println("is a standard facility...u cannot deactivate it<br/>");
        }
        if (msges != null && msges.length>0) {
        out.println("<font color='red'>you already sent a request for<br/>");
        for (String msg : msges) {
        if (msg != null) {
        out.println(map1.get(msg) + "<br/>");
        }
        }
        out.println("facilities  </font><br/>");
        }
        if (succ != null && succ.length>0) {
        out.println("<font color='red'>your request for<br/>");
        for (String msg : succ) {
        if (msg != null) {
        out.println(map1.get(msg) + "<br/>");
        }
        }
        out.println("facilities are sent to  manager approval </font><br/>");
        }*/
         if (msges[0] != null && succ[0] == null ) {
        out.println("<font color='red'>you already sent a request for<br/>");
        for (String msg : msges) {
        if (msg != null) {
        out.println(map1.get(msg) + "<br/>");
        }
        }
        out.println("facility  </font><br/>");

        } else if (succ[0] != null && msges[0] == null ) {
        out.println("<font color='red'>your request for<br/>");
        for (String msg1 : succ) {
        if (msg1 != null) {
        out.println(map1.get(msg1) + "<br/>");
        }
        }
        out.println("facility are sent to  manager approval </font>");

        } else if (msges[0] != null && succ[0] != null ) {
        out.println("<font color='red'>you already sent a request for<br/>");
        for (String msg : msges) {
        if (msg != null) {
        out.println(map1.get(msg) + "<br/>");
        }
        }
        out.println("facilities  </font><br/>");
        out.println("<font color='red'>your request for<br/>");
        for (String msg1 : succ) {
        if (msg1 != null) {
        out.println(map1.get(msg1) + "<br/>");
        }
        }
        out.println("facilities are sent to  manager approval </font>");

        } 

            %>

            <div >
                <br/><a href="showActiveFacility.do"><img src="images/goback.png" onmouseover="this.src='images/goback_mo.png';" onmouseout="this.src='images/goback.png';" alt="GoBack" /></a>
            
            </div>
        </center>

    </body>
</html>
