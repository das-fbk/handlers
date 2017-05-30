package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.Segment;
import eu.fbk.das.domainobject.executable.utils.Rome2Rio.TripAlternativeRome2Rio;
import eu.fbk.das.domainobject.executable.utils.ViaggiaTrento.TravelViaggiaTrento;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class ChooseAlternativeExecutable extends
		AbstractExecutableActivityInterface {

	private ProcessEngine pe;
	private TravelAssistantBot bot;

	public ChooseAlternativeExecutable(ProcessEngine processEngine,
			TravelAssistantBot bot) {
		this.pe = processEngine;
		this.bot = bot;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		Element choice = doi.getStateVariableContentByName("ChosenPlan");
		System.out.println(bot.getChoosenAlternative());

		choice.setTextContent(bot.getChoosenAlternative());
		// save result in response variable
		doi.setStateVariableContentByVarName("ChosenPlan", choice);

		if (bot.getViaggiaTrentoAlternatives() != null) {
			System.out.println(bot.getChoosenAlternative());

			TravelViaggiaTrento alternative = this.getViaggiaAlternative(
					bot.getViaggiaTrentoAlternatives(),
					bot.getChoosenAlternative());
			// Element goalHOAA = doi
			// .getStateVariableContentByName("HOAAPlanGoal");
			// String extractedString = new String();
			// TODO: To Be Finalized
			currentConcrete.setExecuted(true);

		} else if (bot.getChoosenAlternative() != null) {
			TripAlternativeRome2Rio alternative = this
					.getR2RAlternative(bot.getRomeToRioAlternatives(),
							bot.getChoosenAlternative());

			Element goalHOAA = doi
					.getStateVariableContentByName("HOAAPlanGoal");

			String extractedString = new String();
			if (alternative.getSegments() != null
					|| !alternative.getSegments().isEmpty()) {
				extractedString = generateOverallString(alternative
						.getSegments());
			}
			goalHOAA.setTextContent(extractedString);

			// save result in response variable
			// set the HOAA Goal starting from the choosen plan alternative
			doi.setStateVariableContentByVarName("HOAAPlanGoal", goalHOAA);

			StringTokenizer stk = new StringTokenizer(extractedString, "*");

			// set the journeySegments variable
			Element numberOfSegment = doi
					.getStateVariableContentByName("SegmentsNumber");
			numberOfSegment.setTextContent(Integer.toString(stk.countTokens()));

			currentConcrete.setExecuted(true);
		}
		return;
	}

	private TravelViaggiaTrento getViaggiaAlternative(
			ArrayList<TravelViaggiaTrento> viaggiaAlternatives,
			String choosenAlternative) {

		String[] parts = choosenAlternative.split(". ");
		String index = parts[0];
		int indexValue = Integer.parseInt(index);
		TravelViaggiaTrento result = viaggiaAlternatives.get(indexValue);
		return result;

	}

	private TripAlternativeRome2Rio getR2RAlternative(
			ArrayList<TripAlternativeRome2Rio> romeToRioAlternatives,
			String choosenAlternative) {
		TripAlternativeRome2Rio result = null;

		for (int i = 0; i < romeToRioAlternatives.size(); i++) {
			boolean found = false;
			TripAlternativeRome2Rio current = romeToRioAlternatives.get(i);
			String mean = current.getMean();
			StringTokenizer stk = new StringTokenizer(mean, ",");
			int tokenSize = stk.countTokens();
			for (int j = 0; j < tokenSize; j++) {
				String token = stk.nextToken();
				if (!choosenAlternative.contains(token)) {
					break;
				} else if (j == tokenSize - 1) {
					found = true;
					result = current;
					break;
				}
			}
		}
		return result;
	}

	private String generateOverallString(ArrayList<Segment> segments) {
		String result = "";
		if (segments.size() == 1) {
			String mean = segments.get(0).getMean();
			String agency = segments.get(0).getAgency();
			String from = segments.get(0).getFrom();
			String to = segments.get(0).getTo();
			result = mean + ";" + agency + ";" + from + ";" + to;

		} else {
			for (int i = 0; i < segments.size(); i++) {
				String mean = segments.get(i).getMean();
				String agency = segments.get(i).getAgency();
				String from = segments.get(i).getFrom();
				String to = segments.get(i).getTo();
				if (i == 0) {
					result = mean + ";" + agency + ";" + from + ";" + to + "*";
				} else if (i == segments.size() - 1) {
					result = result + mean + ";" + agency + ";" + from + ";"
							+ to;
				} else {
					result = result + mean + ";" + agency + ";" + from + ";"
							+ to + "*";
				}
			}
		}
		return result;
	}
}