DROP TABLE IF EXISTS sales_item;

CREATE TABLE sales_item
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    title   TEXT NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT,
    min_price_wanted INTEGER NOT NULL,
    status TEXT,
    writer TEXT NOT NULL,
    password TEXT NOT NULL
);