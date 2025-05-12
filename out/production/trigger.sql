DELIMITER //

CREATE TRIGGER update_inventory_after_distribution
AFTER INSERT ON cakes_distributed
FOR EACH ROW
BEGIN
    -- Check if the cake exists in the inventory
    IF EXISTS (SELECT 1 FROM inventory WHERE cake_name = NEW.cake_name) THEN
        -- Update the inventory quantity by subtracting the distributed quantity
        UPDATE inventory
        SET quantity = quantity - NEW.quantity
        WHERE cake_name = NEW.cake_name;
    ELSE
        -- Optional: Handle the case where the cake does not exist in the inventory
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Distributed cake not found in inventory';
    END IF;
END //

DELIMITER ;