package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.CALCOLA;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.CHANGES;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DATEHOUR;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DISTANCE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ENGLISH;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ESPANOL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ITALIANO;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.LOCATION;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.MANUAL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.NEXT;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.PRICE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.SEATS;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.TIME;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import se.walkercrou.places.Place;
import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TravelBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TripAlternativeBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TravelRome2Rio;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.TravelViaggiaTrento;

/**
 * Created by antbucc
 */
public class Keyboards {

	private static ArrayList<TravelRome2Rio> travelsRomeToRio;
	private static ArrayList<TravelBlaBlaCar> travelsBlaBlaCar;
	private static ArrayList<TravelViaggiaTrento> travelsViaggiaTrento;

	// region utilities
	public static ArrayList<TravelRome2Rio> getDifferentWayTravelRomeToRio() {
		return travelsRomeToRio;
	}

	public static ArrayList<TravelViaggiaTrento> getDifferentWayTravelViaggiaTrento() {
		return travelsViaggiaTrento;
	}

	public static ArrayList<TravelBlaBlaCar> getDifferentWayTravelBlaBlaCar() {
		return travelsBlaBlaCar;
	}

	// Keyboard to ask user DETAILS
	public static ReplyKeyboardMarkup keyboardChooseViaggiaTrentoRouteType(
			long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton("fastest".toUpperCase()));
		keyboard.add(keyboardRowButton("healthy".toUpperCase()));
		keyboard.add(keyboardRowButton("leastWalking".toUpperCase()));
		keyboard.add(keyboardRowButton("leastChanges".toUpperCase()));
		keyboard.add(keyboardRowButton("greenest".toUpperCase()));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, Menu.VIAGGIATRENTOROUTETYPE);
		return replyKeyboardMarkup.setOneTimeKeyboad(true);
	}

	public static ReplyKeyboardMarkup keyboardChooseViaggiaTrentoTransportType(
			long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton("TRANSIT"));
		keyboard.add(keyboardRowButton("SHAREDBIKE"));
		keyboard.add(keyboardRowButton("SHAREDBIKE_WITHOUT_STATION"));
		keyboard.add(keyboardRowButton("CARWITHPARKING"));
		keyboard.add(keyboardRowButton("SHAREDCAR"));
		keyboard.add(keyboardRowButton("SHAREDCAR_WITHOUT_STATION"));
		keyboard.add(keyboardRowButton("BUS"));
		keyboard.add(keyboardRowButton("TRAIN"));
		keyboard.add(keyboardRowButton("WALK"));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, Menu.VIAGGIATRENTOTRANSPORTTYPE);
		return replyKeyboardMarkup.setOneTimeKeyboad(true);
	}

	// keyboards for the API
	public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
			ArrayList<TripAlternativeRome2Rio> alternatives, String filter) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(PRICE);
		keyboard.get(0).add(TIME);
		keyboard.get(0).add(DISTANCE);
		keyboard.get(0).add(CHANGES);

		travelsRomeToRio = new ArrayList<TravelRome2Rio>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Double duration = alternatives.get(i).getDuration();
			Double cost = alternatives.get(i).getPrice();
			Double distance = alternatives.get(i).getDistance();
			Integer numberChanges = alternatives.get(i).getNumber_changes();

			String distanceString = distance.toString() + " Km";
			String costString = cost.toString() + " \u20ac";
			String shangesString = numberChanges.toString();
			if (numberChanges == 1) {
				shangesString += " change";
			} else {
				shangesString += " changes";
			}

			mean = setKeyboardJourneyOption(mean);

			travelsRomeToRio.add(new TravelRome2Rio(mean, duration.toString(),
					costString, distanceString, shangesString));

		}

		switch (filter) {
		case TIME:
			Collections.sort(travelsRomeToRio, TravelRome2Rio.timeComparator);
			break;
		case DISTANCE:
			Collections.sort(travelsRomeToRio,
					TravelRome2Rio.distanceComparator);
			break;
		case CHANGES:
			Collections
					.sort(travelsRomeToRio, TravelRome2Rio.changesComparator);
			break;
		default:
			Collections.sort(travelsRomeToRio, TravelRome2Rio.priceComparator);
			break;
		}

		for (int i = 0; i < travelsRomeToRio.size(); i++) {
			keyboard.add(keyboardRowButton(travelsRomeToRio.get(i).getMean()));
		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		// Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	/*
	 * public static String setKeyboardJourneyOption(String mean) {
	 * 
	 * mean = Pattern.compile("Night train", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Night_trai"));
	 * 
	 * mean = Pattern.compile("night train", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("night_trai"));
	 * 
	 * mean = Pattern.compile("Night bus", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Night_bu"));
	 * 
	 * mean = Pattern.compile("night bus", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("night_bu"));
	 * 
	 * mean = Pattern.compile("Car ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Car_ferr"));
	 * 
	 * mean = Pattern.compile("car ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("car_ferr"));
	 * 
	 * if (mean.contains("Line")) { mean = Pattern .compile(mean.substring(0,
	 * mean.indexOf("train")), Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Train")); }
	 * 
	 * if (mean.contains("line")) { mean = Pattern .compile(mean.substring(0,
	 * mean.indexOf("train")), Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("train")); }
	 * 
	 * // train mean = Pattern.compile(", train to",
	 * Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement(" to"));
	 * 
	 * mean = Pattern .compile("Train", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement("\uD83D\uDE84 " + "Train"));
	 * 
	 * mean = Pattern .compile("train", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\uD83D\uDE84 " + "train"));
	 * 
	 * // night train mean = Pattern .compile("Night_trai", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\uD83D\uDE86 " +
	 * "Night train"));
	 * 
	 * mean = Pattern .compile("night_trai", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\uD83D\uDE86 " + "night train"));
	 * 
	 * // bus mean = Pattern .compile("Bus", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst(Matcher.quoteReplacement("\uD83D\uDE8C " + "Bus"));
	 * 
	 * mean = Pattern.compile("bus", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\uD83D\uDE8C " + "bus"));
	 * 
	 * // night bus mean = Pattern .compile("Night_bu", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\ud83d\ude8d " +
	 * "Night bus"));
	 * 
	 * mean = Pattern .compile("night_bu", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\ud83d\ude8d " + "night bus"));
	 * 
	 * // drive mean = Pattern .compile("Drive", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement("\uD83D\uDE98 " + "Drive"));
	 * 
	 * mean = Pattern .compile("drive", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\uD83D\uDE98 " + "drive"));
	 * 
	 * // car ferry mean = Pattern .compile("Car_ferr", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst(Matcher.quoteReplacement("\u26F4 " +
	 * "Car ferry"));
	 * 
	 * mean = Pattern.compile("car_ferr", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\u26F4 " + "car ferry"));
	 * 
	 * // ferry mean = Pattern.compile("Ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceFirst(Matcher.quoteReplacement("\u26F4 " + "Ferry"));
	 * 
	 * mean = Pattern.compile("ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\u26F4 " + "ferry"));
	 * 
	 * // fly mean = Pattern .compile("Fly", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst(Matcher.quoteReplacement("\uD83D\uDEEB " + "Fly"));
	 * 
	 * mean = Pattern.compile("fly", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\uD83D\uDEEB " + "fly"));
	 * 
	 * // shuttle mean = Pattern .compile("Shuttle", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\uD83D\uDE90 " +
	 * "Shuttle"));
	 * 
	 * mean = Pattern .compile("shuttle", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\uD83D\uDE90 " + "shuttle"));
	 * 
	 * // rideshare mean = Pattern .compile("Rideshare", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\uD83C\uDFCE " +
	 * "Rideshare"));
	 * 
	 * mean = Pattern .compile("rideshare", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\uD83C\uDFCE " + "rideshare"));
	 * 
	 * // eurotunnel mean = Pattern .compile("Eurotunnel", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\uD83D\uDD73 " +
	 * "Eurotunnel"));
	 * 
	 * mean = Pattern .compile("eurotunnel", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\uD83D\uDD73 " + "eurotunnel"));
	 * 
	 * // taxi mean = Pattern .compile("Taxi", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement("\ud83d\ude95 " + "Taxi"));
	 * 
	 * mean = Pattern.compile("taxi", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\ud83d\ude95 " + "taxi"));
	 * 
	 * // unknown mean = Pattern.compile("Unknown",
	 * Pattern.LITERAL).matcher(mean)
	 * .replaceFirst(Matcher.quoteReplacement("\u2047 " + "Unknown"));
	 * 
	 * mean = Pattern.compile("unknown", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\u2047 " + "unknown"));
	 * 
	 * // helicopter mean = Pattern .compile("Helicopter", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\ud83d\ude81 " +
	 * "Helicopter"));
	 * 
	 * mean = Pattern .compile("helicopter", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\ud83d\ude81 " + "helicopter"));
	 * 
	 * // tram mean = Pattern .compile("Tram", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement("\ud83d\ude8b " + "Tram"));
	 * 
	 * mean = Pattern.compile("tram", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("\ud83d\ude8b " + "tram"));
	 * 
	 * // bicycle mean = Pattern .compile("Bicycle", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\ud83d\udeb2 " +
	 * "Bicycle"));
	 * 
	 * mean = Pattern .compile("bicycle", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\ud83d\udeb2 " + "bicycle"));
	 * 
	 * // animal mean = Pattern .compile("Animal", Pattern.LITERAL)
	 * .matcher(mean) .replaceFirst( Matcher.quoteReplacement("\ud83d\udc2b " +
	 * "Animal"));
	 * 
	 * mean = Pattern .compile("animal", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement("\ud83d\udc2b " + "animal"));
	 * 
	 * return mean; }
	 */
	// viaggiaTrento
	public static ReplyKeyboardMarkup keyboardChooseStartViaggiaTrento(
			long chatId, ArrayList<TravelViaggiaTrento> alternatives) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// ViaggiaTrentoAPIWrapper viaggiaTrento = new
		// ViaggiaTrentoAPIWrapper();
		travelsViaggiaTrento = alternatives;

		Collections.sort(alternatives, TravelViaggiaTrento.timeComparator);

		for (int i = 0; i < alternatives.size(); i++) {
			int rest = Integer.parseInt(alternatives.get(i).getDuration()) % 60;
			int hour = Integer.parseInt(alternatives.get(i).getDuration()) / 60;
			String result = "";
			if (rest < 10) {
				result += (i + 1) + ". " + "\u23f1" + hour + ".0" + rest
						+ "h\n";
			} else {
				result += (i + 1) + ". " + "\u23f1" + hour + "." + rest + "h\n";
			}
			keyboard.add(keyboardRowButton(result));

		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, Menu.VIAGGIATRENTODESTINATION);
		return replyKeyboardMarkup;
	}

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

	private static ReplyKeyboardMarkup keyboardFromManual(long chatId,
			List<String> zone, Menu menu) {

		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		/*
		 * byte[] emojiBytes = new byte[] { (byte) 0xE2, (byte) 0x9D, (byte)
		 * 0x93 }; String helpEmoticon = new String(emojiBytes,
		 * Charset.forName("UTF-8")); String help = helpEmoticon + " HELP";
		 * keyboard.add(keyboardRowButton(help));
		 */

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardFrom(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		// add buttons to ask how to insert the FROM (MANUAL Or AUTOMATIC)

		// add localization button only for mobile phone.
		keyboard.add(keyboardLocationButton(LOCATION, true, true));
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
		return replyKeyboardMarkup.setOneTimeKeyboad(true);

	}

	private static ReplyKeyboardMarkup keyboardTo(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();

		List<KeyboardRow> keyboard = new ArrayList<>();

		/*
		 * byte[] emojiBytes = new byte[] { (byte) 0xE2, (byte) 0x9D, (byte)
		 * 0x93 }; String helpEmoticon = new String(emojiBytes,
		 * Charset.forName("UTF-8")); String help = helpEmoticon + " HELP";
		 * keyboard.add(keyboardRowButton(help));
		 */
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	// ///// INIZIO PULIZIA /////////

	// private static KeyboardRow keyboardRowLocation() {
	// KeyboardRow keyboardRow = new KeyboardRow();
	// keyboardRow
	// .add(new KeyboardButton("LOCATION").setRequestLocation(true));

	// return keyboardRow;
	// }

	// keyboard to send Rome2Rio Results
	// public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
	// ArrayList<TripAlternative> alternatives, String filter) {
	// return keyboardZoneRome2RioResult(chatId, alternatives,
	// Menu.ROME2RIORESULT, filter);
	// }

	/*
	 * private static ReplyKeyboardMarkup keyboardZoneRome2RioResult(long
	 * chatId, ArrayList<TripAlternative> alternatives, Menu menu, String
	 * filter) { ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
	 * List<KeyboardRow> keyboard = new ArrayList<>();
	 * 
	 * keyboard.add(new KeyboardRow()); keyboard.get(0).add(PRICE);
	 * keyboard.get(0).add(TIME); keyboard.get(0).add(DISTANCE);
	 * keyboard.get(0).add(CHANGES);
	 * 
	 * travels = new ArrayList<Travel>();
	 * 
	 * for (int i = 0; i < alternatives.size(); i++) { String mean =
	 * alternatives.get(i).getMean(); Double duration =
	 * alternatives.get(i).getDuration(); Double cost =
	 * alternatives.get(i).getPrice(); Double distance =
	 * alternatives.get(i).getDistance(); Integer numberChanges =
	 * alternatives.get(i).getNumber_changes();
	 * 
	 * String durationString = ""; if (duration < 60) { if (duration == 1) {
	 * durationString = duration.toString() + " min"; } else { durationString =
	 * duration.toString() + " mins"; } } else { int rest = duration.intValue()
	 * % 60; int hour = duration.intValue() / 60; durationString = hour + "." +
	 * rest + " h"; }
	 * 
	 * String distanceString = distance.toString() + " Km"; String costString =
	 * cost.toString() + " \u20ac"; String shangesString =
	 * numberChanges.toString(); if (numberChanges == 1) { shangesString +=
	 * " change"; } else { shangesString += " changes"; }
	 * 
	 * String index = Integer.toString(i); mean =
	 * setKeyboardJourneyOption(index, mean);
	 * 
	 * travels.add(new Travel(i, mean, durationString, costString,
	 * distanceString, shangesString));
	 * 
	 * }
	 * 
	 * switch (filter) { case TIME: Collections.sort(travels,
	 * Travel.timeComparator); break; case DISTANCE: Collections.sort(travels,
	 * Travel.distanceComparator); break; case CHANGES:
	 * Collections.sort(travels, Travel.changesComparator); break; default:
	 * Collections.sort(travels, Travel.priceComparator); break; }
	 * 
	 * for (int i = 0; i < travels.size(); i++) {
	 * keyboard.add(keyboardRowButton(travels.get(i).getMean())); }
	 * 
	 * replyKeyboardMarkup.setKeyboard(keyboard);
	 * 
	 * // Current.setMenu(chatId, menu); return replyKeyboardMarkup; }
	 */

	public static String setKeyboardJourneyOption(String mean) {

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

		// train
		mean = Pattern.compile(", train to", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement(" to"));

		mean = Pattern
				.compile("Train", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDE84 " + "Train"));

		mean = Pattern
				.compile("train", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\uD83D\uDE84 " + "train"));

		// night train
		mean = Pattern
				.compile("Night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDE86 "
								+ "Night train"));

		mean = Pattern
				.compile("night_trai", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\uD83D\uDE86 "
								+ "night train"));

		// bus
		mean = Pattern
				.compile("Bus", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(Matcher.quoteReplacement("\uD83D\uDE8C " + "Bus"));

		mean = Pattern.compile("bus", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\uD83D\uDE8C " + "bus"));

		// night bus
		mean = Pattern
				.compile("Night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\ude8d " + "Night bus"));

		mean = Pattern
				.compile("night_bu", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\ud83d\ude8d " + "night bus"));

		// drive
		mean = Pattern
				.compile("Drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDE98 " + "Drive"));

		mean = Pattern
				.compile("drive", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\uD83D\uDE98 " + "drive"));

		// national-rail
		mean = Pattern
				.compile("National-rail", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDE84 "
								+ "National-rail"));

		mean = Pattern
				.compile("national-rail", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\uD83D\uDE84 "
								+ "national-rail"));

		// car ferry
		mean = Pattern
				.compile("Car_ferr", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(Matcher.quoteReplacement("\u26F4 " + "Car ferry"));

		mean = Pattern.compile("car_ferr", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\u26F4 " + "car ferry"));

		// ferry
		mean = Pattern.compile("Ferry", Pattern.LITERAL).matcher(mean)
				.replaceFirst(Matcher.quoteReplacement("\u26F4 " + "Ferry"));

		mean = Pattern.compile("ferry", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\u26F4 " + "ferry"));

		// fly
		mean = Pattern
				.compile("Fly", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(Matcher.quoteReplacement("\uD83D\uDEEB " + "Fly"));

		mean = Pattern.compile("fly", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\uD83D\uDEEB " + "fly"));

		// plane
		mean = Pattern
				.compile("Plane", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDEEB " + "Plane"));

		mean = Pattern
				.compile("plane", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\uD83D\uDEEB " + "plane"));

		// shuttle
		mean = Pattern
				.compile("Shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDE90 " + "Shuttle"));

		mean = Pattern
				.compile("shuttle", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\uD83D\uDE90 " + "shuttle"));

		// rideshare
		mean = Pattern
				.compile("Rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83C\uDFCE " + "Rideshare"));

		mean = Pattern
				.compile("rideshare", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\uD83C\uDFCE " + "rideshare"));

		// walk
		mean = Pattern
				.compile("Walk", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83c\udfc3 " + "Walk"));

		mean = Pattern.compile("walk", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\ud83c\udfc3 " + "walk"));

		// eurotunnel
		mean = Pattern
				.compile("Eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\uD83D\uDD73 " + "Eurotunnel"));

		mean = Pattern
				.compile("eurotunnel", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\uD83D\uDD73 " + "eurotunnel"));

		// taxi
		mean = Pattern
				.compile("Taxi", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\ude95 " + "Taxi"));

		mean = Pattern.compile("taxi", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\ud83d\ude95 " + "taxi"));

		// unknown
		mean = Pattern.compile("Unknown", Pattern.LITERAL).matcher(mean)
				.replaceFirst(Matcher.quoteReplacement("\u2047 " + "Unknown"));

		mean = Pattern.compile("unknown", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\u2047 " + "unknown"));

		// helicopter
		mean = Pattern
				.compile("Helicopter", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\ude81 " + "Helicopter"));

		mean = Pattern
				.compile("helicopter", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\ud83d\ude81 " + "helicopter"));
		// tram
		mean = Pattern
				.compile("Tram", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\ude8b " + "Tram"));

		mean = Pattern.compile("tram", Pattern.LITERAL).matcher(mean)
				.replaceAll(Matcher.quoteReplacement("\ud83d\ude8b " + "tram"));

		// bicycle
		mean = Pattern
				.compile("Bicycle", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\udeb2 " + "Bicycle"));

		mean = Pattern
				.compile("bicycle", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\ud83d\udeb2 " + "bicycle"));

		// animal
		mean = Pattern
				.compile("Animal", Pattern.LITERAL)
				.matcher(mean)
				.replaceFirst(
						Matcher.quoteReplacement("\ud83d\udc2b " + "Animal"));

		mean = Pattern
				.compile("animal", Pattern.LITERAL)
				.matcher(mean)
				.replaceAll(
						Matcher.quoteReplacement("\ud83d\udc2b " + "animal"));

		return mean;
	}

	/*
	 * public static String setKeyboardJourneyOption(String index, String mean)
	 * {
	 * 
	 * mean = Pattern.compile("Night train", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Night_trai"));
	 * 
	 * mean = Pattern.compile("night train", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("night_trai"));
	 * 
	 * mean = Pattern.compile("Night bus", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Night_bu"));
	 * 
	 * mean = Pattern.compile("night bus", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("night_bu"));
	 * 
	 * mean = Pattern.compile("Car ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Car_ferr"));
	 * 
	 * mean = Pattern.compile("car ferry", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("car_ferr"));
	 * 
	 * if (mean.contains("Line")) { mean = Pattern .compile(mean.substring(0,
	 * mean.indexOf("train")), Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("Train")); }
	 * 
	 * if (mean.contains("line")) { mean = Pattern .compile(mean.substring(0,
	 * mean.indexOf("train")), Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement("train")); }
	 * 
	 * mean = Pattern.compile(", train to", Pattern.LITERAL).matcher(mean)
	 * .replaceAll(Matcher.quoteReplacement(" to"));
	 * 
	 * mean = Pattern .compile("Train", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE84" +
	 * "Train"));
	 * 
	 * mean = Pattern .compile("train", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE84" +
	 * "train"));
	 * 
	 * mean = Pattern .compile("Night_trai", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE86" +
	 * "Night train"));
	 * 
	 * mean = Pattern .compile("night_trai", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE86" +
	 * "night train"));
	 * 
	 * mean = Pattern .compile("Bus", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE8C" +
	 * "Bus"));
	 * 
	 * mean = Pattern .compile("bus", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE8C" +
	 * "bus"));
	 * 
	 * mean = Pattern .compile("Night_bu", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\ud83d\ude8d" +
	 * "Night bus"));
	 * 
	 * mean = Pattern .compile("night_bu", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\ud83d\ude8d" +
	 * "night bus"));
	 * 
	 * mean = Pattern .compile("Drive", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE98" +
	 * "Drive"));
	 * 
	 * mean = Pattern .compile("drive", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE98" +
	 * "drive"));
	 * 
	 * mean = Pattern .compile("Car_ferr", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\u26F4" +
	 * "Car ferry"));
	 * 
	 * mean = Pattern .compile("car_ferr", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\u26F4" +
	 * "car ferry"));
	 * 
	 * mean = Pattern .compile("Fly", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDEEB" +
	 * "Fly"));
	 * 
	 * mean = Pattern .compile("fly", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDEEB" +
	 * "fly"));
	 * 
	 * mean = Pattern .compile("Shuttle", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE90" +
	 * "Shuttle"));
	 * 
	 * mean = Pattern .compile("shuttle", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDE90" +
	 * "shuttle"));
	 * 
	 * mean = Pattern .compile("Rideshare", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83C\uDFCE" +
	 * "Rideshare"));
	 * 
	 * mean = Pattern .compile("rideshare", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83C\uDFCE" +
	 * "rideshare"));
	 * 
	 * mean = Pattern .compile("Eurotunnel", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\uD83D\uDD73" +
	 * "Eurotunnel"));
	 * 
	 * mean = Pattern .compile("eurotunnel", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\uD83D\uDD73" +
	 * "eurotunnel"));
	 * 
	 * mean = Pattern .compile("Taxi", Pattern.LITERAL) .matcher(mean)
	 * .replaceFirst( Matcher.quoteReplacement(index + "-" + "\ud83d\ude95" +
	 * "Taxi"));
	 * 
	 * mean = Pattern .compile("taxi", Pattern.LITERAL) .matcher(mean)
	 * .replaceAll( Matcher.quoteReplacement(index + "-" + "\ud83d\ude95" +
	 * "taxi"));
	 * 
	 * return mean; }
	 */
	// / to visualize completed addresses alternative by GoogleAPI
	public static ReplyKeyboardMarkup keyboardAddressAlternatives(long chatId,
			List<Place> alternatives, String filter) {
		return keyboardZoneAddressAlternatives(chatId, alternatives,
				Menu.REFINETOADDRESS, filter);
	}

	private static ReplyKeyboardMarkup keyboardZoneAddressAlternatives(
			long chatId, List<Place> addresses, Menu menu, String filter) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < addresses.size(); i++) {
			String current = addresses.get(i).getAddress();

			keyboard.add(keyboardRowButton(current));

			replyKeyboardMarkup.setKeyboard(keyboard);

		}

		return replyKeyboardMarkup;

	}

	// ///// FINE PULIZIA /////

	private static ReplyKeyboardMarkup keyboardCalcola(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();

		List<KeyboardRow> keyboard = new ArrayList<>();

		/*
		 * byte[] emojiBytes = new byte[] { (byte) 0xE2, (byte) 0x9D, (byte)
		 * 0x93 }; String helpEmoticon = new String(emojiBytes,
		 * Charset.forName("UTF-8")); String help = helpEmoticon + " HELP";
		 * keyboard.add(keyboardRowButton(help));
		 */
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardAskDetails(long chatId,
			List<String> zone, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardRowButton("Yes"));
		keyboard.add(keyboardRowButton("No"));

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup.setOneTimeKeyboad(true);

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
			ArrayList<TripAlternativeRome2Rio> alternatives, Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Double duration = alternatives.get(i).getDuration();
			Double cost = alternatives.get(i).getPrice();
			Double distance = alternatives.get(i).getDistance();
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
			long chatId, ArrayList<TripAlternativeRome2Rio> alternatives,
			Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		for (int i = 0; i < alternatives.size(); i++) {
			String mean = alternatives.get(i).getMean();
			Double duration = alternatives.get(i).getDuration();
			Double cost = alternatives.get(i).getPrice();
			Double distance = alternatives.get(i).getDistance();
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

	// region keyboard

	public static ReplyKeyboardMarkup keyboardStart(long chatId) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(keyboardLocationButton(LOCATION, false, true));
		keyboard.add(keyboardRowButton(MANUAL));
		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, Menu.START);
		return replyKeyboardMarkup.setOneTimeKeyboad(true);

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

	public static ReplyKeyboardMarkup keyboardCalcola(long chatId,
			List<String> sources) {
		return keyboardCalcola(chatId, sources, Menu.DETAILS);
		// return keyboardCalcola(chatId, sources, Menu.REFINEFROMADDRESS);
	}

	public static ReplyKeyboardMarkup keyboardRome2Rio(long chatId,
			List<String> sources) {
		return keyboardZoneRome2Rio(chatId, sources, Menu.ROME2RIO);
	}

	public static ReplyKeyboardMarkup keyboardRome2RioAfterChoose(long chatId) {
		return keyboardRome2RioAfterChoose(chatId, Menu.ROME2RIOAFTERCHOOSE);
	}

	private static ReplyKeyboardMarkup keyboardRome2RioAfterChoose(long chatId,
			Menu menu) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();

		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(NEXT);

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);

		return replyKeyboardMarkup.setOneTimeKeyboad(true);
	}

	public static ReplyKeyboardMarkup keyboardRome2RioDestination(long chatId,
			List<String> sources) {
		return keyboardZoneRome2Rio(chatId, sources, Menu.ROME2RIODEST);
	}

	/*
	 * public static ReplyKeyboardMarkup keyboardRome2RioResult(long chatId,
	 * ArrayList<TripAlternative> alternatives) {
	 * 
	 * return keyboardZoneRome2RioResult(chatId, alternatives,
	 * Menu.ROME2RIORESULT); }
	 */
	public static ReplyKeyboardMarkup keyboardViaggiaTrentoResult(long chatId,
			ArrayList<TripAlternativeRome2Rio> alternatives) {
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

	public static ReplyKeyboardMarkup keyboardBlaBlaCarResult(long chatId,
			ArrayList<TripAlternativeBlaBlaCar> alternatives, String filter) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(DATEHOUR);
		keyboard.get(0).add(PRICE);
		keyboard.get(0).add(SEATS);

		travelsBlaBlaCar = new ArrayList<TravelBlaBlaCar>();

		for (int i = 0; i < alternatives.size(); i++) {
			String dateHour = alternatives.get(i).getDate() + " "
					+ alternatives.get(i).getHour().substring(0, 5)
					+ "\ud83d\udcc5";
			String price = "";
			if (alternatives.get(i).getPrice() <= alternatives.get(i)
					.getRecommended_price() + 0.5
					&& alternatives.get(i).getPrice() >= alternatives.get(i)
							.getRecommended_price() - 0.5) {
				price = "\ud83d\udd36" + alternatives.get(i).getPrice()
						+ " \u20ac";
			} else if (alternatives.get(i).getPrice() < alternatives.get(i)
					.getRecommended_price() - 0.5) {
				price = "\ud83d\udd35" + alternatives.get(i).getPrice()
						+ " \u20ac";
			} else if (alternatives.get(i).getPrice() > alternatives.get(i)
					.getRecommended_price() + 0.5) {
				price = "\ud83d\udd34" + alternatives.get(i).getPrice()
						+ " \u20ac";
			}

			String seats_left = alternatives.get(i).getSeats_left()
					+ "\ud83d\udcba";
			String car_model;
			if (alternatives.get(i).getCar_model().equals("null")) {
				car_model = "\ud83d\ude98";
			} else {
				car_model = alternatives.get(i).getCar_model() + "\ud83d\ude98";
			}
			String distance = alternatives.get(i).getDistance() + " Km";

			double mins = (alternatives.get(i).getPerfect_duration() % 3600) / 3600.0;
			int hour = alternatives.get(i).getPerfect_duration() / 3600;
			DecimalFormat df = new DecimalFormat("#.##");
			String dx = df.format(hour + mins);
			String perfect_duration = dx + " h";

			String perfect_price = alternatives.get(i).getRecommended_price()
					+ " \u20ac";

			String mean = (i + 1) + ".  " + dateHour + "     " + price
					+ "     " + seats_left;

			if (alternatives.get(i).getSeats_left() > 0) {
				travelsBlaBlaCar.add(new TravelBlaBlaCar(mean, dateHour, price,
						seats_left, car_model, distance, perfect_duration,
						perfect_price));
			}

		}

		switch (filter) {
		case PRICE:
			Collections.sort(travelsBlaBlaCar, TravelBlaBlaCar.priceComparator);
			break;
		case SEATS:
			Collections.sort(travelsBlaBlaCar,
					TravelBlaBlaCar.seatsLeftComparator);
			break;
		default:
			Collections.sort(travelsBlaBlaCar,
					TravelBlaBlaCar.dateHourComparator);
			break;
		}

		for (int i = 0; i < travelsBlaBlaCar.size(); i++) {
			keyboard.add(keyboardRowButton(travelsBlaBlaCar.get(i).getMean()));
		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		// Current.setMenu(chatId, menu);
		return replyKeyboardMarkup;
	}

	private static ReplyKeyboardMarkup keyboardBlaBlaCarResult(long chatId,
			ArrayList<TripAlternativeBlaBlaCar> alternatives, Menu menu,
			String filter) {
		ReplyKeyboardMarkup replyKeyboardMarkup = keyboard();
		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(new KeyboardRow());
		keyboard.get(0).add(DATEHOUR);
		keyboard.get(0).add(PRICE);
		keyboard.get(0).add(SEATS);

		travelsBlaBlaCar = new ArrayList<TravelBlaBlaCar>();

		for (int i = 0; i < alternatives.size(); i++) {
			String dateHour = alternatives.get(i).getDate() + " "
					+ alternatives.get(i).getHour().substring(0, 5)
					+ "\ud83d\udcc5";
			String price = "";
			if (alternatives.get(i).getPrice() <= alternatives.get(i)
					.getRecommended_price() + 0.5
					&& alternatives.get(i).getPrice() >= alternatives.get(i)
							.getRecommended_price() - 0.5) {
				price = "\ud83d\udd36" + alternatives.get(i).getPrice()
						+ " \u20ac";
			} else if (alternatives.get(i).getPrice() < alternatives.get(i)
					.getRecommended_price() - 0.5) {
				price = "\ud83d\udd35" + alternatives.get(i).getPrice()
						+ " \u20ac";
			} else if (alternatives.get(i).getPrice() > alternatives.get(i)
					.getRecommended_price() + 0.5) {
				price = "\ud83d\udd34" + alternatives.get(i).getPrice()
						+ " \u20ac";
			}

			String seats_left = alternatives.get(i).getSeats_left()
					+ "\ud83d\udcba";
			String car_model;
			if (alternatives.get(i).getCar_model().equals("null")) {
				car_model = "\ud83d\ude98";
			} else {
				car_model = alternatives.get(i).getCar_model() + "\ud83d\ude98";
			}
			String distance = alternatives.get(i).getDistance() + " Km";

			double mins = (alternatives.get(i).getPerfect_duration() % 3600) / 3600.0;
			int hour = alternatives.get(i).getPerfect_duration() / 3600;
			DecimalFormat df = new DecimalFormat("#.##");
			String dx = df.format(hour + mins);
			String perfect_duration = dx + " h";

			String perfect_price = alternatives.get(i).getRecommended_price()
					+ " \u20ac";

			String mean = (i + 1) + ".  " + dateHour + "     " + price
					+ "     " + seats_left;

			if (alternatives.get(i).getSeats_left() > 0) {
				travelsBlaBlaCar.add(new TravelBlaBlaCar(mean, dateHour, price,
						seats_left, car_model, distance, perfect_duration,
						perfect_price));
			}

		}

		switch (filter) {
		case PRICE:
			Collections.sort(travelsBlaBlaCar, TravelBlaBlaCar.priceComparator);
			break;
		case SEATS:
			Collections.sort(travelsBlaBlaCar,
					TravelBlaBlaCar.seatsLeftComparator);
			break;
		default:
			Collections.sort(travelsBlaBlaCar,
					TravelBlaBlaCar.dateHourComparator);
			break;
		}

		for (int i = 0; i < travelsBlaBlaCar.size(); i++) {
			keyboard.add(keyboardRowButton(travelsBlaBlaCar.get(i).getMean()));
		}

		replyKeyboardMarkup.setKeyboard(keyboard);

		Current.setMenu(chatId, menu);
		return replyKeyboardMarkup.setOneTimeKeyboad(true);
	}

}
