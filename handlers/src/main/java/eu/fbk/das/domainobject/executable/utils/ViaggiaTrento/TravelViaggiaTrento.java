package eu.fbk.das.domainobject.executable.utils.ViaggiaTrento;

import java.util.ArrayList;
import java.util.Comparator;

public class TravelViaggiaTrento {

	String duration;
	ArrayList<String> steps;
	ArrayList<String> routes;

	public TravelViaggiaTrento(String duration, ArrayList<String> steps,
			ArrayList<String> routes) {
		super();
		this.duration = duration;
		this.steps = steps;
		this.routes = routes;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public ArrayList<String> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<String> steps) {
		this.steps = steps;
	}

	public ArrayList<String> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<String> routes) {
		this.routes = routes;
	}

	public static Comparator<TravelViaggiaTrento> timeComparator = new Comparator<TravelViaggiaTrento>() {

		public int compare(TravelViaggiaTrento t1, TravelViaggiaTrento t2) {

			int travel1 = Integer.parseInt(t1.getDuration());
			int travle2 = Integer.parseInt(t2.getDuration());

			return travel1 - travle2;
		}
	};
}
