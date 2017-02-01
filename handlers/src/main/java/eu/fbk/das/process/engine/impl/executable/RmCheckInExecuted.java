package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;
import eu.fbk.das.process.engine.impl.context.util.ContextUtil;

public class RmCheckInExecuted extends AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    private static Map<String, Integer> counter = new HashMap<String, Integer>();

    public RmCheckInExecuted(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance d = pe.getDomainObjectInstance(proc);
	String ensemble = d.getEnsemble();
	// find doi
	List<DomainObjectInstance> users = pe.findAllDomainObjectByType("User");
	List<String> userNames = users
		.stream()
		.filter(doi -> doi.getEnsemble() != null
			&& doi.getEnsemble().equals(ensemble)
			&& ContextUtil.isUsingFlexibus(pe, doi))
		.map(doi -> doi.getId())
		.collect(Collectors.toCollection(ArrayList::new));
	// update counter relative to route management
	if (!counter.containsKey(d.getId())) {
	    counter.put(d.getId(), 0);
	}
	Integer t = counter.get(d.getId());
	// delegate to context action
	StoryboardOneContext.getInstance().checkIn(userNames.get(t), ensemble);

	counter.put(d.getId(), t + 1);
	pa.setExecuted(true);
    }

}
