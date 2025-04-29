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

/**
 * Repository for importing data into the database using jOOQ.
 * <p>
 * This class demonstrates jOOQ's data manipulation capabilities with a focus on:
 * <ul>
 *   <li>Transactional operations for maintaining data integrity</li>
 *   <li>Insert, update, and delete operations</li>
 *   <li>Handling of complex data structures with related records</li>
 *   <li>Error handling and validation</li>
 * </ul>
 * <p>
 * The implementation shows how jOOQ can be used for write operations while maintaining
 * clean separation of concerns and ensuring data consistency.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ImportRepository {
    private final DSLContext dsl;

    /**
     * Creates a new broker record with related information.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Transactional operations to ensure data consistency</li>
     *   <li>Insert operations with returning clause to get generated IDs</li>
     *   <li>Handling of collections and related entities</li>
     *   <li>Use of jOOQ's fluent API for building insert statements</li>
     * </ul>
     * <p>
     * The method creates a broker record and then inserts all related degrees, emails, and phone numbers.
     * The entire operation is wrapped in a transaction to ensure data consistency.
     *
     * @param brokerDto DTO containing broker information to be created
     * @return The created broker DTO with assigned ID
     * @throws NullPointerException if the insert operation fails to return an ID
     */
    @Transactional
    public BrokerDto createBroker(BrokerDto brokerDto) {
        // Insert the broker and get the generated ID
        // Using requireNonNull to ensure we have an ID; otherwise, throw an exception
        UUID brokerId = Objects.requireNonNull(dsl.insertInto(BROKER)
                        .set(BROKER.FIRST_NAME, brokerDto.getFirstName())
                        .set(BROKER.LAST_NAME, brokerDto.getLastName())
                        .set(BROKER.OFFICE_ID, brokerDto.getOfficeId())
                        .set(BROKER.IS_MLS, brokerDto.getIsPaidUser())
                        .returning(BROKER.ID)
                        .fetchOne())
                .getId();

        // Insert broker degrees if provided
        // This demonstrates handling optional collections in the data model
        if (brokerDto.getDegreeBefore() != null && !brokerDto.getDegreeBefore().isEmpty()) {
            for (String degree : brokerDto.getDegreeBefore()) {
                dsl.insertInto(BROKER_DEGREE)
                        .set(BROKER_DEGREE.BROKER_ID, brokerId)
                        .set(BROKER_DEGREE.DEGREE_NAME, degree)
                        .execute();
            }
        }

        // Insert email addresses if provided
        if (brokerDto.getEmails() != null && !brokerDto.getEmails().isEmpty()) {
            for (EmailDto emailDto : brokerDto.getEmails()) {
                dsl.insertInto(EMAIL)
                        .set(EMAIL.BROKER_ID, brokerId)
                        .set(EMAIL.EMAIL_, emailDto.email())
                        .set(EMAIL.TYPE, emailDto.type())
                        .execute();
            }
        }

        // Insert phone numbers if provided
        if (brokerDto.getPhoneNumbers() != null && !brokerDto.getPhoneNumbers().isEmpty()) {
            for (PhoneNumberDto phoneDto : brokerDto.getPhoneNumbers()) {
                dsl.insertInto(PHONE_NUMBER)
                        .set(PHONE_NUMBER.BROKER_ID, brokerId)
                        .set(PHONE_NUMBER.NUMBER, phoneDto.number())
                        .set(PHONE_NUMBER.TYPE, phoneDto.type())
                        .execute();
            }
        }

        // Set the generated ID on the DTO before returning
        brokerDto.setId(brokerId);
        return brokerDto;
    }

    /**
     * Updates an existing broker record with all related information.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Checking existence before performing operations</li>
     *   <li>Transactional updates with related records</li>
     *   <li>Delete-then-insert pattern for handling collection updates</li>
     *   <li>Error handling with custom exceptions</li>
     * </ul>
     * <p>
     * The method follows a "delete all and recreate" pattern for related collections
     * (degrees, emails, phone numbers) which is simple but effective for maintaining
     * data consistency. All operations are performed within a transaction.
     *
     * @param brokerDto DTO containing updated broker information
     * @return The updated broker DTO
     * @throws EntityNotFoundException if the broker doesn't exist
     */
    @Transactional
    public BrokerDto updateBroker(BrokerDto brokerDto) {
        // Check if the broker exists before attempting update
        // This is a jOOQ pattern for existence checking
        boolean exists = dsl.fetchExists(
                dsl.selectOne()
                        .from(BROKER)
                        .where(BROKER.ID.eq(brokerDto.getId()))
        );

        if (!exists) {
            throw new EntityNotFoundException("Broker not found with ID: " + brokerDto.getId());
        }

        // Update the broker's main information
        dsl.update(BROKER)
                .set(BROKER.FIRST_NAME, brokerDto.getFirstName())
                .set(BROKER.LAST_NAME, brokerDto.getLastName())
                .set(BROKER.IS_MLS, brokerDto.getIsPaidUser())
                .where(BROKER.ID.eq(brokerDto.getId()))
                .execute();

        // Update broker degrees: delete all existing then insert new ones
        // This is a simple but effective pattern for collection updates
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

        // Update emails: delete all existing then insert new ones
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

        // Update phone numbers: delete all existing then insert new ones
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

    /**
     * Deletes a broker and all related records.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Cascading deletes through foreign key constraints</li>
     *   <li>Checking operation results for error handling</li>
     *   <li>Proper logging of important operations</li>
     * </ul>
     * <p>
     * The method leverages database foreign key constraints with CASCADE delete
     * to automatically remove all related records when a broker is deleted.
     * This is more efficient than manually deleting each related record.
     *
     * @param brokerId ID of the broker to delete
     * @throws EntityNotFoundException if the broker doesn't exist
     */
    @Transactional
    public void deleteBroker(UUID brokerId) {
        // Delete the broker and check if any rows were affected
        int deleted = dsl.deleteFrom(BROKER)
                .where(BROKER.ID.eq(brokerId))
                .execute();

        // If no rows were deleted, the broker didn't exist
        if (deleted == 0) {
            throw new EntityNotFoundException("Broker not found with ID: " + brokerId);
        }

        // Log successful deletion, just because we can
        log.info("Broker with ID {} was deleted along with all related records", brokerId);
    }
}
