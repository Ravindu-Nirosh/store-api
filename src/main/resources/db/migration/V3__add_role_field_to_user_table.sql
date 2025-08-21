ALTER TABLE `store_api`.`users`
ADD COLUMN `role` VARCHAR(20) NOT NULL DEFAULT 'USER' AFTER `password`;