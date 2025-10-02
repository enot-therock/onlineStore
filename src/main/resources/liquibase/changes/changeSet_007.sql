-- liquibase formatted sql

-- changeSet enot:01.10.2025_06

ALTER TABLE image
ADD COLUMN media_type VARCHAR(255),
ADD COLUMN file_size BIGINT;