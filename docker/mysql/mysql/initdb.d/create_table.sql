CREATE DATABASE IF NOT EXISTS lecture;

USE lecture;

CREATE TABLE lectures (
                          lecture_id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          applied_quantity INT NOT NULL,
                          total_quantity INT,
                          date_applied_start DATETIME(6) NOT NULL,
                          created_at DATETIME(6),
                          updated_at DATETIME(6),
                          PRIMARY KEY (lecture_id)
) ENGINE=InnoDB;

CREATE TABLE lecture_histories (
                                   history_id BIGINT NOT NULL AUTO_INCREMENT,
                                   user_id BIGINT NOT NULL,
                                   lecture_id BIGINT,
                                   date_applied DATETIME(6) NOT NULL,
                                   created_at DATETIME(6),
                                   updated_at DATETIME(6),
                                   PRIMARY KEY (history_id),
                                   FOREIGN KEY (lecture_id) REFERENCES lectures (lecture_id)
) ENGINE=InnoDB;