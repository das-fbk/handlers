package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcVar;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;
import eu.fbk.das.process.engine.api.jaxb.GoalType;

public class UserExecuteTripHoaa extends AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    private static int hoaaCounter = 1;

    private static final Logger logger = LogManager
	    .getLogger(UserExecuteTripHoaa.class);

    public UserExecuteTripHoaa(ProcessEngine processEngine) {
	this.pe = processEngine;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	AbstractActivity currentAbstract = (AbstractActivity) proc
		.getCurrentActivity();

	// search variable using doi for process (in this case user)
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	ProcessDiagram userProcess = doi.getProcess();

	// get value from variable
	ProcVar variable = pe.getVariablesFor(userProcess,
		currentAbstract.getReceiveVar());
	if (variable == null) {

	    logger.error("Variable not found "
		    + currentAbstract.getReceiveVar());
	    return;
	}
	logger.debug("Variable found: " + variable.toString());
	if (variable.getValue() == null || variable.getValue().isEmpty()) {
	    logger.error("Value for variable "
		    + variable.toString()
		    + " not valid, must be a string with comma separated values");
	    return;
	}
	StringTokenizer stk = new StringTokenizer(variable.getValue(), ",");
	List<AbstractActivity> result = new ArrayList<AbstractActivity>();
	int start = currentAbstract.getTarget();
	while (stk.hasMoreTokens()) {
	    String token = stk.nextToken();
	    Optional<AbstractActivity> act = buildAbstractActivityfor(doi,
		    token, start, start + 1);
	    if (act.isPresent()) {
		result.add(act.get());
		start++;
	    }
	}

	// extend current processes with hoaas
	int target = currentAbstract.getTarget();
	for (AbstractActivity act : result) {
	    proc.getActivities().add(target, act);
	    target++;
	}
	// re-arrange source e target for all activities
	int i = 0;
	for (ProcessActivity act : proc.getActivities()) {
	    act.setSource(i);
	    act.setTarget(i + 1);
	    i++;
	}
	System.out.println();

    }

    private Optional<AbstractActivity> buildAbstractActivityfor(
	    DomainObjectInstance doi, String token, int source, int target) {

	switch (token) {
	case "parkAndRide":
	    return Optional.of(buildParkAndRide(doi, source, target));
	case "bikeSharing":
	    return Optional.of(buildBikeSharing(doi, source, target));
	case "flexibus":
	    return Optional.of(buildFlexibus(doi, source, target));
	case "walk":
	    return Optional.of(buildWalk(doi, source, target));

	default:
	    return Optional.empty();
	}
    }

    private AbstractActivity buildFlexibus(DomainObjectInstance doi,
	    int source, int target) {
	GoalType goal = new GoalType();
	Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("RoutePassengerStatus");
	dp.getState().add("DESTINATION_REACHED");
	point.getDomainProperty().add(dp);
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(source, target,
		"USER_ExecuteTrip_" + doi.getId() + "_Flexibus", goal);
	act.setAbstract(true);
	hoaaCounter++;

	// extend knowledge with external knowledge of domain property
	// pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

	return act;
    }

    private AbstractActivity buildBikeSharing(DomainObjectInstance doi,
	    int source, int target) {
	GoalType goal = new GoalType();
	Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("BikeSharingTrip");
	dp.getState().add("RACK_REACHED");
	point.getDomainProperty().add(dp);
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(source, target,
		"USER_ExecuteTrip_" + doi.getId() + "_BikeSharing", goal);
	act.setAbstract(true);
	hoaaCounter++;

	return act;
    }

    private AbstractActivity buildParkAndRide(DomainObjectInstance doi,
	    int source, int target) {
	GoalType goal = new GoalType();
	Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("ParkAndRideTrip");
	dp.getState().add("PARK_REACHED");
	point.getDomainProperty().add(dp);
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(source, target,
		"USER_ExecuteTrip_" + doi.getId() + "_ParkAndRide", goal);
	act.setAbstract(true);
	hoaaCounter++;

	return act;
    }

    private AbstractActivity buildWalk(DomainObjectInstance doi, int source,
	    int target) {
	GoalType goal = new GoalType();
	Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("WalkPlan");
	dp.getState().add("PATH_EXECUTED");
	point.getDomainProperty().add(dp);
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(source, target,
		"USER_ExecuteTrip_" + doi.getId() + "_Walk", goal);
	// add event in order to reset dp only for walk because there is no pre
	Event event = new Event();
	event.setDpName("WalkPlan");
	event.setEventName("walkGoBack");
	if (act.getEffect() == null) {
	    EffectType effect = new EffectType();
	    effect.getEvent().add(event);
	    act.setEffect(effect);
	}
	act.getEffect().getEvent().add(event);
	act.setAbstract(true);
	hoaaCounter++;

	// extend knowledge with external knowledge of domain property
	pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

	return act;
    }
}
