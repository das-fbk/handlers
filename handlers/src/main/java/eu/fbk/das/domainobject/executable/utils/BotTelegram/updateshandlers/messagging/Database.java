package eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by antbucc
 */
public class Database {

	public static List<String> getRome2RioSource() throws ExecutionException {
		List<String> sources = new ArrayList<String>();
		sources.add("TRENTO");
		sources.add("BOLZANO");
		sources.add("LONDRA");
		return sources;
	}

	public static List<String> getRome2RioDestination()
			throws ExecutionException {
		List<String> sources = new ArrayList<String>();
		sources.add("TRENTO");
		sources.add("BOLZANO");
		sources.add("LONDRA");
		return sources;
	}

	// endregion gets

}
