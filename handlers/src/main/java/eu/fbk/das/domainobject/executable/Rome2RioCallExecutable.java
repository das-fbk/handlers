package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Current;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Menu;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Rome2RioAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
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

	private JSONObject rome2rioJson;
	private TravelAssistantBot bot;

	public Rome2RioCallExecutable(ProcessEngine processEngine,
			ArrayList<TripAlternativeRome2Rio> alternatives,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.rome2rioJson = new JSONObject();
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.info("Domain Object with a state! ");
			// concrete logic

			Element from = doi.getStateVariableContentByName("From");
			Element to = doi.getStateVariableContentByName("To");
			String fromValue = from.getFirstChild().getNodeValue();
			String toValue = to.getFirstChild().getNodeValue();

			this.rome2rioJson = this.CallRome2Rio(fromValue, toValue);
			// update the PlannerOutput variable value

			Element jsonElement = doi
					.getStateVariableContentByName("PlannerOutput");
			jsonElement.setTextContent("Rome2Rio<>"
					+ this.rome2rioJson.toString());
			// save result in response variable
			doi.setStateVariableContentByVarName("PlannerOutput", jsonElement);

			// save the Rome2Rio alternatives list in the bot variable
			ArrayList<TripAlternativeRome2Rio> romeToRioAlternatives = new ArrayList<TripAlternativeRome2Rio>();

			Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();
			romeToRioAlternatives = rome2RioWrapper.getRome2RioAlternatives(
					fromValue, toValue);

			bot.setRomeToRioAlternatives(romeToRioAlternatives);
			Current.setMenu(bot.getCurrentID(), Menu.ROME2RIORESULT);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}

	private JSONObject CallRome2Rio(String from, String to) {
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		JSONObject result = new JSONObject();

		result = rome2RioWrapper.getRome2RioResponse(from, to);

		return result;
	}

}
