package eu.fbk.das.domainobject.executable.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.AbstractActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point;
import eu.fbk.das.process.engine.api.jaxb.ClauseType.Point.DomainProperty;
import eu.fbk.das.process.engine.api.jaxb.GoalType;

//this executable generates a HOAA for each "R2R" segment of the user chosen alternative, 
//followed by a concrete activity used for the "backToPlanned" event
public class TestJourneyExecutable extends AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(TestJourneyExecutable.class);

	private ProcessEngine pe;

	private static int hoaaCounter = 1;
	private static String hoaaName = null;

	public TestJourneyExecutable(ProcessEngine processEngine) {
		this.pe = processEngine;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		AbstractActivity currentAbstract = (AbstractActivity) proc
				.getCurrentActivity();
		// hoaaName = currentAbstract.getName() + "-";
		hoaaName = "JourneyLeg-";

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		Element segmentsNumber = doi
				.getStateVariableContentByName("SegmentsNumber");
		String segments = null;
		if (segmentsNumber != null) {
			if (segmentsNumber.getFirstChild() != null) {
				segments = segmentsNumber.getFirstChild().getNodeValue();
			}
		}

		List<ProcessActivity> result = new ArrayList<ProcessActivity>();
		int start = currentAbstract.getTarget();
		Optional<AbstractActivity> act = buildAbstractActivityfor(doi,
				segments, start, start + 1);
		if (act.isPresent()) {
			result.add(act.get());
			start++;
		}

		// extend current processes with hoaas
		int target = currentAbstract.getTarget();
		for (ProcessActivity activity : result) {
			proc.getActivities().add(target, activity);
			target++;
		}
		// re-arrange source e target for all activities
		int i = 0;
		for (ProcessActivity actv : proc.getActivities()) {
			actv.setSource(i);
			actv.setTarget(i + 1);
			i++;
		}
	}

	private Optional<AbstractActivity> buildAbstractActivityfor(
			DomainObjectInstance doi, String token, int source, int target) {
		return Optional.of(buildLeg(doi, source, target, token));
	}

	private AbstractActivity buildLeg(DomainObjectInstance doi, int source,
			int target, String token) {
		GoalType goal = new GoalType();
		Point point = new Point();

		DomainProperty dpTest = new DomainProperty();
		dpTest.setDpName("TestJourney");
		dpTest.getState().add("TEST6");

		DomainProperty dpBus = new DomainProperty();
		dpBus.setDpName("BusJourney");
		dpBus.getState().add("BUS_RESPONSE_SENT");

		DomainProperty dpTrain = new DomainProperty();
		dpTrain.setDpName("TrainJourney");
		dpTrain.getState().add("TRAIN7");

		DomainProperty dpBike = new DomainProperty();
		dpBike.setDpName("BikeJourney");
		dpBike.getState().add("BIKE11");

		DomainProperty dpPark = new DomainProperty();
		dpPark.setDpName("ParkingManager");
		dpPark.getState().add("PARK_BOOKED");

		DomainProperty dpCable = new DomainProperty();
		dpCable.setDpName("CableJourney");
		dpCable.getState().add("CABLE6");

		DomainProperty dpGoogle = new DomainProperty();
		dpGoogle.setDpName("LocalPlanner");
		dpGoogle.getState().add("LOCAL_ALTERNATIVES_SENT");

		ArrayList<DomainProperty> dpArray = new ArrayList<DomainProperty>();
		dpArray.add(dpBike);
		dpArray.add(dpTrain);
		dpArray.add(dpBus);
		dpArray.add(dpTest);
		dpArray.add(dpPark);
		dpArray.add(dpCable);
		dpArray.add(dpGoogle);

		if (Integer.parseInt(token) > 4) {
			List<String> random = new ArrayList<String>(Arrays.asList("1", "2",
					"3", "4"));
			token = random.get(new Random().nextInt(random.size()));
		}

		switch (token) {
		case "1":
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			break;

		case "2":
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			break;

		case "3":
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			break;

		case "4":
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			break;
		// case "5":
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// break;
		//
		// case "6":
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// point.getDomainProperty().add(
		// dpArray.get(new Random().nextInt(dpArray.size())));
		// break;

		default:
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			point.getDomainProperty().add(
					dpArray.get(new Random().nextInt(dpArray.size())));
			break;

		}

		goal.getPoint().add(point);
		AbstractActivity act = new AbstractActivity(source, target, hoaaName
				+ hoaaCounter, goal);
		act.setAbstract(true);
		act.setAbstractType("GeneratedAbstract");
		hoaaCounter++;

		return act;
	}

}
