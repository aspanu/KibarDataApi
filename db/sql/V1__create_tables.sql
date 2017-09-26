
CREATE TABLE user_activity
(
id INTEGER AUTO_INCREMENT UNIQUE ,
user_id INTEGER UNIQUE ,
activity_id INTEGER UNIQUE ,
creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
interaction_type VARCHAR(8000), # Enum
PRIMARY KEY (id)
);

CREATE TABLE activities
(
id INTEGER AUTO_INCREMENT UNIQUE ,
name VARCHAR(100),
creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
activity_type VARCHAR(100),
activity_date DATE,
description TEXT,
PRIMARY KEY (id)
);

CREATE TABLE users
(
id INTEGER AUTO_INCREMENT UNIQUE ,
firstName VARCHAR(100),
lastName VARCHAR(100),
email VARCHAR(1000),
google_sub_id VARCHAR(100),
company VARCHAR(1000),
position VARCHAR(100),
user_type VARCHAR(100), # Enum
creation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
last_update_time DATE,
PRIMARY KEY (id)
);

ALTER TABLE user_activity ADD FOREIGN KEY id_idxfk (activity_id) REFERENCES activities (id);
ALTER TABLE user_activity ADD FOREIGN KEY id_idxfk_1 (user_id) REFERENCES users (id);

