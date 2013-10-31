
# --- !Ups

CREATE TABLE owner (
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	PRIMARY KEY(email)
);

ALTER TABLE task ADD user varchar(255);
ALTER TABLE task ADD FOREIGN KEY (user) REFERENCES owner(email);

# --- !Downs

DROP TABLE owner;
ALTER TABLE task DROP FOREIGN KEY user;
ALTER TABLE task DROP user;
