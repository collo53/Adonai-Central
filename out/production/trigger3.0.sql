DELIMITER $$

CREATE TRIGGER after_cake_distributed
AFTER INSERT ON cakes_distributed
FOR EACH ROW
BEGIN
    UPDATE inventory
    SET quantity = quantity - NEW.quantity
    WHERE cake_name = NEW.cake_name;
END $$

DELIMITER ;
