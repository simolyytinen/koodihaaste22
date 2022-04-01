CREATE TABLE restaurant (
    id VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    city VARCHAR(64) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE vote (
    restaurantid VARCHAR(64) NOT NULL,
    voterid VARCHAR(64) NOT NULL,
    votingdate DATE NOT NULL,

    PRIMARY KEY(restaurantid, voterid, votingdate),
    FOREIGN KEY(restaurantid) REFERENCES restaurant(id)
);
