ALTER TABLE "order"
    ADD fulfillment_status VARCHAR(10);

UPDATE "order" SET fulfillment_status = 'FULFILLED' WHERE true;
