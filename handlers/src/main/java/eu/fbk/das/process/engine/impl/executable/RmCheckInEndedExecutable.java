package eu.fbk.das.process.engine.impl.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;
import eu.fbk.das.process.engine.impl.context.util.ContextUtil;

public class RmCheckInEndedExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public RmCheckInEndedExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	String ensemble = pe.getDomainObjectInstance(proc).getEnsemble();
	// find doi
	// List<DomainObjectInstance> users =
	// pe.findAllDomainObjectByType("User");
	// List<String> userNames = users
	// .stream()
	// .filter(doi -> ContextUtil.isUsingFlexibus(pe, doi)
	// && doi.getEnsemble().equals(ensemble))
	// .map(doi -> doi.getId())
	// .collect(Collectors.toCollection(ArrayList::new));
	List<DomainObjectInstance> passengers = pe
		.findAllDomainObjectByType("User");
	List<String> userNames = passengers
		.stream()
		.filter(doi -> ContextUtil.isUsingFlexibus(pe, doi)
			&& doi.getEnsemble() != null
			&& doi.getEnsemble().equals(ensemble))
		.map(doi -> doi.getId())
		.collect(Collectors.toCollection(ArrayList::new));

	// delegate to context action
	if (StoryboardOneContext.getInstance().isCheckInEnded(pe, userNames)) {
	    // eseguo l'effetto
	    // <tns1:event dpName="HandleRoute"
	    // eventName="closeCheckIn"></tns1:event>
	    EffectType effect = new EffectType();
	    Event event = new Event();
	    event.setDpName("HandleRoute");
	    event.setEventName("closeCheckIn");
	    effect.getEvent().add(event);
	    pe.applyEffect(proc, effect);
	}
	pa.setExecuted(true);
    }

}
