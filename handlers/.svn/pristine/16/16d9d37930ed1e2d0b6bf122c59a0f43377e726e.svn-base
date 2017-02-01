package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.ProcVar;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.GoalType;

/**
 * Test Handler for Hoaa, see tests in process-engine-impl
 */
public class TestHoaaHandler extends AbstractExecutableActivityInterface {

    private static int hoaaCounter = 1;

    private static final Logger logger = LogManager
	    .getLogger(TestHoaaHandler.class);

    private ProcessEngine pe;

    public TestHoaaHandler(ProcessEngine processEngine) {
	this.pe = processEngine;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	AbstractActivity currentAbstract = (AbstractActivity) proc
		.getCurrentActivity();

	// get value from variable
	ProcVar variable = pe.getVariablesFor(proc,
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
	    result.add(buildAbstractActivityfor(token, start, start + 1));
	    start++;
	}
	// extend current processes with hoaas
	int target = currentAbstract.getTarget();
	for (AbstractActivity act : result) {
	    proc.getActivities().add(target, act);
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

    private AbstractActivity buildAbstractActivityfor(String token, int source,
	    int target) {

	GoalType goal = new GoalType();
	Point point = new Point();
	DomainProperty dp = new DomainProperty();
	dp.setDpName("MultiModalPlanner");
	dp.getState().add("REQUEST_LOADED");
	point.getDomainProperty().add(dp);
	goal.getPoint().add(point);
	AbstractActivity act = new AbstractActivity(source, target, "HoAA"
		+ hoaaCounter, goal);
	hoaaCounter++;
	return act;
    }

}
