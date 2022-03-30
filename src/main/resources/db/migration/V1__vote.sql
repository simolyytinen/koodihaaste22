CREATE TABLE vote (
    restaurantid VARCHAR(64) NOT NULL,
    voterid VARCHAR(64) NOT NULL,
    votingdate DATE NOT NULL,

    PRIMARY KEY(restaurantid, voterid, votingdate)
);
