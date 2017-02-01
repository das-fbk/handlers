package eu.fbk.das.process.engine.impl.executable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FcRouteStarted extends AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(FcRouteStarted.class);

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	if (StoryboardOneContext.getInstance().isFlexibusDriverRouteClosed()) {
	    logger.debug("FlexibusDriverRouteClosed true, concrete executed ");
	    pa.setExecuted(true);
	} else {
	    logger.debug("FlexibusDriverRouteClosed false, waiting");
	}

    }
}
