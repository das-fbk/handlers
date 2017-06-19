package eu.fbk.das.domainobject.executable.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.DomainObjectsVariablesUtils;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;
import eu.fbk.das.process.engine.api.jaxb.GoalType;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

//this executable generates a HOAA for each "R2R" segment of the user chosen alternative, 
//followed by a concrete activity used for the "backToPlanned" event
public class TAdefineJourneyLegsExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(TAdefineJourneyLegsExecutable.class);

	private ProcessEngine pe;

	private static int hoaaCounter = 1;
	private static String hoaaName = null;

	public TAdefineJourneyLegsExecutable(ProcessEngine processEngine) {
		this.pe = processEngine;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		AbstractActivity currentAbstract = (AbstractActivity) proc
				.getCurrentActivity();
		// hoaaName = currentAbstract.getName() + "-";
		hoaaName = "JourneyLeg-";

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		Element transportations = doi
				.getStateVariableContentByName("HOAAPlanGoal");
		String transportMeans = null;
		if (transportations != null) {
			if (transportations.getFirstChild() != null) {
				transportMeans = transportations.getFirstChild().getNodeValue();
			}
		}
		if (transportMeans == null || transportMeans.isEmpty()) {
			logger.error("Variable transport means not found ");
			return;
		} else {
			logger.debug("Variable transportMeans equals to " + transportMeans);
		}

		StringTokenizer stk = new StringTokenizer(transportMeans, "*");
		//
		// // set the journeySegments variable
		// Element numberOfSegment = doi
		// .getStateVariableContentByName("SegmentsNumber");
		// numberOfSegment.setTextContent(Integer.toString(stk.countTokens()));

		List<ProcessActivity> result = new ArrayList<ProcessActivity>();
		int start = currentAbstract.getTarget();
		while (stk.hasMoreTokens()) {
			String token = stk.nextToken();
			Optional<AbstractActivity> act = buildAbstractActivityfor(doi,
					token, start, start + 1);
			Optional<ConcreteActivity> concrete = buildConcreteActivityfor(doi,
					token, start, start + 1);
			if (act.isPresent()) {
				result.add(act.get());
				start++;
			}
			if (concrete.isPresent()) {
				result.add(concrete.get());
				start++;
			}
		}

		// extend current processes with hoaas
		int target = currentAbstract.getTarget();
		for (ProcessActivity act : result) {
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
	}

	private Optional<ConcreteActivity> buildConcreteActivityfor(
			DomainObjectInstance doi, String token, int source, int target) {
		GoalType goal = new GoalType();
		Point point = new Point();
		DomainProperty dp = new DomainProperty();
		dp.setDpName("TravelAssistant");
		dp.getState().add("LEG_REFINED");
		point.getDomainProperty().add(dp);
		goal.getPoint().add(point);

		EffectType effect = new EffectType();
		Event event = new Event();
		event.setDpName("TravelAssistant");
		event.setEventName("backToPlanned");
		effect.getEvent().add(event);

		int index = hoaaCounter - 1;
		ConcreteActivity act = new ConcreteActivity(source, target,
				"TA_SaveLeg-" + index, null);

		act.setEffect(effect);

		return Optional.of(act);
	}

	private Optional<AbstractActivity> buildAbstractActivityfor(
			DomainObjectInstance doi, String token, int source, int target) {
		return Optional.of(buildLeg(doi, source, target, token));
	}

	private AbstractActivity buildLeg(DomainObjectInstance doi, int source,
			int target, String token) {
		GoalType goal = new GoalType();
		Point point = new Point();
		DomainProperty dp = new DomainProperty();
		dp.setDpName("TravelAssistant");
		dp.getState().add("LEG_REFINED");
		point.getDomainProperty().add(dp);
		goal.getPoint().add(point);
		AbstractActivity act = new AbstractActivity(source, target, hoaaName
				+ hoaaCounter, goal);
		act.setAbstract(true);
		act.setAbstractType("GeneratedAbstract");
		hoaaCounter++;

		// setting the variables for the generated abstract activity
		List<VariableType> actionVariable = buildActionVariables(act, token);
		act.setActionVariables(actionVariable);

		return act;
	}

	private List<VariableType> buildActionVariables(AbstractActivity activity,
			String token) {
		// String varPrefix = activity.getName() + ".";
		String varPrefix = "";
		List<VariableType> actionVariable = new ArrayList<VariableType>();
		VariableType legDetails = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "LegDetails", token);

		actionVariable.add(legDetails);
		return actionVariable;
	}

}
