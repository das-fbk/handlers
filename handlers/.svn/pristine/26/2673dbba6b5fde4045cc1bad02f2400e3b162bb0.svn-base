package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ObjectDiagram;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class RpPassengerChekedOutExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RpPassengerChekedOutExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance rmdoi = findRouteManagement(pe
		.getDomainObjectInstance(proc));
	// to ask for checkout, wait all passengers checkin first!
	int c = 0;
	List<DomainObjectInstance> passengers = pe
		.findAllDomainObjectByType("RoutePassenger");
	passengers = filterByRouteManagemet(passengers, rmdoi);

	for (DomainObjectInstance passenger : passengers) {
	    List<ObjectDiagram> internal = passenger.getInternalKnowledge();
	    if (ExecutableUtil.isInState(internal, "RoutePassengerStatus",
		    "PASSENGER_CHECKED_IN")
		    || ExecutableUtil.isInState(internal,
			    "RoutePassengerStatus", "DESTINATION_REACHED")) {
		c++;
	    }
	}
	if (c >= passengers.size()) {
	    // Passenger do checkout
	    DomainObjectInstance d = pe.getDomainObjectInstance(proc);
	    StoryboardOneContext.getInstance().passengerCheckedOut(d.getId(),
		    d.getEnsemble());
	    pa.setExecuted(true);

	}

    }

    private List<DomainObjectInstance> filterByRouteManagemet(
	    List<DomainObjectInstance> passengers, DomainObjectInstance rmdoi) {
	List<DomainObjectInstance> result = new ArrayList<DomainObjectInstance>();
	for (DomainObjectInstance doi : passengers) {
	    if (pe.getCorrelated(doi).stream()
		    .filter(d -> d.getId().equals(rmdoi.getId())).count() == 1) {
		result.add(doi);

	    }
	}
	return result;
    }

    private DomainObjectInstance findRouteManagement(DomainObjectInstance doi) {
	DomainObjectInstance doiPass = pe.getCorrelated(doi).stream()
		.filter(d -> d.getId().startsWith("RoutePassenger"))
		.findFirst().get();
	DomainObjectInstance doiRm = pe.getCorrelated(doiPass).stream()
		.filter(d -> d.getId().startsWith("RouteManagement"))
		.findFirst().get();
	return doiRm;
    }

}
