package eu.fbk.das.domainobject.executable.utils;

import java.util.ArrayList;

public class TripAlternative {

	public TripAlternative(String mean, Long price, Long duration,
			Long distance, ArrayList<Segment> segments) {
		super();
		this.mean = mean;
		this.price = price;
		this.duration = duration;
		this.distance = distance;
		this.segments = segments;
	}

	private String mean;
	private ArrayList<Segment> segments;

	public ArrayList<Segment> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<Segment> segments) {
		this.segments = segments;
	}

	private Long price;
	private Long duration;
	private Long distance;

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}
}
