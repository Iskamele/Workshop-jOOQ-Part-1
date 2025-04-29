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

/**
 * REST controller for data export operations.
 * <p>
 * This controller provides endpoints for retrieving property and office information.
 * It demonstrates:
 * <ul>
 *   <li>RESTful API design with appropriate HTTP methods</li>
 *   <li>Path variable and request parameter handling</li>
 *   <li>Response entity wrapping</li>
 *   <li>Validation of input parameters</li>
 *   <li>OpenAPI documentation with Swagger annotations</li>
 * </ul>
 * <p>
 * TODO The controller follows REST best practices and uses Spring's validation framework
 * to ensure input parameters are valid before processing requests.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "Export", description = "Export API related to properties operations")
public class ExportController {

    private final ExportService exportService;

    /**
     * Retrieves detailed information about a specific property.
     * <p>
     * This endpoint demonstrates:
     * <ul>
     *   <li>Path variable usage for resource identification</li>
     *   <li>Proper HTTP GET semantics for retrieving data</li>
     *   <li>Response wrapping with ResponseEntity</li>
     * </ul>
     *
     * @param officeId   ID of the office that owns the property
     * @param propertyId ID of the property to retrieve
     * @return Response entity containing the property DTO
     */
    @GetMapping("/export/offices/{officeId}/properties/{propertyId}")
    @Operation(summary = "Get property details by ID", description = "Retrieves detailed information about a specific property")
    @ApiResponse(content = @Content(schema = @Schema(implementation = PropertyDto.class, description = "Property with all related details including broker, address, and images"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<PropertyDto> getPropertyById(
            @PathVariable UUID officeId,
            @PathVariable UUID propertyId
    ) {
        return ResponseEntity.ok(this.exportService.getPropertyById(officeId, propertyId));
    }

    /**
     * Retrieves a paginated list of properties for a specific broker.
     * <p>
     * This endpoint demonstrates:
     * <ul>
     *   <li>Path variable usage for resource identification</li>
     *   <li>Request parameter handling for pagination</li>
     *   <li>Parameter validation with constraints</li>
     *   <li>Returning paginated responses</li>
     * </ul>
     *
     * @param officeId   ID of the office
     * @param brokerId   ID of the broker whose properties to retrieve
     * @param pageSize   Number of records per page (between 1 and 20)
     * @param pageNumber Page number to retrieve (minimum 0)
     * @return Response entity containing a paginated list of property DTOs
     */
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

    /**
     * Retrieves all offices with their contact details.
     * <p>
     * This endpoint demonstrates:
     * <ul>
     *   <li>Collection resource endpoint design</li>
     *   <li>Returning a list of resources</li>
     *   <li>Proper HTTP GET semantics for retrieving data</li>
     * </ul>
     *
     * @return Response entity containing a list of office DTOs
     */
    @GetMapping("/export/offices")
    @Operation(summary = "Get all offices", description = "Retrieves a list of all available real estate offices with their contact information and address details")
    @ApiResponse(content = @Content(schema = @Schema(implementation = OfficeDto.class, description = "List of offices with contact details and location information"), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "200")
    public ResponseEntity<List<OfficeDto>> getAllOffices() {
        return ResponseEntity.ok(this.exportService.getAllOffices());
    }

}
