package eu.fbk.das.process.engine.impl.context.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesUtil {

    private static final Logger logger = LogManager
	    .getLogger(PropertiesUtil.class);

    public static Properties getEnsembleProperties() {
	Properties p = new Properties();
	try {
	    p.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(
		    "ensembles.properties"));
	    return p;
	} catch (FileNotFoundException e) {
	    logger.error("Error loading file ", e);
	    return null;
	} catch (IOException e) {
	    logger.error("Error loading file ", e);
	    return null;
	}
    }

}
