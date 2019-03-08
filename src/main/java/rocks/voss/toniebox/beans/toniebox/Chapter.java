package rocks.voss.toniebox.beans.toniebox;

import lombok.Data;

@Data
public class Chapter {
	private String id;
	private String file;
	private String title;
	private float seconds;
	private boolean transcoding;
}
