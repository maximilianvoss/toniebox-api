package rocks.voss.toniebox.beans.amazon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FieldsBean implements Serializable {
	private String key;
	private String policy;
	@JsonProperty("x-amz-algorithm")
	private String xAmzAlgorithm;
	@JsonProperty("x-amz-credential")
	private String xAmzCredential;
	@JsonProperty("x-amz-date")
	private String xAmzDate;
	@JsonProperty("x-amz-signature")
	private String xAmzSignature;
}
