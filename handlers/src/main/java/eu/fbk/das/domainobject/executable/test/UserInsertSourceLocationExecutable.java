package eu.fbk.das.domainobject.executable.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class UserInsertSourceLocationExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public UserInsertSourceLocationExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		// List<String> places = Arrays.asList("Trento", "Roma", "Malaga",
		// "Barcellona", "Madrid", "Berlino", "Varsavia", "Amsterdam",
		// "Milano", "Torino", "Londra");
		List<String> places = Arrays.asList("Trento", "Roma", "Malaga",
				"Barcellona", "Madrid", "Berlino", "Varsavia", "Amsterdam",
				"Milano", "Torino", "Londra", "Parigi", "Lisbona", "Mosca",
				"Stoccolma", "Miami", "Casablanca", "Oslo", "Caracas",
				"Brasilia", "Montevideo", "Pretoria", "Melbourne", "Pechino");
		// List<String> places = Arrays.asList("Trento");
		String randomSource = places.get(new Random().nextInt(places.size()));

		// if (bot.getCurrentLocation()) {
		// // From location calculated automatically and already memorized.
		// // in this case nothing to do
		// currentConcrete.setExecuted(true);
		//
		// } else if (bot.getManualLocation()) {
		// From will be inserted manually by the user
		// here we memorixe the from
		// if (bot.getSourceReceived()) {
		Element from = doi.getStateVariableContentByName("From");
		from.setTextContent(randomSource);
		// save result in response variable
		doi.setStateVariableContentByVarName("From", from);
		currentConcrete.setExecuted(true);
		// pe.stepAll();
		// }
		// }

		return;
	}
}
