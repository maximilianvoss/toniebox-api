package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.fxml.builder.URLBuilder;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.Serializable;

@Data
public class TonieUpdateBean implements Serializable {
	@JsonIgnore
	private Logger log = Logger.getLogger(getClass().getName());

	private TonieContentDataBean content;
	private TonieChapterBean currentChapters[];
	private TonieChapterBean deletedChapters[];

	@JsonIgnore
	public JsonNode getJson() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode returnJson = mapper.valueToTree(this);
		log.debug("Json: " + returnJson.toString());
		return returnJson;
	}
}
