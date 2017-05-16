package eu.fbk.das.domainobject.executable.utils;

/**
 * Domain Objects Demonstrator constants
 */
public final class HigherOrderActivitiesConstant {

	private HigherOrderActivitiesConstant() {
	}

	// Rome2Rio transportation modes
	// unknown, plane, helicopter, car, bus, taxi, rideshare, shuttle, towncar,
	// train, tram, cablecar, subway, ferry, foot, animal, bicycle
	public static final String UNKNOWN = "unknown";
	public static final String PLANE = "plane";
	public static final String HELICOPTER = "helicopter";
	public static final String CAR = "car";
	public static final String BUS = "bus";
	public static final String TAXI = "taxi";
	public static final String RIDESHARE = "rideshare";
	public static final String SHUTTLE = "shuttle";
	public static final String TOWNCAR = "towncar";
	public static final String TRAIN = "train";
	public static final String TRAM = "tram";
	public static final String CABLECAR = "cablecar";
	public static final String SUBWAY = "subway";
	public static final String FERRY = "ferry";
	public static final String FOOT = "foot";
	public static final String ANIMAL = "animal";
	public static final String BICYCLE = "bicycle";

	// ViaggiaTrento transportation modes
	// public static final String WALK = "walk";
	// public static final String CARSHARING = "carsharing";

	public static final String NOTHING = "";

	// scopes
	public static String Current_Scope = "";

	// utility methods
	public static String getTransportationMode(String mode) {
		switch (mode) {
		case "unknown":
			return UNKNOWN;
		case "plane":
			return PLANE;
		case "helicopter":
			return HELICOPTER;
		case "car":
			return CAR;
		case "bus":
			return BUS;
		case "taxi":
			return TAXI;
		case "rideshare":
			return RIDESHARE;
		case "shuttle":
			return SHUTTLE;
		case "towncar":
			return TOWNCAR;
		case "train":
			return TRAIN;
		case "night train":
			return TRAIN;
		case "tram":
			return TRAM;
		case "cablecar":
			return CABLECAR;
		case "subway":
			return SUBWAY;
		case "ferry":
			return FERRY;
		case "foot":
			return FOOT;
		case "animal":
			return ANIMAL;
		case "bicycle":
			return BICYCLE;

		default:
			return NOTHING;
		}
	}

	public static String getCurrent_Scope() {
		return Current_Scope;
	}

	public static void setCurrent_Scope(String current_Scope) {
		Current_Scope = current_Scope;
	}
}
