
<%
	if (request.getAttribute("orderId") != null) {
%><div class="container" style="height: 100px; text-align: center;">
	<p>Your order number is:
	<p><%=request.getAttribute("orderId")%>
	</p>
</div>
<button type="button" class="btn btn-dark" onclick="redirectFRomModal()">Home</button>
<%
	} else if (request.getAttribute("validationExeption") != null) {
%>
<div class="container" style="height: 100px; text-align: center;">
	<p><%=request.getAttribute("validationExeption")%></p>
</div>
<button type="button" class="btn btn-dark hide-button" onclick="hideModal()">OK</button>
<%
	}
%>