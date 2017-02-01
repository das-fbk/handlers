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

public class DriverWaitCheckInExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(DriverWaitCheckInExecutable.class);

    private ProcessEngine pe;

    public DriverWaitCheckInExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// Assumiamo che esista una sola route e un solo passeggero, quindi
	// il driver quando esegue questo executable si trova gia' al pickupoint
	// giusto per se e che l'unico passeggero faccia checkin sul flexibus

	List<DomainObjectInstance> dois = pe
		.findAllDomainObjectByType("RoutePassenger");
	int c = 0;
	for (DomainObjectInstance passengerInstance : dois) {
	    List<ObjectDiagram> internal = passengerInstance
		    .getInternalKnowledge();
	    if (ExecutableUtil.isInState(internal, "RoutePassengerStatus",
		    "PASSENGER_CHECKED_IN")) {
		c++;
	    } else {
		logger.debug("Waiting for passengers check-in");
	    }
	}
	if (c >= dois.size()) {
	    logger.debug("All passengers check-in on flexibus!");
	    pa.setExecuted(true);
	}

    }

}
