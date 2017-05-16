package eu.fbk.das.domainobject.executable;

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
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.GoalType;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

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
	}

	private Optional<AbstractActivity> buildAbstractActivityfor(
			DomainObjectInstance doi, String token, int source, int target) {
		// StringTokenizer stk = new StringTokenizer(token, ";");
		// String mean = "";
		// String company = "";
		// String departure = "";
		// String destination = "";
		// if (stk.countTokens() == 4) {
		// while (stk.hasMoreTokens()) {
		// mean = stk.nextToken();
		// company = stk.nextToken();
		// departure = stk.nextToken();
		// destination = stk.nextToken();
		// }
		// }
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
		// update the doi state with the variables belonging to the new
		// generated abstract activity
		// updateDoiState(doi, actionVariable);
		// extend knowledge with external knowledge of domain property
		// pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

		return act;
	}

	private List<VariableType> buildActionVariables(AbstractActivity activity,
			String token) {
		// String varPrefix = activity.getName() + ".";
		String varPrefix = "";
		List<VariableType> actionVariable = new ArrayList<VariableType>();
		VariableType legDetails = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "LegDetails", token);
		// VariableType companyName = DomainObjectsVariablesUtils.newVariable(
		// varPrefix, "Company", company);
		// VariableType sourcePoint = DomainObjectsVariablesUtils.newVariable(
		// varPrefix, "Source", departure);
		// VariableType destinationPoint = DomainObjectsVariablesUtils
		// .newVariable(varPrefix, "Destination", destination);
		// VariableType resultList = DomainObjectsVariablesUtils.newVariable(
		// varPrefix, "ResultList", "");
		// VariableType userChoice = DomainObjectsVariablesUtils.newVariable(
		// varPrefix, "UserSelection", "");
		// VariableType defaultSolution =
		// DomainObjectsVariablesUtils.newVariable(
		// varPrefix, "DefaultSolution", "");

		actionVariable.add(legDetails);
		// actionVariable.add(companyName);
		// actionVariable.add(sourcePoint);
		// actionVariable.add(destinationPoint);
		// actionVariable.add(resultList);
		// actionVariable.add(userChoice);
		// actionVariable.add(defaultSolution);

		return actionVariable;
	}

	// private void updateDoiState(DomainObjectInstance doi,
	// List<VariableType> actionVariable) {
	// if (doi != null) {
	// if (doi.getState() != null) {
	// if (doi.getState().getStateVariable() != null) {
	// doi.getState().getStateVariable().addAll(actionVariable);
	// }
	// } else {
	// State s = new State();
	// s.getStateVariable().addAll(actionVariable);
	// doi.setState(s);
	// }
	// }
	// }
}
