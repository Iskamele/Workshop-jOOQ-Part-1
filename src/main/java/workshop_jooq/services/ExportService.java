package workshop_jooq.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import workshop_jooq.dtos.OfficeDto;
import workshop_jooq.dtos.PropertyDto;
import workshop_jooq.repositories.ExportRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service for exporting data from the application.
 * <p>
 * This service acts as an intermediary between controllers and repositories,
 * following the standard layered architecture pattern. It delegates data access
 * operations to the repository layer while providing a business-oriented API
 * to controllers.
 * <p>
 * In this implementation, the service is thin and mainly delegates to the repository,
 * but in a real-world application, it would contain business logic, validation,
 * authorization checks, and other cross-cutting concerns.
 */
@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportRepository exportRepository;

    /**
     * Retrieves detailed information about a property.
     *
     * @param officeId   ID of the office that owns the property
     * @param propertyId ID of the property to retrieve
     * @return Complete property DTO with all related information
     */
    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
        return exportRepository.getPropertyById(officeId, propertyId);
    }

    /**
     * Retrieves a paginated list of properties for a specific broker.
     *
     * @param officeId   ID of the office
     * @param brokerId   ID of the broker whose properties to retrieve
     * @param pageSize   Number of records per page
     * @param pageNumber Page number to retrieve (0-based)
     * @return Paginated list of property DTOs
     */
    public Page<PropertyDto> getPropertiesShortInfoForBroker(UUID officeId, UUID brokerId, int pageSize, int pageNumber) {
        return exportRepository.getPropertiesShortInfoForBroker(officeId, brokerId, pageSize, pageNumber);
    }

    /**
     * Retrieves all offices with their contact details.
     *
     * @return List of office DTOs with contact information
     */
    public List<OfficeDto> getAllOffices() {
        return exportRepository.getAllOffices();
    }

}
