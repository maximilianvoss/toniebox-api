package rocks.voss.toniebox;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import rocks.voss.toniebox.beans.Tonie;

class URLBuilder {
	private static Logger log = Logger.getLogger(URLBuilder.class.getName());

	protected static String getTonieUrl(final String URLConstant) {
		return getTonieUrl(URLConstant, (String) null);
	}

	protected static String getTonieUrl(final String URLConstant, Tonie tonie) {
		return getTonieUrl(URLConstant, tonie.getTonieId());
	}

	protected static String getTonieUrl(final String URLConstant, final String tonieId) {
		if ( tonieId == null ) {
			log.debug("URL: " + URLConstant);
			return URLConstant;
		}
		String returnUrl = StringUtils.replace(URLConstant, "%s", tonieId);
		log.debug("URL: " + returnUrl);
		return returnUrl;
	}
}
