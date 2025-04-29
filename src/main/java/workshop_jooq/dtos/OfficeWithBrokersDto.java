package workshop_jooq.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Setter
@FieldNameConstants
public class OfficeWithBrokersDto extends OfficeDto {
    private List<BrokerDto> brokers;
}
