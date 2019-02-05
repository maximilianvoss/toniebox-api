package rocks.voss.toniebox.beans.toniebox;

import lombok.Data;

import java.io.Serializable;

@Data
public class TonieContentDataBean implements Serializable {
	private String file;
	private float length;
	private float remaining;
	private boolean inProgress;
	private TonieChapterBean chapters[];
}
