package rocks.voss.toniebox;

import org.apache.commons.lang3.StringUtils;

public class URLBuilder {

	public static String getTonieUrl(final String URLConstant) {
		return getTonieUrl(URLConstant, (String) null);
	}

	public static String getTonieUrl(final String URLConstant, Tonie tonie) {
		return getTonieUrl(URLConstant, tonie.getTonieId());
	}

	public static String getTonieUrl(final String URLConstant, final String tonieId) {
		if ( tonieId == null ) {
			return URLConstant;
		}
		return StringUtils.replace(URLConstant, "%s", tonieId);
	}
}
