CREATE TABLE IF NOT EXISTS PUBLIC.USER_DATA (
                                  EMAIL CHARACTER VARYING NOT NULL,
                                  LOGIN CHARACTER VARYING NOT NULL,
                                  NAME CHARACTER VARYING NOT NULL,
                                  BIRTHDAY TIMESTAMP NOT NULL,
                                  USER_ID INTEGER NOT NULL AUTO_INCREMENT,
                                  CONSTRAINT USER_PK PRIMARY KEY (USER_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_2 ON PUBLIC.USER_DATA (USER_ID);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM (
                             GENRE_ID INTEGER,
                             MPA_ID INTEGER NOT NULL,
                             NAME CHARACTER VARYING NOT NULL,
                             DESCRIPTION CHARACTER VARYING NOT NULL,
                             RELEASE_DATE TIMESTAMP NOT NULL,
                             DURATION INTEGER NOT NULL,
                             USER_ID INTEGER,
                             FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
                             CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_20 ON PUBLIC.FILM (FILM_ID);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
                              GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
                              GENRE_NAME CHARACTER VARYING NOT NULL,
                              CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_5 ON PUBLIC.GENRE (GENRE_ID);

CREATE TABLE IF NOT EXISTS PUBLIC.MPA (
                            MPA_ID INTEGER NOT NULL AUTO_INCREMENT,
                            MPA_NAME CHARACTER VARYING,
                            CONSTRAINT MPA_PK PRIMARY KEY (MPA_ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_1 ON PUBLIC.MPA (MPA_ID);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIEND (
                               ID INTEGER NOT NULL AUTO_INCREMENT,
                               USER_ID INTEGER,
                               FRIEND_ID INTEGER,
                               CONSTRAINT FRIEND_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_7 ON PUBLIC.FRIEND (ID);