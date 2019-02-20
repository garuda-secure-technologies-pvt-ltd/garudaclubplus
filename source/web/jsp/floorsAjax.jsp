
<%@page pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<span id="ble" class="pad2">
   <logic:present name="places">
            <input type="hidden" name="floorId" value="0" />
            <% ArrayList places = (ArrayList) request.getSession().getAttribute("places");%>
            <c:forEach var="place" items="${places}">
                <c:set var="var" value="false" />
                <c:forEach var="busy" items="${busy}">
                    <c:if test="${place.id == busy.id}">
                        <input type=submit name="id" value="${place.name}" onclick="getLocation('${place.id}');" class="busy">
                        <c:set var="var" value="true" />
                    </c:if>
                </c:forEach>
                    <c:if test="${var == false}">
                       <input type=submit name="id" value="${place.name}" onclick="getLocation('${place.id}');" class="notbusy">
                    </c:if>
            </c:forEach>
    </logic:present>
</span>
