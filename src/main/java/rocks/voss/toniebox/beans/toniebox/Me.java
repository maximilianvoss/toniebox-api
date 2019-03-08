package rocks.voss.toniebox.beans.toniebox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Me {
    private String email;
    private String uuid;
    private String firstName;
    private String lastName;
    private String sex;
    private boolean acceptedTermsOfUse;
    private boolean tracking;
    private String authCode;
    private String profileImage;
    @JsonProperty("isVerified")
    private boolean verified;
    private String locale;
    private int notificationCount;
}
