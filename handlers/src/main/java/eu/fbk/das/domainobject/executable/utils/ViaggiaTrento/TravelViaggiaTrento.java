package eu.fbk.das.domainobject.executable.utils.ViaggiaTrento;

import java.util.ArrayList;
import java.util.Comparator;

public class TravelViaggiaTrento {

	String duration;
	ArrayList<String> steps;
	ArrayList<String> routes;
	ArrayList<String> routeId;

	public TravelViaggiaTrento(String duration, ArrayList<String> steps,
			ArrayList<String> routes, ArrayList<String> routeId) {
		super();
		this.duration = duration;
		this.steps = steps;
		this.routes = routes;
		this.routeId = routeId;
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

	public ArrayList<String> getRouteId() {
		return routeId;
	}

	public void setRouteId(ArrayList<String> routeId) {
		this.routeId = routeId;
	}

	public static Comparator<TravelViaggiaTrento> timeComparator = new Comparator<TravelViaggiaTrento>() {

		public int compare(TravelViaggiaTrento t1, TravelViaggiaTrento t2) {

			int travel1 = Integer.parseInt(t1.getDuration());
			int travle2 = Integer.parseInt(t2.getDuration());

			return travel1 - travle2;
		}
	};
}
