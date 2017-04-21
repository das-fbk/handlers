package eu.fbk.das.domainobject.executable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Menu;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class ShowResultsExecutable extends AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(ShowResultsExecutable.class);

	private ProcessEngine pe;
	private TravelAssistantBot bot;

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
		ProcessDiagram process = doi.getProcess();

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			// retrieve PLAN variable
			Element plan = doi.getStateVariableContentByName("PlanList");
			if (plan.getFirstChild() != null) {
				String planValue = plan.getFirstChild().getNodeValue();

				// send PLAN to the user

				SendMessage sendMessage = new SendMessage();
				sendMessage.setText("Seleziona una della seguenti alternative");

				Long id = bot.getCurrentID();

				String idString = id.toString();
				sendMessage.setChatId(idString);

				sendMessage
						.setReplyMarkup(keyboardRome2RioResult(id, planValue));

				try {
					bot.sendMessage(sendMessage);
					currentConcrete.setExecuted(true);
					// pe.stepAll();
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return;
	}

	public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
			String alternatives) {
		return keyboardZoneRome2RioResult(chatId, alternatives,
				Menu.CHOOSERESULT);
	}

	private static ReplyKeyboardMarkup keyboardZoneRome2RioResult(long chatId,
			String alternatives, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();
		String[] pieces = alternatives.split("-");
		for (int i = 0; i < pieces.length; i++) {
			String current = pieces[i];
			keyboard.add(keyboardRowButton(current));

		}

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		// keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(false);

		return replyKeyboardMarkup;
	}

	private static KeyboardRow keyboardRowButton(String value) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(value);

		return keyboardRow;
	}

}