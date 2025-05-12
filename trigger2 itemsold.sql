DELIMITER //

CREATE TRIGGER after_item_sold_insert
AFTER INSERT ON items_sold
FOR EACH ROW
BEGIN
    DECLARE current_quantity INT;

    -- Check the current quantity in cakes_distributed for the station and cake
    SELECT quantity INTO current_quantity 
    FROM cakes_distributed 
    WHERE station_name = NEW.station_name AND cake_name = NEW.cake_name;

    -- Update the quantity in cakes_distributed if the entry exists
    IF current_quantity IS NOT NULL THEN
        -- Reduce the quantity in cakes_distributed by the sold quantity in items_sold
        UPDATE cakes_distributed
        SET quantity = GREATEST(0, current_quantity - NEW.quantity) -- Prevent negative quantities
        WHERE station_name = NEW.station_name AND cake_name = NEW.cake_name;
    ELSE
        -- Optional: handle cases where no distributed record exists, e.g., log an error or insert initial record
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No corresponding distributed record found for the sale';
    END IF;

END //

DELIMITER ;
