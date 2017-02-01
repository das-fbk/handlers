package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class RpPassengerCheckedInExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RpPassengerCheckedInExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	String userName = pe.getDomainObjectInstance(proc).getId();
	if (StoryboardOneContext.getInstance().isPassengerCheckedIn(userName)) {
	    pa.setExecuted(true);
	}

    }
}
