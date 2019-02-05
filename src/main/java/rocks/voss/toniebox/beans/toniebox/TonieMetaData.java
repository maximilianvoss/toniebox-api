package rocks.voss.toniebox.beans.toniebox;

import lombok.Data;

import java.io.Serializable;

@Data
public class TonieMetaData implements Serializable {
	private int maxChapters;
	private int maxContentLength;
	private float remainingContentLength;
	private boolean tonieIsLive;
	private boolean tosAccepted;
}
