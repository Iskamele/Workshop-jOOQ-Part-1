package workshop_jooq.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.UUID;

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

    @JsonIgnore
    private UUID id;
    @JsonIgnore
    private UUID brokerId;
    @JsonIgnore
    private UUID addressId;
}
