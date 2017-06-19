package eu.fbk.das.testServices;

public class ExperimentResult extends Loggable {

	private int id; // (counter)

	private String dv1 = ""; // service name
	private String dv2 = "";// from
	private String dv3 = ""; // to
	private double dv4 = 0; // response time
	private int dv5 = 0; // number of alternatives
	private double dv6 = 0; // (average) number of different modes
							// (legs/segments)
							// per alternative

	public ExperimentResult() {
		super();
	}

	public ExperimentResult(int id, String dv1, String dv2, String dv3,
			double dv4, int dv5, double dv6) {
		this.id = id;
		this.dv1 = dv1;
		this.dv2 = dv2;
		this.dv3 = dv3;
		this.dv4 = dv4;
		this.dv5 = dv5;
		this.dv6 = dv6;

	}

	public String getCsvFileHeader(String commaDelimiter) {
		String result = "id" + commaDelimiter + "service_name" + commaDelimiter
				+ "from" + commaDelimiter + "to" + commaDelimiter
				+ "response_time" + commaDelimiter + "num_of_alternatives"
				+ commaDelimiter + "average_modes";

		return result;
	}

	public String toCsv(String commaDelimiter) {
		String result = "";
		result += this.id + commaDelimiter;
		result += this.dv1 + commaDelimiter;
		result += this.dv2 + commaDelimiter;
		result += this.dv3 + commaDelimiter;
		result += this.dv4 + commaDelimiter;
		result += this.dv5 + commaDelimiter;
		result += this.dv6;

		return result;
	}

	@Override
	public String toString() {
		return "ExperimentResult [id=" + id + ", dv1=" + dv1 + ", dv2=" + dv2
				+ ", dv3=" + dv3 + ", dv4=" + dv4 + ", dv5=" + dv5 + ", dv6="
				+ dv6 + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDv1() {
		return dv1;
	}

	public void setDv1(String dv1) {
		this.dv1 = dv1;
	}

	public String getDv2() {
		return dv2;
	}

	public void setDv2(String dv2) {
		this.dv2 = dv2;
	}

	public String getDv3() {
		return dv3;
	}

	public void setDv3(String dv3) {
		this.dv3 = dv3;
	}

	public double getDv4() {
		return dv4;
	}

	public void setDv4(double dv4) {
		this.dv4 = dv4;
	}

	public int getDv5() {
		return dv5;
	}

	public void setDv5(int dv5) {
		this.dv5 = dv5;
	}

	public double getDv6() {
		return dv6;
	}

	public void setDv6(double dv6) {
		this.dv6 = dv6;
	}

}
