package eu.fbk.das.process.engine.impl.executable;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FdAllPickupPointReachedExecutable extends
	AbstractExecutableActivityInterface {

    private ProcessEngine pe;

    public FdAllPickupPointReachedExecutable(ProcessEngine pe) {
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
	// delegate to context action
	if (StoryboardOneContext.getInstance().isAllPickupPointReached(pe,
		doi.getId(), ensemble)) {
	    // eseguo l'effetto
	    // <tns1:event dpName="FlexibusDriverStatus"
	    // eventName="allPickupPointReached"></tns1:event> --> EffectType
	    EffectType effect = new EffectType();
	    Event event = new Event();
	    event.setDpName("FlexibusDriverStatus");
	    event.setEventName("allPickupPointReached");
	    effect.getEvent().add(event);
	    pe.applyEffect(proc, effect);
	}
	pa.setExecuted(true);

    }
}
