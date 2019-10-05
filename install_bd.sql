
CREATE TABLE users(
    id number(11) NOT NULL,
    uname varchar(50) NOT NULL,
    pwd varchar(50) NOT NULL,
    locking number(1) NOT NULL,
    PRIMARY KEY(id)
);

CREATE SEQUENCE users_seq;

CREATE TABLE stories(
  id number(11) NOT NULL,
  author number(11) NOT NULL,
  isOpen number(1) NOT NULL,
  isPublic number(1) NOT NULL,
  title varchar(500) NOT NULL,
  locked number(1) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_users_stories
    FOREIGN KEY (author)
    REFERENCES users(id)
);



CREATE SEQUENCE story_seq START WITH 5 INCREMENT BY 1;
CREATE SEQUENCE history_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE paragraphs( 
    storyId number(11) NOT NULL,
    id number(11) NOT NULL,
    paragraphText varchar(4000) NOT NULL,
    author number(11) NOT NULL,
    isConclusive number(1) NOT NULL,
    PRIMARY KEY(storyId, id),
  CONSTRAINT fk_users_paragraphs
    FOREIGN KEY (author)
    REFERENCES users(id),
  CONSTRAINT fk_stories_paragraphs
    FOREIGN KEY (storyId)
    REFERENCES stories(id)
);



CREATE TABLE choices(
    storyId number(11) NOT NULL,
    sourceId number(11) NOT NULL,
    destinationId number(11),
    choice varchar(500) NOT NULL,
    choiceId number(11) NOT NULL,
    locked number(1) DEFAULT (0),
    PRIMARY KEY (storyId, sourceId, choiceId),
  CONSTRAINT fk_stories_choices
    FOREIGN KEY (storyId)
    REFERENCES stories(id),
  CONSTRAINT fk_source_choices
    FOREIGN KEY (storyId, sourceId)
    REFERENCES paragraphs(storyId, id),
  CONSTRAINT fk_destination_choices
    FOREIGN KEY (storyId, destinationId)
    REFERENCES paragraphs(storyId, id)
);



CREATE TABLE invites(
    storyId number(11) NOT NULL,
    userId number(11) NOT NULL,
    PRIMARY KEY (storyId, userId),
    CONSTRAINT fk_stories_invites
      FOREIGN KEY (storyId)
      REFERENCES stories(id),
    CONSTRAINT fk_users_invites
      FOREIGN KEY (userId)
      REFERENCES users(id)
);




CREATE TABLE conditions(
    storyId number(11) NOT NULL,
    visited number(1) NOT NULL,
    subjectId number(11) NOT NULL,
    objectId number(11) NOT NULL,
    CONSTRAINT fk_stories_conditions
      FOREIGN KEY (storyId)
      REFERENCES stories(id),
    CONSTRAINT fk_subject_conditions
      FOREIGN KEY (storyId, subjectId)
      REFERENCES paragraphs(storyId, id),
    CONSTRAINT fk_object_conditions
      FOREIGN KEY (storyId, objectId)
      REFERENCES paragraphs(storyId, id)
);

CREATE TABLE history(
    id number(11) NOT NULL,
    nextPar number(1),
    previousPar number(11),
    storyId number(11) NOT NULL,
    paragraphId number(11) NOT NULL,
    userId number(11) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_history_story
    FOREIGN KEY (storyId)
    REFERENCES stories(id),
    CONSTRAINT fk_users_history
      FOREIGN KEY (userId)
      REFERENCES users(id)
);



INSERT INTO users VALUES(1, 'titi', 'titi', 0);
INSERT INTO users VALUES(2, 'chai', 'chai', 0);
INSERT INTO users VALUES(3, 'zaza', 'zaza', 0);
INSERT INTO users VALUES(4, 'toto', 'toto', 0);
INSERT INTO users VALUES(5, 'nounou', 'nounou', 0);
INSERT INTO users VALUES(6, 'mimi', 'mimi', 0);

