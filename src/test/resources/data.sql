DELETE FROM USERS;
DELETE FROM FILMS;
INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES ( 'user1@mail.ru', 'user1', 'userName1', '1970-05-01' ),
       ( 'user2@mail.ru', 'user2', 'userName2', '1970-05-02' ),
       ( 'user3@mail.ru', 'user3', 'userName3', '1970-05-03' );

INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
VALUES ( 'film1', 'description1', '2010-05-01', '120', '1' ),
       ( 'film2', 'description2', '2010-05-02', '90', '2' ),
       ( 'film3', 'description3', '2010-05-03', '110', '3' );


