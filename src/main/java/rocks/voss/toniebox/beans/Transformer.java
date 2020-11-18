package rocks.voss.toniebox.beans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class Transformer {
    private static final Logger log = Logger.getLogger(Transformer.class.getName());

    public static <T> T createBean(Class<T> clazz, InputStream stream) throws IOException {
        String json = IOUtils.toString(stream, "UTF-8");
        return createBean(clazz, json);
    }

    private static <T> T createBean(Class<T> clazz, String json) throws IOException {
        log.debug("Json: " + json);
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode jsonNode = mapper.readValue(json, JsonNode.class);
        return mapper.treeToValue(jsonNode, clazz);
    }

    private static JsonNode getJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode returnJson = mapper.valueToTree(object);
        log.debug("Json: " + returnJson.toString());
        return returnJson;
    }

    public static String getJsonString(Object object) {
        return getJson(object).toString();
    }
}
