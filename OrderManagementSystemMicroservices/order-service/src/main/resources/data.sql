-- Seed orders (user/product IDs align with auth-service and product-service seed data)

INSERT INTO orders (user_id, user_name, user_email, product_id, product_name, price_at_purchase)
SELECT 1, 'Alice', 'alice@example.com', 1, 'Keyboard', 65.00
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 1 AND product_id = 1);

INSERT INTO orders (user_id, user_name, user_email, product_id, product_name, price_at_purchase)
SELECT 1, 'Alice', 'alice@example.com', 2, 'Mouse', 25.00
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 1 AND product_id = 2);

INSERT INTO orders (user_id, user_name, user_email, product_id, product_name, price_at_purchase)
SELECT 2, 'Bob', 'bob@example.com', 3, 'Laptop', 999.00
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 2 AND product_id = 3);

INSERT INTO orders (user_id, user_name, user_email, product_id, product_name, price_at_purchase)
SELECT 2, 'Bob', 'bob@example.com', 4, 'Monitor', 299.00
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 2 AND product_id = 4);

INSERT INTO orders (user_id, user_name, user_email, product_id, product_name, price_at_purchase)
SELECT 3, 'Charlie', 'charlie@example.com', 5, 'Headphones', 149.00
WHERE NOT EXISTS (SELECT 1 FROM orders WHERE user_id = 3 AND product_id = 5);
