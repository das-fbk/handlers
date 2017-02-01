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
import eu.fbk.das.process.engine.api.jaxb.GoalType;

public class ToHoaaOrganizeTripExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(ToHoaaOrganizeTripExecutable.class);

    private ProcessEngine pe;

    private static int hoaaCounter = 1;

    public ToHoaaOrganizeTripExecutable(ProcessEngine processEngine) {
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
	List<DomainProperty> result = new ArrayList<DomainProperty>();
	// int start = currentAbstract.getTarget();
	while (stk.hasMoreTokens()) {
	    String token = stk.nextToken();
	    Optional<DomainProperty> act = buildAbstractActivityfor(doi, token);
	    if (act.isPresent()) {
		result.add(act.get());
		// start++;
	    }
	}
	// build abstract activity
	Point point = new Point();
	GoalType goal = new GoalType();
	for (DomainProperty dp : result) {
	    point.getDomainProperty().add(dp);
	}
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(
		currentAbstract.getTarget(), currentAbstract.getTarget() + 1,
		"TO_HOAAorganizeTrip_" + doi.getId() + "_" + hoaaCounter, goal);
	act.setAbstract(true);
	hoaaCounter++;

	// extend current processes with hoaas
	int target = currentAbstract.getTarget();
	// for (AbstractActivity act : result) {
	proc.getActivities().add(target, act);
	target++;
	// }
	// re-arrange source e target for all activities
	int i = 0;
	for (ProcessActivity a : proc.getActivities()) {
	    a.setSource(i);
	    a.setTarget(i + 1);
	    i++;
	}
	System.out.println();

    }

    private Optional<DomainProperty> buildAbstractActivityfor(
	    DomainObjectInstance doi, String token) {

	switch (token) {
	case "parkAndRide":
	    return Optional.of(buildParkAndRide(doi));
	case "bikeSharing":
	    return Optional.of(buildBikeSharing(doi));
	case "flexibus":
	    return Optional.of(buildFlexibus(doi));

	default:
	    return Optional.empty();
	}
    }

    private DomainProperty buildFlexibus(DomainObjectInstance doi) {
	// GoalType goal = new GoalType();
	// Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("FlexibusTrip");
	dp.getState().add("TRIP_BOOKED");
	// point.getDomainProperty().add(dp);
	// goal.getPoint().add(point);
	// AbstractActivity act = new AbstractActivity(source, target,
	// "TO_HOAAorganizeTrip_" + hoaaCounter, goal);
	// hoaaCounter++;

	// extend knowledge with external knowledge of domain property
	pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

	return dp;
    }

    private DomainProperty buildBikeSharing(DomainObjectInstance doi) {
	// GoalType goal = new GoalType();
	// Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("BikeSharingTrip");
	dp.getState().add("USER_REGISTERED");
	// point.getDomainProperty().add(dp);
	// goal.getPoint().add(point);
	// AbstractActivity act = new AbstractActivity(source, target,
	// "TO_HOAAorganizeTrip_" + hoaaCounter, goal);
	// hoaaCounter++;

	// extend knowledge with external knowledge of domain property
	pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

	return dp;
    }

    private DomainProperty buildParkAndRide(DomainObjectInstance doi) {
	// GoalType goal = new GoalType();
	// Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("ParkAndRideTrip");
	dp.getState().add("CAR_ROUTE_PLANNED");
	// point.getDomainProperty().add(dp);
	// goal.getPoint().add(point);
	// AbstractActivity act = new AbstractActivity(source, target,
	// "TO_HOAAorganizeTrip_" + hoaaCounter, goal);
	// hoaaCounter++;

	// extend knowledge with external knowledge of domain property
	pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

	return dp;
    }

}
