package rocks.voss.toniebox.beans.toniebox;

import lombok.Data;

import java.io.Serializable;

@Data
public class TonieChapterBean implements Serializable {
	private String identifier;
	private String file;
	private String title;
	private int position;
	private float length;
	private float progress;
	private boolean locked;
	private String content;
}
