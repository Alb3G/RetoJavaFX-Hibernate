create database reto2;

use reto2;

CREATE TABLE User(
                     id Serial,
                     user_name varchar(150),
                     email varchar(255) unique,
                     password varchar(100),
                     is_admin boolean,
                     is_verified boolean,
                     created_at datetime
);

CREATE TABLE Movie(
                      id SERIAL,
                      title text,
                      genre varchar(50),
                      release_year int,
                      description text,
                      director varchar(100),
                      teaser_url text,
                      poster varchar(200),
                      UNIQUE (title(255))
);

CREATE TABLE MovieCopy(
                          copy_id serial,
                          movie_id int,
                          user_id int,
                          movie_condition enum('Excellent', 'Good', 'Bad') not null,
                          platform enum('DVD', 'BluRay', 'VHS') default 'DVD'
);

CREATE TABLE SessionToken(
                             session_token_id Serial,
                             user_id int,
                             token text,
                             time_stamp datetime
);

-- Insertar los 3 usuarios
INSERT INTO User (user_name, email, password, is_admin, is_verified, created_at) VALUES
                                                                                     ('User2','user2@gmail.com', '$2a$12$Wg9eME0GtCliFnndYos0FuL16fSXhDp1ZHn7KrpfLT/dP4XAQ1IYK', true, true, '2024-11-14T10:27:48.219590'), -- root2
                                                                                     ('User3','user3@gmail.com', '$2a$12$KWAnzW2BDPAfP9braSxZD.mAYTzMbgHAAPewcjLGgjBg2YZ.VPhBG', false, true, '2024-11-14T10:27:48.219590'); -- root3

