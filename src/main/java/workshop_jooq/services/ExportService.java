package workshop_jooq.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import workshop_jooq.dtos.OfficeDto;
import workshop_jooq.dtos.PropertyDto;
import workshop_jooq.repositories.ExportRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final ExportRepository exportRepository;

    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
        return exportRepository.getPropertyById(officeId, propertyId);
    }

    public Page<PropertyDto> getPropertiesShortInfoForBroker(UUID officeId, UUID brokerId, int pageSize, int pageNumber) {
        return exportRepository.getPropertiesShortInfoForBroker(officeId, brokerId, pageSize, pageNumber);
    }

    public List<OfficeDto> getAllOffices() {
        return exportRepository.getAllOffices();
    }

}
