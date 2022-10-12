--liquibase formatted sql

--changeset AUDIT:001
--comment: creating drones audit table
CREATE TABLE IF NOT EXISTS DRONE_STATE_HISTORY
(
    ID                INTEGER       NOT NULL,
    DRONE_ID          INTEGER       NOT NULL,
    BATTERY_LEVEL     INTEGER       not null,
    DATE              TIMESTAMP     not null,
    CONSTRAINT PK_DRONE_STATE_HISTORY PRIMARY KEY (ID)
);
--rollback drop table if exists RONE_STATE_HISTORY;


--changeset AUDIT:002
--comment: sequence for drone_state_history PK
CREATE SEQUENCE IF NOT EXISTS DRONES_HISTORY_SEQUENCE start with 1 increment by 1;
--rollback DROP SEQUENCE IF EXISTS DRONES_HISTORY_SEQUENCE;