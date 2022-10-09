package business.externalinterfaces;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


public class RulesConfigProperties {
	private static final String PROPERTIES = "src\\main\\resources\\rulesconfig.properties";
	private static final Logger LOG = Logger.getLogger("");
	
	private static final String PROPS = 
		System.getProperty("user.dir") + "/" + PROPERTIES;
	private static Properties props;
	
	static {
		readProps();
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
		
	}
	private static void readProps() {
		readProps(PROPS);
		
	}
	
	/**
	 * This method allows a client of this properties configurator
	 * to point to a different location for the properties file.
	 * @param propsLoc
	 */
	public static void readProps(String loc) {
		System.out.println(loc);
		Properties ret = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(loc);
			if (is != null) ret.load(is);
		}
		catch (IOException e) {
			LOG.warning("Unable to read properties file for Ebazaar");
		}
		finally {
			props = ret;
		}
	}
	
	
	
	
}

