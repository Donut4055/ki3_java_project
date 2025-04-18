
CREATE DATABASE recruitment_system;
USE recruitment_system;




CREATE TABLE technology (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE candidate (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           phone VARCHAR(20),
                           experience INT DEFAULT 0,
                           gender VARCHAR(10) CHECK (gender IN ('Nam', 'Nữ')),
                           status VARCHAR(20) DEFAULT 'active',
                           description TEXT,
                           dob DATE
);

CREATE TABLE _account (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
                          candidateId INT,
                          FOREIGN KEY (candidateId) REFERENCES candidate(id)
);

CREATE TABLE candidate_technology (
                                      candidateId INT NOT NULL,
                                      technologyId INT NOT NULL,
                                      PRIMARY KEY (candidateId, technologyId),
                                      FOREIGN KEY (candidateId) REFERENCES candidate(id),
                                      FOREIGN KEY (technologyId) REFERENCES technology(id)
);


CREATE TABLE recruitment_position (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(100) NOT NULL,
                                      description TEXT,
                                      minSalary DECIMAL(12,2),
                                      maxSalary DECIMAL(12,2),
                                      minExperience INT DEFAULT 0,
                                      createdDate DATE DEFAULT (CURDATE()),
                                      expiredDate DATE NOT NULL
);

CREATE TABLE recruitment_position_technology (
                                                 recruitmentPositionId INT NOT NULL,
                                                 technologyId INT NOT NULL,
                                                 PRIMARY KEY (recruitmentPositionId, technologyId),
                                                 FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position(id),
                                                 FOREIGN KEY (technologyId) REFERENCES technology(id)
);


CREATE TABLE application (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             candidateId INT NOT NULL,
                             recruitmentPositionId INT NOT NULL,
                             cvUrl VARCHAR(255) NOT NULL,
                             progress ENUM('pending', 'handling', 'interviewing', 'done') NOT NULL DEFAULT 'pending',
                             interviewRequestDate DATETIME,
                             interviewRequestResult VARCHAR(100),
                             interviewLink VARCHAR(255),
                             interviewTime DATETIME,
                             interviewResult VARCHAR(50),
                             interviewResultNote TEXT,
                             destroyAt DATETIME,
                             createAt DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updateAt DATETIME ON UPDATE CURRENT_TIMESTAMP,
                             destroyReason TEXT,
                             FOREIGN KEY (candidateId) REFERENCES candidate(id),
                             FOREIGN KEY (recruitmentPositionId) REFERENCES recruitment_position(id)
);


DELIMITER $$
-- login
CREATE PROCEDURE sp_login_account (
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_role ENUM('ADMIN', 'USER')
)
BEGIN
    SELECT * FROM _account
    WHERE username = p_username
      AND password = p_password
      AND role = p_role;
END $$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE register_user(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender VARCHAR(10),
    IN p_description TEXT,
    IN p_dob DATE,  -- Đảm bảo tham số dob được khai báo đúng
    OUT p_new_candidate_id INT
)
BEGIN
    -- Thêm thông tin ứng viên vào bảng candidate
    INSERT INTO candidate (name, email, phone, experience, gender, status, description, dob)
    VALUES (p_name, p_email, p_phone, p_experience, p_gender, 'active', p_description, p_dob); -- Sử dụng tham số dob

    -- Lấy id của ứng viên vừa thêm vào bảng candidate
    SET p_new_candidate_id = LAST_INSERT_ID();

    -- Thêm tài khoản vào bảng _account và liên kết với ứng viên
    INSERT INTO _account (username, password, role, candidateId)
    VALUES (p_username, p_password, 'USER', p_new_candidate_id);

END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE update_candidate_info(
    IN p_candidateId INT,
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_gender VARCHAR(10),
    IN p_experience INT,
    IN p_description TEXT,
    IN p_dob DATE
)
BEGIN
    UPDATE candidate
    SET name = p_name,
        email = p_email,
        phone = p_phone,
        gender = p_gender,
        experience = p_experience,
        description = p_description,
        dob = p_dob
    WHERE id = p_candidateId;
END $$

DELIMITER ;



