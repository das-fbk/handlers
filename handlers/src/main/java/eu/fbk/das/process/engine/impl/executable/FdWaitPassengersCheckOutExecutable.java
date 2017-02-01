package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;
import eu.fbk.das.process.engine.impl.context.util.ContextUtil;

public class FdWaitPassengersCheckOutExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public FdWaitPassengersCheckOutExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// find doi
	String driverName = pe.getDomainObjectInstance(proc).getId();
	List<DomainObjectInstance> users = pe
		.findAllDomainObjectByType("User");
	List<String> userNames = users.stream()
		.filter(doi -> ContextUtil.isUsingFlexibus(pe, doi))
		.map(doi -> doi.getId())
		.collect(Collectors.toCollection(ArrayList::new));
	// delegate to context action
	if (StoryboardOneContext.getInstance().isCheckOutEnded(pe, driverName,
		userNames)) {
	    // se tutti i passeggeri hanno fatto checkkout..
	    pa.setExecuted(true);
	}

    }
}
