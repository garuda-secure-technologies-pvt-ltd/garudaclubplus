<%@page import="com.openbravo.pos.ticket.MinimumUsageInfo,java.util.ArrayList"%>


<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title>Minimum Usage Info</title>
        <link rel=StyleSheet href="style.css" type='text/css' media=screen>
             

    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus" /><br>
        </center>
        <br/>
        <center>
        <%ArrayList<MinimumUsageInfo> minfos = (ArrayList<MinimumUsageInfo>) request.getSession().getAttribute("muinfo");
        double deficit = 0.0, limit = 0.0, usage = 0.0;
        String msg=(String)request.getAttribute("msg");

        if(minfos.size()==0)
            out.println("No Minimum Usages");

        else if (minfos.size() > 1) {
            out.println("<table >");
            out.println("<tr><th>MinUsage</th><th>Limit</th></tr>");
            for (MinimumUsageInfo minfo : minfos) {
                limit += minfo.getLimit();
                usage = minfo.getUsage();
                /*deficit = minfo.getLimit() - minfo.getUsage();
                if (deficit < 0) {
                deficit = 0.0;
                }*/


        %>
        <tr>
            <td><%=minfo.getMname()%></td>
            <td><%=minfo.getLimit()%></td>

        </tr>

        <% }
            deficit = limit - usage;
            if (deficit < 0) {
                deficit = 0.0;
            }

            // out.println("<tr ><td colspan='2'>Deficit =" + limit + "-" + usage + "=" + deficit + "</td></tr>");
            out.println("<tr ><td colspan='2' align='right'>" + limit + "</td></tr>");
            out.println("<tr ><td >Usage   </td><td>" + usage + "</td></tr>");
            out.println("<tr ><td >Deficit    </td><td>" + deficit + "</td></tr>");
            out.println("</table>");
        } else {
            out.println("<table>");
            out.println("<tr><th>MinUsage</th><th>Limit</th><th>Usage</th><th>Deficit</th></tr>");
            for (MinimumUsageInfo minfo : minfos) {

                deficit = minfo.getLimit() - minfo.getUsage();
                if (deficit < 0) {
                    deficit = 0.0;
                }


        %>
        <tr>
            <td><%=minfo.getMname()%></td>
            <td><%=minfo.getLimit()%></td>
            <td><%=minfo.getUsage()%></td>
            <td><%=deficit%></td>

        </tr>

        <% }


            out.println("</table>");
        }
        %>
        <br/>
        <%=msg%>
            <a href='showHome.do'><img src="images/home.png" onmouseover="this.src='images/home_mo.png';" onmouseout="this.src='images/home.png';" alt="Home" /></a>
            <a href="logout.do"><img src="images/logoutm.png" onmouseover="this.src='images/logoutm_mo.png';" onmouseout="this.src='images/logoutm.png';" alt="LogOut"/></a><br/>
        </center> 

    </body>
</html>
