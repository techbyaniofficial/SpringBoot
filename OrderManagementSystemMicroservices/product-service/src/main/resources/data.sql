INSERT INTO products (name, price, active)
SELECT 'Keyboard', 75.00, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Keyboard');

INSERT INTO products (name, price, active)
SELECT 'Mouse', 25.00, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Mouse');

INSERT INTO products (name, price, active)
SELECT 'Laptop', 999.00, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop');

INSERT INTO products (name, price, active)
SELECT 'Monitor', 299.00, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Monitor');

INSERT INTO products (name, price, active)
SELECT 'Headphones', 149.00, true
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Headphones');
