DROP TABLE users;
DROP TABLE books;
DROP TABLE authors;
DROP TABLE authorISBN;
DROP TABLE overdueBooks;
DROP TABLE studentBook;
DROP TABLE studentFine;
DROP TABLE blockedStudents;

CREATE TABLE users (
    userId INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(30) NOT NULL,
    password VARCHAR (30) NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR (30) NOT NULL,
    role INT NOT NULL,
    PRIMARY KEY (userId)
);

INSERT INTO users (email, password, firstName, lastName, role)
VALUES
    ('rakhimovnavruz@gmail.com', '11111', 'Navruz', 'Rakhimov', 0),
    ('dostonbek@gmail.com', '22222', 'Doston', 'Dostonov', 2),
    ('magomedov@gmail.com', '33333', 'Magomed', 'Magomedov', 2),
    ('elonmusk@gmail.com', '44444', 'Elon', 'Musk', 2),
    ('ccovington@gmail.com', '22222', 'Colby', 'Covington', 2),
    ('dc@gmail.com', '11111', 'Daniel', 'Cormier', 1),
    ('jlawrence@gmail.com', '11111','Jennifer', 'Lawrence', 1);


CREATE TABLE books (
    isbn VARCHAR (20) NOT NULL,
    title VARCHAR (100) NOT NULL,
    editionNumber INT NOT NULL,
    copyright varchar (4) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (isbn)
);

INSERT INTO books (isbn, title, editionNumber, copyright, quantity)
VALUES
   ('0132151006','Internet & World Wide Web How to Program',5,'2012', 50),
   ('0133807800','Java How to Program',10,'2015', 30),
   ('0132575655','Java How to Program, Late Objects Version',10,'2015', 40),
   ('013299044X','C How to Program',7,'2013', 50),
   ('0132990601','Simply Visual Basic 2010',4,'2013', 70),
   ('0133406954','Visual Basic 2012 How to Program',6,'2014', 110),
   ('0133379337','Visual C# 2012 How to Program',5,'2014', 60),
   ('0136151574','Visual C++ How to Program',2,'2008', 80),
   ('0133378713','C++ How to Program',9,'2014', 90),
   ('0133764036','Android How to Program',2,'2015', 80),
   ('0133570924','Android for Programmers: An App.App-Driven Approach, Volume 1',2,'2014', 90),
   ('0132121360','Android for Programmers: An App.App-Driven Approach',1,'2012', 100);

CREATE TABLE authors (
   authorID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
   firstName VARCHAR (20) NOT NULL,
   lastName VARCHAR (30) NOT NULL,
   PRIMARY KEY (authorID)
);

INSERT INTO authors (firstName, lastName)
VALUES
   ('Paul','Deitel'),
   ('Harvey','Deitel'),
   ('Abbey','Deitel'),
   ('Dan','Quirk'),
   ('Michael','Morgano');


CREATE TABLE authorISBN (
   authorID INT NOT NULL,
   isbn VARCHAR (20) NOT NULL,
   FOREIGN KEY (authorID) REFERENCES authors (authorID),
   FOREIGN KEY (isbn) REFERENCES books (isbn)
);

INSERT INTO authorISBN (authorID,isbn)
VALUES
   (1,'0132151006'), (2,'0132151006'), (3,'0132151006'), (1,'0133807800'), (2,'0133807800'),
   (1,'0132575655'), (2,'0132575655'), (1,'013299044X'), (2,'013299044X'), (1,'0132990601'),
   (2,'0132990601'), (3,'0132990601'), (1,'0133406954'), (2,'0133406954'), (3,'0133406954'),
   (1,'0133379337'), (2,'0133379337'), (1,'0136151574'), (2,'0136151574'), (4,'0136151574'),
   (1,'0133378713'), (2,'0133378713'), (1,'0133764036'), (2,'0133764036'), (3,'0133764036'),
   (1,'0133570924'), (2,'0133570924'), (3,'0133570924'), (1,'0132121360'), (2,'0132121360'),
   (3,'0132121360'), (5,'0132121360');


CREATE TABLE overdueBooks (
    userId INT NOT NULL,
    isbn VARCHAR (20) NOT NULL,
    borrowedDate DATE NOT NULL,
    expiredDate DATE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (isbn) REFERENCES books (isbn)
);

INSERT INTO overdueBooks (userId, isbn, borrowedDate, expiredDate)
VALUES
    (2, '0132151006', '2020-12-11', '2021-12-25'),
    (3, '013299044X', '2020-12-06', '2020-12-20'),
    (3, '0136151574', '2020-12-01', '2020-12-15');


CREATE TABLE studentBook (
    userId INT NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    borrowedDate DATE NOT NULL,
    dueDate DATE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (isbn) REFERENCES books (isbn)
);

INSERT INTO studentBook (userId, isbn, borrowedDate, dueDate)
VALUES
    (2, '013299044X', '2020-12-30', '2021-01-13'),
    (3, '0133807800', '2020-12-28', '2021-01-11'),
    (3, '013299044X', '2020-12-06', '2020-12-20'),
    (3, '0136151574', '2020-12-01', '2020-12-15'),
    (5, '0133406954', '2021-01-05', '2021-01-19'),
    (4, '0133406954', '2021-01-09', '2021-01-23'),
    (2, '0132151006', '2020-12-01', '2020-12-15');


CREATE TABLE studentFine (
    userId INT NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    fine DOUBLE NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (userId),
    FOREIGN KEY (isbn) REFERENCES books (isbn)
);

INSERT INTO studentFine (userId, isbn, fine)
VALUES
    (2, '0132151006', 4.00),
    (3, '013299044X', 5.25),
    (3, '0136151574', 6.50);

CREATE TABLE blockedStudents (
    userId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES users (userId)
);

INSERT INTO blockedStudents(userId) VALUES (4);



