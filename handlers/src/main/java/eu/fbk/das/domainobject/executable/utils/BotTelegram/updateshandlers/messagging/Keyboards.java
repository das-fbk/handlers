package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.CALCOLA;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ENGLISH;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ESPANOL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ITALIANO;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.LOCATION;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.MANUAL;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import eu.fbk.das.domainobject.executable.utils.TripAlternative;

/**
 * Created by antbucc
 */
public class Keyboards {

	// region utilities

	private static ReplyKeyboardMarkup keyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(false);

		return replyKeyboardMarkup;
	}

	private static KeyboardRow keyboardLocationButton(String value,
			Boolean user, Boolean location) {

		KeyboardButton keyboardLocationButton = new KeyboardButton();
		keyboardLocationButton.setText(value);
		keyboardLocationButton.setRequestLocation(true);
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(keyboardLocationButton);

		return keyboardRow;

	}

	private static KeyboardRow keyboardRowButton(String value) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(value);

		return keyboardRow;
	}

	private static KeyboardRow keyboardRowLocation() {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow
				.add(new KeyboardButton("LOCATION").setRequestLocation(true));

		return keyboardRow;
	}

	private static ReplyKeyboardMarkup keyboardFromManual(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardFrom(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// add buttons to ask how to insert the FROM (MANUAL Or AUTOMATIC)
		keyboard.add(keyboardLocationButton(LOCATION, false, true));
		keyboard.add(keyboardRowButton(MANUAL));

		// BACK BUTTON
		/*
		 * byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte)
		 * 0x94, (byte) 0x99 }; String backEmoticon = new String(emojiBytes,
		 * Charset.forName("UTF-8"));
		 * 
		 * keyboard.add(keyboardRowButton(backEmoticon));
		 */
		// END BACK BUTTON

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardTo(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardAskDetails(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton("SI"));
		keyboard.add(keyboardRowButton("NO"));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardTransportType(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardTimeDeparture(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardZoneRome2Rio(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// for (String p : zone)
		// keyboard.add(keyboardRowButton(p));

		// keyboard.add(keyboardRowLocation());
		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardZoneRome2RioResult(long chatId,
			ArrayList<TripAlternative> alternatives, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Long duration = alternatives.get(i).getDuration();
			Long cost = alternatives.get(i).getPrice();
			Long distance = alternatives.get(i).getDistance();
			String durationString = duration.toString() + " min";
			String distanceString = distance.toString() + " Km";
			String costString = cost.toString() + " \u20ac";

			String finalString = mean + " [" + durationString + " - "
					+ distanceString + " - " + costString + "]";
			keyboard.add(keyboardRowButton(finalString));

		}

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	// keyboard con i risultato di Viaggia Trento
	private static ReplyKeyboardMarkup keyboardZoneViaggiaTrentoResult(
			long chatId, ArrayList<TripAlternative> alternatives, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Long duration = alternatives.get(i).getDuration();
			Long cost = alternatives.get(i).getPrice();
			Long distance = alternatives.get(i).getDistance();
			String durationString = duration.toString() + " min";
			String distanceString = distance.toString() + " Km";
			String costString = cost.toString() + " \u20ac";

			String finalString = mean + " [" + durationString + " - "
					+ distanceString + " - " + costString + "]";
			keyboard.add(keyboardRowButton(finalString));

		}
		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboard keyboardZoneRome2RioResult(Long chatId,
			ArrayList<TripAlternative> alternatives, Menu menu) {
		// TODO Auto-generated method stub
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			keyboard.add(keyboardRowButton(mean));

		}

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;

	}

	private static InlineKeyboardButton first(InlineKeyboardButton btn) {
		return btn.setText("« " + btn.getText());
	}

	private static InlineKeyboardButton second(InlineKeyboardButton btn) {
		return btn.setText("‹ " + btn.getText());
	}

	private static InlineKeyboardButton penultimate(InlineKeyboardButton btn) {
		return btn.setText(btn.getText() + " ›");
	}

	private static InlineKeyboardButton last(InlineKeyboardButton btn) {
		return btn.setText(btn.getText() + " »");
	}

	// endregion utilities

	// region keyboard

	public static ReplyKeyboardMarkup keyboardStart(long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// prima keyboard dopo lo start
		// chiedo all'utente se vuole usare la sua localizzazione come punto di
		// partenza
		// oppure scriverle

		keyboard.add(keyboardLocationButton(LOCATION, false, true));
		keyboard.add(keyboardRowButton(MANUAL));

		// keyboard.add(keyboardRowButton(ROME2RIO));

		// keyboard.add(keyboardRowButton(TAXICOMMAND));
		// keyboard.add(keyboardRowButton(AUTOBUSCOMMAND));
		// keyboard.add(keyboardRowButton(TRAINSCOMMAND));
		// keyboard.add(keyboardRowButton(PARKINGSCOMMAND));
		// keyboard.add(keyboardRowButton(BIKESHARINGSCOMMAND));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, Menu.START);
		return replyKeyboardMarkup;
	}

	public static ReplyKeyboardMarkup keyboardLanguage(long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton(ITALIANO));
		keyboard.add(keyboardRowButton(ENGLISH));
		keyboard.add(keyboardRowButton(ESPANOL));

		replyKeyboardMarkup.setKeyboard(keyboard);

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		Current.setMenu(chatId, Menu.LANGUAGE);
		return replyKeyboardMarkup;
	}

	// keyboard per la scrittura del FROM del viaggio automatico
	public static ReplyKeyboardMarkup keyboardAskFrom(long chatId,
			List<String> sources) {
		return keyboardFrom(chatId, sources, Menu.ASKLOCATION);
	}

	// keyboard per la scrittura del FROM del viaggio manuale
	public static ReplyKeyboardMarkup keyboardAskFromManual(long chatId,
			List<String> sources) {
		return keyboardFromManual(chatId, sources, Menu.FROMMANUAL);
	}

	// keyboard per la scrittura del To del viaggio
	public static ReplyKeyboardMarkup keyboardAskTo(long chatId,
			List<String> sources) {
		return keyboardTo(chatId, sources, Menu.TO);
	}

	// ask user details about the journey

	public static ReplyKeyboardMarkup keyboardAskDetails(long chatId,
			List<String> sources) {
		return keyboardAskDetails(chatId, sources, Menu.DETAILS);
	}

	// keyboard per chiedere l'orario di partenza
	public static ReplyKeyboardMarkup keyboardAskTimeDeparture(long chatId,
			List<String> sources) {
		return keyboardTimeDeparture(chatId, sources, Menu.TIMEDEPARTURE);
	}

	// keyboard per chiedere il tipo di trasporto da usare

	public static ReplyKeyboardMarkup keyboardAskTransportType(long chatId,
			List<String> sources) {
		return keyboardTransportType(chatId, sources, Menu.TRANSPORTTYPE);
	}

	public static ReplyKeyboardMarkup keyboardRome2Rio(long chatId,
			List<String> sources) {
		return keyboardZoneRome2Rio(chatId, sources, Menu.ROME2RIO);
	}

	public static ReplyKeyboardMarkup keyboardRome2RioDestination(long chatId,
			List<String> sources) {
		return keyboardZoneRome2Rio(chatId, sources, Menu.ROME2RIODEST);
	}

	public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
			ArrayList<TripAlternative> alternatives) {
		return keyboardZoneRome2RioResult(chatId, alternatives,
				Menu.ROME2RIORESULT);
	}

	public static ReplyKeyboardMarkup keyboardViaggiaTrentoResult(long chatId,
			ArrayList<TripAlternative> alternatives) {
		return keyboardZoneRome2RioResult(chatId, alternatives,
				Menu.ROME2RIORESULT);
	}

	public static ReplyKeyboardMarkup keyboardCalcolaRome2Rio(long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton(CALCOLA));

		replyKeyboardMarkup.setKeyboard(keyboard);

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		Current.setMenu(chatId, Menu.CALCOLA);
		return replyKeyboardMarkup;
	}

	public static ReplyKeyboardMarkup keyboardCalcolaViaggiaTrento(long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton(CALCOLA));

		replyKeyboardMarkup.setKeyboard(keyboard);

		byte[] emojiBytes = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x94,
				(byte) 0x99 };
		String backEmoticon = new String(emojiBytes, Charset.forName("UTF-8"));

		keyboard.add(keyboardRowButton(backEmoticon));
		;
		Current.setMenu(chatId, Menu.CALCOLAVIAGGIA);
		return replyKeyboardMarkup;
	}

	// endregion keyboard

}
