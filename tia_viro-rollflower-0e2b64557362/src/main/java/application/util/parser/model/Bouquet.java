package application.util.parser.model;

public class Bouquet {
	private Long wrapperId;
	private Long decorationId;
	private Flower[] flowers;

	public Long getWrapperId() {
		return wrapperId;
	}

	public void setWrapperId(Long wrapperId) {
		this.wrapperId = wrapperId;
	}

	public Long getDecorationId() {
		return decorationId;
	}

	public void setDecorationId(Long decorationId) {
		this.decorationId = decorationId;
	}

	public Flower[] getFlowers() {
		return flowers;
	}

	public void setFlowers(Flower[] flowers) {
		this.flowers = flowers;
	}
}
