package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.DomainObjectsVariablesUtils;
import eu.fbk.das.domainobject.executable.utils.HigherOrderActivitiesConstant;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.DomainObject.State;
import eu.fbk.das.process.engine.api.jaxb.GoalType;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class TAidentifyLegExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(TAidentifyLegExecutable.class);

	private ProcessEngine pe;

	private static int hoaaCounter = 1;
	private static String hoaaName = "";
	private static String scopeName = "";
	private static String leg = "";

	public TAidentifyLegExecutable(ProcessEngine processEngine) {
		this.pe = processEngine;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		AbstractActivity currentAbstract = (AbstractActivity) proc
				.getCurrentActivity();
		hoaaName = currentAbstract.getName() + "-";

		// find the scope which this activity belongs to, if any
		if (proc.getFather() != null) {
			if (proc.getFather().getCurrentActivity().isAbstract()) {
				AbstractActivity fatherAct = (AbstractActivity) proc
						.getFather().getCurrentActivity();
				if (fatherAct.getAbstractType().equalsIgnoreCase(
						"GeneratedAbstract")) {
					scopeName = fatherAct.getName();
					// TODO: eliminare get(0) e cercare la variabile per nome
					// (perchè contiene solo la variabile LegDetails, in
					// posizione 0)
					leg = ((Element) fatherAct.getActionVariables().get(0)
							.getContent()).getFirstChild().getNodeValue();
				}
			}
		}

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		// the host process is the process of the DO in which the HOAA is in
		// execution
		ProcessDiagram hostProcess = doi.getProcess();
		// we set the process variables to be used by the DataViewer
		pe.addProcVar(hostProcess, "isInScope", "true");
		pe.addProcVar(hostProcess, "scopePrefix", hoaaName + hoaaCounter);
		// pe.addProcVar(hostProcess, "doInstanceName", doi.getId());

		if (leg == null || leg.isEmpty() || leg.equals("")) {
			logger.error("Variable leg not found! ");
			return;
		} else {
			logger.debug("Variable leg equals to " + leg);
		}

		// StringTokenizer stk = new StringTokenizer(legDetails, "*");
		List<AbstractActivity> result = new ArrayList<AbstractActivity>();
		int start = currentAbstract.getTarget();
		// while (stk.hasMoreTokens()) {
		String token = leg;
		Optional<AbstractActivity> act = buildAbstractActivityfor(doi, token,
				start, start + 1);
		if (act.isPresent()) {
			result.add(act.get());
			start++;
		}
		// }
		/***************** INIT: build abstract activity with goal in AND *********************/
		// Point point = new Point();
		// GoalType goal = new GoalType();
		// for (DomainProperty dp : result) {
		// point.getDomainProperty().add(dp);
		// }
		// goal.getPoint().add(point);
		// String absActName = "GeneratedAbstract_";
		// AbstractActivity act = new AbstractActivity(
		// currentAbstract.getTarget(), currentAbstract.getTarget() + 1,
		// absActName + doi.getId() + "_" + hoaaCounter, goal);
		// act.setAbstract(true);
		// hoaaCounter++;
		/***************** END: build abstract activity with goal in AND *********************/

		// extend current process with hoaas
		int target = currentAbstract.getTarget();
		for (AbstractActivity activity : result) {
			proc.getActivities().add(target, activity);
			target++;
		}
		// re-arrange source e target for all activities
		int i = 0;
		for (ProcessActivity activity : proc.getActivities()) {
			activity.setSource(i);
			activity.setTarget(i + 1);
			i++;
		}
		System.out.println();

	}

	private Optional<AbstractActivity> buildAbstractActivityfor(
			DomainObjectInstance doi, String token, int source, int target) {
		StringTokenizer stk = new StringTokenizer(token, ";");
		String mean = "";
		String company = "";
		String departure = "";
		String destination = "";
		if (stk.countTokens() == 4) {
			while (stk.hasMoreTokens()) {
				mean = stk.nextToken();
				company = stk.nextToken();
				departure = stk.nextToken();
				destination = stk.nextToken();
			}
		}
		String transportMode = HigherOrderActivitiesConstant
				.getTransportationMode(mean.toLowerCase());
		switch (transportMode) {
		case "train":
			return Optional.of(buildGlobal(doi, source, target, mean, company,
					departure, destination));
		case "bus":
			return Optional.of(buildGlobal(doi, source, target, mean, company,
					departure, destination));
		case "rideshare":
			return Optional.of(buildRideshare(doi, source, target, mean,
					company, departure, destination));

		default:
			return Optional.empty();
		}
	}

	private AbstractActivity buildRideshare(DomainObjectInstance doi,
			int source, int target, String mean, String company,
			String departure, String destination) {
		GoalType goal = new GoalType();
		Point point = new Point();
		DomainProperty dp = new DomainProperty();
		dp.setDpName("RideSharing");
		dp.getState().add("AVAILABLE_PASSAGES_SENT");
		point.getDomainProperty().add(dp);
		goal.getPoint().add(point);
		AbstractActivity act = new AbstractActivity(source, target, hoaaName
				+ hoaaCounter, goal);
		act.setAbstract(true);
		act.setAbstractType("GeneratedAbstract");
		hoaaCounter++;

		// setting the variables for the generated abstract activity
		List<VariableType> actionVariable = buildActionVariables(act, mean,
				company, departure, destination);
		act.setActionVariables(actionVariable);
		// update the doi state with the variables belonging to the new
		// generated abstract activity
		updateDoiState(doi, actionVariable);
		// extend knowledge with external knowledge of domain property
		pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");
		return act;
	}

	private AbstractActivity buildGlobal(DomainObjectInstance doi, int source,
			int target, String mean, String company, String departure,
			String destination) {
		GoalType goal = new GoalType();
		Point point = new Point();
		DomainProperty dp = new DomainProperty();
		dp.setDpName("GlobalPlanner");
		dp.getState().add("ALTERNATIVES_SENT");
		point.getDomainProperty().add(dp);
		goal.getPoint().add(point);
		AbstractActivity act = new AbstractActivity(source, target, hoaaName
				+ hoaaCounter, goal);
		act.setAbstract(true);
		act.setAbstractType("GeneratedAbstract");
		hoaaCounter++;

		// setting the variables for the generated abstract activity
		// List<VariableType> actionVariable = buildActionVariables(act,
		// departure, destination);
		// act.setActionVariables(actionVariable);
		// update the doi state with the variables belonging to the new
		// generated abstract activity
		// updateDoiState(doi, actionVariable);
		// extend knowledge with external knowledge of domain property
		pe.addExternalKnowledge(doi, dp.getDpName(), "INITIAL");

		return act;
	}

	private List<VariableType> buildActionVariables(AbstractActivity activity,
			String mean, String company, String departure, String destination) {
		String varPrefix = activity.getName() + ".";
		List<VariableType> actionVariable = new ArrayList<VariableType>();
		VariableType transportMean = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "Mean", mean);
		VariableType companyName = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "Company", company);
		VariableType sourcePoint = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "Source", departure);
		VariableType destinationPoint = DomainObjectsVariablesUtils
				.newVariable(varPrefix, "Destination", destination);
		VariableType resultList = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "ResultList", "");
		VariableType userChoice = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "UserSelection", "");
		VariableType defaultSolution = DomainObjectsVariablesUtils.newVariable(
				varPrefix, "DefaultSolution", "");

		actionVariable.add(transportMean);
		actionVariable.add(companyName);
		actionVariable.add(sourcePoint);
		actionVariable.add(destinationPoint);
		actionVariable.add(resultList);
		actionVariable.add(userChoice);
		actionVariable.add(defaultSolution);

		return actionVariable;
	}

	private void updateDoiState(DomainObjectInstance doi,
			List<VariableType> actionVariable) {
		if (doi != null) {
			if (doi.getState() != null) {
				if (doi.getState().getStateVariable() != null) {
					doi.getState().getStateVariable().addAll(actionVariable);
				}
			} else {
				State s = new State();
				s.getStateVariable().addAll(actionVariable);
				doi.setState(s);
			}
		}
	}
}
