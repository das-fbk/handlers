package eu.fbk.das.domainobject.executable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Language;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Menu;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Travel;
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

	// bot elements
	public static final String PRICE = "\u2193" + "\ud83d\udcb5";
	public static final String TIME = "\u2193" + "\u23f3";
	public static final String CHANGES = "\u2193" + "\u0058";
	public static final String DISTANCE = "\u2193" + "\u33ce";

	private static ArrayList<Travel> travels;

	// end bot elements

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

				sendMessage.setReplyMarkup(keyboardRome2RioResult(id,
						bot.getAlternatives(), "filter"));

				// send alternatives to the user
				try {
					sendMessageDefault(
							keyboardRome2RioResult(bot.getCurrentID(),
									bot.getAlternatives(), "NULL"),
							textRome2RioResult(Language.ENGLISH,
									getDifferentWayTravel(), ""));
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}
		currentConcrete.setExecuted(true);
		return;
	}

	public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
			ArrayList<TripAlternative> alternatives, String filter) {
		return keyboardZoneRome2RioResult(chatId, alternatives,
				Menu.ROME2RIORESULT, filter);
	}

	public static ArrayList<Travel> getDifferentWayTravel() {
		return travels;
	}

	private void sendMessageDefault(ReplyKeyboard keyboard, String text)
			throws TelegramApiException {
		SendMessage sendMessage = new SendMessage().setChatId(
				bot.getCurrentID()).enableMarkdown(true);
		// if (this.getProximity() != null) {

		// if (this.getProximity().equals("globale")) {
		String textNew = text.concat(" con il servizio Rome2Rio");

		sendMessage.setText(textNew);
		sendMessage.setReplyMarkup(keyboard);
		bot.sendMessage(sendMessage);

	}

	public static String textRome2RioResult(Language language,
			ArrayList<Travel> travels, String choose) {
		String result = getMessage("rome2riodifferentway", language.locale())
				+ "\n";
		switch (choose) {
		case TIME:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getDuration() + "    "
						+ travels.get(i).getCost() + "    "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getNumber_changes() + "\n\n";
			}
			break;
		case CHANGES:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getNumber_changes() + "    "
						+ travels.get(i).getDuration() + "    "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getCost() + "\n\n";
			}
			break;
		case DISTANCE:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getDuration() + "    "
						+ travels.get(i).getCost() + "    "
						+ travels.get(i).getNumber_changes() + "\n\n";
			}
			break;
		default:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getCost() + "    "
						+ travels.get(i).getDuration() + "    "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getNumber_changes() + "\n\n";
			}
			break;
		}

		result += getMessage("rome2rioresult", language.locale());
		return result;
	}

	private static String getMessage(String msg, Locale locale,
			String... params) {
		ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle",
				locale);

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);

		formatter.applyPattern(bundle.getString(msg));
		return formatter.format(params);

	}

	private static ReplyKeyboardMarkup keyboardZoneRome2RioResult(long chatId,
			ArrayList<TripAlternative> alternatives, Menu menu, String filter) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(PRICE);
		keyboard.get(0).add(TIME);
		keyboard.get(0).add(DISTANCE);
		keyboard.get(0).add(CHANGES);

		travels = new ArrayList<Travel>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Double duration = alternatives.get(i).getDuration();
			Double cost = alternatives.get(i).getPrice();
			Double distance = alternatives.get(i).getDistance();
			Integer numberChanges = alternatives.get(i).getNumber_changes();

			String durationString = "";
			if (duration < 60) {
				if (duration == 1) {
					durationString = duration.toString() + " min";
				} else {
					durationString = duration.toString() + " mins";
				}
			} else {
				int rest = duration.intValue() % 60;
				int hour = duration.intValue() / 60;
				durationString = hour + "." + rest + " h";
			}

			String distanceString = distance.toString() + " Km";
			String costString = cost.toString() + " \u20ac";
			String shangesString = numberChanges.toString();
			if (numberChanges == 1) {
				shangesString += " change";
			} else {
				shangesString += " changes";
			}

			String index = Integer.toString(i);
			mean = setKeyboardJourneyOption(index, mean);

			travels.add(new Travel(i, mean, durationString, costString,
					distanceString, shangesString));

		}

		switch (filter) {
		case TIME:
			Collections.sort(travels, Travel.timeComparator);
			break;
		case DISTANCE:
			Collections.sort(travels, Travel.distanceComparator);
			break;
		case CHANGES:
			Collections.sort(travels, Travel.changesComparator);
			break;
		default:
			Collections.sort(travels, Travel.priceComparator);
			break;
		}

		for (int i = 0; i < travels.size(); i++) {
			keyboard.add(keyboardRowButton(travels.get(i).getMean()));
		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		// Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	public static String setKeyboardJourneyOption(String index, String mean) {

		mean = Pattern.compile("Night train", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Night_trai"));

		mean = Pattern.compile("night train", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("night_trai"));

		mean = Pattern.compile("Night bus", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Night_bu"));

		mean = Pattern.compile("night bus", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("night_bu"));

		mean = Pattern.compile("Car ferry", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("Car_ferr"));

		mean = Pattern.compile("car ferry", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("car_ferr"));

		if (mean.contains("Line")) {
			mean = Pattern
					.compile(mean.substring(0, mean.indexOf("train")),
							Pattern.LITERAL).matcher(mean)
					.replaceAll(Matcher.quoteReplacement("Train"));
		}

		if (mean.contains("line")) {
			mean = Pattern
					.compile(mean.substring(0, mean.indexOf("train")),
							Pattern.LITERAL).matcher(mean)
					.replaceAll(Matcher.quoteReplacement("train"));
		}

		mean = Pattern.compile(", train to", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement(" to"));

		mean = Pattern
				.compile("Train", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE84"
								+ "Train"));

		mean = Pattern
				.compile("train", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE84"
								+ "train"));

		mean = Pattern
				.compile("Night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE86"
								+ "Night train"));

		mean = Pattern
				.compile("night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE86"
								+ "night train"));

		mean = Pattern
				.compile("Bus", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE8C"
								+ "Bus"));

		mean = Pattern
				.compile("bus", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE8C"
								+ "bus"));

		mean = Pattern
				.compile("Night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\ud83d\ude8d"
								+ "Night bus"));

		mean = Pattern
				.compile("night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\ud83d\ude8d"
								+ "night bus"));

		mean = Pattern
				.compile("Drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE98"
								+ "Drive"));

		mean = Pattern
				.compile("drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE98"
								+ "drive"));

		mean = Pattern
				.compile("Car_ferr", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\u26F4"
								+ "Car ferry"));

		mean = Pattern
				.compile("car_ferr", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\u26F4"
								+ "car ferry"));

		mean = Pattern
				.compile("Fly", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDEEB"
								+ "Fly"));

		mean = Pattern
				.compile("fly", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDEEB"
								+ "fly"));

		mean = Pattern
				.compile("Shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE90"
								+ "Shuttle"));

		mean = Pattern
				.compile("shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDE90"
								+ "shuttle"));

		mean = Pattern
				.compile("Rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83C\uDFCE"
								+ "Rideshare"));

		mean = Pattern
				.compile("rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83C\uDFCE"
								+ "rideshare"));

		mean = Pattern
				.compile("Eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDD73"
								+ "Eurotunnel"));

		mean = Pattern
				.compile("eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\uD83D\uDD73"
								+ "eurotunnel"));

		mean = Pattern
				.compile("Taxi", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement(index + "-" + "\ud83d\ude95"
								+ "Taxi"));

		mean = Pattern
				.compile("taxi", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement(index + "-" + "\ud83d\ude95"
								+ "taxi"));

		return mean;
	}

	private static KeyboardRow keyboardRowButton(String value) {
		KeyboardRow keyboardRow = new KeyboardRow();
		keyboardRow.add(value);

		return keyboardRow;
	}

	private static ReplyKeyboardMarkup keyboard() {
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboad(false);

		return replyKeyboardMarkup;
	}

}