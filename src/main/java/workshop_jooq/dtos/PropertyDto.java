package workshop_jooq.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for property information.
 * <p>
 * It uses Jackson annotations for JSON serialization control and Lombok
 * for reducing boilerplate code. The {@code @FieldNameConstants} annotation
 * generates a nested class with field name constants for safer field references.
 * <p>
 * The {@code @JsonInclude} annotation ensures null fields are excluded from
 * JSON serialization, which helps keep API responses concise.
 */
@Getter
@Setter
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDto {
    private List<String> images;
    private Integer price;
    private Boolean isPublicPrice;
    private BrokerDto broker;
    private AddressDto address;

    /**
     * Property ID - excluded from JSON serialization as it's used internally
     * This demonstrates using @JsonIgnore for fields that should not be exposed in the API
     */
    @JsonIgnore
    private UUID id;
    @JsonIgnore
    private UUID brokerId;
    @JsonIgnore
    private UUID addressId;
}
