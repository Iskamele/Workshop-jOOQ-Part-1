package workshop_jooq.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@FieldNameConstants
public class BrokerDto {
    private String firstName;
    private String lastName;
    private List<String> degreeBefore;
    private Boolean isPaidUser;
    private List<EmailDto> emails;
    private List<PhoneNumberDto> phoneNumbers;

    @JsonIgnore
    private UUID id;
    @JsonIgnore
    private UUID officeId;
}
