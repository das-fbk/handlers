package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FcWaitRouteCreatedExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public FcWaitRouteCreatedExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	DomainObjectInstance empl = pe.getCorrelated(doi).stream()
		.filter(d -> d.getType().equals("User")).findFirst().get();
	String ensemble = empl.getEnsemble();
	if (StoryboardOneContext.getInstance().isRouteCreationWaiting(ensemble) == StoryboardOneContext.ROUTE_CREATION_BEFORE) {
	    StoryboardOneContext.getInstance().setRouteCreationWaiting(
		    ensemble, StoryboardOneContext.ROUTE_CREATION_WAIT);
	    pa.setExecuted(true);
	} else if (StoryboardOneContext.getInstance().isRouteCreationWaiting(
		ensemble) == StoryboardOneContext.ROUTE_CREATION_WAIT) {
	    // just wait
	} else if (StoryboardOneContext.getInstance().isRouteCreationWaiting(
		ensemble) == StoryboardOneContext.ROUTE_CREATION_CREATED) {
	    pa.setExecuted(true);
	}
    }

}
