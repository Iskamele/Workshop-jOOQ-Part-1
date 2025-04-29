package workshop_jooq.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import workshop_jooq.dtos.OfficeDto;
import workshop_jooq.dtos.PropertyDto;
import workshop_jooq.services.ExportService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "Export", description = "Export API related to properties operations")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/export/offices/{officeId}/properties/{propertyId}")
    @Operation(summary = "Get property details by ID", description = "Retrieves detailed information about a specific property")
    @ApiResponse(content = @Content(schema = @Schema(implementation = PropertyDto.class, description = "Property with all related details including broker, address, and images"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<PropertyDto> getPropertyById(
            @PathVariable UUID officeId,
            @PathVariable UUID propertyId
    ) {
        return ResponseEntity.ok(this.exportService.getPropertyById(officeId, propertyId));
    }

    @GetMapping("/export/offices/{officeId}/brokers/{brokerId}/properties")
    @Operation(summary = "Get properties for a specific broker", description = "Retrieves a paginated list of properties associated with a particular broker within an office")
    @ApiResponse(content = @Content(schema = @Schema(implementation = PropertyDto.class, description = "Paginated list of properties with basic information"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<Page<PropertyDto>> getPropertiesShortInfoForBroker(
            @PathVariable UUID officeId,
            @PathVariable UUID brokerId,
            @RequestParam("pageSize") @Max(20) @Min(1) int pageSize,
            @RequestParam("pageNumber") @Min(0) int pageNumber
    ) {
        return ResponseEntity.ok(this.exportService.getPropertiesShortInfoForBroker(officeId, brokerId, pageSize, pageNumber));
    }

    @GetMapping("/export/offices")
    @Operation(summary = "Get all offices", description = "Retrieves a list of all available real estate offices with their contact information and address details")
    @ApiResponse(content = @Content(schema = @Schema(implementation = OfficeDto.class, description = "List of offices with contact details and location information"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<List<OfficeDto>> getAllOffices() {
        return ResponseEntity.ok(this.exportService.getAllOffices());
    }

}
