package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
public class TonieUpdateBean implements Serializable {
	private TonieContentDataBean content;
	private TonieChapterBean currentChapters[];
	private TonieChapterBean deletedChapters[];

	public static TonieUpdateBean createBean(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
		return mapper.treeToValue(jsonNode, TonieUpdateBean.class);
	}

	@JsonIgnore
	public JsonNode getJson() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.valueToTree(this);
	}
}
