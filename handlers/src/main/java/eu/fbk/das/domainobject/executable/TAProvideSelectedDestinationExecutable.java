package eu.fbk.das.domainobject.executable;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class TAProvideSelectedDestinationExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public TAProvideSelectedDestinationExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		if (bot.getChoosenRevisedToAddress() != null) {

			String revisedTo = bot.getChoosenRevisedToAddress();
			// update TO variable
			Element to = doi.getStateVariableContentByName("To");
			to.setTextContent(revisedTo);
			// save result in response variable
			doi.setStateVariableContentByVarName("To", to);

			currentConcrete.setExecuted(true);
			// pe.stepAll();
		}
		return;
	}

}
