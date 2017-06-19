package eu.fbk.das.domainobject.executable.test;

import java.util.List;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
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
		// if (doiState != null) {
		//
		// // retrieve PLAN variable
		// Element plan = doi.getStateVariableContentByName("PlanList");
		// if (plan.getFirstChild() != null) {
		// String planValue = plan.getFirstChild().getNodeValue();
		//
		// // check if the result is from Rome2Rio or ViaggiaTrento
		// if (planValue.contains("ViaggiaTrento")) {
		// // VIAGGIA TRENTO RESULTS
		//
		// SendMessage sendMessage = new SendMessage();
		// Long id = bot.getCurrentID();
		//
		// String idString = id.toString();
		// sendMessage.setChatId(idString);
		// // send alternatives to the user
		// try {
		// System.out.println(bot.getCurrentID());
		// bot.sendMessageDefault(Keyboards
		// .keyboardChooseStartViaggiaTrento(id,
		// bot.getViaggiaTrentoAlternatives()),
		// Texts.textViaggiaTrentoTrip(Current
		// .getLanguage(id), Keyboards
		// .getDifferentWayTravelViaggiaTrento()));
		//
		// } catch (TelegramApiException e1) {
		//
		// e1.printStackTrace();
		// }
		//
		// } else {
		// // ROME2RIO RESULTS
		// // send PLAN to the user
		//
		// SendMessage sendMessage = new SendMessage();
		//
		// Long id = bot.getCurrentID();
		//
		// String idString = id.toString();
		// sendMessage.setChatId(idString);
		//
		// // send alternatives to the user
		// try {
		// System.out.println(bot.getCurrentID());
		// bot.sendMessageDefault(
		// Keyboards.keyboardRome2RioResult(id,
		// bot.getRomeToRioAlternatives(), "NULL"),
		// Texts.textRome2RioResult(Current
		// .getLanguage(id), Keyboards
		// .getDifferentWayTravelRomeToRio(), ""));
		//
		// } catch (TelegramApiException e1) {
		//
		// e1.printStackTrace();
		// }
		// }
		//
		// }
		//
		// }
		currentConcrete.setExecuted(true);
		return;
	}
}