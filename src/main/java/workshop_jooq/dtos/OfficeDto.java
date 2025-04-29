package workshop_jooq.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@FieldNameConstants
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficeDto {
    private String officeName; // name
    private AddressDto address;
    private Boolean isShowOnExport; // isHidden
    private LocalDate dateOpening;
    private String[] tags;
    private List<EmailDto> emails;
    private List<PhoneNumberDto> phoneNumbers;

    private String cookedAddress;

    @JsonIgnore
    private UUID id;
}
