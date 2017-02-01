package eu.fbk.das.process.engine.impl.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.impl.context.api.Context;
import eu.fbk.das.process.engine.impl.context.api.Ensemble;
import eu.fbk.das.process.engine.impl.context.api.Role;
import eu.fbk.das.process.engine.impl.context.util.PropertiesUtil;

/**
 * A singleton context for Allow Ensembles storyboard one
 */
public class StoryboardOneContext implements Context {

    private static String firstRoute = null;
    private static String secondRoute = null;

    public static final Integer ROUTE_CREATION_BEFORE = 0;
    public static final Integer ROUTE_CREATION_WAIT = 1;
    public static final Integer ROUTE_CREATION_CREATED = 2;

    private static StoryboardOneContext instance;

    public static StoryboardOneContext getInstance() {
	if (instance == null) {
	    instance = new StoryboardOneContext();
	}
	return instance;
    }

    private Map<String, Ensemble> ensembles = new HashMap<String, Ensemble>();
    private List<Role> roles = new ArrayList<Role>();

    private ArrayListMultimap<String, Route> routeRegistry = ArrayListMultimap
	    .create();
    private Multimap<String, Route> actorToRouteRegistry = ArrayListMultimap
	    .create();
    private Map<String, PickupPoint> actorToPickupPointRegistry = new HashMap<String, PickupPoint>();
    private Map<String, PickupPoint> actorCheckInRegistry = new HashMap<String, PickupPoint>();
    private Map<String, PickupPoint> actorCheckOutRegistry = new HashMap<String, PickupPoint>();
    private Map<String, Integer> routeCreationWaiting = new HashMap<String, Integer>();
    private boolean flexibusDriverRouteClosed;

    /**
     * Build a default context for storyboard one
     */
    public StoryboardOneContext() {
	firstRoute = PropertiesUtil.getEnsembleProperties().getProperty(
		"ensemble1", "Ensemble_1");
	secondRoute = PropertiesUtil.getEnsembleProperties().getProperty(
		"ensemble2", "Ensemble_2");
	// TODO: static creation of route here
	Route first = buildRoute("1", 2);
	routeRegistry.put(firstRoute, first);
	Route second = buildRoute("2", 2);
	routeRegistry.put(secondRoute, second);

	routeCreationWaiting.put(firstRoute, ROUTE_CREATION_BEFORE);
	routeCreationWaiting.put(secondRoute, ROUTE_CREATION_BEFORE);
    }

    @Override
    public List<Ensemble> getEnsembles() {
	List<Ensemble> result = new ArrayList<Ensemble>();
	for (Ensemble ensemble : ensembles.values()) {
	    result.add(ensemble);
	}
	return result;
    }

    @Override
    public void setEnembles(List<Ensemble> ensembles) {
	// TODO
	// this.ensembles = ensembles;
    }

    @Override
    public List<Role> getRoles() {
	return roles;
    }

    @Override
    public void setRoles(List<Role> roles) {
	this.roles = roles;
    }

    /**
     * Assign given passenger to a route, if there is no route, create one
     */
    public void assignPassengerToRoute(ProcessEngine pe, String id,
	    String ensemble) {
	// TODO: usare informazione su journeyId per assegnare la route corretta
	// TODO: select always first route from flexibuscompany1
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();
	actorToRouteRegistry.put(id, route);
	System.out.println("Passeggero " + id + " assegnato alla route "
		+ route.name);
	System.out.println();
    }

    private Route buildRoute(String name, int ppNumber) {
	List<PickupPoint> ppoint = new ArrayList<PickupPoint>();
	for (int i = 0; i < ppNumber; i++) {
	    ppoint.add(new PickupPoint(name + "_" + String.valueOf(i)));
	}
	return new Route(name, ppoint);
    }

    /**
     * RouteManager assign driver to route
     */
    public void assignDriver(ProcessEngine pe, String driverName,
	    String ensemble) {
	// TODO: select route for driver using ensemble
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();
	actorToRouteRegistry.put(driverName, route);
	System.out.println("Driver " + driverName + " assegnato alla route "
		+ route.name);
	System.out.println();
    }

