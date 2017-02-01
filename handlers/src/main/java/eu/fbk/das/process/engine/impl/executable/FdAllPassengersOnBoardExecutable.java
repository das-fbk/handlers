package eu.fbk.das.process.engine.impl.executable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.process.engine.api.AbstractExecutableActivityInterface;
import eu.fbk.das.process.engine.api.DomainObjectInstance;
import eu.fbk.das.process.engine.api.ProcessEngine;
import eu.fbk.das.process.engine.api.domain.ProcessActivity;
import eu.fbk.das.process.engine.api.domain.ProcessDiagram;
import eu.fbk.das.process.engine.api.jaxb.EffectType;
import eu.fbk.das.process.engine.api.jaxb.EffectType.Event;
import eu.fbk.das.process.engine.impl.context.StoryboardOneContext;

public class FdAllPassengersOnBoardExecutable extends
	AbstractExecutableActivityInterface {

    private static final Logger logger = LogManager
	    .getLogger(FdAllPassengersOnBoardExecutable.class);

    private ProcessEngine pe;

    public FdAllPassengersOnBoardExecutable(ProcessEngine pe) {
	this.pe = pe;
    }

    @Override
    public void execute(ProcessDiagram proc, ProcessActivity pa) {
	DomainObjectInstance doi = pe.getDomainObjectInstance(proc);
	if (StoryboardOneContext.getInstance().isAllPassengersOnBoard(pe,
		doi.getId())) {
	    logger.debug("All passengers here!");
	    pa.setExecuted(true);
	    // eseguo l'effetto
	    // <tns1:event dpName="FlexibusDriverStatus"
	    // eventName="nextPickupPoint"></tns1:event>
	    EffectType effect = new EffectType();
	    Event event = new Event();
	    event.setDpName("FlexibusDriverStatus");
	    event.setEventName("nextPickupPoint");
	    effect.getEvent().add(event);
	    pe.applyEffect(proc, effect);

	    // }
	}
    }

}
