package rocks.voss.toniebox.beans.toniebox;

import lombok.Data;

@Data
public class Household {
    private String id;
    private String name;
    private String image;
    private boolean foreignCreativeTonieContent;
    private String access;
    private boolean canLeave;
    private String ownerName;
}
