<%@ page session="false" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><?xml version="1.0" encoding="UTF-8"?>
<cas:serviceResponse xmlns:cas="http://www.yale.edu/tp/cas">
<cas:authenticationSuccess>
<cas:user>${assertion.chainedAuthentications[0].principal.id}</cas:user>
<c:if test="${not empty pgtIou}">
	<cas:proxyGrantingTicket>${pgtIou}</cas:proxyGrantingTicket>
</c:if>
<c:if test="${fn:length(assertion.chainedAuthentications) > 1}">
<cas:proxies>
<c:forEach var="proxy" items="${assertion.chainedAuthentications}" varStatus="loopStatus" begin="${fn:length(assertion.chainedAuthentications)-1}" end="1" step="-1">
	<cas:proxy>${proxy.principal.id}</cas:proxy>
</c:forEach>
</cas:proxies>
</c:if>
</cas:authenticationSuccess>
</cas:serviceResponse>