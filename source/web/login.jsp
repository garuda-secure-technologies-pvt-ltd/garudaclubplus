<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title><bean:message key="welcome.title"/></title>

        <html:base/>
    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="GarudaClubPlus PDA" /><br><br>
        </center>
        <logic:messagesPresent >
            <html:messages id="msg">
                <p>
                    <strong><center><font color="red" size="-1"><bean:write name="msg" /></font></center></strong>
                </p>
            </html:messages>
        </logic:messagesPresent>
        <html:form action="login.do" method="post">
            <center>
                <table class="pad">
                    <tbody>
                        <tr>
                            <td ><bean:message key="message.login" /></td>
                        </tr>
                        <tr>
                            <td><html:text property="login" size="13"/></td>
                        </tr>
                        <tr>
                            <td><bean:message key="message.password" /></td>
                        </tr>
                        <tr>
                            <td><html:password property="password" size="13"/></td>
                        </tr>
                        <tr>
                            <td><center><html:submit style="width:100px;"><bean:message key="button.login" /></html:submit></center></td>
                        </tr>
                    </tbody>
                </table>
            </center>
        </html:form>
    </body>
</html>
