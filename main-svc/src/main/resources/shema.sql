CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250)        NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    initiator          BIGINT    NOT NULL,
    annotation         VARCHAR   NOT NULL,
    category           BIGINT    NOT NULL,
    created_on         TIMESTAMP NOT NULL,
    description        VARCHAR   NOT NULL,
    event_date         TIMESTAMP NOT NULL,
    location_lat       FLOAT     NOT NULL,
    location_lon       FLOAT     NOT NULL,
    paid               BOOLEAN   NOT NULL,
    participant_limit  INTEGER   NOT NULL,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN   NOT NULL,
    state              VARCHAR   NOT NULL,
    title              VARCHAR   NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event     BIGINT    NOT NULL,
    requester BIGINT    NOT NULL,
    created   TIMESTAMP NOT NULL,
    status    VARCHAR   NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_users FOREIGN KEY (requester) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN     NOT NULL,
    title  VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    record_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
)