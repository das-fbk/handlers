package eu.fbk.das.domainobject.executable.test;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class TAProvideSelectedSourceExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public TAProvideSelectedSourceExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		if (bot.getChoosenRevisedFromAddress() != null) {

			String revisedFrom = bot.getChoosenRevisedFromAddress();

			// update FROM variable
			Element from = doi.getStateVariableContentByName("From");
			from.setTextContent(revisedFrom);
			// save result in response variable
			doi.setStateVariableContentByVarName("From", from);

			currentConcrete.setExecuted(true);
		}
		return;
	}

}