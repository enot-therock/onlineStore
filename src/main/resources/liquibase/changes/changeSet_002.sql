-- liquibase formatted sql

-- changeSet enot:25.09.2025_02

ALTER TABLE users ADD COLUMN enabled BOOLEAN;