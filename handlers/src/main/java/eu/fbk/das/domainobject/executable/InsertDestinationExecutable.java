package eu.fbk.das.domainobject.executable;

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
		ProcessDiagram process = doi.getProcess();

		if (bot.getDestinationReceived()) {
			// if (bot.getDestinationReceived()) {
			// set activity to executed
			logger.debug("Bot received Destination Info");

			// update the TO variable value
			Element to = doi.getStateVariableContentByName("To");
			to.setTextContent(bot.getDestination());
			// save result in response variable
			doi.setStateVariableContentByVarName("To", to);
			currentConcrete.setExecuted(true);
			// pe.stepAll();
		}

		return;
	}
}