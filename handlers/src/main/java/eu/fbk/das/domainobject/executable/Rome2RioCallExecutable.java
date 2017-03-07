package eu.fbk.das.domainobject.executable;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.domainobject.executable.utils.Rome2RioAPIWrapper;
import eu.fbk.das.domainobject.executable.utils.TripAlternative;
import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.VariableType;

public class Rome2RioCallExecutable extends AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(Rome2RioCallExecutable.class);

	private ProcessEngine pe;

	private static int hoaaCounter = 1;

	public Rome2RioCallExecutable(ProcessEngine processEngine) {
		this.pe = processEngine;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
		ProcessDiagram process = doi.getProcess();

		// get the domain object state
		List<VariableType> doiState = doi.getState().getStateVariable();
		if (doiState != null) {

			logger.error("Domain Object with a state! ");
			// concrete logic
			// extract from and to variables
			Element from = (Element) doi.getState().getStateVariable().get(0)
					.getContent();
			Element to = (Element) doi.getState().getStateVariable().get(1)
					.getContent();

			String fromValue = from.getFirstChild().getNodeValue();
			String toValue = to.getFirstChild().getNodeValue();

			// call Rome2Rio Service
			String result = this.CallRome2Rio(fromValue, toValue);
			Element response = (Element) doi.getState().getStateVariable()
					.get(2).getContent();
			response.setTextContent(result);

			// save result in response variable
			doiState.get(2).setContent(response);

			Element response1 = (Element) doi.getState().getStateVariable()
					.get(2).getContent();
			String value = response1.getFirstChild().getNodeValue();
			logger.debug("Domain Object state! " + value);

			// set activity to executed
			currentConcrete.setExecuted(true);
			return;
		}
		logger.debug("Domain Object without a state! ");
		currentConcrete.setExecuted(true);
		return;
	}

	String CallRome2Rio(String from, String to) {

		String result = null;
		Rome2RioAPIWrapper rome2RioWrapper = new Rome2RioAPIWrapper();

		boolean nontrovate = true;

		ArrayList<TripAlternative> alternatives = new ArrayList<TripAlternative>();

		alternatives = rome2RioWrapper.getRome2RioAlternatives(from, to);

		if (alternatives.size() != 0) {
			result = alternatives.get(0).getMean();
		}
		return result;
	}
}
