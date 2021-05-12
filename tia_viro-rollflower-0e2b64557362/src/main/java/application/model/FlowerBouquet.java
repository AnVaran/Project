package application.model;

import java.util.List;

import application.model.component.FlowerComponent;

public class FlowerBouquet {

	private Long id;
	private Long orderId;
	private Float price;
	private Wrapper wrapperComponent;
	private Decoration decorationComponent;
	private List<FlowerComponent> flowerComponent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Wrapper getWrapperComponent() {
		return wrapperComponent;
	}

	public void setWrapperComponent(Wrapper wrapperComponent) {
		this.wrapperComponent = wrapperComponent;
	}

	public Decoration getDecorationComponent() {
		return decorationComponent;
	}

	public void setDecorationComponent(Decoration decorationComponent) {
		this.decorationComponent = decorationComponent;
	}

	public List<FlowerComponent> getFlowerComponent() {
		return flowerComponent;
	}

	public void setFlowerComponent(List<FlowerComponent> flowerComponent) {
		this.flowerComponent = flowerComponent;
	}

}
