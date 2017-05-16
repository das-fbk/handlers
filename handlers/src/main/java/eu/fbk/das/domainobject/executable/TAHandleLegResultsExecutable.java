package eu.fbk.das.domainobject.executable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ConcreteActivity;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class TAHandleLegResultsExecutable extends
		AbstractExecutableActivityInterface {

	private static final Logger logger = LogManager
			.getLogger(TAHandleLegResultsExecutable.class);

	private ProcessEngine pe;
	private static String SCOPE_PREFIX = new String();

	// private static String HOSTING_INSTANCE_NAME = new String();

	public TAHandleLegResultsExecutable(ProcessEngine processEngine) {
		this.pe = processEngine;
	}

	@Override
	public void execute(ProcessDiagram proc, ProcessActivity pa) {
		ConcreteActivity currentConcrete = (ConcreteActivity) proc
				.getCurrentActivity();

		DomainObjectInstance doi = pe.getDomainObjectInstance(proc);

		if (pe.checkVarCondition("isInScope", "true", doi.getProcess())) {
			SCOPE_PREFIX = (pe.getVariablesFor(doi.getProcess(), "scopePrefix"))
					.getValue();
			// HOSTING_INSTANCE_NAME = (pe.getVariablesFor(doi.getProcess(),
			// "doInstanceName")).getValue();
			Element elemResultList = doi
					.getStateVariableContentByName(SCOPE_PREFIX + "."
							+ "ResultList");
			Element el = doi.getStateVariableContentByName("PlannerOutput");
			el.setTextContent(elemResultList.getFirstChild().getNodeValue());

			currentConcrete.setExecuted(true);
			return;
		}
	}
}