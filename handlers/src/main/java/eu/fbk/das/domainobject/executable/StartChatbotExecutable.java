package eu.fbk.das.domainobject.executable;

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
	private Long chatID;

	public StartChatbotExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot, Long chatID) {
		this.pe = processEngine;
		this.bot = bot;
		this.chatID = chatID;
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

			String[] parts = doi.getId().split("_");
			String userID = parts[1];
			long l = Long.parseLong(userID);

			// concrete logic
			// if ((bot.getCurrentID() == l) && bot.getStartReceived())
			if (bot.getStartReceived()) {
				// set activity to executed
				logger.debug("Bot received Start Command ");
				currentConcrete.setExecuted(true);
				// pe.stepAll();
				// ActionEvent e = new ActionEvent(pe,
				// ActionEvent.ACTION_PERFORMED, "step");
				// aListner.actionPerformed(event);
			}
		}

		return;
	}
}
