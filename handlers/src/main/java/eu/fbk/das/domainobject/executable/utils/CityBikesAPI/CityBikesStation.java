package eu.fbk.das.domainobject.executable.utils.CityBikesAPI;

public class CityBikesStation {

	private String id;
	private int empty_slots;
	private int free_bikes;

	private Double latitude;
	private Double longitude;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getEmpty_slots() {
		return empty_slots;
	}

	public void setEmpty_slots(int empty_slots) {
		this.empty_slots = empty_slots;
	}

	public int getFree_bikes() {
		return free_bikes;
	}

	public void setFree_bikes(int free_bikes) {
		this.free_bikes = free_bikes;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
