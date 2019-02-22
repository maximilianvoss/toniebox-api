package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;


@Data
public class TonieContentBean implements Serializable {
	@JsonIgnore
	private static Logger log = Logger.getLogger(TonieChapterBean.class.getName());

	private TonieMetaData _meta;
	private TonieContentDataBean data;

	public static TonieContentBean createBean(String json) throws IOException {
		log.debug("Json: " + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
		return mapper.treeToValue(jsonNode, TonieContentBean.class);
	}
}
