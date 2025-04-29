package workshop_jooq.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Getter
@Setter
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private Integer number;
    private GisDto coordinates;

    @JsonIgnore
    private UUID id;
}
