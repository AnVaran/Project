package application.model.component;

import application.model.Flower;

public class FlowerComponent {
	private Long id;
	private Integer flowerQuantyty;
	private Flower flowerItem;
	private Long bouquetId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFlowerQuantyty() {
		return flowerQuantyty;
	}

	public void setFlowerQuantyty(Integer flowerQuantyty) {
		this.flowerQuantyty = flowerQuantyty;
	}

	public Flower getFlowerItem() {
		return flowerItem;
	}

	public void setFlowerItem(Flower flowerItem) {
		this.flowerItem = flowerItem;
	}

	public Long getBouquetId() {
		return bouquetId;
	}

	public void setBouquetId(Long bouquetId) {
		this.bouquetId = bouquetId;
	}

}
