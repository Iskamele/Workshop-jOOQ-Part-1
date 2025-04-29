CREATE TABLE address
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    country    VARCHAR(50),
    city       TEXT,
    street     VARCHAR(50),
    number     INTEGER,
    gis_id     UUID, -- Связь с таблицей GIS
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE gis
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    latitude   DOUBLE PRECISION NOT NULL,
    longitude  DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

ALTER TABLE address
    ADD CONSTRAINT fk_address_gis
        FOREIGN KEY (gis_id)
            REFERENCES gis (id)
            ON DELETE SET NULL;

CREATE TABLE degree_before
(
    name TEXT PRIMARY KEY NOT NULL,
    text TEXT             NOT NULL
);

CREATE TABLE office
(
    id           UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    name         TEXT NOT NULL,
    address_id   UUID REFERENCES address (id) ON DELETE SET NULL,
    "isHidden"   BOOLEAN                  DEFAULT FALSE,
    date_opening DATE,
    tags         TEXT[],
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE broker
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    office_id  UUID REFERENCES office (id) ON DELETE CASCADE,
    is_mls     BOOLEAN                  DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE broker_degree
(
    broker_id   UUID REFERENCES broker (id) ON DELETE CASCADE,
    degree_name TEXT REFERENCES degree_before (name) ON DELETE CASCADE,
    PRIMARY KEY (broker_id, degree_name)
);

CREATE TABLE email
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    email      TEXT NOT NULL,
    type       TEXT,
    broker_id  UUID REFERENCES broker (id) ON DELETE CASCADE,
    office_id  UUID REFERENCES office (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT email_owner_check CHECK (
        (broker_id IS NOT NULL AND office_id IS NULL) OR
        (broker_id IS NULL AND office_id IS NOT NULL)
        )
);

CREATE TABLE phone_number
(
    id         UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    number     TEXT NOT NULL,
    type       TEXT,
    broker_id  UUID REFERENCES broker (id) ON DELETE CASCADE,
    office_id  UUID REFERENCES office (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT phone_owner_check CHECK (
        (broker_id IS NOT NULL AND office_id IS NULL) OR
        (broker_id IS NULL AND office_id IS NOT NULL)
        )
);

CREATE TABLE property
(
    id              UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    price           INTEGER NOT NULL,
    is_public_price BOOLEAN                  DEFAULT TRUE,
    address_id      UUID    REFERENCES address (id) ON DELETE SET NULL,
    broker_id       UUID    REFERENCES broker (id) ON DELETE SET NULL,
    office_id       UUID REFERENCES office (id) ON DELETE CASCADE,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE image
(
    id          UUID PRIMARY KEY         DEFAULT gen_random_uuid(),
    property_id UUID NOT NULL REFERENCES property (id) ON DELETE CASCADE,
    image_url   TEXT NOT NULL,
    is_main     BOOLEAN                  DEFAULT FALSE,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_broker_office_id ON broker (office_id);
CREATE INDEX idx_property_broker_id ON property (broker_id);
CREATE INDEX idx_property_office_id ON property (office_id);
CREATE INDEX idx_property_address_id ON property (address_id);
CREATE INDEX idx_email_broker_id ON email (broker_id);
CREATE INDEX idx_email_office_id ON email (office_id);
CREATE INDEX idx_phone_broker_id ON phone_number (broker_id);
CREATE INDEX idx_phone_office_id ON phone_number (office_id);
CREATE INDEX idx_property_image_property_id ON image (property_id);
CREATE INDEX idx_address_gis_id ON address (gis_id);