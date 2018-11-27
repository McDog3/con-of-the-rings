--User: id (pk), username, email, password
--Role: id (pk), name
--UserRoles: id(pk), user_id (fk), role_id (fk)   //A user may have multiple roles [Unique constraint on user_id, role_id]
--Convention: id (pk), name, year   //startDate?, endDate?
--Package: id (pk), name     //More fields for the "package" bought? ie include_shirt, num_of_days, etc?
--Attendee: id (pk), user_id (fk), convention_id (fk), package_id (fk) [unique constraint on user_id, convention_id, package_id]



CREATE TABLE user (
  id INT AUTO_INCREMENT NOT NULL,
  username VARCHAR(20) NOT NULL,
  email VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE role (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE user_roles (
  id INT AUTO_INCREMENT NOT NULL,
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(user_id) REFERENCES user(id),
  FOREIGN KEY(role_id) REFERENCES role(id),
  CONSTRAINT UC_UserRole UNIQUE (user_id, role_id)
);

CREATE TABLE convention (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(100) NOT NULL,
  year INT NOT NULL,
  --startDate DATE NOT NULL,
  --endDate DATE NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE package (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE attendee (
  id INT AUTO_INCREMENT NOT NULL,
  user_id INT NOT NULL,
  convention_id INT NOT NULL,
  package_id INT NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(user_id) REFERENCES user(id),
  FOREIGN KEY(convention_id) REFERENCES convention(id),
  FOREIGN KEY(package_id) REFERENCES package(id),
  CONSTRAINT UC_Attendee UNIQUE (user_id, convention_id, package_id)
);
