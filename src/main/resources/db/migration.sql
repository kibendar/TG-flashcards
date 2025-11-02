-- Migration script for Telegram Flashcards Bot Database
-- Database: telegram_flashcards_bot
-- Description: Creates all necessary tables and constraints for the flashcard learning system

-- Drop existing tables if they exist (in correct order to handle foreign key constraints)
DROP TABLE IF EXISTS flashcard_repetition_list CASCADE;
DROP TABLE IF EXISTS flashcard_education_list CASCADE;
DROP TABLE IF EXISTS flashcard CASCADE;
DROP TABLE IF EXISTS flashcard_package CASCADE;
DROP TABLE IF EXISTS account CASCADE;

-- Create account table (User entity)
CREATE TABLE account (
    id BIGINT PRIMARY KEY,
    current_flashcard BIGINT,
    start_study_time TIMESTAMP,
    end_study_time TIMESTAMP,
    hard_card BIGINT DEFAULT 0,
    hardest_card BIGINT DEFAULT 0
);

-- Create flashcard_package table
CREATE TABLE flashcard_package (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    user_id BIGINT,
    CONSTRAINT fk_package_user FOREIGN KEY (user_id)
        REFERENCES account(id) ON DELETE CASCADE
);

-- Create flashcard table
CREATE TABLE flashcard (
    id BIGSERIAL PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    package_id BIGINT,
    CONSTRAINT fk_flashcard_package FOREIGN KEY (package_id)
        REFERENCES flashcard_package(id) ON DELETE CASCADE
);

-- Create flashcard_education_list table (composite primary key)
CREATE TABLE flashcard_education_list (
    id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    flashcard_id BIGINT,
    PRIMARY KEY (id, user_id),
    CONSTRAINT fk_education_user FOREIGN KEY (user_id)
        REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT fk_education_flashcard FOREIGN KEY (flashcard_id)
        REFERENCES flashcard(id) ON DELETE CASCADE
);

-- Create flashcard_repetition_list table (composite primary key)
CREATE TABLE flashcard_repetition_list (
    id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    flashcard_id BIGINT,
    PRIMARY KEY (id, user_id),
    CONSTRAINT fk_repetition_user FOREIGN KEY (user_id)
        REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT fk_repetition_flashcard FOREIGN KEY (flashcard_id)
        REFERENCES flashcard(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_flashcard_package_user ON flashcard_package(user_id);
CREATE INDEX idx_flashcard_package ON flashcard(package_id);
CREATE INDEX idx_education_user ON flashcard_education_list(user_id);
CREATE INDEX idx_education_flashcard ON flashcard_education_list(flashcard_id);
CREATE INDEX idx_repetition_user ON flashcard_repetition_list(user_id);
CREATE INDEX idx_repetition_flashcard ON flashcard_repetition_list(flashcard_id);

-- Comments for documentation
COMMENT ON TABLE account IS 'Stores Telegram bot users and their learning session data';
COMMENT ON TABLE flashcard_package IS 'Collections of flashcards grouped by topic';
COMMENT ON TABLE flashcard IS 'Individual flashcards with questions and answers';
COMMENT ON TABLE flashcard_education_list IS 'Temporary list of flashcards in user learning queue';
COMMENT ON TABLE flashcard_repetition_list IS 'Temporary list of flashcards marked for repetition';

COMMENT ON COLUMN account.id IS 'Telegram chat ID of the user';
COMMENT ON COLUMN account.current_flashcard IS 'Current position in learning session';
COMMENT ON COLUMN account.hard_card IS 'Count of cards rated as hard (25-50% difficulty)';
COMMENT ON COLUMN account.hardest_card IS 'Count of cards rated as hardest (0-25% difficulty)';
