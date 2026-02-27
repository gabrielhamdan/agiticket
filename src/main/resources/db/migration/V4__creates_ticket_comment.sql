CREATE TABLE ticket
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    created_by  BIGINT       NOT NULL,
    assigned_to BIGINT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,

    CONSTRAINT fk_ticket_created_by
        FOREIGN KEY (created_by)
            REFERENCES "user" (id),

    CONSTRAINT fk_ticket_assigned_to
        FOREIGN KEY (assigned_to)
            REFERENCES "user" (id)
);

CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    content    TEXT      NOT NULL,
    ticket_id  BIGINT    NOT NULL,
    author_id  BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comment_ticket
        FOREIGN KEY (ticket_id)
            REFERENCES ticket (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_comment_author
        FOREIGN KEY (author_id)
            REFERENCES "user" (id)
);