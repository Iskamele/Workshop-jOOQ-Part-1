package workshop_jooq.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import workshop_jooq.dtos.BrokerDto;
import workshop_jooq.repositories.ImportRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ImportRepository importRepositoryl;

    public BrokerDto createBroker(BrokerDto brokerDto) {
        return importRepositoryl.createBroker(brokerDto);
    }

    public BrokerDto updateBroker(BrokerDto brokerDto) {
        return importRepositoryl.updateBroker(brokerDto);
    }

    public void deleteBroker(UUID brokerId) {
        importRepositoryl.deleteBroker(brokerId);
    }

}
