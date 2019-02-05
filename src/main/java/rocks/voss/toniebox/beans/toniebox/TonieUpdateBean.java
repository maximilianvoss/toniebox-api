package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class TonieUpdateBean implements Serializable {
	private TonieContentDataBean content;
	private TonieChapterBean currentChapters[];
	private TonieChapterBean deletedChapters[];

	@JsonIgnore
	public JsonNode getJson() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.valueToTree(this);
	}
}