    public boolean isRouteSoldOut(ProcessEngine pe, List<String> userNames) {
	//
	// Route route = routeRegistry.get("FlexibusCompany1").stream()
	// .findFirst().get();
	// TODO: il check va fatto sul numero di utenti che sono registrati nel
	// sistema. La route e' sold out quando tutti gli utenti hanno fatto
	// registrazione all'unico pickup point diquesta route
	int ppNumber = userNames.size();
	int c = 0;
	for (String userName : userNames) {
	    if (actorToPickupPointRegistry.get(userName) != null) {
		c++;
	    }
	}
	if (c == ppNumber) {
	    System.out.println("Route sold out");
	    return true;
	}
	return false;
    }

    // TODO: assumiamo una persona per pp
    public boolean isCheckInEnded(ProcessEngine pe, List<String> userNames) {
	//
	// Route route = routeRegistry.get("FlexibusCompany1").stream()
	// .findFirst().get();
	// int ppNumber = route.pickupPoints.size();
	int ppNumber = userNames.size();
	int c = 0;
	for (String userName : userNames) {
	    if (actorCheckInRegistry.get(userName) != null) {
		c++;
	    }
	}
	if (c == ppNumber) {
	    System.out.println("Checkin ended");
	    return true;
	}
	return false;
    }

    public void gotoNextPickupPoint(String driverName, String ensemble) {
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();

	// for driver, assign to next pickuppoint
	if (actorToPickupPointRegistry.get(driverName) == null) {
	    // driver has to go to first pick point
	    PickupPoint pp = route.pickupPoints.get(0);
	    actorToPickupPointRegistry.put(driverName, pp);
	} else {
	    // real move to next pickpoint
	    PickupPoint pp = actorToPickupPointRegistry.get(driverName);
	    // mo to next pp means select next from route
	    int cipp = route.pickupPoints.indexOf(pp);
	    if (cipp + 1 < route.pickupPoints.size()) {
		PickupPoint nextPp = route.pickupPoints.get(cipp + 1);
		actorToPickupPointRegistry.put(driverName, nextPp);
	    }
	}
    }

    public boolean isAllPassengersOnBoard(ProcessEngine pe, String driverName) {
	// TODO: attenzione qui andiamo a controllare che lo stesso numero di
	// persone a cui si trova il flexibus che hanno fatto checkin sia lo
	// stesso numero di quelle che erano state assegnate a quel pp
	PickupPoint pp = actorToPickupPointRegistry.get(driverName);
	int c = 0;
	int d = 0;
	for (String name : actorCheckInRegistry.keySet()) {
	    if (actorCheckInRegistry.get(name) != null
		    && actorCheckInRegistry.get(name).equals(pp)) {
		c++;
	    }
	}
	for (String name : actorToPickupPointRegistry.keySet()) {
	    if (actorToPickupPointRegistry.get(name) != null
		    && actorToPickupPointRegistry.get(name).equals(pp)) {
		d++;
	    }
	}
	// note: -1 is because driver is at the same pickpoint
	return c == d - 1;
    }

    public boolean isAllPickupPointReached(ProcessEngine pe, String driverName,
	    String ensemble) {
	// all pickpoint are reached when flexibus is on last pickpoint
	PickupPoint pp = actorToPickupPointRegistry.get(driverName);
	if (pp != null) {
	    Route route = routeRegistry.get(ensemble).stream().findFirst()
		    .get();
	    PickupPoint lastPp = getDestination(route);
	    if (pp.equals(lastPp)) {
		return true;
	    }
	}
	return false;
    }

    private PickupPoint getDestination(Route route) {
	return route.pickupPoints.get(route.pickupPoints.size() - 1);
    }

    public void passengerCheckedOut(String userName, String ensemble) {
	if (actorCheckOutRegistry.get(userName) == null) {
	    Route route = routeRegistry.get(ensemble).stream().findFirst()
		    .get();
	    PickupPoint lastPp = getDestination(route);
	    actorCheckOutRegistry.put(userName, lastPp);
	    System.out.println("Passegger " + userName + " fa checkout");
	    System.out.println();
	}
    }

    public boolean isCheckOutEnded(ProcessEngine pe, String driverName,
	    List<String> userNames) {
	//
	int pNumber = actorCheckInRegistry.size();
	int c = 0;
	for (String userName : userNames) {
	    if (actorCheckOutRegistry.get(userName) != null) {
		c++;
	    }
	}
	return c == pNumber;
    }

    public Integer isRouteCreationWaiting(String ensemble) {
	return routeCreationWaiting.get(ensemble);
    }

    public void setRouteCreationWaiting(String ensemble, Integer value) {
	routeCreationWaiting.put(ensemble, value);
    }

