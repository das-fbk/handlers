package eu.fbk.das.domainobject.executable.test;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class StartChatbotExecutable extends AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(StartChatbotExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public StartChatbotExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot, Long chatID) {
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
			// removed reference to the bot function
			// set activity to executed
			logger.debug("Bot received Start Command ");
			currentConcrete.setExecuted(true);
		}
		return;
	}
}
