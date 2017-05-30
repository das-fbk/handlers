package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.CHANGES;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DATEHOUR;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DISTANCE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.PRICE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.SEATS;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.TIME;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TravelBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TravelRome2Rio;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.TravelViaggiaTrento;

/**
 * Created by antbucc
 */
public class Texts {

	public static String textLanguage(Language language) {
		return getMessage("changelang", language.locale());
	}

	public static String textStart(Language language) {
		return getMessage("welcome", language.locale());
	}

	public static String textError(Language language) {
		return getMessage("error", language.locale());
	}

	// endregion commands

	// region Menu.START

	public static String textStartHelp(Language language) {
		return textStart(language);
	}

	public static String textStartMain(Language language) {
		return getMessage("startmain", language.locale());
	}

	public static String textStartAutobus(Language language) {
		return getMessage("startbus", language.locale());
	}

	public static String textStartTrains(Language language) {
		return getMessage("starttrain", language.locale());
	}

	public static String textStartParkings(Language language) {
		return getMessage("startparking", language.locale());
	}

	public static String textStartRome2Rio(Language language) {
		return getMessage("startrome2rio", language.locale());
	}

	public static String textStartRome2RioDestination(Language language) {
		return getMessage("startrome2rioDestination", language.locale());
	}

	public static String textRome2RioCalcola(Language language) {
		return getMessage("startrome2rioCacola", language.locale());
	}

	public static String textRome2RioResult(Language language) {
		return getMessage("rome2rioresult", language.locale());
	}

	public static String textViaggiaTrentoResult(Language language) {
		return getMessage("viaggiatrentoresult", language.locale());
	}

	public static String textViaggiaTrentoCalcola(Language language) {
		return getMessage("viaggiaTrentoCalcola", language.locale());
	}

	public static String textViaggiaTimeDeparture(Language language) {
		return getMessage("viaggiatimedeparture", language.locale());
	}

	public static String textTransportType(Language language) {
		return getMessage("viaggiatransporttype", language.locale());
	}

	public static String textStartBikeSharings(Language language) {
		return getMessage("startbikesharing", language.locale());
	}

	// endregion Menu.START

	// region Menu.LANGUAGE

	public static String textLanguageChange(Language language) {
		return getMessage("langchanged", language.locale());
	}

	public static String textWelcome(Language language) {
		return getMessage("welcome", language.locale());
	}

	public static String textSource(Language language) {
		return getMessage("source", language.locale());
	}

	public static String textDestination(Language language) {
		return getMessage("destination", language.locale());
	}

	public static String textDetails(Language language) {
		return getMessage("details", language.locale());
	}

	public static String textRoutePlanning(Language language) {
		return getMessage("routeplanning", language.locale());
	}

	// endregion Menu.LANGUAGE

