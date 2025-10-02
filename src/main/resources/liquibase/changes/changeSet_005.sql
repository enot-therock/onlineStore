-- liquibase formatted sql

-- changeSet enot:29.09.2025_05

ALTER TABLE image DROP COLUMN file_size;

DROP TABLE images;