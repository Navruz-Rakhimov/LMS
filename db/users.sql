DROP TABLE Users;

CREATE TABLE Users (
    UserId INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    Username VARCHAR(30) NOT NULL,
    Password VARCHAR (30) NOT NULL,
    Email VARCHAR(30) NOT NULL,
    Role INT NOT NULL,
    PRIMARY KEY (Username)
);


INSERT INTO Users (Username, Password, Email, Role) VALUES  ('navruz', '12345', 'rakhimovnavruz@gmail.com', 0);

