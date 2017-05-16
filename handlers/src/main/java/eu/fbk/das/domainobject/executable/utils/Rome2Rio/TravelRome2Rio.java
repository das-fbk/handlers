package eu.fbk.das.domainobject.executable.utils.Rome2Rio;

import java.util.Comparator;

import com.google.common.base.CharMatcher;

public class TravelRome2Rio {
	private String mean;
	private String duration;
	private String cost;
	private String distance;
	private String number_changes;
	
	public TravelRome2Rio(String mean, String duration, String cost, String distance, String number_changes) {
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


	public static Comparator <TravelRome2Rio> priceComparator = new Comparator<TravelRome2Rio>() {

        public int compare(TravelRome2Rio t1, TravelRome2Rio t2) {
        	
            int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1.getCost()));
            int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2.getCost()));

            return travel1-travle2;
        }
    };
    
    public static Comparator <TravelRome2Rio> timeComparator = new Comparator<TravelRome2Rio>() {

        public int compare(TravelRome2Rio t1, TravelRome2Rio t2) {
        	
            int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1.getDuration()));
            int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2.getDuration()));

            return travel1-travle2;
        }
    };
    
    public static Comparator <TravelRome2Rio> distanceComparator = new Comparator<TravelRome2Rio>() {

        public int compare(TravelRome2Rio t1, TravelRome2Rio t2) {
        	
            int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1.getDistance()));
            int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2.getDistance()));

            return travel1-travle2;
        }
    };
    
    public static Comparator <TravelRome2Rio> changesComparator = new Comparator<TravelRome2Rio>() {

        public int compare(TravelRome2Rio t1, TravelRome2Rio t2) {
        	
            int travel1 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t1.getNumber_changes().toString()));
            int travle2 = Integer.parseInt(CharMatcher.DIGIT.retainFrom(t2.getNumber_changes().toString()));

            return travel1-travle2;
        }
    };

}
