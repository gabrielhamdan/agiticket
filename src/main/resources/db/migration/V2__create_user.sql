CREATE TABLE "user"
(
    id           BIGSERIAL PRIMARY KEY,
    user_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    user_role    VARCHAR(255) NOT NULL,
    user_enabled BOOLEAN      NOT NULL DEFAULT FALSE
);