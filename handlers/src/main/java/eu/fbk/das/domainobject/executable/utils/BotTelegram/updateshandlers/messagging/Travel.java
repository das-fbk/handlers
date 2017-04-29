package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class Travel {
	private int index;
	private String mean;
	private String duration;
	private String cost;
	private String distance;
	private String number_changes;

	public Travel(int index, String mean, String duration, String cost,
			String distance, String number_changes) {
		this.index = index;
		this.mean = mean;
		this.duration = duration;
		this.cost = cost;
		this.distance = distance;
		this.number_changes = number_changes;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getNumber_changes() {
		return number_changes;
	}

	public void setNumber_changes(String number_changes) {
		this.number_changes = number_changes;
	}

	public static Comparator<Travel> priceComparator = new Comparator<Travel>() {

		public int compare(Travel t1, Travel t2) {

			int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1
					.getCost()));
			int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2
					.getCost()));

			return travel1 - travle2;
		}
	};

	public static Comparator<Travel> timeComparator = new Comparator<Travel>() {

		public int compare(Travel t1, Travel t2) {

			int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1
					.getDuration()));
			int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2
					.getDuration()));

			return travel1 - travle2;
		}
	};

	public static Comparator<Travel> distanceComparator = new Comparator<Travel>() {

		public int compare(Travel t1, Travel t2) {

			int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1
					.getDistance()));
			int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2
					.getDistance()));

			return travel1 - travle2;
		}
	};

	public static Comparator<Travel> changesComparator = new Comparator<Travel>() {

		public int compare(Travel t1, Travel t2) {

			int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1
					.getNumber_changes().toString()));
			int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2
					.getNumber_changes().toString()));

			return travel1 - travle2;
		}
	};

}
