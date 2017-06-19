package eu.fbk.das.domainobject.executable.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class InsertDestinationExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(InsertDestinationExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public InsertDestinationExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		List<String> places = Arrays.asList("Trento", "Roma", "Malaga",
				"Barcellona", "Madrid", "Berlino", "Varsavia", "Amsterdam",
				"Milano", "Torino", "Londra", "Parigi", "Lisbona", "Mosca",
				"Stoccolma", "Miami", "Casablanca", "Oslo", "Caracas",
				"Brasilia", "Montevideo", "Pretoria", "Melbourne", "Pechino");
		// List<String> places = Arrays.asList("Torino");
		String randomDestination = places.get(new Random().nextInt(places
				.size()));

		// if (bot.getDestinationReceived()) {
		// // set activity to executed
		// logger.info("Bot received Destination Info");

		// update the TO variable value
		Element to = doi.getStateVariableContentByName("To");
		to.setTextContent(randomDestination);
		// save result in response variable
		doi.setStateVariableContentByVarName("To", to);
		currentConcrete.setExecuted(true);
		// }

		return;
	}
}