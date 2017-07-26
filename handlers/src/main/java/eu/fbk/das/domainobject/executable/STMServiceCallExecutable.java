package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.updateshandlers.messagging.Keyboards;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Rome2RioAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TravelsRomeToRioAfterChoose;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class STMServiceCallExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public STMServiceCallExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		Element all = doi.getStateVariableContentByName("SelectedAlternative");
		Element now = doi.getStateVariableContentByName("CurrentSegment");

		String allValue = all.getFirstChild().getNodeValue();
		String nowValue = now.getFirstChild().getNodeValue();

		// qui gestisco la scelta di R2R
		// CODICE MICHAEL
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		String from = bot.getStart();
		String to = bot.getDestination();

		ArrayList<TravelsRomeToRioAfterChoose> travelsRomeToRioAfterChoose = new ArrayList<TravelsRomeToRioAfterChoose>();
		travelsRomeToRioAfterChoose = rome2RioWrapper.getRome2RioAfterChoose(
				allValue, from, to);

		ArrayList<String> help = new ArrayList<String>();

		StringTokenizer stk1 = new StringTokenizer(nowValue, ";");

		while (stk1.hasMoreTokens()) {
			String token1 = stk1.nextToken();
			help.add(token1);
		}

		// System.out.println("STRINGA RISULTANTE: " + help.toString());

		int t = 0;

		for (int i = 0; i < travelsRomeToRioAfterChoose.size(); i++) {
			if (travelsRomeToRioAfterChoose.get(i).getVehicle()
					.equals(help.get(0))
					&& travelsRomeToRioAfterChoose.get(i).getAgency()
							.equals(help.get(1))
					&& travelsRomeToRioAfterChoose.get(i).getStart()
							.equals(help.get(2))
					&& travelsRomeToRioAfterChoose.get(i).getArrive()
							.equals(help.get(3))) {

				travelsRomeToRioAfterChoose
						.get(i)
						.setVehicle(
								Keyboards
										.setKeyboardJourneyOption(travelsRomeToRioAfterChoose
												.get(i).getVehicle()));
				t = i;
			}

			// create the string for the solution choosen usin R2R
			String result = travelsRomeToRioAfterChoose.get(t).getPosition()
					+ "-" + travelsRomeToRioAfterChoose.get(t).getStart() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getArrive() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getDuration() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getVehicle() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getAgency() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getDistance() + "-"
					+ travelsRomeToRioAfterChoose.get(t).getPrice();

			Element resultList = doi
					.getStateVariableContentByName("ResultList");

			resultList.setTextContent("Rome2RioChoosen<>" + result);

			currentConcrete.setExecuted(true);

			return;
		}
	}

}