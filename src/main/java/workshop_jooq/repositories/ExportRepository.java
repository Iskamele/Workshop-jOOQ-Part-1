package workshop_jooq.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import workshop_jooq.dtos.AddressDto;
import workshop_jooq.dtos.BrokerDto;
import workshop_jooq.dtos.EmailDto;
import workshop_jooq.dtos.GisDto;
import workshop_jooq.dtos.OfficeDto;
import workshop_jooq.dtos.PhoneNumberDto;
import workshop_jooq.dtos.PropertyDto;

import java.util.List;
import java.util.UUID;

import static jooq.generated.tables.Address.ADDRESS;
import static jooq.generated.tables.Broker.BROKER;
import static jooq.generated.tables.BrokerDegree.BROKER_DEGREE;
import static jooq.generated.tables.Email.EMAIL;
import static jooq.generated.tables.Gis.GIS;
import static jooq.generated.tables.Image.IMAGE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.PhoneNumber.PHONE_NUMBER;
import static jooq.generated.tables.Property.PROPERTY;
import static org.jooq.impl.DSL.multiset;


/**
 * Repository for exporting data from the database using jOOQ.
 * <p>
 * This class demonstrates various advanced jOOQ techniques for querying and mapping complex
 * related data structures:
 * <ul>
 *   <li>Hierarchical data retrieval with multiple joins</li>
 *   <li>Conditional selection using DSL.when()</li>
 *   <li>Multiset queries for fetching nested collections</li>
 *   <li>Record mapping to DTOs</li>
 *   <li>Pagination implementation</li>
 * </ul>
 * <p>
 * The class includes examples of both good and bad practices with accompanying comments.
 * Sometimes bad practices come out as good, so use your head.
 *
 * @author Oleksandr Novikov
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ExportRepository {
    private final DSLContext dsl;

    /**
     * Example 2: Using asterisk() for full object retrieval
     * <p>
     * Good practices:
     * - Retrieves complete objects without having to specify each column
     * - Uses into() for mapping records to DTOs
     * <p>
     * Drawbacks:
     * - asterisk() can be less efficient by retrieving unnecessary columns
     * - When table structure changes, queries might retrieve unexpected columns
     * - Manual field mapping is still required in complex scenarios
     */
//    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
//        return dsl.select(
//                        PROPERTY.ID,
//                        PROPERTY.PRICE,
//                        PROPERTY.IS_PUBLIC_PRICE,
//                        ADDRESS.asterisk(),
//                        GIS.asterisk()
//                )
//                .from(PROPERTY)
//                .leftJoin(ADDRESS).on(PROPERTY.ADDRESS_ID.eq(ADDRESS.ID))
//                .leftJoin(GIS).on(ADDRESS.GIS_ID.eq(GIS.ID))
//                .where(PROPERTY.ID.eq(propertyId))
//                .and(PROPERTY.OFFICE_ID.eq(officeId))
//                .fetchOne(r -> {
//                    PropertyDto propertyDto = new PropertyDto();
//                    propertyDto.setId(r.get(PROPERTY.ID));
//                    propertyDto.setPrice(r.get(PROPERTY.PRICE));
//                    propertyDto.setPublicPrice(r.get(PROPERTY.IS_PUBLIC_PRICE));
//
//                    AddressDto addressRecord = r.into(AddressDto.class);
//                    GisDto gisRecord = r.into(GisDto.class);
//
//                    if (addressRecord != null) {
//                        AddressDto addressInfo = new AddressDto();
//                        addressInfo.setId(addressRecord.getId());
//                        addressInfo.setCountry(addressRecord.getCountry());
//                        addressInfo.setCity(addressRecord.getCity());
//                        addressInfo.setStreet(addressRecord.getStreet());
//                        addressInfo.setNumber(addressRecord.getNumber());
//
//                        if (gisRecord != null) {
//                            GisDto gisInfo = new GisDto();
//                            gisInfo.setLatitude(gisRecord.getLatitude());
//                            gisInfo.setLongitude(gisRecord.getLongitude());
//                            addressInfo.setCoordinates(gisInfo);
//                        }
//                        propertyDto.setAddress(addressInfo);
//                    }
//
//                    return propertyDto;
//                });
//    }

    /**
     * Example a: Using String concatenation for nested fields
     * <p>
     * Bad practices:
     * - String concatenation for field aliases is error-prone and difficult to maintain
     * - Changes to DTO field names would break the queries
     * - Lacks type safety, which is one of the main benefits of using jOOQ
     */
