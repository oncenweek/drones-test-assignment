--liquibase formatted sql

--changeset INIT:001
--comment: creating drones table
CREATE TABLE IF NOT EXISTS DRONES
(
    ID                INTEGER       NOT NULL,
    SERIAL_NUMBER     varchar(100)  not null,
    MODEL             varchar(32)   not null,
    WEIGHT_LIMIT      INTEGER       not null,
    BATTERY_CAPACITY  INTEGER       not null,
    STATE             varchar(32)   not null,
    CONSTRAINT PK_DRONES PRIMARY KEY (ID),
    CONSTRAINT DRONE_SERIAL_UNIQUE UNIQUE (SERIAL_NUMBER)
);
--rollback drop table if exists DRONES;


--changeset INIT:002
--comment: creating medications table
CREATE TABLE IF NOT EXISTS MEDICATIONS
(
    ID     INTEGER       NOT NULL,
    NAME   varchar(512)  not null,
    WEIGHT INTEGER       not null,
    CODE   varchar(32)   not null,
    IMAGE  varchar(1024) not null,
    CONSTRAINT PK_MEDICATIONS PRIMARY KEY (ID)
);
--rollback drop table if exists MEDICATIONS;


--changeset INIT:003
--comment: creating drone_medications table
CREATE TABLE IF NOT EXISTS DRONE_MEDICATIONS
(
    DRONE_ID       INTEGER NOT NULL,
    MEDICATION_ID  INTEGER NOT NULL,

    foreign key (DRONE_ID) references DRONES(ID),
    foreign key (MEDICATION_ID) references MEDICATIONS(ID),
    CONSTRAINT DRONE_MEDICATION_UNIQUE UNIQUE (DRONE_ID, MEDICATION_ID)
);
--rollback drop table if exists DRONE_MEDICATIONS;


--changeset INIT:004
--comment: sequence for drones PK
CREATE SEQUENCE IF NOT EXISTS DRONES_SEQUENCE start with 1 increment by 1;
--rollback DROP SEQUENCE IF EXISTS DRONES_SEQUENCE;

--changeset INIT:005
--comment: sequence for medications PK
CREATE SEQUENCE IF NOT EXISTS MEDICATIONS_SEQUENCE start with 1 increment by 1;
--rollback DROP SEQUENCE IF EXISTS MEDICATIONS_SEQUENCE;