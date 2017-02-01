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

public class FdTakePassengerExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(FdTakePassengerExecutable.class);

    private ProcessEngine pe;

    public FdTakePassengerExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// <tns:effect> -->
	// <tns1:event dpName="FlexibusDriverStatus"
	// eventName="takePassenger"></tns1:event> -->
	// <tns1:event dpName="FlexibusDriverStatus"
	// eventName="backToPassengerBoarding"></tns1:event> -->
	// </tns:effect> -->

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
		logger.debug("Waiting for passengers at pickup point");
	    }
	}
	// if (c >= dois.size()) {
	if (c >= 2) {
	    logger.debug("All passengers to pickup point");
	    pa.setExecuted(true);

	}
    }

}
