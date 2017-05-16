package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.BlaBlaCarAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TripAlternativeBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

//class to call the BlaBlaCar Service 
public class BBCServiceCallExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(BBCServiceCallExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	ArrayList<TripAlternativeBlaBlaCar> blaBlaCarAlternatives;

	public BBCServiceCallExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
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
			// concrete activity logic
			Element source = doi.getStateVariableContentByName("Source");
			Element destination = doi
					.getStateVariableContentByName("Destination");
			String sourceValue = source.getFirstChild().getNodeValue();
			String destinationValue = destination.getFirstChild()
					.getNodeValue();

			// call BlaBlaCar Wrapped Service
			BlaBlaCarAPIWrapper blablaWrapper = new BlaBlaCarAPIWrapper();

			blaBlaCarAlternatives = blablaWrapper.getBlaBlaCarAlternatives(
					sourceValue, destinationValue);

			bot.setBlaBlaCarAlternatives(blaBlaCarAlternatives);

			// update the ResultList variable value
			Element jsonElement = doi
					.getStateVariableContentByName("ResultList");
			jsonElement.setTextContent("BlaBlaCar<>"
					+ blaBlaCarAlternatives.toString());
			// save result in response variable
			doi.setStateVariableContentByVarName("ResultList", jsonElement);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}
}
