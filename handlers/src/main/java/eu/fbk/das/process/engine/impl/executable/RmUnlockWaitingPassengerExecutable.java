package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class RmUnlockWaitingPassengerExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RmUnlockWaitingPassengerExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	String ensemble = doi.getEnsemble();
	StoryboardOneContext.getInstance().setRouteCreationWaiting(ensemble,
		StoryboardOneContext.ROUTE_CREATION_CREATED);
	pa.setExecuted(true);

    }

}
