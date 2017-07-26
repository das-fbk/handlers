package eu.fbk.das.domainobject.executable.utils.BotTelegram;

import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.BACKCOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.CHANGES;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DATEHOUR;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.DISTANCE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ENGLISH;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ESPANOL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ITALIANO;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.LANGUAGECOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.MANUAL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.PRICE;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.SEATS;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.STARTCOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.TIME;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.getDifferentWayTravelRomeToRio;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskDetails;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskFrom;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskFromManual;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskTo;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardCalcola;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardChooseViaggiaTrentoRouteType;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardChooseViaggiaTrentoTransportType;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardRome2RioResult;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts.textRome2RioResult;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts.textViaggiaTrentoRouteType;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts.textViaggiaTrentoTransportType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import eu.fbk.das.domainobject.executable.utils.BlaBlaCar.TripAlternativeBlaBlaCar;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Current;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Database;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Menu;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts;
import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TravelsRomeToRioAfterChoose;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.TravelViaggiaTrento;

/**
 * Created by antbucc
 */
public class TravelAssistantBot extends TelegramLongPollingBot {

	private String token, name;
	private String proximity;
	private String timeDeparture;
	private String transportType;
	private Boolean startReceived;
	private Boolean sourceReceived;
	private Boolean destinationReceived;
	private Boolean resultsReceived;
	private Integer currentChatId;
	private ArrayList<Long> chatIds;
	private Boolean CurrentLocation;
	private String currentDate;
	private Boolean optionalDataDefined;
	private String ChoosenAlternative;
	private String ChoosenRevisedFromAddress;
	ArrayList<TripAlternativeRome2Rio> romeToRioAlternatives;
	ArrayList<TravelViaggiaTrento> viaggiaTrentoAlternatives;
	TravelsRomeToRioAfterChoose Rome2RioAfterChoose;

	public TravelsRomeToRioAfterChoose getRome2RioAfterChoose() {
		return Rome2RioAfterChoose;
	}

	public void setRome2RioAfterChoose(
			TravelsRomeToRioAfterChoose rome2RioAfterChoose) {
		Rome2RioAfterChoose = rome2RioAfterChoose;
	}

	public ArrayList<TravelViaggiaTrento> getViaggiaTrentoAlternatives() {
		return viaggiaTrentoAlternatives;
	}

	public void setViaggiaTrentoAlternatives(
			ArrayList<TravelViaggiaTrento> viaggiaTrentoAlternatives) {
		this.viaggiaTrentoAlternatives = viaggiaTrentoAlternatives;
	}

	public ArrayList<TripAlternativeRome2Rio> getRomeToRioAlternatives() {
		return romeToRioAlternatives;
	}

	public void setRomeToRioAlternatives(
			ArrayList<TripAlternativeRome2Rio> romeToRioAlternatives) {
		this.romeToRioAlternatives = romeToRioAlternatives;
	}

	ArrayList<TripAlternativeBlaBlaCar> blaBlaCarAlternatives;

	public ArrayList<TripAlternativeBlaBlaCar> getBlaBlaCarAlternatives() {
		return blaBlaCarAlternatives;
	}

	private String ChoosenRevisedToAddress;

	private ActionListener aListner;
	private ActionEvent event;

	public TravelAssistantBot(String name, String token, Boolean startReceived,
			Boolean sourceReceived, Boolean destinationReceived,
			Boolean resultsReceived, ActionListener aListner, ActionEvent event) {
		super();
		this.token = token;
		this.name = name;
		this.userIDs = new ArrayList<Integer>();
		this.startReceived = startReceived;
		this.sourceReceived = sourceReceived;
		this.destinationReceived = destinationReceived;
		this.resultsReceived = resultsReceived;
		this.chatIds = new ArrayList<Long>();
		this.CurrentLocation = false;
		this.ManualLocation = false;
		this.optionalDataDefined = false;
		this.aListner = aListner;
		this.event = event;

	}

