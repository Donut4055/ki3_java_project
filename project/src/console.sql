
CREATE DATABASE recruitment_system;
USE recruitment_system;


CREATE TABLE _account (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

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

-- Lấy danh sách công nghệ tuyển dụng
CREATE PROCEDURE sp_get_active_technologies()
BEGIN
    SELECT * FROM technology
    WHERE name NOT LIKE '%\_deleted' ESCAPE '\\';
END;


-- Thêm công nghệ
CREATE PROCEDURE sp_add_technology(IN p_name VARCHAR(100))
BEGIN
    IF EXISTS (SELECT 1 FROM technology WHERE name = p_name) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Technology name already exists';
    ELSE
        INSERT INTO technology(name) VALUES(p_name);
    END IF;
END$$

-- Xóa công nghệ
CREATE PROCEDURE sp_delete_technology(IN p_id INT)
BEGIN
    DECLARE v_name VARCHAR(100);

    SELECT name INTO v_name FROM technology WHERE id = p_id;

    IF EXISTS (
        SELECT 1 FROM candidate_technology WHERE technologyId = p_id
        UNION
        SELECT 1 FROM recruitment_position_technology WHERE technologyId = p_id
    ) THEN
        UPDATE technology SET name = CONCAT(v_name, '_deleted') WHERE id = p_id;
    ELSE
        DELETE FROM technology WHERE id = p_id;
    END IF;
END$$

-- Sửa công nghệ
CREATE PROCEDURE sp_update_technology(IN p_id INT, IN p_new_name VARCHAR(100))
BEGIN
    IF EXISTS (SELECT 1 FROM technology WHERE name = p_new_name AND id != p_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Technology name already exists';
    ELSE
        UPDATE technology SET name = p_new_name WHERE id = p_id;
    END IF;
END$$

-- Hiển thị danh sách ứng viên
CREATE PROCEDURE sp_get_candidates()
BEGIN
    SELECT * FROM candidate;
END;

--  Khóa / mở khóa tài khoản ứng viên
CREATE PROCEDURE sp_toggle_candidate_status(IN p_id INT)
BEGIN
    UPDATE candidate
    SET status = IF(status = 'active', 'inactive', 'active')
    WHERE id = p_id;
END;

-- Reset mật khẩu ứng viên
CREATE PROCEDURE sp_reset_password(IN p_account_id INT, OUT p_new_password VARCHAR(100))
BEGIN
    SET p_new_password = SUBSTRING(MD5(RAND()), 1, 10); -- 10 ký tự ngẫu nhiên
    UPDATE _account SET password = p_new_password WHERE id = p_account_id;
END$$

-- Tìm kiếm ứng viên theo tên
CREATE PROCEDURE sp_search_candidates_by_name(IN p_keyword VARCHAR(100))
BEGIN
    SELECT * FROM candidate
    WHERE name LIKE CONCAT('%', p_keyword, '%');
END;

-- Lọc ứng viên theo kinh nghiệm
CREATE PROCEDURE sp_filter_candidates_by_experience(IN p_min INT, IN p_max INT)
BEGIN
    SELECT * FROM candidate
    WHERE experience BETWEEN p_min AND p_max;
END;

-- Lọc ứng viên theo tuổi
CREATE PROCEDURE sp_filter_candidates_by_age(IN p_min_age INT, IN p_max_age INT)
BEGIN
    SELECT *, TIMESTAMPDIFF(YEAR, dob, CURDATE()) AS age
    FROM candidate
    WHERE TIMESTAMPDIFF(YEAR, dob, CURDATE()) BETWEEN p_min_age AND p_max_age;
END;

-- Lọc ứng viên theo giới tính
CREATE PROCEDURE sp_filter_candidates_by_gender(IN p_gender VARCHAR(10))
BEGIN
    SELECT * FROM candidate
    WHERE gender = p_gender;
END;

-- Lọc ứng viên theo công nghệ
CREATE PROCEDURE sp_filter_candidates_by_technology(IN p_tech_id INT)
BEGIN
    SELECT c.*
    FROM candidate c
             JOIN candidate_technology ct ON c.id = ct.candidateId
    WHERE ct.technologyId = p_tech_id;
END;

-- Thêm vị trí tuyển dụng + nhiều công nghệ
CREATE PROCEDURE sp_add_position_with_technologies (
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(12,2),
    IN p_maxSalary DECIMAL(12,2),
    IN p_minExp INT,
    IN p_expiredDate DATE,
    IN p_technology_ids TEXT
)
BEGIN
    DECLARE v_pos_id INT;
    DECLARE v_tech_id INT;
    DECLARE v_pos INT DEFAULT 1;
    DECLARE v_comma_pos INT;
    DECLARE v_id_str VARCHAR(10);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
        END;

    START TRANSACTION;

    INSERT INTO recruitment_position (name, description, minSalary, maxSalary, minExperience, expiredDate)
    VALUES (p_name, p_description, p_minSalary, p_maxSalary, p_minExp, p_expiredDate);

    SET v_pos_id = LAST_INSERT_ID();

    WHILE v_pos <= CHAR_LENGTH(p_technology_ids) DO
            SET v_comma_pos = LOCATE(',', p_technology_ids, v_pos);
            IF v_comma_pos = 0 THEN
                SET v_comma_pos = CHAR_LENGTH(p_technology_ids) + 1;
            END IF;
            SET v_id_str = SUBSTRING(p_technology_ids, v_pos, v_comma_pos - v_pos);
            SET v_tech_id = CAST(v_id_str AS UNSIGNED);
            INSERT INTO recruitment_position_technology VALUES (v_pos_id, v_tech_id);
            SET v_pos = v_comma_pos + 1;
        END WHILE;

    COMMIT;
END$$

-- Xoá vị trí tuyển dụng
CREATE PROCEDURE sp_delete_position(IN p_id INT)
BEGIN
    DECLARE v_name VARCHAR(100);

    SELECT name INTO v_name FROM recruitment_position WHERE id = p_id;

    IF EXISTS (
        SELECT 1 FROM application WHERE recruitmentPositionId = p_id
        UNION
        SELECT 1 FROM recruitment_position_technology WHERE recruitmentPositionId = p_id
    ) THEN
        UPDATE recruitment_position SET name = CONCAT(v_name, '_deleted') WHERE id = p_id;
    ELSE
        DELETE FROM recruitment_position WHERE id = p_id;
    END IF;
END$$

-- Lấy danh sách vị trí tuyển dụng
CREATE PROCEDURE sp_get_active_positions()
BEGIN
    SELECT * FROM recruitment_position
    WHERE name NOT LIKE '%\_deleted' ESCAPE '\\';
END;

--  Cập nhật vị trí tuyển dụng
CREATE PROCEDURE sp_update_position (
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(12,2),
    IN p_maxSalary DECIMAL(12,2),
    IN p_minExp INT,
    IN p_expiredDate DATE
)
BEGIN
    UPDATE recruitment_position
    SET name = p_name,
        description = p_description,
        minSalary = p_minSalary,
        maxSalary = p_maxSalary,
        minExperience = p_minExp,
        expiredDate = p_expiredDate
    WHERE id = p_id;
END$$
DELIMITER ;