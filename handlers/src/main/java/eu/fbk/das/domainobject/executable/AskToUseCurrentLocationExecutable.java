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

public class AskToUseCurrentLocationExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(InsertDestinationExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public AskToUseCurrentLocationExecutable(ProcessEngine processEngine,
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

		// se l'utente ha selezionato la localizzazione automatica
		// oppure vuol inserire la FROM manualmente
		// l'attivita' viene eseguita

		if (bot.getCurrentLocation()) {
			// From location calculated automatically
			currentConcrete.setExecuted(true);

			Element from = doi.getStateVariableContentByName("From");
			from.setTextContent(bot.getStart());
			// save result in response variable
			doi.setStateVariableContentByVarName("From", from);
		} else if (bot.getManualLocation()) {
			// From will be inserted manually by the user
			currentConcrete.setExecuted(true);
		}

		return;
	}

}