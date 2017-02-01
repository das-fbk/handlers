package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class RmAssignDriverExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RmAssignDriverExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance d = pe.getDomainObjectInstance(proc);
	// find doi
	DomainObjectInstance doi = pe.findDomainObjectByType("FlexibusDriver");

	// delegate to context action
	StoryboardOneContext.getInstance().assignDriver(pe, doi.getId(),
		d.getEnsemble());

	pa.setExecuted(true);
    }

}
