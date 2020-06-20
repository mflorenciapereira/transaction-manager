CREATE TABLE IF NOT EXISTS transactions (
   id VARCHAR (45) NOT NULL,
   accountId VARCHAR (45) NOT NULL,
   type VARCHAR(10),
   amount FLOAT,
   effective_date TIMESTAMP
);