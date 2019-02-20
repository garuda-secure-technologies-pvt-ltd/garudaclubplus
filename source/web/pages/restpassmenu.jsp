<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <link rel="SHORTCUT ICON" href="images/favicon.ico">
        <meta name = "viewport" content = "user-scalable=no, width=device-width">
        <title>Change password</title>

        <script language="javascript">
            function validate()
        {
            var str=true;
            document.getElementById("msg1").innerHTML="";
            document.getElementById("msg2").innerHTML="";
            document.getElementById("msg3").innerHTML="";
            var oldPass=document.frm.oldPass.value;
            var newPass=document.frm.newPass.value;
             var confirmPass=document.frm.confirmPass.value;

            if(oldPass=='')
            {
                document.getElementById("msg1").innerHTML="Old password required";
                str=false;
            }
            else if(oldPass.length<8)
            {
                document.getElementById("msg1").innerHTML="Old password can not be less than 8 characters.";
                str=false;
            }

            if(newPass=='')
            {
                document.getElementById("msg2").innerHTML="New Password required";
                str=false;
            }
            else if(newPass.length<8)
            {
                document.getElementById("msg2").innerHTML="New password can not be less than 8 characters.";
                str=false;
            }
            if(confirmPass=='')
            {
                document.getElementById("msg3").innerHTML="Confirm Password required";
                str=false;
            }
            else if(confirmPass.length<8)
            {
                document.getElementById("msg3").innerHTML="Confirm password can not be less than 8 characters.";
                str=false;
            }
            else if(document.frm.newPass.value!=document.frm.confirmPass.value)
            {
                document.getElementById("msg3").innerHTML="Password and Confirm Password does not match";
                str=false;
            }

            return str;
        }
        </script>


    </head>
    <body>
        <center>
            <img src="images/logo.png" alt="Garuda Club Plus"  /><br>
        </center>

        <logic:messagesPresent >
            <html:messages id="msg">
                <p>
                    <strong><center><font color="red" size="-1"><bean:write name="msg" /></font></center></strong>
                </p>
            </html:messages>
        </logic:messagesPresent>


        <form action="restPassMenu.do" method="post" onSubmit="return validate()" name="frm">
            <center>
                <div id="msg1" style="color:#FF0000"></div>
                <div id="msg2" style="color:#FF0000"></div>
                <div id="msg3" style="color:#FF0000"></div>

                <table >

                    <tr>
                        <td ><bean:message key="message.oldPass" /></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="password" name="oldPass" size="12">
                        </td>

                    </tr>

                    <tr>
                        <td><bean:message key="message.newPass" /></td>
                    </tr>
                    <tr>

                        <td> <input type="password" name="newPass" size="12"></td>

                    </tr>

                    <tr>
                        <td><bean:message key="message.confirmPass" /></td>
                    </tr>
                    <tr>

                        <td> <input type="password" name="confirmPass" size="12"></td>

                    </tr>

                    <tr>
                        <td><input type="submit" style="width:100px;" value="Submit"></td>
                    </tr>
                    <tr>
                        <td><a href='showHome.do'><img src="images/homem.png" onmouseover="this.src='images/homem_mo.png';" onmouseout="this.src='images/homem.png';" alt="Home" /></a></td>
                    </tr>

                </table>
            </center>
        </form>

    </body>
</html>
