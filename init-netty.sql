CREATE DATABASE IF NOT EXISTS netty;

USE netty;

-- 删除的时候，必须先删除有外键约束的表

-- 联系请求表
DROP TABLE IF EXISTS contactAsks;
-- 联系人表
DROP TABLE IF EXISTS contacts;
-- 消息表
DROP TABLE IF EXISTS messages;
-- 群成员表
DROP TABLE IF EXISTS group2user;
-- 群消息表
DROP TABLE IF EXISTS groupMessages;

-- 用户信息表
DROP TABLE IF EXISTS users;
-- 群信息表
DROP TABLE IF EXISTS groups;


CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    pwd VARCHAR(20) NOT NULL,
    register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    online BOOL NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入初始记录
INSERT INTO users (name, pwd, online) VALUES ('姜明贤', 'jmx', FALSE);
INSERT INTO users (name, pwd, online) VALUES ('姜爸爸', 'jbb', FALSE);
INSERT INTO users (name, pwd, online) VALUES ('姜妈妈', 'jmm', FALSE);
INSERT INTO users (name, pwd, online) VALUES ('何凡', 'hf', FALSE);
INSERT INTO users (name, pwd, online) VALUES ('何爸爸', 'hbb', FALSE);
INSERT INTO users (name, pwd, online) VALUES ('何妈妈', 'hmm', FALSE);


CREATE TABLE contactAsks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    ask_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ask_content VARCHAR(100) NOT NULL,
    valid BOOL NOT NULL DEFAULT TRUE,
    FOREIGN KEY(user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    FOREIGN KEY(contact_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE contacts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    confirm_time TIMESTAMP NULL,
    FOREIGN KEY(user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    FOREIGN KEY(contact_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE messages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    send_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    FOREIGN KEY(contact_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 群相关

CREATE TABLE groups (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE group2user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    join_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(group_id) REFERENCES groups(id)
    ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE groupMessages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    send_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(group_id) REFERENCES groups(id)
    ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES users(id)
    ON DELETE CASCADE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


SELECT 'ok' AS 'result';