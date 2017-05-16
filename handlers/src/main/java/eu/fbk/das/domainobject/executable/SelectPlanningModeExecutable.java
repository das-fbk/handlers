package eu.fbk.das.domainobject.executable;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class SelectPlanningModeExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;

	public SelectPlanningModeExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;

	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// retrieve from
		Element from = doi.getStateVariableContentByName("from");
		String fromValue = from.getFirstChild().getNodeValue();

		// retrieve to
		Element to = doi.getStateVariableContentByName("to");
		String toValue = to.getFirstChild().getNodeValue();

		// add logic to select between local and global

		String proximity = this.calculateProximity(fromValue, toValue);

		// assign the value "local" or "global"
		pe.addProcVar(proc, "planner", proximity);

		currentConcrete.setExecuted(true);

		return;
	}

	private String calculateProximity(String start, String destination) {
		String result = "";
		// add here the right logic to decide between local and global

		if (start.contains("trento") || destination.contains("rovereto")) {
			result = "globale";

		} else {
			result = "globale";
		}

		return result;
	}

}