package eu.fbk.das.domainobject.executable.utils;

public class Segment {

	private String mean;

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	private String agency;
	private String from;
	private String to;

	public Segment(String mean, String agency, String from, String to) {
		super();
		this.mean = mean;
		this.agency = agency;
		this.from = from;
		this.to = to;
	}

}
