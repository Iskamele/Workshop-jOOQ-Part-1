package workshop_jooq.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
public class GisDto {
    public Double latitude;
    private Double longitude;
}
