package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.Segment;
import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.domainobject.executable.utils.BotTelegram.TravelAssistantBot;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class ChooseAlternativeExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(InsertDestinationExecutable.class);

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
		ProcessDiagram process = doi.getProcess();

		Element choice = doi.getStateVariableContentByName("ChosenPlan");
		System.out.println(bot.getChoosenAlternative());
		choice.setTextContent(bot.getChoosenAlternative());
		// save result in response variable
		doi.setStateVariableContentByVarName("ChosenPlan", choice);

		// Split the alternative String to retrieve the index selected
		String[] parts = bot.getChoosenAlternative().split(",");
		String index = parts[0];
		int indexValue = Integer.parseInt(index);

		// retrieve the right alternative from the global variable
		System.out.println("ALTERNATIVE: " + bot.getAlternatives().size());
		TripAlternative alternative = bot.getAlternatives().get(indexValue);

		Element goalHOAA = doi.getStateVariableContentByName("HOAAPlanGoal");
		String extractedString = generateOverallString(alternative
				.getSegments());
		// String extractedString = "train-bus";
		goalHOAA.setTextContent(extractedString);

		// save result in response variable
		// set the HOAA Goal starting from the choosen plan alternative
		doi.setStateVariableContentByVarName("HOAAPlanGoal", goalHOAA);

		currentConcrete.setExecuted(true);
		// pe.stepAll();

		return;
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