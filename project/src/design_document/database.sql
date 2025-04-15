
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