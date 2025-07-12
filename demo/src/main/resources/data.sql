-- Sellers
INSERT INTO seller (name, emerald_balance) VALUES
  ('Alice Smith', 1500.00),
  ('Bob Johnson', 800.50),
  ('Charlie Brown', 2000.00);

-- Products for Alice
INSERT INTO product (name, seller_id) VALUES
  ('Wireless Mouse', 1),
  ('USB-C Cable', 1),
  ('Bluetooth Keyboard', 1);

-- Products for Bob
INSERT INTO product (name, seller_id) VALUES
  ('Gaming Monitor', 2),
  ('Webcam HD', 2),
  ('Desk Lamp', 2);

-- Products for Charlie
INSERT INTO product (name, seller_id) VALUES
  ('Fitness Tracker', 3),
  ('Smart Scale', 3),
  ('Yoga Mat', 3);
