package rocks.voss.toniebox.beans.amazon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import rocks.voss.toniebox.beans.toniebox.TonieMetaData;
import rocks.voss.toniebox.beans.toniebox.TonieContentDataBean;

import java.io.IOException;
import java.io.Serializable;


@Data
public class TonieContentBean implements Serializable {

	private TonieMetaData _meta;
	private TonieContentDataBean data;

	public static TonieContentBean createBean(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
		return mapper.treeToValue(jsonNode, TonieContentBean.class);
	}
}
