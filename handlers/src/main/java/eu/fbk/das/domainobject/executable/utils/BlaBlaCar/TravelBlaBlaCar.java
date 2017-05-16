package eu.fbk.das.domainobject.executable.utils.BlaBlaCar;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class TravelBlaBlaCar {
	private String mean;
	private String dateHour;
	private String price;
	private String seats_left;
	private String car_model;
	private String distance;
	private String perfect_duration;
	private String perfect_price;

	public TravelBlaBlaCar(String mean, String dateHour, String price,
			String seats_left, String car_model, String distance,
			String perfect_duration, String perfect_price) {
		super();
		this.mean = mean;
		this.dateHour = dateHour;
		this.price = price;
		this.seats_left = seats_left;
		this.car_model = car_model;
		this.distance = distance;
		this.perfect_duration = perfect_duration;
		this.perfect_price = perfect_price;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getDateHour() {
		return dateHour;
	}

	public void setDateHour(String dateHour) {
		this.dateHour = dateHour;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSeats_left() {
		return seats_left;
	}

	public void setSeats_left(String seats_left) {
		this.seats_left = seats_left;
	}

	public String getCar_model() {
		return car_model;
	}

	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPerfect_duration() {
		return perfect_duration;
	}

	public void setPerfect_duration(String perfect_duration) {
		this.perfect_duration = perfect_duration;
	}

	public String getPerfect_price() {
		return perfect_price;
	}

	public void setPerfect_price(String perfect_price) {
		this.perfect_price = perfect_price;
	}

	public static Comparator<TravelBlaBlaCar> priceComparator = new Comparator<TravelBlaBlaCar>() {

		public int compare(TravelBlaBlaCar t1, TravelBlaBlaCar t2) {

			int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1
					.getPrice()));
			int travel2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2
					.getPrice()));

			return travel1 - travel2;
		}
	};

	public static Comparator<TravelBlaBlaCar> seatsLeftComparator = new Comparator<TravelBlaBlaCar>() {

		public int compare(TravelBlaBlaCar t1, TravelBlaBlaCar t2) {

			int value = CharMatcher.DIGIT
					.retainFrom(t1.getSeats_left())
					.compareTo(CharMatcher.DIGIT.retainFrom(t2.getSeats_left()));

			return value;
		}
	};

	public static Comparator<TravelBlaBlaCar> dateHourComparator = new Comparator<TravelBlaBlaCar>() {

		public int compare(TravelBlaBlaCar t1, TravelBlaBlaCar t2) {

			int value1 = t1.dateHour.substring(0, 2).compareTo(
					t2.dateHour.substring(0, 2));
			if (value1 == 0) {
				int value2 = t1.dateHour.substring(3, 5).compareTo(
						t2.dateHour.substring(3, 5));
				if (value2 == 0) {
					int value3 = t1.dateHour.substring(6, 10).compareTo(
							t2.dateHour.substring(6, 10));
					if (value3 == 0) {
						int value4 = t1.dateHour.substring(11, 13).compareTo(
								t2.dateHour.substring(11, 13));
						if (value4 == 0) {
							return t1.dateHour.substring(14, 16).compareTo(
									t2.dateHour.substring(14, 16));
						} else {
							return value4;
						}
					} else {
						return value3;
					}

				} else {
					return value2;
				}
			}
			return value1;
		}
	};

}
