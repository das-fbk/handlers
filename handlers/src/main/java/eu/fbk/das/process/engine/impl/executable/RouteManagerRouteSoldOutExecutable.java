package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.ProcVar;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class RouteManagerRouteSoldOutExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RouteManagerRouteSoldOutExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	ProcVar passengerNumber = pe.getVariablesFor(proc, "PassengersNumber");
	pa.setExecuted(true);
    }

}
