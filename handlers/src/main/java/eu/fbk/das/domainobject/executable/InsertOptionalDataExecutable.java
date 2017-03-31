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

public class InsertOptionalDataExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(InsertDestinationExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public InsertOptionalDataExecutable(ProcessEngine processEngine,
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

		// check if the optional Data has been inserted by the user
		if (bot.getOptionalDataDefined()) {
			logger.debug("Bot received Optional Data for a journey");
			Element date = doi.getStateVariableContentByName("Date");
			date.setTextContent(bot.getCurrentDate());
			doi.setStateVariableContentByVarName("Date", date);
			// DepartureTime
			Element time = doi.getStateVariableContentByName("DepartureTime");
			time.setTextContent(bot.getDepartureTime());
			doi.setStateVariableContentByVarName("DepartureTime", time);
			// TransportType
			Element transport = doi
					.getStateVariableContentByName("TransportType");
			transport.setTextContent(bot.getTransportType());
			doi.setStateVariableContentByVarName("TransportType", transport);
			// RouteType
			Element route = doi.getStateVariableContentByName("RouteType");
			route.setTextContent(bot.getRouteType());
			doi.setStateVariableContentByVarName("RouteType", route);

			// set activity to be executed
			currentConcrete.setExecuted(true);
		}

		return;
	}
}