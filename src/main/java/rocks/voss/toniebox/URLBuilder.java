package rocks.voss.toniebox;

import org.apache.commons.lang3.StringUtils;
import rocks.voss.toniebox.beans.Tonie;

class URLBuilder {
	protected static String getTonieUrl(final String URLConstant) {
		return getTonieUrl(URLConstant, (String) null);
	}

	protected static String getTonieUrl(final String URLConstant, Tonie tonie) {
		return getTonieUrl(URLConstant, tonie.getTonieId());
	}

	protected static String getTonieUrl(final String URLConstant, final String tonieId) {
		if ( tonieId == null ) {
			return URLConstant;
		}
		return StringUtils.replace(URLConstant, "%s", tonieId);
	}
}
