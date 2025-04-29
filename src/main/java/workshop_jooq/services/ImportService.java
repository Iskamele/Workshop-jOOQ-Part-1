package workshop_jooq.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import workshop_jooq.dtos.BrokerDto;
import workshop_jooq.repositories.ImportRepository;

import java.util.UUID;

/**
 * Service for importing data into the application.
 * <p>
 * This service acts as an intermediary between controllers and repositories,
 * handling broker creation, update, and deletion operations. It follows the
 * standard layered architecture pattern, delegating data access operations
 * to the repository layer.
 * <p>
 * TODO In this implementation, the service is thin and mainly delegates to the repository,
 * but in a real-world application, it would typically include additional business logic,
 * validation rules, authorization checks, and integration with other services.
 */
@Service
@RequiredArgsConstructor
public class ImportService {
    private final ImportRepository importRepositoryl;

    /**
     * Creates a new broker with all associated data.
     *
     * @param brokerDto DTO containing the broker information to create
     * @return The created broker DTO with assigned ID
     */
    public BrokerDto createBroker(BrokerDto brokerDto) {
        return importRepositoryl.createBroker(brokerDto);
    }

    /**
     * Updates an existing broker with all associated data.
     *
     * @param brokerDto DTO containing the updated broker information
     * @return The updated broker DTO
     */
    public BrokerDto updateBroker(BrokerDto brokerDto) {
        return importRepositoryl.updateBroker(brokerDto);
    }

    /**
     * Deletes a broker and all associated data.
     *
     * @param brokerId ID of the broker to delete
     */
    public void deleteBroker(UUID brokerId) {
        importRepositoryl.deleteBroker(brokerId);
    }

}
