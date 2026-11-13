CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username)
) ENGINE = InnoDB;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB;

CREATE TABLE projects (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    creation_date DATE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE project_members (
    id BIGINT NOT NULL AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_project_members_project_user UNIQUE (project_id, user_id),
    CONSTRAINT fk_project_members_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_project_members_user FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB;

CREATE TABLE product_backlogs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    project_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_backlogs_project UNIQUE (project_id),
    CONSTRAINT fk_product_backlogs_project FOREIGN KEY (project_id) REFERENCES projects (id)
) ENGINE = InnoDB;

CREATE TABLE epics (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    goal VARCHAR(500),
    creation_date DATE NOT NULL,
    target_date DATE,
    product_backlog_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_epics_product_backlog FOREIGN KEY (product_backlog_id) REFERENCES product_backlogs (id)
) ENGINE = InnoDB;

CREATE TABLE sprint_backlogs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    project_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_sprint_backlogs_project FOREIGN KEY (project_id) REFERENCES projects (id)
) ENGINE = InnoDB;

CREATE TABLE user_stories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    role VARCHAR(100),
    action VARCHAR(500),
    goal VARCHAR(500),
    priority VARCHAR(30) NOT NULL,
    creation_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL,
    critere_acceptation VARCHAR(500),
    epic_id BIGINT,
    product_backlog_id BIGINT NOT NULL,
    sprint_backlog_id BIGINT,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_stories_epic FOREIGN KEY (epic_id) REFERENCES epics (id),
    CONSTRAINT fk_user_stories_product_backlog FOREIGN KEY (product_backlog_id) REFERENCES product_backlogs (id),
    CONSTRAINT fk_user_stories_sprint_backlog FOREIGN KEY (sprint_backlog_id) REFERENCES sprint_backlogs (id)
) ENGINE = InnoDB;

CREATE TABLE tasks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(30) NOT NULL,
    user_story_id BIGINT,
    sprint_backlog_id BIGINT,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_user_story FOREIGN KEY (user_story_id) REFERENCES user_stories (id),
    CONSTRAINT fk_tasks_sprint_backlog FOREIGN KEY (sprint_backlog_id) REFERENCES sprint_backlogs (id)
) ENGINE = InnoDB;

CREATE INDEX idx_user_stories_product_backlog ON user_stories (product_backlog_id);
CREATE INDEX idx_user_stories_epic ON user_stories (epic_id);
CREATE INDEX idx_user_stories_sprint_backlog ON user_stories (sprint_backlog_id);
CREATE INDEX idx_tasks_user_story ON tasks (user_story_id);
CREATE INDEX idx_tasks_sprint_backlog ON tasks (sprint_backlog_id);
CREATE INDEX idx_sprint_backlogs_project ON sprint_backlogs (project_id);
CREATE INDEX idx_epics_product_backlog ON epics (product_backlog_id);
