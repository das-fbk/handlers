package eu.fbk.das.process.engine.impl.executable;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

/**
 * Executable for passenger concrete activity to wait flexibus at pickup point
 */
public class RpWaitFlexibusExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(RpWaitFlexibusExecutable.class);

    private ProcessEngine pe;

    public RpWaitFlexibusExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// TODO Assumiamo che esista una sola route e un solo flexibus, quindi
	// l'utente quando esegue questo executable si trova gia' al pickupoint
	// giusto per se e aspetta l'unico flexibus che mai passera' di li

	DomainObjectInstance driverDoi = pe
		.findDomainObjectByType("FlexibusDriver");
	List<ObjectDiagram> internal = driverDoi.getInternalKnowledge();
	if (ExecutableUtil.isInState(internal, "FlexibusDriverStatus",
		"AT_PICKUP_POINT")) {
	    logger.debug("Passenger no longer waiting on pickupoint - flexibus is here");
	    pa.setExecuted(true);
	} else {
	    logger.debug("Passenger waiting at pickupoint");
	}
    }

}
