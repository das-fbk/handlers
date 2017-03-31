package eu.fbk.das.domainobject.executable.utils.BotTelegram;

import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.BACKCOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ENGLISH;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ESPANOL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.ITALIANO;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.LANGUAGECOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.MANUAL;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Commands.STARTCOMMAND;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskDetails;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskFrom;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskFromManual;
import static eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards.keyboardAskTo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Current;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Database;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Texts;
import eu.fbk.das.domainobject.executable.utils.GoogleAPI.GoogleAPIWrapper;

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
	private String TransportType;
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

	ArrayList<TripAlternative> alternatives = new ArrayList<TripAlternative>();

	public ArrayList<TripAlternative> getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(ArrayList<TripAlternative> alternatives) {
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLongit() {
		return longit;
	}

	public void setLongit(Double longit) {
		this.longit = longit;
	}

	private Double lat, longit;

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

	public TravelAssistantBot(String name, String token, Boolean startReceived,
			Boolean sourceReceived, Boolean destinationReceived,
			Boolean resultsReceived) {
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

	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
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
		Message message = cbq.getMessage();
		/*
		 * if (message.getText().startsWith(AUTOBUSCOMMAND)) autobusEdit(cbq);
		 * else if (message.getText().startsWith(TRAINSCOMMAND)) {
		 * trainsEdit(cbq); }
		 */
	}

	private final ConcurrentHashMap<Integer, Integer> userState = new ConcurrentHashMap<>();

	// Incoming Messages Handler

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
			break;

		case BACKCOMMAND:
			// sendMessageDefault(message, keyboardStart(chatId),
			// textStartMain(Current.getLanguage(chatId)));
			break;
		case MANUAL:
			// Manual definition of the FROM location
			// set first the user choice to insert manual source
			this.setManualLocation(true);
			sendMessageDefault(
					message,
					keyboardAskFromManual(chatId,
							Database.getRome2RioDestination()),
					"Inserisci la citta' di partenza");
			break;

		// end messages-based commands

		default:
			System.out.println("MENU: " + Current.getMenu(chatId));
			switch (Current.getMenu(chatId)) {
			// menu messages
			case START:
				sendMessageDefault(
						message,
						keyboardAskFrom(chatId,
								Database.getRome2RioDestination()),
						"Benvenuto nel tuo assistente di viaggio virtuale. Per iniziare seleziona  "
								+ "una delle due alternative");
				System.out.println("qui inizi il chat bot");
				// memorize ChatID Started
				this.setStartReceived(true);

				this.setCurrentID(chatId);
				break;
			case FROMMANUAL:
				this.setStart(message.getText());
				this.setSourceReceived(true);
				sendMessageDefault(
						message,
						keyboardAskTo(chatId, Database.getRome2RioDestination()),
						"Ora inserisci la citta' di destinazione");
				this.setCurrentID(chatId);
				break;

			case FROM:

				// Journey Source memorization
				this.setStart(message.getText());
				this.setSourceReceived(true);
				sendMessageDefault(
						message,
						keyboardAskTo(chatId, Database.getRome2RioDestination()),
						"Ora inserisci la citta' di destinazione");
				this.setCurrentID(chatId);
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
						"VUOI DEFINIRE I DETTAGLI DEL TUO VIAGGIO?");

				this.setCurrentID(chatId);

				break;
			case DETAILS:
				System.out.println(message.getText());
				String option = message.getText();
				if (option.equalsIgnoreCase("SI")) {
					// here we define defaultData
					// CURRENT DATE
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

					SendMessage sendMessage = new SendMessage()
							.setChatId(message.getChatId().toString());
					sendMessage.setText("--- CALCOLO ALTERNATIVE IN CORSO ---");

					sendMessage(sendMessage);

				} else if (option.equalsIgnoreCase("NO")) {
					// here we define defaultData
					// CURRENT DATE
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

					SendMessage sendMessage = new SendMessage()
							.setChatId(message.getChatId().toString());
					sendMessage.setText("--- CALCOLO ALTERNATIVE IN CORSO ---");
					sendMessage(sendMessage);

				} else {
					// choosen trip received
					this.setChoosenAlternative(message.getText());

				}
				break;
			case CALCOLA:
				this.setResultsReceived(true);
				this.setCurrentID(chatId);

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
			// case CALCOLA:
			/*
			 * ArrayList<TripAlternative> alternatives1 = new
			 * ArrayList<TripAlternative>(); // qui dovresti selezionare o
			 * Rome2Rio o ViaggiaTrento in base // alla proximity if
			 * (this.getProximity().equals("globale")) {
			 * 
			 * Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();
			 * 
			 * boolean nontrovate1 = true;
			 * 
			 * String from1 = this.getStart(); String to1 =
			 * this.getDestination(); alternatives1 =
			 * rome2RioWrapper.getRome2RioAlternatives( from1, to1);
			 * 
			 * if (alternatives1.size() != 0) { // visualizzo le alternative
			 * come bottoni sendMessageDefault(message,
			 * keyboardRome2RioResult(chatId, alternatives1),
			 * textRome2RioResult(Current.getLanguage(chatId))); nontrovate1 =
			 * false; }
			 * 
			 * } else { // ask more informations before to call the Viaggia
			 * Trento // Service sendMessageDefault( message,
			 * keyboardAskTo(chatId, Database.getRome2RioDestination()),
			 * textStartRome2RioDestination(Current .getLanguage(chatId)));
			 * 
			 * }
			 */

			// break;
			}
		}
	}

	private String calculateProximity(String start, String destination) {
		// TODO Auto-generated method stub
		String result = "";

		if (start.contains("trento") || destination.contains("rovereto")) {
			result = "locale";

		} else {
			result = "globale";
		}

		return result;
	}

	private void handleIncomingPositionMessage(Message message)
			throws TelegramApiException, ExecutionException {

		Long chatId = message.getChatId();

		// memorizza la lat e long dell'utente
		this.setLat(message.getLocation().getLatitude());
		this.setLongit(message.getLocation().getLongitude());
		System.out.println(Current.getMenu(message.getChatId()));

		// qui calcola la posizione corrente usando il wrapper di Google API

		GoogleAPIWrapper googleAPI = new GoogleAPIWrapper();
		String indirizzo = googleAPI
				.getAddress(this.getLat(), this.getLongit());
		System.out.println("result: " + indirizzo);
		String[] parts = indirizzo.split(",");
		System.out.println(parts[2]);

		String[] subparts = parts[2].split(" ");
		String citta = subparts[2];

		this.setStart(citta);

		switch (Current.getMenu(message.getChatId())) {
		case ASKLOCATION:
			this.setCurrentLocation(true);
			break;
		}
	}

	// endregion handlers

	// region voids

	private void error(Message message) throws TelegramApiException {
		sendMessageDefaultWithReply(message, null,
				Texts.textError(Current.getLanguage(message.getChatId())));
	}

	// endregion voids

	// region utilities

	private void answerCallbackQuery(CallbackQuery cbq, String aCbqText)
			throws TelegramApiException {
		AnswerCallbackQuery aCbq = new AnswerCallbackQuery();
		aCbq.setCallbackQueryId(cbq.getId());
		aCbq.setText(aCbqText);
		answerCallbackQuery(aCbq);
	}

	private void editMessageDefault(Message message,
			InlineKeyboardMarkup keyboard, String messageText)
			throws TelegramApiException {
		EditMessageText edit = new EditMessageText();
		edit.enableMarkdown(true);
		edit.setMessageId(message.getMessageId());
		edit.setChatId(message.getChatId().toString());
		edit.setText(messageText);
		edit.setReplyMarkup(keyboard);
		editMessageText(edit);
	}

	private void sendMessageDefaultWithReply(Message message,
			ReplyKeyboard keyboard, String text) throws TelegramApiException {
		SendMessage sendMessage = new SendMessage().setChatId(
				message.getChatId().toString()).enableMarkdown(true);
		sendMessage.setText(text);
		sendMessage.setReplyMarkup(keyboard);
		sendMessage.setReplyToMessageId(message.getMessageId());

		sendMessage(sendMessage);
	}

	private void sendMessageDefault(Message message, ReplyKeyboard keyboard,
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

	private void sendMessageDefault(Message message, String text)
			throws TelegramApiException {
		sendMessageDefault(message, null, text);
	}

	private void sendVenueDefault(Message message, Float latitude,
			Float longitude) throws TelegramApiException {
		SendVenue sendVenue = new SendVenue().setChatId(message.getChatId()
				.toString());
		sendVenue.setLatitude(latitude);
		sendVenue.setLongitude(longitude);

		sendVenue(sendVenue);
	}

	// endregion utilities

}