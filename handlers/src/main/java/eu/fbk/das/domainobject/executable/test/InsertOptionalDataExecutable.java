package eu.fbk.das.domainobject.executable.test;

import java.time.ZonedDateTime;
import java.util.Date;

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

		// check if the optional Data has been inserted by the user
		// if (bot.getOptionalDataDefined()) {
		logger.info("Bot received Optional Data for a journey");
		Element date = doi.getStateVariableContentByName("Date");
		Date currentDate = new Date();
		String data = currentDate.toString();
		date.setTextContent(data);
		doi.setStateVariableContentByVarName("Date", date);
		// DepartureTime
		Element time = doi.getStateVariableContentByName("DepartureTime");
		ZonedDateTime zdt = ZonedDateTime.now();
		String nowTime = zdt.toString();
		time.setTextContent(nowTime);
		doi.setStateVariableContentByVarName("DepartureTime", time);
		// TransportType
		Element transport = doi.getStateVariableContentByName("TransportType");
		transport.setTextContent("TRANSIT");
		doi.setStateVariableContentByVarName("TransportType", transport);
		// RouteType
		Element route = doi.getStateVariableContentByName("RouteType");
		route.setTextContent("fastest");
		doi.setStateVariableContentByVarName("RouteType", route);

		// set activity to be executed
		currentConcrete.setExecuted(true);
		// pe.stepAll();
		// }

		return;
	}
}