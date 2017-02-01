package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

/**
 * Main variables for RouteManager during route ensemble creation
 */
public class RouteManagerCreateRouteEnsembleExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RouteManagerCreateRouteEnsembleExecutable(ProcessEngine processEngine) {
	this.pe = processEngine;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// number of pickpoints dependens on route stops for flexibus

	pe.addProcVar(proc, "PickupPointNumber", "1");
	pe.addProcVar(proc, "PassengersNumber", "2");

	pa.setExecuted(true);
    }
}
