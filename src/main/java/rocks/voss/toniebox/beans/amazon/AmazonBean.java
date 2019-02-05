package rocks.voss.toniebox.beans.amazon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
public class AmazonBean implements Serializable {

	private UrlBean url;
	private String uuid;

	public static AmazonBean createBean(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
		return mapper.treeToValue(jsonNode, AmazonBean.class);
	}
}
