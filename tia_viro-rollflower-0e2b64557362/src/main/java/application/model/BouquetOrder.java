package application.model;

import java.util.List;

import application.enums.OrderStatusEnum;

public class BouquetOrder {
	private Long id;
	private List<FlowerBouquet> bouquets;
	private String shippingAdres;
	private String commentary;
	private OrderStatusEnum orderStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<FlowerBouquet> getBouquets() {
		return bouquets;
	}

	public void setBouquets(List<FlowerBouquet> bouquets) {
		this.bouquets = bouquets;
	}

	public String getShippingAdres() {
		return shippingAdres;
	}

	public void setShippingAdres(String shippingAdres) {
		this.shippingAdres = shippingAdres;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public OrderStatusEnum getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatusEnum orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BouquetOrder other = (BouquetOrder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderStatus != other.orderStatus)
			return false;
		return true;
	}

}
