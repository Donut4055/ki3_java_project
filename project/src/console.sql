
CREATE DATABASE recruitment_system;
USE recruitment_system;




CREATE TABLE technology (
                            technology_id INT PRIMARY KEY AUTO_INCREMENT,
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
                                      FOREIGN KEY (technologyId) REFERENCES technology(technology_id)
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
                                                 FOREIGN KEY (technologyId) REFERENCES technology(technology_id)
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
DROP PROCEDURE IF EXISTS sp_count_candidates$$
CREATE PROCEDURE sp_count_candidates()
BEGIN
    SELECT COUNT(*) AS total
    FROM candidate;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS sp_count_recruitment_positions$$
CREATE PROCEDURE sp_count_recruitment_positions()
BEGIN
    -- Đếm tất cả vị trí mà tên không kết thúc bằng "_deleted"
    SELECT COUNT(*) AS total
    FROM recruitment_position
    WHERE name NOT LIKE '%_deleted';
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS sp_count_technologies$$
CREATE PROCEDURE sp_count_technologies()
BEGIN
    SELECT COUNT(*) AS total
    FROM technology
    WHERE name NOT LIKE '%\_deleted';
END $$
DELIMITER ;

DELIMITER $$

-- 1. Đếm đơn đã nộp (destroyAt IS NULL)
DROP PROCEDURE IF EXISTS sp_count_submitted_applications$$
CREATE PROCEDURE sp_count_submitted_applications(
    IN p_candidateId INT
)
BEGIN
    SELECT COUNT(*) AS total
    FROM application
    WHERE candidateId = p_candidateId
      AND destroyAt IS NULL;
END $$

-- 2. Ứng viên phản hồi phỏng vấn
DROP PROCEDURE IF EXISTS sp_candidate_respond_interview$$
CREATE PROCEDURE sp_candidate_respond_interview(
    IN p_appId INT,
    IN p_response VARCHAR(20)
)
BEGIN
    UPDATE application
    SET interviewRequestResult = p_response
    WHERE id = p_appId;
END $$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS sp_count_active_positions$$
CREATE PROCEDURE sp_count_active_positions()
BEGIN
    SELECT COUNT(*) AS total
    FROM recruitment_position
    WHERE expiredDate >= CURDATE()
      AND name NOT LIKE '%\_deleted';
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_login_account(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_role     ENUM('ADMIN','USER')
)
BEGIN
    SELECT
        a.id,
        a.username,
        a.password,
        a.role,
        COALESCE(c.status, 'active') AS status
    FROM _account a
             LEFT JOIN candidate c
                       ON a.candidateId = c.id
    WHERE a.username = p_username
      AND a.password = p_password
      AND a.role     = p_role;
END $$
DELIMITER ;



CREATE PROCEDURE register_user(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255),
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_experience INT,
    IN p_gender VARCHAR(10),
    IN p_description TEXT,
    IN p_dob DATE,
    OUT p_new_candidate_id INT
)
BEGIN
    INSERT INTO candidate (name, email, phone, experience, gender, status, description, dob)
    VALUES (p_name, p_email, p_phone, p_experience, p_gender, 'active', p_description, p_dob); -- Sử dụng tham số dob

    SET p_new_candidate_id = LAST_INSERT_ID();

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

DELIMITER $$
CREATE PROCEDURE get_technologies(IN offset INT, IN pageLimit INT)
BEGIN
    SELECT * FROM technology LIMIT offset, pageLimit;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE add_technology(IN name VARCHAR(255))
BEGIN
    INSERT INTO technology (name) VALUES (name);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_technology(IN id_in INT, IN newName VARCHAR(255))
BEGIN
    UPDATE technology SET name = newName WHERE technology_id = id_in;
END $$
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE delete_technology(IN id_in INT)
BEGIN
    DECLARE techName VARCHAR(255);

    SELECT name INTO techName
    FROM technology
    WHERE technology_id = id_in;
    IF EXISTS (SELECT 1 FROM recruitment_position_technology WHERE technologyId = id_in) THEN
        UPDATE technology
        SET name = CONCAT(name, '_deleted')
        WHERE technology_id = id_in;
    ELSE
        DELETE FROM technology WHERE technology_id = id_in;
    END IF;