	private static String getMessage(String msg, Locale locale,
			String... params) {
		ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle",
				locale);

		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		formatter.applyPattern(bundle.getString(msg));
		return formatter.format(params);

	}

	public static String textRome2RioResult(Language language,
			ArrayList<TravelRome2Rio> travels, String choose) {
		String result = getMessage("rome2riodifferentway", language.locale())
				+ "\n";
		int rest = 0;
		int hour = 0;
		String durationString = "";
		switch (choose) {
		case TIME:
			for (int i = 0; i < travels.size(); i++) {
				Float help = Float.parseFloat(travels.get(i).getDuration());
				rest = help.intValue() % 60;
				hour = help.intValue() / 60;
				if (rest < 10) {
					durationString = hour + ".0" + rest + " h";
				} else {
					durationString = hour + "." + rest + " h";
				}
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getCost() + "    " + "*"
						+ durationString + "*" + "    "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getNumber_changes() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     PRICE" + PRICE + "        *TIME*" + TIME
					+ "        DISTANCE" + DISTANCE + "        CHANGES"
					+ CHANGES + "\n\n";
			break;
		case CHANGES:
			for (int i = 0; i < travels.size(); i++) {
				Float help = Float.parseFloat(travels.get(i).getDuration());
				rest = help.intValue() % 60;
				hour = help.intValue() / 60;
				if (rest < 10) {
					durationString = hour + ".0" + rest + " h";
				} else {
					durationString = hour + "." + rest + " h";
				}
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getCost() + "    " + durationString
						+ "    " + travels.get(i).getDistance() + "    " + "*"
						+ travels.get(i).getNumber_changes() + "*" + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     PRICE" + PRICE + "        TIME" + TIME
					+ "        DISTANCE" + DISTANCE + "        *CHANGES*"
					+ CHANGES + "\n\n";
			break;
		case DISTANCE:
			for (int i = 0; i < travels.size(); i++) {
				Float help = Float.parseFloat(travels.get(i).getDuration());
				rest = help.intValue() % 60;
				hour = help.intValue() / 60;
				if (rest < 10) {
					durationString = hour + ".0" + rest + " h";
				} else {
					durationString = hour + "." + rest + " h";
				}
				result += travels.get(i).getMean() + "\n" + "        "
						+ travels.get(i).getCost() + "    " + durationString
						+ "    " + "*" + travels.get(i).getDistance() + "*"
						+ "    " + travels.get(i).getNumber_changes() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     PRICE" + PRICE + "        TIME" + TIME
					+ "        *DISTANCE*" + DISTANCE + "        CHANGES"
					+ CHANGES + "\n\n";
			break;
		default:
			for (int i = 0; i < travels.size(); i++) {
				Float help = Float.parseFloat(travels.get(i).getDuration());
				rest = help.intValue() % 60;
				hour = help.intValue() / 60;
				if (rest < 10) {
					durationString = hour + ".0" + rest + " h";
				} else {
					durationString = hour + "." + rest + " h";
				}
				result += travels.get(i).getMean() + "\n" + "        " + "*"
						+ travels.get(i).getCost() + "*" + "    "
						+ durationString + "    "
						+ travels.get(i).getDistance() + "    "
						+ travels.get(i).getNumber_changes() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     *PRICE*" + PRICE + "        TIME" + TIME
					+ "        DISTANCE" + DISTANCE + "        CHANGES"
					+ CHANGES + "\n\n";
			break;
		}

		result += getMessage("rome2rioresult", language.locale());
		return result;
	}

	public static String textBlaBlaCarResult(Language language,
			ArrayList<TravelBlaBlaCar> travels, String choose) {
		String result = getMessage("blablacarbestway", language.locale())
				+ "\n";
		result += "        " + travels.get(0).getPerfect_price() + "    "
				+ travels.get(0).getPerfect_duration() + "    "
				+ travels.get(0).getDistance() + "\n";
		result += "        " + "\ud83d\udd35 "
				+ getMessage("blablacargoodprice", language.locale()) + "\n";
		result += "        " + "\ud83d\udd36 "
				+ getMessage("blablacarokprice", language.locale()) + "\n";
		result += "        " + "\ud83d\udd34 "
				+ getMessage("blablacarbadprice", language.locale()) + "\n\n";
		result += getMessage("rome2riodifferentway", language.locale()) + "\n";

		switch (choose) {
		case PRICE:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean().substring(0, 4)
						+ travels.get(i).getDateHour() + "     " + "*"
						+ travels.get(i).getPrice() + "*" + "     "
						+ travels.get(i).getSeats_left() + "\n" + "          "
						+ travels.get(i).getCar_model() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     DATE&TIME" + DATEHOUR + "        *PRICE*" + PRICE
					+ "        SEATS" + SEATS + "\n\n";
			break;
		case SEATS:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean().substring(0, 4)
						+ travels.get(i).getDateHour() + "     "
						+ travels.get(i).getPrice() + "     " + "*"
						+ travels.get(i).getSeats_left() + "*" + "\n"
						+ "          " + travels.get(i).getCar_model() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     DATE&TIME" + DATEHOUR + "        PRICE" + PRICE
					+ "        *SEATS*" + SEATS + "\n\n";
			break;
		default:
			for (int i = 0; i < travels.size(); i++) {
				result += travels.get(i).getMean().substring(0, 4) + "*"
						+ travels.get(i).getDateHour() + "*" + "     "
						+ travels.get(i).getPrice() + "     "
						+ travels.get(i).getSeats_left() + "\n" + "          "
						+ travels.get(i).getCar_model() + "\n\n";
			}
			result += getMessage("rome2riosortby", language.locale()) + "\n";
			result += "     *DATE&TIME*" + DATEHOUR + "        PRICE" + PRICE
					+ "        SEATS" + SEATS + "\n\n";
			break;
		}

		result += getMessage("rome2rioresult", language.locale());

		return result;
	}

	public static String textViaggiaTrentoTrip(Language language,
			ArrayList<TravelViaggiaTrento> travels) {
		String result = getMessage("rome2riodifferentway", language.locale())
				+ "\n";
		for (int i = 0; i < travels.size(); i++) {
			int rest = Integer.parseInt(travels.get(i).getDuration()) % 60;
			int hour = Integer.parseInt(travels.get(i).getDuration()) / 60;
			if (rest < 10) {
				result += "\n*" + (i + 1) + ". " + "\u23f1" + hour + ".0"
						+ rest + "h*\n";
			} else {
				result += "\n*" + (i + 1) + ". " + "\u23f1" + hour + "." + rest
						+ "h*\n";
			}

			for (int j = 0; j < travels.get(i).getRoutes().size(); j++) {
				if (travels.get(i).getSteps().get(j).equals("BUS")) {
					result += "    " + Commands.BUSVIAGGIATRENTO
							+ travels.get(i).getSteps().get(j) + " "
							+ travels.get(i).getRoutes().get(j) + "\n";
				} else if (travels.get(i).getSteps().get(j).equals("TRAIN")) {
					result += "    " + Commands.TRAINVIAGGIATRENTO
							+ travels.get(i).getSteps().get(j) + " "
							+ travels.get(i).getRoutes().get(j) + "\n";
				} else {
					result += "    " + Commands.WALKVIAGGIATRENTO
							+ travels.get(i).getSteps().get(j) + " "
							+ travels.get(i).getRoutes().get(j) + "\n";
				}

			}

		}
		result += "\n";
		result += getMessage("rome2rioresult", language.locale());
		return result;
	}

}
