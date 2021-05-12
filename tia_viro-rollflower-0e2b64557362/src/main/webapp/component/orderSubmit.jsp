<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="application.model.Flower"%>
<%@ page import="application.model.Decoration"%>
<%@ page import="application.model.Wrapper"%>
<%@ page import="application.model.FlowerBouquet"%>
<%@ page import="application.model.component.FlowerComponent"%>
<table class="table">
	<tr>
		<th>Bouquet #</th>
		<th>Wrapper</th>
		<th>Decoration</th>
		<th>Flower</th>
	</tr>
	<%
		List<FlowerBouquet> bouquetList = (List<FlowerBouquet>) request.getAttribute("order");
		int count = 1;
		for (FlowerBouquet bouquet : bouquetList) {
	%><tr>
		<td><%=count++%></td>
		<td
			data-wrapper-id='<%=bouquet.getWrapperComponent() == null ? 0 : bouquet.getWrapperComponent().getId()%>'>
			<%=bouquet.getWrapperComponent() == null ? "None" : bouquet.getWrapperComponent().getName()%>
		</td>
		<td
			data-decoration-id='<%=bouquet.getDecorationComponent() == null ? 0 : bouquet.getDecorationComponent().getId()%>'>
			<%=bouquet.getDecorationComponent() == null ? "None" : bouquet.getDecorationComponent().getName()%>
		</td>
		<td><ul>
				<%
					for (FlowerComponent component : bouquet.getFlowerComponent()) {
							StringBuffer sb = new StringBuffer();
							String flowerColor = component.getFlowerItem().getColor().toString();
							sb.append(flowerColor.substring(0, 1));
							sb.append(flowerColor.substring(1, flowerColor.length()).toLowerCase());
							sb.append(" ");
							sb.append(component.getFlowerItem().getName().toLowerCase());
							sb.append(", ");
							sb.append(component.getFlowerItem().getLength().toString().toLowerCase());
							sb.append(" - ");
							sb.append(String.valueOf(component.getFlowerQuantyty()));
							sb.append(" pc.");
							String flowerName = sb.toString();
				%><li data-flower-id='<%=component.getFlowerItem().getId()%>'><%=flowerName%></li>
				<%
					}
				%>
			</ul></td>
	</tr>
	<%
		}
	%>
</table>
<form class="submit-order-form">
	<div class="input-group">
		<div class="input-group-prepend">
			<span class="input-group-text">Shipping address and contact
				phone</span>
		</div>
		<input type="text" class="form-control address-input"
			placeholder="Address" required> <input type="text"
			class="form-control phone-input" placeholder="Phone" required>
	</div>
	<div class="input-group" style="margin-top: 20px;">
		<div class="input-group-prepend">
			<span class="input-group-text">Commentary</span>
		</div>
		<textarea class="form-control commentary-text" aria-label="With textarea"></textarea>
	</div>
	<button type="submit" class="btn btn-dark btn-block"
		style="margin-top: 20px;">Order it!</button>
</form>