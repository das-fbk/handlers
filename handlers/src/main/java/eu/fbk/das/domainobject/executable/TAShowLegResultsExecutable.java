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

public class TAShowLegResultsExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public static final String PRICE = "\u0024"; // \ud83d\udcb5
	public static final String TIME = "\u231a";
	public static final String CHANGES = "\u25cf" + "\u25cb" + "\u25cf";
	public static final String DISTANCE = "\u21e5";

	public static final String DATEHOUR = "\ud83d\udcc5";
	public static final String RIDERRATING = "\u2b50";

	public static final String ROME2RIO = "ROME2RIO";
	public static final String BLABLACAR = "BLABLACAR";

	public static final String CALCOLA = "\ud83d\udd0d" + "CALCOLA";

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

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			// retrieve the result list from the service
			Element result = doi.getStateVariableContentByName("PlanList");
			if (result.getFirstChild() != null
					&& !result.getFirstChild().getNodeValue().isEmpty()) {

				// send PLAN to the user

				SendMessage sendMessage = new SendMessage();
				sendMessage.setText("List of possible solutions");

				String plan = result.getFirstChild().getNodeValue();

				Long id = bot.getCurrentID();

				// qui devo differenziare tra Rome2Rio e BlaBlaCar
				try {
					bot.sendMessageDefault(
							Keyboards.keyboardRome2RioAfterChoose(id),
							Texts.textRome2RioAfterChoose(
									Current.getLanguage(id), plan));
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// send results to the user
				/*
				 * try {
				 * bot.sendMessageDefault(Keyboards.keyboardBlaBlaCarResult( id,
				 * bot.getAlternativesBlaBlaCar(), "NULL"), Texts
				 * .textBlaBlaCarResult(Current.getLanguage(id),
				 * Keyboards.getDifferentWayTravelBlaBlaCar(), "")); } catch
				 * (TelegramApiException e) {
				 * 
				 * e.printStackTrace(); }
				 */

			}

		}
		currentConcrete.setExecuted(true);
		return;
	}
}