	@Override
	public void onUpdateReceived(Update update) {

		// memorizza ID utente per le notifiche

		try {
			if (update.hasCallbackQuery())
				handleIncomingCallbackQuery(update.getCallbackQuery());

			if (update.hasMessage()) {
				Message message = update.getMessage();
				if (message.hasText())
					handleIncomingTextMessage(message);
				if (message.hasLocation())
					handleIncomingPositionMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleIncomingCallbackQuery(CallbackQuery cbq)
			throws TelegramApiException, ExecutionException {
		// Message message = cbq.getMessage();
		/*
		 * if (message.getText().startsWith(AUTOBUSCOMMAND)) autobusEdit(cbq);
		 * else if (message.getText().startsWith(TRAINSCOMMAND)) {
		 * trainsEdit(cbq); }
		 */
	}

	// Incoming Messages Handler
	private void handleIncomingTextMessage(Message message)
			throws TelegramApiException, ExecutionException {
		System.out.println(message);
		Long chatId = message.getChatId();

		Integer userID = message.getFrom().getId();
		this.userIDs.add(userID);

		// System.out.println(userID);

		switch (message.getText()) {
		// region commands
		case LANGUAGECOMMAND:
			// sendMessageDefault(message, keyboardLanguage(chatId),
			// textLanguage(Current.getLanguage(chatId)));
			break;

		case STARTCOMMAND:

			// sendMessageDefault(message, keyboardStart(chatId),
			// textStart(Current.getLanguage(chatId)));
			// update hasmap (bot, chatID)
			this.setStartReceived(true);
			launchEffect();
			break;

		case BACKCOMMAND:
			// sendMessageDefault(message, keyboardStart(chatId),
			// textStartMain(Current.getLanguage(chatId)));
			break;
		case MANUAL:
			// Manual definition of the FROM location
			// set first the user choice to insert manual source
			this.setManualLocation(true);
			this.sendMessageDefault(
					message,
					keyboardAskFromManual(chatId,
							Database.getRome2RioDestination()),
					Texts.textSource(Current.getLanguage(chatId)));
			launchEffect();
			break;

		// end messages-based commands

		default:
			System.out.println("MENU: " + Current.getMenu(chatId));
			switch (Current.getMenu(chatId)) {
			// menu messages
			case START:
				this.sendMessageDefault(
						message,
						keyboardAskFrom(chatId,
								Database.getRome2RioDestination()),
						Texts.textWelcome(Current.getLanguage(chatId)));
				this.setStartReceived(true);

				launchEffect();
				this.setCurrentID(chatId);
				break;
			case FROMMANUAL:
				this.setStart(message.getText());
				this.setSourceReceived(true);
				this.sendMessageDefault(
						message,
						keyboardAskTo(chatId, Database.getRome2RioDestination()),
						Texts.textDestination(Current.getLanguage(chatId)));

				this.setCurrentID(chatId);

				launchEffect();
				break;

			case FROM:

				// Journey Source memorization
				this.setStart(message.getText());
				this.setSourceReceived(true);
				sendMessageDefault(
						message,
						keyboardAskTo(chatId, Database.getRome2RioDestination()),
						Texts.textDestination(Current.getLanguage(chatId)));
				this.setCurrentID(chatId);

				launchEffect();
				break;
			case TO:

				// Journey Destination memorization
				this.setDestination(message.getText());
				this.setDestinationReceived(true);
				// send message
				sendMessageDefault(
						message,
						keyboardAskDetails(chatId,
								Database.getRome2RioDestination()),
						Texts.textDetails(Current.getLanguage(chatId)));

				this.setCurrentID(chatId);
				launchEffect();
				break;

			case DETAILS:
				System.out.println(message.getText());
				String option = message.getText();
				if (option.equalsIgnoreCase("Yes")) {
					// here we define defaultData
					// CURRENT DATE
					System.out.println("UTENTE VUOLE METTERE DETTAGLI");
					sendMessageDefault(message,
							keyboardChooseViaggiaTrentoRouteType(chatId),
							textViaggiaTrentoRouteType(Current
									.getLanguage(chatId)));
					/*
					 * Date currentDate = new Date(); String data =
					 * currentDate.toString(); this.setCurrentDate(data); //
					 * DEPARTUR TIME - CURRENT TIME ZonedDateTime zdt =
					 * ZonedDateTime.now(); String time = zdt.toString();
					 * this.setDepartureTime(time); // TRANSPORT TYPE - TRANSIT
					 * this.setTransportType("TRANSIT"); // ROUTE TYPE - fastest
					 * this.setRouteType("fastest");
					 * 
					 * this.setOptionalDataDefined(true);
					 * 
					 * sendMessageDefault( message, keyboardCalcola(chatId,
					 * Database.getRome2RioDestination()),
					 * "--- Route planning in progress ---");
					 */

				} else if (option.equalsIgnoreCase("No")) {
					// here we define defaultData
					// CURRENT DATE
					System.out.println("UTENTE NON VUOLE DETTAGLI");
					Date currentDate = new Date();
					String data = currentDate.toString();
					this.setCurrentDate(data);
					// DEPARTUR TIME - CURRENT TIME
					ZonedDateTime zdt = ZonedDateTime.now();
					String time = zdt.toString();
					this.setDepartureTime(time);
					// TRANSPORT TYPE - TRANSIT
					this.setTransportType("TRANSIT");
					// ROUTE TYPE - fastest
					this.setRouteType("fastest");

					this.setOptionalDataDefined(true);
					/*
					 * SendMessage sendMessage = new SendMessage()
					 * .setChatId(message.getChatId().toString());
					 * sendMessage.setText
					 * ("--- CALCOLO ALTERNATIVE IN CORSO ---");
					 * sendMessage(sendMessage);
					 */
					sendMessageDefault(
							message,
							keyboardCalcola(chatId,
									Database.getRome2RioDestination()),
							Texts.textRoutePlanning(Current.getLanguage(chatId)));

				} else {

					String fromAddress = message.getText();
					this.setChoosenRevisedFromAddress(fromAddress);
					Current.setMenu(chatId, Menu.REFINETOADDRESS);
					this.setChoosenAlternative(message.getText());

				}
				launchEffect();
				break;

			case VIAGGIATRENTOROUTETYPE:
				// set the Route Type
				this.setRouteType(message.getText().toLowerCase());

				sendMessageDefault(message,
						keyboardChooseViaggiaTrentoTransportType(chatId),
						textViaggiaTrentoTransportType(Current
								.getLanguage(chatId)));

				break;

			case VIAGGIATRENTOTRANSPORTTYPE:
				// set the Route TRANSPORT
				this.setTransportType(message.getText());
				sendMessageDefault(
						message,
						keyboardCalcola(chatId,
								Database.getRome2RioDestination()),
						Texts.textRoutePlanning(Current.getLanguage(chatId)));
				this.setOptionalDataDefined(true);
				// launchEffect();
				break;

			case ROME2RIORESULT:

				switch (message.getText()) {
				case PRICE:
					sendMessageDefault(
							message,
							keyboardRome2RioResult(chatId,
									romeToRioAlternatives, message.getText()),
							textRome2RioResult(Current.getLanguage(chatId),
									getDifferentWayTravelRomeToRio(),
									message.getText()));
					break;
				case TIME:
					sendMessageDefault(
							message,
							keyboardRome2RioResult(chatId,
									romeToRioAlternatives, message.getText()),
							textRome2RioResult(Current.getLanguage(chatId),
									getDifferentWayTravelRomeToRio(),
									message.getText()));
					break;
				case CHANGES:
					sendMessageDefault(
							message,
							keyboardRome2RioResult(chatId,
									romeToRioAlternatives, message.getText()),
							textRome2RioResult(Current.getLanguage(chatId),
									getDifferentWayTravelRomeToRio(),
									message.getText()));
					break;
				case DISTANCE:
					sendMessageDefault(
							message,
							keyboardRome2RioResult(chatId,
									romeToRioAlternatives, message.getText()),
							textRome2RioResult(Current.getLanguage(chatId),
									getDifferentWayTravelRomeToRio(),
									message.getText()));
					break;
				default:
					// the alternative has been selected by the user
					// choosen trip received
					// String fromAddress = message.getText();
					// this.setChoosenRevisedFromAddress(fromAddress);

					Current.setMenu(chatId, Menu.AFTERROME2RIO);
					this.setChoosenAlternative(message.getText());

				}
				break;
			case AFTERROME2RIO:
				// here we receive the selection of a Rome2Rio alternatives
				System.out.println("AFTERROME2RIO");
				switch (message.getText()) {
				case DATEHOUR:
					sendMessageDefault(message,
							Keyboards.keyboardBlaBlaCarResult(chatId,
									this.getBlaBlaCarAlternatives(),
									message.getText()),
							Texts.textBlaBlaCarResult(
									Current.getLanguage(chatId),
									Keyboards.getDifferentWayTravelBlaBlaCar(),
									message.getText()));
					break;
				case PRICE:
					sendMessageDefault(message,
							Keyboards.keyboardBlaBlaCarResult(chatId,
									this.getBlaBlaCarAlternatives(),
									message.getText()),
							Texts.textBlaBlaCarResult(
									Current.getLanguage(chatId),
									Keyboards.getDifferentWayTravelBlaBlaCar(),
									message.getText()));
					break;
				case SEATS:
					sendMessageDefault(message,
							Keyboards.keyboardBlaBlaCarResult(chatId,
									this.getBlaBlaCarAlternatives(),
									message.getText()),
							Texts.textBlaBlaCarResult(
									Current.getLanguage(chatId),
									Keyboards.getDifferentWayTravelBlaBlaCar(),
									message.getText()));
					break;
				default:
					break;
				}
				break;
			case REFINEFROMADDRESS:
				String fromAddress = message.getText();
				this.setChoosenRevisedFromAddress(fromAddress);
				Current.setMenu(chatId, Menu.REFINETOADDRESS);
				break;
			case REFINETOADDRESS:

				String toAddress = message.getText();
				this.setChoosenRevisedToAddress(toAddress);
				// Current.setMenu(chatId, Menu.REFINETOADDRESS);
				break;
			case VIAGGIATRENTODESTINATION:
				String selectedAlternative = message.getText();
				this.setChoosenAlternative(selectedAlternative);
				break;
			case CALCOLA:
				this.setResultsReceived(true);
				this.setCurrentID(chatId);

				launchEffect();
				break;

			case LANGUAGE:
				// region menu.LANGUAGE
				switch (message.getText()) {
				case ITALIANO:
					/*
					 * Current.setLanguage(chatId, Language.ITALIANO);
					 * sendMessageDefault(message, keyboardLanguage(chatId),
					 * textLanguageChange(Current.getLanguage(chatId)));
					 */
					break;
				case ENGLISH:
					/*
					 * Current.setLanguage(chatId, Language.ENGLISH);
					 * sendMessageDefault(message, keyboardLanguage(chatId),
					 * textLanguageChange(Current.getLanguage(chatId)));
					 */
					break;
				case ESPANOL:
					/*
					 * Current.setLanguage(chatId, Language.ESPANOL);
					 * sendMessageDefault(message, keyboardLanguage(chatId),
					 * textLanguageChange(Current.getLanguage(chatId)));
					 */
					break;

				default:
					/*
					 * sendMessageDefaultWithReply(message,
					 * keyboardStart(chatId),
					 * textError(Current.getLanguage(chatId)));
					 */
					break;
				}
				// endregion menu.LANGUAGE
				break;
			/*
			 * case START: // region menu.START
			 * System.out.println("qui: "+message.getText()); switch
			 * (message.getText()) {
			 * 
			 * 
			 * case MANUAL: // sendMessageDefault(message,
			 * keyboardRome2Rio(chatId, Database.getRome2RioSource()),
			 * textStartRome2Rio(Current.getLanguage(chatId)));
			 * System.out.println("Inserimenti Manauel dall'utente"); break;
			 * case ROME2RIO:
			 * 
			 * sendMessageDefault(message, keyboardRome2Rio(chatId,
			 * Database.getRome2RioSource()),
			 * textStartRome2Rio(Current.getLanguage(chatId))); break;
			 * 
			 * case HELPCOMMAND: sendMessageDefault(message,
			 * keyboardStart(chatId),
			 * textStartHelp(Current.getLanguage(chatId))); break; case
			 * TAXICOMMAND: sendMessageDefault(message, keyboardStart(chatId),
			 * textStartTaxi(Database.getTaxiContacts())); break; case
			 * AUTOBUSCOMMAND: sendMessageDefault(message,
			 * keyboardAutobus(chatId, Database.getAutobusRoutes()),
			 * textStartAutobus(Current.getLanguage(chatId))); break; case
			 * TRAINSCOMMAND: sendMessageDefault(message, keyboardTrains(chatId,
			 * Database.getTrainsRoutes()),
			 * textStartTrains(Current.getLanguage(chatId))); break; case
			 * PARKINGSCOMMAND: sendMessageDefault(message,
			 * keyboardParkings(chatId, Database.getParkings()),
			 * textStartParkings(Current.getLanguage(chatId))); break; case
			 * BIKESHARINGSCOMMAND: sendMessageDefault(message,
			 * keyboardBikeSharings(chatId, Database.getBikeSharings()),
			 * textStartBikeSharings(Current.getLanguage(chatId))); break;
			 * default: sendMessageDefaultWithReply(message,
			 * keyboardStart(chatId), textError(Current.getLanguage(chatId)));
			 * break; } // endregion start menu break;
			 */

			case ROME2RIO:
				/*
				 * // region menu.ROME2RIO this.setStart(message.getText());
				 * 
				 * sendMessageDefault( message,
				 * keyboardRome2RioDestination(chatId,
				 * Database.getRome2RioDestination()),
				 * textStartRome2RioDestination(Current .getLanguage(chatId)));
				 */
				break;
			case ROME2RIODEST:
				// region menu.ROME2RIO
				/*
				 * this.setDestination(message.getText());
				 * 
				 * sendMessageDefault(message, keyboardCalcolaRome2Rio(chatId),
				 * textRome2RioCalcola(Current.getLanguage(chatId)));
				 */
				break;

			}
		}
	}

	private void handleIncomingPositionMessage(Message message)
			throws TelegramApiException, ExecutionException {

		this.setLat(message.getLocation().getLatitude());
		this.setLongit(message.getLocation().getLongitude());
		System.out.println(Current.getMenu(message.getChatId()));

		GoogleAPIWrapper googleAPI = new GoogleAPIWrapper();
		String indirizzo = googleAPI
				.getAddress(this.getLat(), this.getLongit());
		// System.out.println("result: " + indirizzo);
		// String[] parts = indirizzo.split(",");
		// System.out.println(parts[2]);

		// String[] subparts = parts[2].split(" ");
		// String citta = subparts[2];

		switch (Current.getMenu(message.getChatId())) {
		case ASKLOCATION:
			this.setStart(indirizzo);
			this.setSourceReceived(true);
			this.sendMessageDefault(
					message,
					keyboardAskTo(message.getChatId(),
							Database.getRome2RioDestination()), Texts
							.textDestination(Current.getLanguage(message
									.getChatId())));

			this.setCurrentID(message.getChatId());
			this.setCurrentLocation(true);
			launchEffect();
			break;

		}
	}

	// endregion handlers

	public void sendMessageDefault(ReplyKeyboard keyboard, String text)
			throws TelegramApiException {
		SendMessage sendMessage = new SendMessage().setChatId(
				this.getCurrentID()).enableMarkdown(true);

		sendMessage.setText(text);
		sendMessage.setReplyMarkup(keyboard);
		this.sendMessage(sendMessage);

	}

	public void sendMessageDefault(Message message, ReplyKeyboard keyboard,
			String text) throws TelegramApiException {
		SendMessage sendMessage = new SendMessage().setChatId(
				message.getChatId().toString()).enableMarkdown(true);
		if (this.getProximity() != null) {
			String textNew = "";
			if (this.getProximity().equals("globale")) {
				textNew = text.concat(" con il servizio Rome2Rio");
			} else {
				textNew = text.concat(" con il servizio Viaggia Trento");
			}
			sendMessage.setText(textNew);
			sendMessage.setReplyMarkup(keyboard);
			sendMessage(sendMessage);

		} else {
			sendMessage.setText(text);
			sendMessage.setReplyMarkup(keyboard);
			sendMessage(sendMessage);

		}

	}

	public void sendMessageDefault(Message message, String text)
			throws TelegramApiException {
		sendMessageDefault(message, null, text);
	}

	/*
	 * private void sendVenueDefault(Message message, Float latitude, Float
	 * longitude) throws TelegramApiException { SendVenue sendVenue = new
	 * SendVenue().setChatId(message.getChatId() .toString());
	 * sendVenue.setLatitude(latitude); sendVenue.setLongitude(longitude);
	 * 
	 * sendVenue(sendVenue); }
	 */
	// endregion utilities

	public String getChoosenAlternative() {
		return ChoosenAlternative;
	}

	public void setChoosenAlternative(String choosenAlternative) {
		ChoosenAlternative = choosenAlternative;
	}

	public Boolean getOptionalDataDefined() {
		return optionalDataDefined;
	}

	public void setOptionalDataDefined(Boolean optionalDataDefined) {
		this.optionalDataDefined = optionalDataDefined;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getDepartureTime() {
		return DepartureTime;
	}

	public void setDepartureTime(String departureTime) {
		DepartureTime = departureTime;
	}

	public String getRouteType() {
		return RouteType;
	}

	public void setRouteType(String routeType) {
		RouteType = routeType;
	}

	private String DepartureTime;
	// private String TransportType;
	private String RouteType;

	public Boolean getCurrentLocation() {
		return CurrentLocation;
	}

	public void setCurrentLocation(Boolean currentLocation) {
		CurrentLocation = currentLocation;
	}

	public Boolean getManualLocation() {
		return ManualLocation;
	}

	public void setManualLocation(Boolean manualLocation) {
		ManualLocation = manualLocation;
	}

	private Boolean ManualLocation;

	private Long currentID;

	public Long getCurrentID() {
		return currentID;
	}

	public void setCurrentID(Long currentID) {
		this.currentID = currentID;
	}

	public ArrayList<Long> getChatIds() {
		return chatIds;
	}

	public void setChatIds(ArrayList<Long> chatIds) {
		this.chatIds = chatIds;
	}

	public void addChatId(Long chatID) {
		this.chatIds.add(chatID);
	}

	public Integer getCurrentChatId() {
		return currentChatId;
	}

	public void setCurrentChatId(Integer currentChatId) {
		this.currentChatId = currentChatId;
	}

	ArrayList<TripAlternativeRome2Rio> alternatives = new ArrayList<TripAlternativeRome2Rio>();
	ArrayList<TripAlternativeBlaBlaCar> alternativesBlaBlaCar = new ArrayList<TripAlternativeBlaBlaCar>();

	public ArrayList<TripAlternativeBlaBlaCar> getAlternativesBlaBlaCar() {
		return alternativesBlaBlaCar;
	}

	public void setAlternativesBlaBlaCar(
			ArrayList<TripAlternativeBlaBlaCar> alternativesBlaBlaCar) {
		this.alternativesBlaBlaCar = alternativesBlaBlaCar;
	}

	public ArrayList<TripAlternativeRome2Rio> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(ArrayList<TripAlternativeRome2Rio> alternatives) {
		this.alternatives = alternatives;
	}

	public Boolean getResultsReceived() {
		return resultsReceived;
	}

	public void setResultsReceived(Boolean resultsReceived) {
		this.resultsReceived = resultsReceived;
	}

	public Boolean getDestinationReceived() {
		return destinationReceived;
	}

	public void setDestinationReceived(Boolean destinationReceived) {
		this.destinationReceived = destinationReceived;
	}

	public Boolean getSourceReceived() {
		return sourceReceived;
	}

	public void setSourceReceived(Boolean sourceReceived) {
		this.sourceReceived = sourceReceived;
	}

	public Boolean getStartReceived() {
		return startReceived;
	}

	public void setStartReceived(Boolean startReceived) {
		this.startReceived = startReceived;
	}

	private ArrayList<Integer> userIDs;

	public ArrayList<Integer> getUserIDs() {
		return userIDs;
	}

	public void setUserIDs(ArrayList<Integer> userIDs) {
		this.userIDs = userIDs;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getTimeDeparture() {
		return timeDeparture;
	}

	public void setTimeDeparture(String timeDeparture) {
		this.timeDeparture = timeDeparture;
	}

	public String getProximity() {
		return proximity;
	}

	public void setProximity(String proximity) {
		this.proximity = proximity;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLongit() {
		return longit;
	}

	public void setLongit(Float longit) {
		this.longit = longit;
	}

	private Float lat, longit;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDestination() {
		return destination;
	}

	public void setStart(String start) {
		this.start = start;
	}

	private String start;
	private String destination;

	public String getStart() {
		return start;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public String getChoosenRevisedFromAddress() {
		return ChoosenRevisedFromAddress;
	}

	public void setChoosenRevisedFromAddress(String choosenRevisedFromAddress) {
		ChoosenRevisedFromAddress = choosenRevisedFromAddress;
	}

	public String getChoosenRevisedToAddress() {
		return ChoosenRevisedToAddress;
	}

	public void setChoosenRevisedToAddress(String choosenRevisedToAddress) {
		ChoosenRevisedToAddress = choosenRevisedToAddress;
	}

	private void launchEffect() {
		aListner.actionPerformed(event);
	}

	public void setBlaBlaCarAlternatives(
			ArrayList<TripAlternativeBlaBlaCar> blaBlaCarAlternatives) {
		this.alternativesBlaBlaCar = blaBlaCarAlternatives;

	}

	public void sendMessageAlternativeAddresses(ReplyKeyboard keyboard,
			String text) {
		SendMessage sendMessage = new SendMessage().setChatId(getCurrentID())
				.enableMarkdown(true);

		String textNew = text.concat("Select the right destination address!");

		sendMessage.setText(textNew);
		sendMessage.setReplyMarkup(keyboard);
		try {
			sendMessage(sendMessage);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}