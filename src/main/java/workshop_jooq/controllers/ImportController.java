package workshop_jooq.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workshop_jooq.dtos.BrokerDto;
import workshop_jooq.services.ImportService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "Import", description = "Import API related to brokers operations")
public class ImportController {

    private final ImportService importService;

    @PostMapping("/export/brokers")
    @Operation(summary = "Create new broker", description = "Creates a new broker with the provided information")
    @ApiResponse(content = @Content(schema = @Schema(implementation = BrokerDto.class, description = "Created broker with generated ID"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "201")
    public ResponseEntity<BrokerDto> createBroker(@RequestBody BrokerDto brokerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.importService.createBroker(brokerDto));
    }

    @PutMapping("/export/brokers/{brokerId}")
    @Operation(summary = "Update existing broker", description = "Updates an existing broker's information")
    @ApiResponse(content = @Content(schema = @Schema(implementation = BrokerDto.class, description = "Updated broker information"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<BrokerDto> updateBroker(
            @PathVariable UUID brokerId,
            @RequestBody BrokerDto brokerDto
    ) {
        brokerDto.setId(brokerId);
        return ResponseEntity.ok(this.importService.updateBroker(brokerDto));
    }

    @DeleteMapping("/export/brokers/{brokerId}")
    @Operation(summary = "Delete broker", description = "Deletes a broker by ID")
    @ApiResponse(responseCode = "204", description = "Broker successfully deleted")
    public ResponseEntity<Void> deleteBroker(@PathVariable UUID brokerId) {
        this.importService.deleteBroker(brokerId);
        return ResponseEntity.noContent().build();
    }
}
