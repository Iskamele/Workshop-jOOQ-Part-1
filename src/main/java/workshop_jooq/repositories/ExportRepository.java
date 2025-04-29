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

@Repository
@RequiredArgsConstructor
@Slf4j
public class ExportRepository {
    private final DSLContext dsl;

    /** ONLY COUNTRY
     * (un/commit JsonInclude in Property/Address)
     */
//    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
//        return dsl.select(
//                        PROPERTY.ID,
//                        PROPERTY.PRICE,
//                        PROPERTY.IS_PUBLIC_PRICE,
//                        ADDRESS.COUNTRY
//                )
//                .from(PROPERTY)
//                .leftJoin(ADDRESS).on(PROPERTY.ADDRESS_ID.eq(ADDRESS.ID))
//                .leftJoin(GIS).on(ADDRESS.GIS_ID.eq(GIS.ID))
//                .where(PROPERTY.ID.eq(propertyId))
//                .and(PROPERTY.OFFICE_ID.eq(officeId))
//                .fetchOne(record -> {
//                    PropertyDto propertyDto = new PropertyDto();
//                    propertyDto.setId(record.get(PROPERTY.ID));
//                    propertyDto.setPrice(record.get(PROPERTY.PRICE));
//                    propertyDto.setPublicPrice(record.get(PROPERTY.IS_PUBLIC_PRICE));
//
//                    AddressDto addressInfo = null;
//                    if (record.get(ADDRESS.ID) != null) {
//                        addressInfo = new AddressDto();
//                        addressInfo.setCountry(record.get(ADDRESS.COUNTRY));
//                    }
//                    propertyDto.setAddress(addressInfo);
//
//                    return propertyDto;
//                });
//    }

    /**
     * Full objects with .asterisk()
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
     * Bad example with string
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
     * Main get property
     */
    public PropertyDto getPropertyById(UUID officeId, UUID propertyId) {
        // Receive Property
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

        // Receive Address
        if (propertyResult != null) {
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

                        // Receive Gis
                        GisDto gisDto = r.into(GisDto.class);
                        if (gisDto != null) {
                            addressDto.setCoordinates(gisDto);
                        }

                        return addressDto;
                    });

            propertyResult.setAddress(addressResult);

            // Receive Images
            List<String> images = dsl.select(
                            IMAGE.IMAGE_URL)
                    .from(IMAGE)
                    .where(IMAGE.PROPERTY_ID.eq(propertyId))
                    .fetch(r -> r.get(IMAGE.IMAGE_URL));

            propertyResult.setImages(images);

            // Receive Broker
            if (propertyResult.getBrokerId() != null) {
                BrokerDto brokerResult = dsl.select(
                                BROKER.FIRST_NAME,
                                BROKER.LAST_NAME,
                                BROKER.IS_MLS.as(BrokerDto.Fields.isPaidUser), // Important if names are different;
                                multiset(
                                        dsl.select(BROKER_DEGREE.DEGREE_NAME)
                                                .from(BROKER_DEGREE)
                                                .where(BROKER_DEGREE.BROKER_ID.eq(BROKER.ID))) // Inside broker select we can use BROKER.ID constant but...
                                        .convertFrom(r -> r.collect(Records.intoList()))
                                        .as(BrokerDto.Fields.degreeBefore)
                        )
                        .from(BROKER)
                        .where(BROKER.ID.eq(propertyResult.getBrokerId()))
                        .fetchOneInto(BrokerDto.class);

                if (brokerResult != null) {
                    // Receive Email
                    List<EmailDto> emails = dsl.select(
                                    EMAIL.EMAIL_,
                                    EMAIL.TYPE)
                            .from(EMAIL)
                            .where(EMAIL.BROKER_ID.eq(propertyResult.getBrokerId())) // ... but without broker's select we CAN'T use BROKER.ID constant, so we need to save an id before and use it like here
                            .fetchInto(EmailDto.class);
                    brokerResult.setEmails(emails);

                    // Receive Phone Number
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

    public Page<PropertyDto> getPropertiesShortInfoForBroker(UUID officeId, UUID brokerId, int pageSize, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        var select = dsl.select(
                        PROPERTY.PRICE,
                        ADDRESS.COUNTRY,
//                        ADDRESS.CITY.as(PropertyDto.Fields.address+"."+AddressDto.Fields.city), // It's working, but string cancat here is not good practice
                        ADDRESS.CITY,
                        ADDRESS.STREET,
                        ADDRESS.NUMBER
                )
                .from(PROPERTY)
                .join(ADDRESS).on(PROPERTY.ADDRESS_ID.eq(ADDRESS.ID)) // Not left join, because we don't need a property with only price field
                .where(PROPERTY.OFFICE_ID.eq(officeId))
                .and(PROPERTY.BROKER_ID.eq(brokerId));

        int totalCount = dsl.fetchCount(select);

        SortField<?> orderByCity = ADDRESS.CITY.asc();

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

        return new PageImpl<>(properties, pageRequest, totalCount);
    }

    public List<OfficeDto> getAllOffices() {
        return dsl.select(
//                        OFFICE.NAME.as(OfficeDto.Fields.officeName), // We don't need it because we use fetch
                        OFFICE.NAME,
                        OFFICE.DATE_OPENING,
                        ADDRESS.COUNTRY,
                        ADDRESS.CITY,
                        ADDRESS.STREET,
                        ADDRESS.NUMBER,
                        multiset(
                                dsl.select(
                                                EMAIL.EMAIL_,
                                                EMAIL.TYPE)
                                        .from(EMAIL)
                                        .where(EMAIL.OFFICE_ID.eq(OFFICE.ID)))
                                .convertFrom(r -> r.into(EmailDto.class)
                                ).as(OfficeDto.Fields.emails),
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

                    office.setCookedAddress(r.get(ADDRESS.COUNTRY) + ", "
                            + r.get(ADDRESS.CITY) + ", "
                            + r.get(ADDRESS.STREET) + ", "
                            + r.get(ADDRESS.NUMBER));

                    List<EmailDto> emails = (List<EmailDto>) r.get(OfficeDto.Fields.emails);
                    office.setEmails(emails);

                    List<PhoneNumberDto> phoneNumbers = (List<PhoneNumberDto>) r.get(OfficeDto.Fields.phoneNumbers);
                    office.setPhoneNumbers(phoneNumbers);

                    return office;
                });
    }
}
