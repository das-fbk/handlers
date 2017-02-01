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
 * Executable for flexibus concrete activity to wait for a passenger at pickup
 * point
 */
public class DriverWaitPersonExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(DriverWaitPersonExecutable.class);

    private ProcessEngine pe;

    public DriverWaitPersonExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// Assumiamo che esista una sola route e un solo passeggero, quindi
	// il driver quando esegue questo executable si trova gia' al pickupoint
	// giusto per se e aspetta l'unico passeggero che mai passera' di li

	List<DomainObjectInstance> dois = pe
		.findAllDomainObjectByType("RoutePassenger");
	int c = 0;
	for (DomainObjectInstance passengerInstance : dois) {
	    List<ObjectDiagram> internal = passengerInstance
		    .getInternalKnowledge();
	    if (ExecutableUtil.isInState(internal, "RoutePassengerStatus",
		    "PICKUP_POINT_REACHED")) {
		c++;
	    } else {
		logger.debug("Waiting for passengers");
	    }
	}
	if (c >= dois.size()) {
	    logger.debug("All passengers here!");
	    pa.setExecuted(true);
	}

    }

}