END $$
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE get_candidates(IN offset INT, IN pageLimit INT)
BEGIN
    SELECT * FROM candidate LIMIT offset, pageLimit;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE lock_unlock_account(
    IN p_candidateId INT,
    IN p_status VARCHAR(20)
)
BEGIN
    UPDATE candidate
    SET status = p_status
    WHERE id = p_candidateId;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE reset_password(
    IN p_candidateId INT,
    OUT p_new_password VARCHAR(255)
)
BEGIN
    SET p_new_password = CONCAT('Pass', FLOOR(1000 + (RAND() * 8999)));  -- Random mật khẩu
    UPDATE _account
    SET password = p_new_password
    WHERE candidateId = p_candidateId;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE search_candidate_by_name(
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT * FROM candidate WHERE name LIKE CONCAT('%', p_name, '%');
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE filter_candidates_by_experience(
    IN p_experience INT
)
BEGIN
    SELECT * FROM candidate WHERE experience >= p_experience;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE filter_candidates_by_age(
    IN p_age INT
)
BEGIN
    SELECT * FROM candidate WHERE TIMESTAMPDIFF(YEAR, dob, CURDATE()) >= p_age;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE filter_candidates_by_gender(
    IN p_gender VARCHAR(10)
)
BEGIN
    SELECT * FROM candidate WHERE gender = p_gender;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE filter_candidates_by_technology(
    IN p_technologyId INT
)
BEGIN
    SELECT c.*
    FROM candidate c
             JOIN candidate_technology ct ON c.id = ct.candidateId
    WHERE ct.technologyId = p_technologyId;
END $$

DELIMITER ;


DELIMITER $$

-- 1. Lấy vị trí tuyển dụng (bỏ qua tên kết thúc '_deleted')
CREATE PROCEDURE get_recruitment_positions(
    IN p_offset INT,
    IN p_limit INT
)
BEGIN
    SELECT *
    FROM recruitment_position
    WHERE name NOT LIKE '%\\_deleted'
    ORDER BY id
    LIMIT p_offset, p_limit;
END $$

-- 2. Thêm vị trí tuyển dụng và trả về ID mới
CREATE PROCEDURE add_recruitment_position(
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(12,2),
    IN p_maxSalary DECIMAL(12,2),
    IN p_minExperience INT,
    IN p_expiredDate DATE,
    OUT p_new_id INT
)
BEGIN
    INSERT INTO recruitment_position
    (name, description, minSalary, maxSalary, minExperience, createdDate, expiredDate)
    VALUES
        (p_name, p_description, p_minSalary, p_maxSalary, p_minExperience, CURDATE(), p_expiredDate);
    SET p_new_id = LAST_INSERT_ID();
END $$

-- 3. Link position với technology
CREATE PROCEDURE link_position_technology(
    IN p_positionId INT,
    IN p_technologyId INT
)
BEGIN
    INSERT INTO recruitment_position_technology
    (recruitmentPositionId, technologyId)
    VALUES
        (p_positionId, p_technologyId);
END $$

-- 4. Cập nhật vị trí tuyển dụng (và xóa liên kết cũ)
CREATE PROCEDURE update_recruitment_position(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    IN p_minSalary DECIMAL(12,2),
    IN p_maxSalary DECIMAL(12,2),
    IN p_minExperience INT,
    IN p_expiredDate DATE
)
BEGIN
    UPDATE recruitment_position
    SET name = p_name,
        description = p_description,
        minSalary = p_minSalary,
        maxSalary = p_maxSalary,
        minExperience = p_minExperience,
        expiredDate = p_expiredDate
    WHERE id = p_id;
    DELETE FROM recruitment_position_technology WHERE recruitmentPositionId = p_id;
END $$

-- 5. Xóa vị trí tuyển dụng hoặc đổi tên nếu có FK
CREATE PROCEDURE delete_recruitment_position(
    IN p_id INT
)
BEGIN
    DECLARE posName VARCHAR(100);
    SELECT name INTO posName FROM recruitment_position WHERE id = p_id;
    IF EXISTS (
        SELECT 1 FROM recruitment_position_technology
        WHERE recruitmentPositionId = p_id
    ) THEN
        UPDATE recruitment_position
        SET name = CONCAT(name, '_deleted')
        WHERE id = p_id;
    ELSE
        DELETE FROM recruitment_position WHERE id = p_id;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

-- 1. Cập nhật thông tin cá nhân ứng viên
CREATE PROCEDURE sp_update_candidate_profile(
    IN p_candidateId INT,
    IN p_name VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(20),
    IN p_gender VARCHAR(10),
    IN p_dob DATE,
    IN p_description TEXT
)
BEGIN
    CALL update_candidate_info(p_candidateId, p_name, p_email, p_phone, p_gender, 0, p_description, p_dob);
END $$
DELIMITER $$
-- 2. Đổi mật khẩu ứng viên
CREATE PROCEDURE sp_change_candidate_password(
    IN  p_candidateId   INT,
    IN  p_oldPassword   VARCHAR(255),
    IN  p_newPassword   VARCHAR(255),
    OUT p_success       TINYINT
)
BEGIN
    DECLARE cnt INT DEFAULT 0;
    -- Kiểm tra mật khẩu cũ
    SELECT COUNT(*) INTO cnt
    FROM _account
    WHERE candidateId = p_candidateId
      AND password    = p_oldPassword;

    IF cnt = 1 THEN
        -- Nếu đúng, cập nhật mật khẩu mới
        UPDATE _account
        SET password = p_newPassword
        WHERE candidateId = p_candidateId;
        SET p_success = 1;
    ELSE
        -- Nếu sai, trả về false
        SET p_success = 0;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

-- 1. Lấy danh sách đơn ứng tuyển đã apply của ứng viên (phân trang)
CREATE PROCEDURE sp_get_submitted_applications(
    IN p_candidateId INT,
    IN p_offset INT,
    IN p_limit INT
)
BEGIN
    SELECT *
    FROM application
    WHERE candidateId = p_candidateId
    ORDER BY createAt DESC
    LIMIT p_offset, p_limit;
END $$

-- 2. Xem chi tiết đơn ứng tuyển
CREATE PROCEDURE sp_get_application_details(
    IN p_applicationId INT
)
BEGIN
    SELECT * FROM application
    WHERE id = p_applicationId;
END $$

-- 3. Lấy danh sách vị trí tuyển dụng đang hoạt động (phân trang)
CREATE PROCEDURE sp_get_active_positions(
    IN p_offset INT,
    IN p_limit INT
)
BEGIN
    SELECT *
    FROM recruitment_position
    WHERE expiredDate >= CURDATE()
      AND name NOT LIKE '%\_deleted'
    ORDER BY createdDate DESC
    LIMIT p_offset, p_limit;
END $$

-- 4. Xem chi tiết vị trí tuyển dụng
CREATE PROCEDURE sp_get_position_details(
    IN p_positionId INT
)
BEGIN
    SELECT * FROM recruitment_position
    WHERE id = p_positionId;
END $$

-- 5. Ứng tuyển vị trí
CREATE PROCEDURE sp_submit_application(
    IN p_candidateId INT,
    IN p_positionId INT,
    IN p_cvUrl VARCHAR(255)
)
BEGIN
    INSERT INTO application (candidateId, recruitmentPositionId, cvUrl)
    VALUES (p_candidateId, p_positionId, p_cvUrl);
END $$

DELIMITER ;

DELIMITER $$

-- 1. Danh sách đơn ứng tuyển (chưa huỷ) phân trang
CREATE PROCEDURE sp_admin_list_applications(
    IN p_offset INT,
    IN p_limit  INT
)
BEGIN
    SELECT *
    FROM application
    WHERE destroyAt IS NULL
    ORDER BY createAt DESC
    LIMIT p_offset, p_limit;
END $$

-- 2. Lọc đơn theo progress
CREATE PROCEDURE sp_admin_filter_by_progress(
    IN p_progress VARCHAR(20),
    IN p_offset   INT,
    IN p_limit    INT
)
BEGIN
    SELECT *
    FROM application
    WHERE progress = p_progress
      AND destroyAt IS NULL
    ORDER BY createAt DESC
    LIMIT p_offset, p_limit;
END $$

-- 3. Lọc đơn theo interviewResult
CREATE PROCEDURE sp_admin_filter_by_result(
    IN p_result VARCHAR(50),
    IN p_offset INT,
    IN p_limit  INT
)
BEGIN
    SELECT *
    FROM application
    WHERE interviewResult = p_result
      AND destroyAt IS NULL
    ORDER BY createAt DESC
    LIMIT p_offset, p_limit;
END $$

-- 4. Huỷ đơn ứng tuyển
CREATE PROCEDURE sp_admin_cancel_application(
    IN p_appId INT,
    IN p_reason TEXT
)
BEGIN
    UPDATE application
    SET destroyAt     = NOW(),
        destroyReason = p_reason,
        progress      = 'done'
    WHERE id = p_appId;
END $$

-- 5. Xem đơn và chuyển pending->handling
CREATE PROCEDURE sp_admin_view_application(
    IN p_appId INT
)
BEGIN
    UPDATE application
    SET progress = CASE WHEN progress = 'pending' THEN 'handling' ELSE progress END
    WHERE id = p_appId;
    SELECT *
    FROM application
    WHERE id = p_appId;
END $$

-- 6. Chuyển sang interviewing
CREATE PROCEDURE sp_admin_set_interview(
    IN p_appId INT,
    IN p_link  VARCHAR(255),
    IN p_time  DATETIME
)
BEGIN
    UPDATE application
    SET interviewRequestDate = NOW(),
        interviewLink        = p_link,
        interviewTime        = p_time,
        progress             = 'interviewing'
    WHERE id = p_appId;
END $$

-- 7. Cập nhật kết quả phỏng vấn
CREATE PROCEDURE sp_admin_update_result(
    IN p_appId   INT,
    IN p_result  VARCHAR(50),
    IN p_note    TEXT
)
BEGIN
    UPDATE application
    SET interviewResult     = p_result,
        interviewResultNote = p_note,
        progress            = 'done'
    WHERE id = p_appId;
END $$

DELIMITER ;