package eu.fbk.das.domainobject.executable.utils.GoogleAPI;


public class GoogleTransitAlternative {

	private int id;

	private String mean;
	private Double price;
	private Double duration;
	private Double distance;
	private Integer number_changes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getNumber_changes() {
		return number_changes;
	}

	public void setNumber_changes(Integer number_changes) {
		this.number_changes = number_changes;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

}