INSERT INTO stories VALUES(1, 1, 0, 0, 'Life', 0);
INSERT INTO stories VALUES(2, 2, 1, 0, 'Revising', 0);
INSERT INTO stories VALUES(3, 2, 0, 0, 'Beautiful ruins', 0);
INSERT INTO stories VALUES(4, 3, 0, 0, 'Adulting', 0);

INSERT INTO invites VALUES(1, 2);
INSERT INTO invites VALUES(1, 3);
INSERT INTO invites VALUES(1, 5);
INSERT INTO invites VALUES(1, 6);
INSERT INTO invites VALUES(3, 4);
INSERT INTO invites VALUES(3, 6);
INSERT INTO invites VALUES(4, 1);
INSERT INTO invites VALUES(4, 2);
INSERT INTO invites VALUES(3, 5);


INSERT INTO paragraphs VALUES(1, 1, 'I was just born. what do i do now?', 1, 0);
INSERT INTO paragraphs VALUES(2, 1, 'I have just realized i have exams. what do i do now?',2, 0);
INSERT INTO paragraphs VALUES(3, 1, 'Pasquale Tursis father has just died.', 2, 0);
INSERT INTO paragraphs VALUES(4, 1, 'Im an adult :O It all happened so fast', 3, 1);


INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 1, 'I scream', 1, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 1, 'Im dead now.', 2, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(2, 1, 'I cry', 1, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(2, 1, 'I decide to get my shit together', 2, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(3, 1, 'Pasquale moves back to live with his mom', 1, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(3, 1, 'Pasquale stays in Florence', 2, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(3, 1, 'Pasquale commits suicide', 3, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(4, 1, 'I scream', 1, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(4, 1, 'I work 24/7', 2, 0);

INSERT INTO paragraphs VALUES(1, 2, 'I scream and The doctors take me to my father. He is so thrilled. 20 Years later, my routine lies on', 5, 0);
INSERT INTO paragraphs VALUES(1, 3, 'Im dead now. Gbye', 5, 1);
INSERT INTO paragraphs VALUES(2, 2, 'I decide to get my shit together. Its not so easy: I have to catch up or else i will', 3, 0);
INSERT INTO paragraphs VALUES(3, 2, 'Pasquale commits suicide. The end.', 4, 1);
INSERT INTO paragraphs VALUES(4, 2, 'I scream. Oh lord!!! Im sick of growing up so fast. So I', 4, 0);

UPDATE choices SET destinationId=2 WHERE sourceId=1 and storyId=1 and choiceId=1;
UPDATE choices SET destinationId=3 WHERE sourceId=1 and storyId=1 and choiceId=2;
UPDATE choices SET destinationId=2 WHERE sourceId=1 and storyId=3 and choiceId=3;
UPDATE choices SET destinationId=2 WHERE sourceId=1 and storyId=4 and choiceId=2;
UPDATE choices SET destinationId=2 WHERE sourceId=1 and storyId=2 and choiceId=2;


INSERT INTO conditions (storyId, visited, subjectId, objectId) VALUES (1, 1, 2, 1);
INSERT INTO conditions (storyId, visited, subjectId, objectId) VALUES (1, 1, 3, 1);
INSERT INTO conditions (storyId, visited, subjectId, objectId) VALUES (2, 0, 2, 1);
INSERT INTO conditions (storyId, visited, subjectId, objectId) VALUES (3, 1, 2, 1);
INSERT INTO conditions (storyId, visited, subjectId, objectId) VALUES (4, 0, 2, 1);

INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 2, 'wetting the bed', 3, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 2, 'eating', 4, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 2, 'sleeping', 5, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(1, 2, 'the story ended but hope (a choice) still remains', 6, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(3, 2, 'the story ended but hope (a choice) still remains', 4, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(4, 2, 'kill myself?', 3, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(2, 2, 'fail', 4, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(2, 2, 'go crazy', 5, 0);
INSERT INTO choices (storyId, sourceId, choice, choiceId, locked) VALUES(2, 2, 'my parents will loathe me', 6, 0);


