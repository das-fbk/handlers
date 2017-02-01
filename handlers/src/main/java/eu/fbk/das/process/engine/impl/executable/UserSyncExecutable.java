package eu.fbk.das.process.engine.impl.executable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;

public class UserSyncExecutable extends AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    private static Map<String, Boolean> syncMap = new HashMap<String, Boolean>();

    public UserSyncExecutable(ProcessEngine processEngine) {
	this.pe = processEngine;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	// get number of users
	List<DomainObjectInstance> users = pe.findAllDomainObjectByType("User");
	// wait all user complete this get this activity
	if (syncMap.size() == users.size()) {
	    pa.setExecuted(true);
	    return;
	}
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	if (!syncMap.containsKey(doi.getId())) {
	    syncMap.put(doi.getId(), true);
	}
    }
}
