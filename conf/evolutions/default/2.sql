
# --- !Ups

CREATE TABLE owner (
	email varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	PRIMARY KEY(email)
);

ALTER TABLE task ADD usuario varchar(255);
ALTER TABLE task ADD FOREIGN KEY (usuario) REFERENCES owner(email);

# --- !Downs

DROP TABLE owner;
ALTER TABLE task DROP FOREIGN KEY usuario;
ALTER TABLE task DROP usuario;
