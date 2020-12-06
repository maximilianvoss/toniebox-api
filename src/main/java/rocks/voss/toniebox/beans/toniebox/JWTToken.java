package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JWTToken {
    @JsonProperty("access_token")
    private String accessToken;
}
