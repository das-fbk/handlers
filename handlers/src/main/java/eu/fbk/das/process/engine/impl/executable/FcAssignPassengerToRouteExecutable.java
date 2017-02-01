package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;
import eu.fbk.das.process.engine.impl.context.util.ContextUtil;

public class FcAssignPassengerToRouteExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public FcAssignPassengerToRouteExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// find doi
	DomainObjectInstance doi = ContextUtil.findDomainObjectForRole(pe,
		proc, "User").get();
	// delegate to context action
	StoryboardOneContext.getInstance().assignPassengerToRoute(pe,
		doi.getId(), doi.getEnsemble());
	pa.setExecuted(true);
    }

}
