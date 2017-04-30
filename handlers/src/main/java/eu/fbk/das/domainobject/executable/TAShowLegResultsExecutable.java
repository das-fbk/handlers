package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Travel;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class TAShowLegResultsExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(TAShowLegResultsExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	// bot elements
	public static final String PRICE = "\u2193" + "\ud83d\udcb5";
	public static final String TIME = "\u2193" + "\u23f3";
	public static final String CHANGES = "\u2193" + "\u0058";
	public static final String DISTANCE = "\u2193" + "\u33ce";

	private static ArrayList<Travel> travels;

	// end bot elements

	public TAShowLegResultsExecutable(ProcessEngine processEngine,
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

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			// retrieve the result list from the service
			Element result = doi.getStateVariableContentByName("PlanList");
			if (result.getFirstChild() != null) {
				String resultValue = result.getFirstChild().getNodeValue();

				// send PLAN to the user

				SendMessage sendMessage = new SendMessage();
				sendMessage.setText("List of possible solutions");

				Long id = bot.getCurrentID();

				String idString = id.toString();
				sendMessage.setChatId(idString);

				// send alternatives to the user
				sendMessageHTML(resultValue);

			}

		}
		currentConcrete.setExecuted(true);
		return;
	}

	private void sendMessageHTML(String link) {

		SendMessage message1 = new SendMessage().setChatId(
				bot.getCurrentID().toString()).enableHtml(true);

		message1.setText("<a href='" + link + "'>RIDE 1</a>");
		try {
			bot.sendMessage(message1); // Sending our message object to user
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}