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
 * Executable for driver concrete activity to wait passengers to checkout
 */
public class DriverWaitCheckOutExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(DriverWaitCheckOutExecutable.class);

    private ProcessEngine pe;

    public DriverWaitCheckOutExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// Assumiamo che esista una sola route e un solo flexibus, quindi
	// il driver quando esegue questo executable si trova gia' a
	// destinazione
	// e aspetta che i passeggeri facciano checkout

	List<DomainObjectInstance> dois = pe
		.findAllDomainObjectByType("RoutePassenger");
	int c = 0;
	for (DomainObjectInstance passengerInstance : dois) {
	    List<ObjectDiagram> internal = passengerInstance
		    .getInternalKnowledge();
	    if (ExecutableUtil.isInState(internal, "RoutePassengerStatus",
		    "DESTINATION_REACHED")) {
		c++;
	    } else {
		logger.debug("Waiting for passengers checkout");
	    }
	}
	if (c >= dois.size()) {
	    logger.debug("No more passengers to checkout");
	    pa.setExecuted(true);
	}

    }

}
