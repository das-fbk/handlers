package eu.fbk.das.domainobject.executable.utils.BlaBlaCar;

public class TripAlternativeBlaBlaCar {

	String id;
	Double price;
	Integer seats_left;
	String date;
	String hour;
	String car_model;
	Integer distance;
	Integer perfect_duration;
	Double recommended_price;

	public TripAlternativeBlaBlaCar(String id, Double price,
			Integer seats_left, String date, String hour, String car_model,
			Integer distance, Integer perfect_duration, Double recommended_price) {
		super();
		this.id = id;
		this.price = price;
		this.seats_left = seats_left;
		this.date = date;
		this.hour = hour;
		this.car_model = car_model;
		this.distance = distance;
		this.perfect_duration = perfect_duration;
		this.recommended_price = recommended_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getSeats_left() {
		return seats_left;
	}

	public void setSeats_left(Integer seats_left) {
		this.seats_left = seats_left;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getCar_model() {
		return car_model;
	}

	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Integer getPerfect_duration() {
		return perfect_duration;
	}

	public void setPerfect_duration(Integer perfect_duration) {
		this.perfect_duration = perfect_duration;
	}

	public Double getRecommended_price() {
		return recommended_price;
	}

	public void setRecommended_price(Double recommended_price) {
		this.recommended_price = recommended_price;
	}

}
