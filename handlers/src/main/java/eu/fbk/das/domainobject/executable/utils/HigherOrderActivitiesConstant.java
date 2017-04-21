package eu.fbk.das.domainobject.executable.utils;

/**
 * Domain Objects Demonstrator constants
 */
public final class HigherOrderActivitiesConstant {

	private HigherOrderActivitiesConstant() {
	}

	// transportation modes
	public static final String BIKE = "bike";
	public static final String CAR = "car";
	public static final String BUS = "bus";
	public static final String TRAIN = "train";
	public static final String WALK = "walk";
	public static final String RIDESHARE = "rideshare";
	public static final String CARSHARING = "carsharing";
	public static final String NOTHING = "";

	// utility methods
	public static String getTransportationMode(String mode) {
		switch (mode) {
		case "bicycle":
			return BIKE;
		case "car":
			return CAR;
		case "walk":
			return WALK;
		case "bus":
			return BUS;
		case "shuttle":
			return BUS;
		case "car sharing":
			return CARSHARING;
		case "rideshare":
			return RIDESHARE;
		case "train":
			return TRAIN;
		case "night train":
			return TRAIN;

		default:
			return NOTHING;
		}
	}
}
