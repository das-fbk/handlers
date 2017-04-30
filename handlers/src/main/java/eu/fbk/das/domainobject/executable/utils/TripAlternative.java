package eu.fbk.das.domainobject.executable.utils;

import java.util.ArrayList;

/*public class TripAlternative {

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
 */

public class TripAlternative {

	public TripAlternative(String mean, Double price, Double duration,
			Double distance, ArrayList<Segment> segments, Integer number_changes) {
		super();
		this.mean = mean;
		this.price = price;
		this.duration = duration;
		this.distance = distance;
		this.segments = segments;
		this.number_changes = number_changes;
	}

	public TripAlternative(String mean, Double price, Double duration,
			Double distance, Integer number_changes) {
		super();
		this.mean = mean;
		this.price = price;
		this.duration = duration;
		this.distance = distance;
		this.number_changes = number_changes;
		this.segments = segments;
	}

	private String mean;
	private Double price;
	private Double duration;
	private Double distance;
	private Integer number_changes;

	private ArrayList<Segment> segments;

	public ArrayList<Segment> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<Segment> segments) {
		this.segments = segments;
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
