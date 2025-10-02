-- liquibase formatted sql

-- changeSet enot:29.09.2025_04


CREATE TABLE image (
id SERIAL PRIMARY KEY,
fileName VARCHAR,
fileSize SERIAL,
mediaType VARCHAR,
filePath VARCHAR,
user_id BEGIN,
    FOREIGN KEY (user_id) REFERENCES (id),
advertisement_id BEGIN,
    FOREIGN KEY (advertisement_id) REFERENCES (id)
);


ALTER TABLE users DROP COLUMN image;
ALTER TABLE advertisement DROP COLUMN image;

ALTER TABLE users ADD COLUMN image_id BEGIN;
ALTER TABLE advertisement ADD COLUMN image_id BEGIN;
ry