CREATE TABLE IF NOT EXISTS users(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories(
     id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
     name VARCHAR(50) NOT NULL,
     CONSTRAINT pk_category PRIMARY KEY (id),
     CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    annotation TEXT NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    published_on TIMESTAMP WITH TIME ZONE,
    initiator_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    event_date TIMESTAMP WITH TIME ZONE,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit INTEGER DEFAULT 0,
    request_moderation BOOLEAN DEFAULT TRUE,
    title VARCHAR(150) NOT NULL,
    state VARCHAR(100) DEFAULT 'PENDING',
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_to_category FOREIGN KEY (category_id) REFERENCES categories,
    CONSTRAINT fk_event_to_user FOREIGN KEY (initiator_id) REFERENCES users
);

CREATE TABLE IF NOT EXISTS request(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    status VARCHAR(20) DEFAULT 'PENDING',
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_to_event FOREIGN KEY (event_id) REFERENCES events,
    CONSTRAINT fk_request_to_user FOREIGN KEY (requester_id) REFERENCES users,
    CONSTRAINT uq_request UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    title VARCHAR(70) NOT NULL,
    pinned BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilation_events(
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_compilation_events_to_event FOREIGN KEY (event_id) REFERENCES events,
    CONSTRAINT fk_compilation_events_to_compilation FOREIGN KEY (compilation_id) REFERENCES compilations,
    CONSTRAINT pk_compilation_events PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS comments(
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    text TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_to_user FOREIGN KEY (author_id) REFERENCES users,
    CONSTRAINT fk_comment_to_event FOREIGN KEY (event_id) REFERENCES events
);