    public void assignPickuPoint(String userName, String ensemble, int ppIndex) {
	// for every user, assign a pickupoint to default route
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();
	if (actorToPickupPointRegistry.get(userName) == null) {
	    PickupPoint pp = route.pickupPoints.get(ppIndex);
	    actorToPickupPointRegistry.put(userName, pp);
	    System.out.println("Passeggero " + userName
		    + " assegnato al pickpoint " + pp.name);
	    System.out.println();
	}
    }

    public void checkIn(String userName, String ensemble) {
	// for every user, assign a pickupoint to default route
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();
	if (actorCheckInRegistry.get(userName) == null) {
	    // get assigned pickpoint
	    PickupPoint pp = actorToPickupPointRegistry.get(userName);
	    actorCheckInRegistry.put(userName, pp);
	    if (pp == null || pp.name == null) {
		// TODO: User_3 cannot do check in, because ther is no pickup
		// point!
		System.out.println();
	    }
	    System.out.println("Passeggero " + userName + " fa checkin al pp "
		    + pp.name);
	    System.out.println();
	}

    }

    public boolean isPassengerCheckedIn(String userName) {
	if (actorCheckInRegistry.get(userName) != null) {
	    return true;
	}
	return false;
    }

    public void addEnsemble(String name, String ensembleName) {
	if (!ensembles.containsKey(ensembleName)) {
	    EnsembleImpl ensemble = new EnsembleImpl(ensembleName);
	    ensemble.add(name);

	    ensembles.put(ensembleName, ensemble);
	} else {
	    Ensemble ensemble = ensembles.get(ensembleName);
	    ensemble.add(name);
	    ensembles.put(ensembleName, ensemble);
	}
    }

    public void flexibusDriverRouteClosed() {
	// mette a true la condizione che sta aspettando la flexibus company,
	// vedi processo relativo
	flexibusDriverRouteClosed = true;
    }

    public boolean isFlexibusDriverRouteClosed() {
	return flexibusDriverRouteClosed;
    }

    public int getDriverCurrentDestinationIndex(String driverName,
	    String ensemble) {
	PickupPoint pp = actorToPickupPointRegistry.get(driverName);
	// TODO: for now only one route!
	List<Route> route = routeRegistry.get(ensemble);
	return route.get(0).pickupPoints.indexOf(pp);
    }

    public List<String> getPassengersAtCurrentPP(String driverName) {
	List<String> result = new ArrayList<String>();
	PickupPoint pp = actorToPickupPointRegistry.get(driverName);
	for (String k : actorToPickupPointRegistry.keySet()) {
	    if (actorToPickupPointRegistry.get(k).name.equals(pp.name)
		    && !k.equals(driverName)) {
		result.add(k);
	    }
	}
	return result;
    }

    // for first collective adaptation scenario only
    public void removeUserFromPickPointsAndRemovePickupPoint(String username,
	    String ensemble) {
	switch (username) {
	case "User_3":
	    if (actorToPickupPointRegistry.containsKey(username)) {
		actorToPickupPointRegistry.remove(username);
	    }
	    if (actorCheckInRegistry.containsKey(username)) {
		actorCheckInRegistry.remove(username);
	    }
	    break;
	case "User_6":
	    if (actorToPickupPointRegistry.containsKey(username)) {
		actorToPickupPointRegistry.remove(username);
	    }
	    if (actorCheckInRegistry.containsKey(username)) {
		actorCheckInRegistry.remove(username);
	    }
	    break;

	default:
	    break;
	}
	// remove pickpoint from route
	Route route = routeRegistry.get(ensemble).stream().findFirst().get();
	route.pickupPoints.remove(1);
    }

    public void addUserToPickUpPointAndEnsemble(String username, String ensemble) {
	switch (username) {
	case "User_6":
	    // get first route
	    Route route = routeRegistry.get(firstRoute).stream().findFirst()
		    .get();
	    // assign new pickup point from first route to user 6
	    PickupPoint pp = route.pickupPoints.get(1);
	    if (actorToPickupPointRegistry.get(username) == null) {
		actorToPickupPointRegistry.put(username, pp);
		System.out.println("Passeggero " + username
			+ " assegnato al pickpoint " + pp.name);
		System.out.println();
	    }

	    // set user checkin
	    // actorCheckInRegistry.put(username, pp);
	    System.out.println("Passeggero " + username + " fa checkin al pp "
		    + pp.name);
	    System.out.println();
	    break;

	default:
	    break;
	}

    }

}
