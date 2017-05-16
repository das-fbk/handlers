package eu.fbk.das.domainobject.executable;

import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Current;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class ShowResultsExecutable extends AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	// bot constants
	public static final String PRICE = "\u2193" + "\ud83d\udcb5";
	public static final String TIME = "\u2193" + "\u23f3";
	public static final String CHANGES = "\u2193" + "\u0058";
	public static final String DISTANCE = "\u2193" + "\u33ce";

	public ShowResultsExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
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

			// retrieve PLAN variable
			Element plan = doi.getStateVariableContentByName("PlanList");
			if (plan.getFirstChild() != null) {
				// String planValue = plan.getFirstChild().getNodeValue();

				// send PLAN to the user

				SendMessage sendMessage = new SendMessage();

				Long id = bot.getCurrentID();

				String idString = id.toString();
				sendMessage.setChatId(idString);

				// send alternatives to the user
				try {
					System.out.println(bot.getCurrentID());
					bot.sendMessageDefault(Keyboards.keyboardRome2RioResult(id,
							bot.getRomeToRioAlternatives(), "NULL"), Texts
							.textRome2RioResult(Current.getLanguage(id),
									Keyboards.getDifferentWayTravelRomeToRio(),
									""));

				} catch (TelegramApiException e1) {

					e1.printStackTrace();
				}

			}

		}
		currentConcrete.setExecuted(true);
		return;
	}

}