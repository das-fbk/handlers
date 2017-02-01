package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.util.ContextUtil;

public class PassengerEnterInRouteEnsembleExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public PassengerEnterInRouteEnsembleExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// When passenger enter in ensemble, we add also information about
	// pickup point
	// find for given role, the name of the user behind it
	DomainObjectInstance doi = ContextUtil.findDomainObjectForRole(pe,
		proc, "User").get();
	int ppnumber = 0;
	// TODO: logica per forzare l'assegnazione del pickuppoint in base al
	// tuo nome
	if (doi.getId().equals("User_1")) {
	    ppnumber = 0;
	} else {
	    ppnumber = 1;
	}
	pe.addProcVar(proc, "PickupPoint", "" + ppnumber);
	pa.setExecuted(true);
    }

}
