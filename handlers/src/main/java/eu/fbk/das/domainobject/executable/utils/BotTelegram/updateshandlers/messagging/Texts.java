package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by antbucc
 */
public class Texts {

	// region commands

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

}
