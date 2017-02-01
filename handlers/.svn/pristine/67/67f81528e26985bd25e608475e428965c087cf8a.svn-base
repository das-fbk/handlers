package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FdRouteClosedNotice extends AbstractExecutableActivityInterface {

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	StoryboardOneContext.getInstance().flexibusDriverRouteClosed();
	pa.setExecuted(true);
    }

}
