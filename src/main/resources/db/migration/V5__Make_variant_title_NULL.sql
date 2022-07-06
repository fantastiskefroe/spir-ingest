ALTER TABLE order_line
    ALTER COLUMN variant_title DROP NOT NULL;

UPDATE order_line
SET variant_title = NULL
WHERE variant_title = '';