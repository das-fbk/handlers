package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FdGoToNextPickupPointExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public FdGoToNextPickupPointExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance d = pe.getDomainObjectInstance(proc);
	DomainObjectInstance empl = pe.getCorrelated(d).stream()
		.filter(c -> c.getType().contains("Employee")).findFirst()
		.get();
	String ensemble = empl.getEnsemble();
	// find doi
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	StoryboardOneContext.getInstance().gotoNextPickupPoint(doi.getId(),
		ensemble);
	pa.setExecuted(true);
    }

}
