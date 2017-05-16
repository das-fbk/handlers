package eu.fbk.das.domainobject.executable;

import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.w3c.dom.Element;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class TARefineDestinationPointExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	// end bot elements

	public TARefineDestinationPointExecutable(ProcessEngine processEngine,
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

			// retrieve the actual to
			Element to = doi.getStateVariableContentByName("To");

			String toValue = to.getFirstChild().getNodeValue();

			// calculate the possible completition alternative using GOOGLE API

			// autocompletition of from and to places
			// AUTOCOMPLETE ADDRESS
			GooglePlaces client = new GooglePlaces(
					"AIzaSyBnLrMivSthmUmUipPfk5sidv7f0QvvDjg");

			List<Place> placesTo = client.getPlacesByQuery(toValue,
					GooglePlaces.MAXIMUM_RESULTS);

			// send alternatives to the user
			// send PLAN to the user

			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Select the right address!");

			Long id = bot.getCurrentID();

			String idString = id.toString();
			sendMessage.setChatId(idString);

			sendMessage.setReplyMarkup(Keyboards.keyboardAddressAlternatives(
					id, placesTo, "filter"));

			bot.sendMessageAlternativeAddresses(Keyboards
					.keyboardAddressAlternatives(bot.getCurrentID(), placesTo,
							"NULL"), "");

		}
		currentConcrete.setExecuted(true);
		return;
	}

}