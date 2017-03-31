package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Rome2RioAPIWrapper;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class Rome2RioCallExecutable extends AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(Rome2RioCallExecutable.class);

	private ProcessEngine pe;
	private ArrayList<TripAlternative> alternatives;
	private TravelAssistantBot bot;

	private static int hoaaCounter = 1;

	public Rome2RioCallExecutable(ProcessEngine processEngine,
			ArrayList<TripAlternative> alternatives, TravelAssistantBot bot) {
		this.pe = processEngine;
		this.alternatives = alternatives;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		ProcessDiagram process = doi.getProcess();

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.error("Domain Object with a state! ");
			// concrete logic

			Element from = doi.getStateVariableContentByName("From");
			Element to = doi.getStateVariableContentByName("To");
			String fromValue = from.getFirstChild().getNodeValue();
			String toValue = to.getFirstChild().getNodeValue();

			// call Rome2Rio Service
			this.alternatives = this.CallRome2Rio(fromValue, toValue);
			bot.setAlternatives(alternatives);

			String result = extractString(alternatives);

			// update the PlanList variable value
			Element planElement = doi.getStateVariableContentByName("PlanList");
			planElement.setTextContent(result);
			// save result in response variable
			doi.setStateVariableContentByVarName("PlanList", planElement);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}

	private String extractString(ArrayList<TripAlternative> alternatives) {
		// extract unique String from the tripAlternatives
		String result = "";
		if (alternatives.size() != 0) {
			for (int i = 0; i < alternatives.size(); i++) {
				String current = alternatives.get(i).getMean();
				if (i == 0) {
					result = i + "," + current + "-";
				} else if (i == alternatives.size() - 1) {
					result = result + i + "," + current;
				} else {
					result = result + i + "," + current + "-";
				}
			}
		}
		return result;
	}

	ArrayList<TripAlternative> CallRome2Rio(String from, String to) {

		String result = "";
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		boolean nontrovate = true;

		ArrayList<TripAlternative> alternatives = new ArrayList<TripAlternative>();

		alternatives = rome2RioWrapper.getRome2RioAlternatives(from, to);

		return alternatives;
		/*
		 * if (alternatives.size() != 0) { for (int i = 0; i <
		 * alternatives.size(); i++) { String current =
		 * alternatives.get(i).getMean(); if (i == 0) { result = current + "-";
		 * } else if (i == alternatives.size() - 1) { result = result + current;
		 * } else { result = result + current + "-"; } } } return result;
		 */

	}
}