//    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
//        return dsl.select(
//                        PROPERTY.ID,
//                        PROPERTY.PRICE,
//                        PROPERTY.IS_PUBLIC_PRICE,
//                        ADDRESS.ID.as(PropertyDto.Fields.address + "." + AddressDto.Fields.id),
//                        ADDRESS.COUNTRY.as(PropertyDto.Fields.address + "." + AddressDto.Fields.country),
//                        GIS.LATITUDE.as(PropertyDto.Fields.address + "." + AddressDto.Fields.coordinates + "." + GisDto.Fields.latitude),
//                        GIS.LONGITUDE.as(PropertyDto.Fields.address + "." + AddressDto.Fields.coordinates + "." + GisDto.Fields.longitude)
//                ).from(PROPERTY)
//                .leftJoin(ADDRESS).on(PROPERTY.ADDRESS_ID.eq(ADDRESS.ID))
//                .leftJoin(GIS).on(ADDRESS.GIS_ID.eq(GIS.ID))
//                .where(PROPERTY.ID.eq(propertyId))
//                .and(PROPERTY.OFFICE_ID.eq(officeId))
//                .fetchOneInto(PropertyDto.class);
//    }

    /**
     * Retrieves detailed property information by its ID and office ID.
     * <p>
     * This is the recommended approach that demonstrates several jOOQ best practices:
     * <ul>
     *   <li>Split complex queries into multiple focused queries</li>
     *   <li>Use conditional selection for access control (e.g., public prices)</li>
     *   <li>Progressive enrichment of DTOs with related data</li>
     * </ul>
     * <p>
     * The method performs several queries in sequence to build a complete property record:
     * 1. First retrieves the basic property information
     * 2. Then fetches the address and geographic information
     * 3. Then retrieves associated images
     * 4. Finally gets broker information if available
     *
     * @param officeId   ID of the office that owns the property
     * @param propertyId ID of the property to retrieve
     * @return Complete property DTO with all related information
     */
    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
        // Retrieve basic Property information
        // Using conditional selection with DSL.when() for price to implement access control
        // This ensures that prices are only included when they're public
        PropertyDto propertyResult = dsl.select(
                        DSL.when(PROPERTY.IS_PUBLIC_PRICE.isTrue(), PROPERTY.PRICE)
                                .otherwise((Integer) null).as(PropertyDto.Fields.price),
                        PROPERTY.IS_PUBLIC_PRICE,
                        PROPERTY.BROKER_ID,
                        PROPERTY.ADDRESS_ID
                )
                .from(PROPERTY)
                .where(PROPERTY.ID.eq(propertyId))
                .and(PROPERTY.OFFICE_ID.eq(officeId))
                .fetchOne(r -> {
                    PropertyDto propertyDto = new PropertyDto();
                    propertyDto.setPrice(r.get(PROPERTY.PRICE));
                    propertyDto.setIsPublicPrice(r.get(PROPERTY.IS_PUBLIC_PRICE));
                    propertyDto.setBrokerId(r.get(PROPERTY.BROKER_ID));
                    propertyDto.setAddressId(r.get(PROPERTY.ADDRESS_ID));
                    return propertyDto;
                });

        // If property exists, fetch additional information
        if (propertyResult != null) {
            // Retrieve Address and GIS information
            // For GIS, using asterisk() is acceptable here as it's a simple table
            AddressDto addressResult = dsl.select(
                            ADDRESS.COUNTRY,
                            ADDRESS.CITY,
                            ADDRESS.STREET,
                            ADDRESS.NUMBER,
                            GIS.asterisk())
                    .from(ADDRESS)
                    .leftJoin(GIS).on(ADDRESS.GIS_ID.eq(GIS.ID))
                    .where(ADDRESS.ID.eq(propertyResult.getAddressId()))
                    .fetchOne(r -> {
                        AddressDto addressDto = new AddressDto();
                        addressDto.setCountry(r.get(ADDRESS.COUNTRY));
                        addressDto.setCity(r.get(ADDRESS.CITY));
                        addressDto.setStreet(r.get(ADDRESS.STREET));
                        addressDto.setNumber(r.get(ADDRESS.NUMBER));

                        // Map GIS data if available
                        GisDto gisDto = r.into(GisDto.class);
                        if (gisDto != null) {
                            addressDto.setCoordinates(gisDto);
                        }

                        return addressDto;
                    });

            propertyResult.setAddress(addressResult);

            // Retrieve property Images as a list of URLs
            List<String> images = dsl.select(
                            IMAGE.IMAGE_URL)
                    .from(IMAGE)
                    .where(IMAGE.PROPERTY_ID.eq(propertyId))
                    .fetch(r -> r.get(IMAGE.IMAGE_URL));

            propertyResult.setImages(images);

            // Retrieve Broker information if associated with the property
            if (propertyResult.getBrokerId() != null) {
                // Use multiset for fetching the broker's degrees in a single query
                // This is more efficient than executing separate queries for each broker-degree relationship
                BrokerDto brokerResult = dsl.select(
                                BROKER.FIRST_NAME,
                                BROKER.LAST_NAME,
                                BROKER.IS_MLS.as(BrokerDto.Fields.isPaidUser), // Field name mapping when DTO field differs from DB
                                multiset(
                                        dsl.select(BROKER_DEGREE.DEGREE_NAME)
                                                .from(BROKER_DEGREE)
                                                .where(BROKER_DEGREE.BROKER_ID.eq(BROKER.ID)))
                                        .convertFrom(r -> r.collect(Records.intoList()))
                                        .as(BrokerDto.Fields.degreeBefore)
                        )
                        .from(BROKER)
                        .where(BROKER.ID.eq(propertyResult.getBrokerId()))
                        .fetchOneInto(BrokerDto.class);

                if (brokerResult != null) {
                    // Retrieve broker's Email information
                    List<EmailDto> emails = dsl.select(
                                    EMAIL.EMAIL_,
                                    EMAIL.TYPE)
                            .from(EMAIL)
                            .where(EMAIL.BROKER_ID.eq(propertyResult.getBrokerId()))
                            .fetchInto(EmailDto.class);
                    brokerResult.setEmails(emails);

                    // Retrieve broker's Phone Number information
                    List<PhoneNumberDto> phoneNumbers = dsl.select(
                                    PHONE_NUMBER.NUMBER,
                                    PHONE_NUMBER.TYPE)
                            .from(PHONE_NUMBER)
                            .where(PHONE_NUMBER.BROKER_ID.eq(propertyResult.getBrokerId()))
                            .fetchInto(PhoneNumberDto.class);
                    brokerResult.setPhoneNumbers(phoneNumbers);
                }

                propertyResult.setBroker(brokerResult);
            }
        }

        return propertyResult;
    }

    /**
     * Retrieves a paginated list of properties for a specific broker.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Using jOOQ with Spring's pagination</li>
     *   <li>Applying joins with specific constraints</li>
     *   <li>Sorting and limiting results</li>
     *   <li>Efficient record mapping</li>
     * </ul>
     *
     * @param officeId   ID of the office
     * @param brokerId   ID of the broker whose properties to retrieve
     * @param pageSize   Number of records per page
     * @param pageNumber Page number to retrieve (0-based)
     * @return Paginated list of property DTOs
     */
    public Page<PropertyDto> getPropertiesShortInfoForBroker(UUID officeId, UUID brokerId, int pageSize, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // Build the base query that will be reused for total count and actual data retrieval
        var select = dsl.select(
                        PROPERTY.PRICE,
                        ADDRESS.COUNTRY,
                        // Bad practice example (commented out):
                        // ADDRESS.CITY.as(PropertyDto.Fields.address+"."+AddressDto.Fields.city), // String concatenation not recommended
                        ADDRESS.CITY,
                        ADDRESS.STREET,
                        ADDRESS.NUMBER
                )
                .from(PROPERTY)
                .join(ADDRESS).on(PROPERTY.ADDRESS_ID.eq(ADDRESS.ID)) // Using inner join as we need address
                .where(PROPERTY.OFFICE_ID.eq(officeId))
                .and(PROPERTY.BROKER_ID.eq(brokerId));

        // Get total count for pagination
        int totalCount = dsl.fetchCount(select);

        // Define sort order
        SortField<?> orderByCity = ADDRESS.CITY.asc();

        // Fetch the page of data with ordering, offset and limit
        List<PropertyDto> properties = select
                .orderBy(orderByCity)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch(r -> {
                    PropertyDto property = new PropertyDto();
                    property.setPrice(r.get(PROPERTY.PRICE));

                    AddressDto address = new AddressDto();
                    address.setCountry(r.get(ADDRESS.COUNTRY));
                    address.setCity(r.get(ADDRESS.CITY));
                    address.setStreet(r.get(ADDRESS.STREET));
                    address.setNumber(r.get(ADDRESS.NUMBER));

                    property.setAddress(address);
                    return property;
                });

        // Create Spring Data Page instance with the data, pagination info, and total count
        return new PageImpl<>(properties, pageRequest, totalCount);
    }

    /**
     * Retrieves all offices with their contact details.
     * <p>
     * This method demonstrates:
     * <ul>
     *   <li>Using multiset for collecting nested entities in a single query</li>
     *   <li>Custom field mapping between DB and DTO fields</li>
     *   <li>Creating derived/calculated fields</li>
     *   <li>Handling arrays and collections</li>
     * </ul>
     *
     * @return List of office DTOs with contact information
     */
    public List<OfficeDto> getAllOffices() {
        return dsl.select(
                        // Field mapping is done in fetch(), in this case alias can provide an exception, so this example below is not useful:
                        // OFFICE.NAME.as(OfficeDto.Fields.officeName),
                        OFFICE.NAME,
                        OFFICE.DATE_OPENING,
                        ADDRESS.COUNTRY,
                        ADDRESS.CITY,
                        ADDRESS.STREET,
                        ADDRESS.NUMBER,
                        // Using multiset to fetch all emails for each office in a single query
                        // This avoids N+1 query problems
                        multiset(
                                dsl.select(
                                                EMAIL.EMAIL_,
                                                EMAIL.TYPE)
                                        .from(EMAIL)
                                        .where(EMAIL.OFFICE_ID.eq(OFFICE.ID)))
                                .convertFrom(r -> r.into(EmailDto.class)
                                ).as(OfficeDto.Fields.emails),
                        // Similar multiset pattern for phone numbers
                        multiset(
                                dsl.select(
                                                PHONE_NUMBER.NUMBER,
                                                PHONE_NUMBER.TYPE)
                                        .from(PHONE_NUMBER)
                                        .where(PHONE_NUMBER.OFFICE_ID.eq(OFFICE.ID)))
                                .convertFrom(r -> r.into(PhoneNumberDto.class)
                                ).as(OfficeDto.Fields.phoneNumbers),
                        OFFICE.TAGS
                )
                .from(OFFICE)
                .leftJoin(ADDRESS).on(ADDRESS.ID.eq(OFFICE.ADDRESS_ID))
                .fetch(r -> {
                    OfficeDto office = new OfficeDto();
                    office.setOfficeName(r.get(OFFICE.NAME));
                    office.setDateOpening(r.get(OFFICE.DATE_OPENING));
                    office.setTags(r.get(OFFICE.TAGS));

                    // Creating a calculated field by concatenating address parts
                    // TODO Move it to service. Don't do some business logic inside the repo
                    office.setCookedAddress(r.get(ADDRESS.COUNTRY) + ", "
                            + r.get(ADDRESS.CITY) + ", "
                            + r.get(ADDRESS.STREET) + ", "
                            + r.get(ADDRESS.NUMBER));

                    // Get emails from multiset result
                    List<EmailDto> emails = (List<EmailDto>) r.get(OfficeDto.Fields.emails);
                    office.setEmails(emails);

                    // Get phone numbers from multiset result
                    List<PhoneNumberDto> phoneNumbers = (List<PhoneNumberDto>) r.get(OfficeDto.Fields.phoneNumbers);
                    office.setPhoneNumbers(phoneNumbers);

                    return office;
                });
    }
}