-- Insertar 25 películas
INSERT INTO Movie (title, genre, release_year, description, director, teaser_url, poster) VALUES
                                                                                              ('The Shawshank Redemption', 'Drama', 1994, 'Two imprisoned men bond over a number of years.', 'Frank Darabont', 'https://www.youtube.com/embed/PLl99DlL6b4?fs=1', 'ShawShank.jpg'),
                                                                                              ('The Godfather', 'Crime', 1972, 'The aging patriarch of an organized crime dynasty.', 'Francis Ford Coppola', 'https://www.youtube.com/embed/UaVTIH8mujA?fs=1', 'Godfather.jpg'),
                                                                                              ('The Dark Knight', 'Action', 2008, 'When the menace known as the Joker emerges.', 'Christopher Nolan', 'https://www.youtube.com/embed/EXeTwQWrcwY?fs=1', 'DarkKnight.jpg'),
                                                                                              ('Pulp Fiction', 'Crime', 1994, 'The lives of two mob hitmen, a boxer, a gangster\'s wife.', 'Quentin Tarantino', 'https://www.youtube.com/embed/tGpTpVyI_OQ?fs=1', 'PulpFiction.jpg'),
                                                                                              ('The Lord of the Rings: The Fellowship of the Ring', 'Fantasy', 2001, 'A meek Hobbit sets out on a journey.', 'Peter Jackson', 'https://www.youtube.com/embed/V75dMMIW2B4?fs=1', 'LordOfRings.jpg'),
                                                                                              ('Forrest Gump', 'Drama', 1994, 'The presidencies of Kennedy and Johnson, Vietnam, etc.', 'Robert Zemeckis', 'https://www.youtube.com/embed/bLvqoHBptjg?fs=1', 'ForrestGump.jpg'),
                                                                                              ('Inception', 'Sci-Fi', 2010, 'A thief who enters the dreams of others.', 'Christopher Nolan', 'https://www.youtube.com/embed/YoHD9XEInc0?fs=1', 'Inception.jpg'),
                                                                                              ('Fight Club', 'Drama', 1999, 'An insomniac office worker forms an underground fight club.', 'David Fincher', 'https://www.youtube.com/embed/BdJKm16Co6M?fs=1', 'FightClub.jpg'),
                                                                                              ('The Matrix', 'Sci-Fi', 1999, 'A computer hacker learns about the true nature of his reality.', 'The Wachowskis', 'https://www.youtube.com/embed/vKQi3bBA1y8?fs=1', 'Matrix.jpg'),
                                                                                              ('Goodfellas', 'Crime', 1990, 'The story of Henry Hill and his life in the mob.', 'Martin Scorsese', 'https://www.youtube.com/embed/2ilzidi_J8Q?fs=1', 'Goodfellas.jpg'),
                                                                                              ('Se7en', 'Thriller', 1995, 'Two detectives hunt a serial killer.', 'David Fincher', 'https://www.youtube.com/embed/znmZoVkCjpI?fs=1', 'Se7en.jpg'),
                                                                                              ('Gladiator', 'Action', 2000, 'A former Roman general seeks revenge.', 'Ridley Scott', 'https://www.youtube.com/embed/P5ieIbInFpg?fs=1', 'Gladiator.jpg'),
                                                                                              ('Saving Private Ryan', 'War', 1998, 'Following the Normandy landings, a group of soldiers.', 'Steven Spielberg', 'https://www.youtube.com/embed/9CiW_DgxCnQ?fs=1', 'PrivateRyan.jpg'),
                                                                                              ('The Silence of the Lambs', 'Thriller', 1991, 'A young FBI agent seeks the help of an imprisoned Dr. Hannibal Lecter.', 'Jonathan Demme', 'https://www.youtube.com/embed/6iB21hsprAQ?fs=1', 'SilenceLambs.jpg'),
                                                                                              ('The Green Mile', 'Drama', 1999, 'The lives of guards on Death Row are affected by one of their charges.', 'Frank Darabont', 'https://www.youtube.com/embed/Ki4haFrqSrw?fs=1', 'GreenMile.jpg'),
                                                                                              ('Schindler\'s List', 'Biography', 1993, 'In German-occupied Poland during World War II, Oskar Schindler.', 'Steven Spielberg', 'https://www.youtube.com/embed/gG22XNhtnoY?fs=1', 'Schidler.jpg'),
                                                                                              ('Interstellar', 'Sci-Fi', 2014, 'A team of explorers travel through a wormhole in space.', 'Christopher Nolan', 'https://www.youtube.com/embed/2LqzF5WauAw?fs=1', 'Interstellar.jpg'),
                                                                                              ('Braveheart', 'Drama', 1995, 'Scottish warrior William Wallace leads his countrymen.', 'Mel Gibson', 'https://www.youtube.com/embed/nMft5QDOHek?fs=1', 'Braveheart.jpg'),
                                                                                              ('The Departed', 'Crime', 2006, 'An undercover cop and a mole in the police try to identify each other.', 'Martin Scorsese', 'https://www.youtube.com/embed/r-MiSNsCdQ4?fs=1', 'Departed.jpg'),
                                                                                              ('Whiplash', 'Drama', 2014, 'A promising young drummer enrolls at a cut-throat music conservatory.', 'Damien Chazelle', 'https://www.youtube.com/embed/7d_jQycdQGo?fs=1', 'Whiplash.jpg'),
                                                                                              ('The Prestige', 'Drama', 2006, 'Two stage magicians engage in a competitive rivalry.', 'Christopher Nolan', 'https://www.youtube.com/embed/ELq7V8vkekI?fs=1', 'ThePrestige.jpg'),
                                                                                              ('The Social Network', 'Biography', 2010, 'The story of the founders of Facebook.', 'David Fincher', 'https://www.youtube.com/embed/lB95KLmpLR4?fs=1', 'SocialNetwork.jpg'),
                                                                                              ('Django Unchained', 'Western', 2012, 'With the help of a German bounty hunter, a freed slave.', 'Quentin Tarantino', 'https://www.youtube.com/embed/0fUCuvNlOCg?fs=1', 'Django.jpg'),
                                                                                              ('The Lion King', 'Animation', 1994, 'Lion cub Simba is forced to flee into the wilderness.', 'Roger Allers, Rob Minkoff', 'https://www.youtube.com/embed/lFzVJEksoDY?fs=1', 'Lionking.jpg'),
                                                                                              ('The Usual Suspects', 'Mystery', 1995, 'A sole survivor tells of the twists in an intricate plot.', 'Bryan Singer', 'https://www.youtube.com/embed/x3t0Nc6fg7w?fs=1', 'UsualSuspects.jpg');

-- Insertar copias de películas para los usuarios
INSERT INTO MovieCopy (movie_id, user_id, movie_condition, platform) VALUES
-- Usuario 1
(1, 1, 'Excellent', 'DVD'),
(2, 1, 'Good', 'BluRay'),
(3, 1, 'Bad', 'VHS'),
(4, 1, 'Good', 'DVD'),
(5, 1, 'Excellent', 'BluRay'),
(1, 1, 'Good', 'BluRay'),  -- Película repetida con diferente condición
-- Usuario 2
(6, 2, 'Good', 'DVD'),
(7, 2, 'Excellent', 'BluRay'),
(8, 2, 'Bad', 'VHS'),
(9, 2, 'Good', 'DVD'),
(10, 2, 'Excellent', 'BluRay'),
(6, 2, 'Bad', 'VHS'),  -- Película repetida con diferente condición

-- Copias adicionales para completar las 25 películas
(16, 1, 'Good', 'DVD'),
(17, 2, 'Bad', 'VHS'),
(19, 1, 'Good', 'DVD'),
(20, 2, 'Excellent', 'BluRay'),
(22, 1, 'Bad', 'DVD'),
(23, 2, 'Excellent', 'BluRay'),
(25, 1, 'Bad', 'DVD');

# drop database reto2;