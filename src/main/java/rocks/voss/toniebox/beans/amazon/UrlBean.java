package rocks.voss.toniebox.beans.amazon;

import lombok.Data;

import java.io.Serializable;

@Data
public class UrlBean implements Serializable {
	private String url;
	private FieldsBean fields;
}

