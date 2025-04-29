package workshop_jooq.repositories;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import workshop_jooq.dtos.BrokerDto;
import workshop_jooq.dtos.EmailDto;
import workshop_jooq.dtos.PhoneNumberDto;

import java.util.Objects;
import java.util.UUID;

import static jooq.generated.tables.Broker.BROKER;
import static jooq.generated.tables.BrokerDegree.BROKER_DEGREE;
import static jooq.generated.tables.Email.EMAIL;
import static jooq.generated.tables.PhoneNumber.PHONE_NUMBER;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ImportRepository {
    private final DSLContext dsl;

    @Transactional
    public BrokerDto createBroker(BrokerDto brokerDto) {
        UUID brokerId = Objects.requireNonNull(dsl.insertInto(BROKER)
                        .set(BROKER.FIRST_NAME, brokerDto.getFirstName())
                        .set(BROKER.LAST_NAME, brokerDto.getLastName())
                        .set(BROKER.OFFICE_ID, brokerDto.getOfficeId())
                        .set(BROKER.IS_MLS, brokerDto.getIsPaidUser())
                        .returning(BROKER.ID)
                        .fetchOne())
                .getId();

        // degree
        if (brokerDto.getDegreeBefore() != null && !brokerDto.getDegreeBefore().isEmpty()) {
            for (String degree : brokerDto.getDegreeBefore()) {
                dsl.insertInto(BROKER_DEGREE)
                        .set(BROKER_DEGREE.BROKER_ID, brokerId)
                        .set(BROKER_DEGREE.DEGREE_NAME, degree)
                        .execute();
            }
        }

        // email
        if (brokerDto.getEmails() != null && !brokerDto.getEmails().isEmpty()) {
            for (EmailDto emailDto : brokerDto.getEmails()) {
                dsl.insertInto(EMAIL)
                        .set(EMAIL.BROKER_ID, brokerId)
                        .set(EMAIL.EMAIL_, emailDto.email())
                        .set(EMAIL.TYPE, emailDto.type())
                        .execute();
            }
        }

        // phone
        if (brokerDto.getPhoneNumbers() != null && !brokerDto.getPhoneNumbers().isEmpty()) {
            for (PhoneNumberDto phoneDto : brokerDto.getPhoneNumbers()) {
                dsl.insertInto(PHONE_NUMBER)
                        .set(PHONE_NUMBER.BROKER_ID, brokerId)
                        .set(PHONE_NUMBER.NUMBER, phoneDto.number())
                        .set(PHONE_NUMBER.TYPE, phoneDto.type())
                        .execute();
            }
        }

        brokerDto.setId(brokerId);
        return brokerDto;
    }

    @Transactional
    public BrokerDto updateBroker(BrokerDto brokerDto) {
        boolean exists = dsl.fetchExists(
                dsl.selectOne()
                        .from(BROKER)
                        .where(BROKER.ID.eq(brokerDto.getId()))
        );

        if (!exists) {
            throw new EntityNotFoundException("Broker not found with ID: " + brokerDto.getId());
        }

        dsl.update(BROKER)
                .set(BROKER.FIRST_NAME, brokerDto.getFirstName())
                .set(BROKER.LAST_NAME, brokerDto.getLastName())
                .set(BROKER.IS_MLS, brokerDto.getIsPaidUser())
                .where(BROKER.ID.eq(brokerDto.getId()))
                .execute();

        // degree
        dsl.deleteFrom(BROKER_DEGREE)
                .where(BROKER_DEGREE.BROKER_ID.eq(brokerDto.getId()))
                .execute();

        if (brokerDto.getDegreeBefore() != null && !brokerDto.getDegreeBefore().isEmpty()) {
            for (String degree : brokerDto.getDegreeBefore()) {
                dsl.insertInto(BROKER_DEGREE)
                        .set(BROKER_DEGREE.BROKER_ID, brokerDto.getId())
                        .set(BROKER_DEGREE.DEGREE_NAME, degree)
                        .execute();
            }
        }

        // email
        dsl.deleteFrom(EMAIL)
                .where(EMAIL.BROKER_ID.eq(brokerDto.getId()))
                .execute();

        if (brokerDto.getEmails() != null && !brokerDto.getEmails().isEmpty()) {
            for (EmailDto emailDto : brokerDto.getEmails()) {
                dsl.insertInto(EMAIL)
                        .set(EMAIL.BROKER_ID, brokerDto.getId())
                        .set(EMAIL.EMAIL_, emailDto.email())
                        .set(EMAIL.TYPE, emailDto.type())
                        .execute();
            }
        }

        // phone
        dsl.deleteFrom(PHONE_NUMBER)
                .where(PHONE_NUMBER.BROKER_ID.eq(brokerDto.getId()))
                .execute();

        if (brokerDto.getPhoneNumbers() != null && !brokerDto.getPhoneNumbers().isEmpty()) {
            for (PhoneNumberDto phoneDto : brokerDto.getPhoneNumbers()) {
                dsl.insertInto(PHONE_NUMBER)
                        .set(PHONE_NUMBER.BROKER_ID, brokerDto.getId())
                        .set(PHONE_NUMBER.NUMBER, phoneDto.number())
                        .set(PHONE_NUMBER.TYPE, phoneDto.type())
                        .execute();
            }
        }

        return brokerDto;
    }

    @Transactional
    public void deleteBroker(UUID brokerId) {
        int deleted = dsl.deleteFrom(BROKER)
                .where(BROKER.ID.eq(brokerId))
                .execute();

        if (deleted == 0) {
            throw new EntityNotFoundException("Broker not found with ID: " + brokerId);
        }

        log.info("Broker with ID {} was deleted along with all related records", brokerId);
    }
}
