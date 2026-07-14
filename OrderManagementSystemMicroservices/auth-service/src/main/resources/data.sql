INSERT INTO users (name, email, password, role)
SELECT 'Alice', 'alice@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alice@example.com');

INSERT INTO users (name, email, password, role)
SELECT 'Bob', 'bob@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'bob@example.com');

INSERT INTO users (name, email, password, role)
SELECT 'Charlie', 'charlie@example.com',
       '$2a$10$YdCQ/614ns60lXDfXx8ANew5KAfub71Z16k2lwske/PF9lL1MhLxS',
       'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'charlie@example.com');

INSERT INTO users (name, email, password, role)
SELECT 'Admin', 'admin@example.com',
       '$2a$10$ypLJwxP9mgrocbK4E/34COnLiZQqPH7BOz1y0z4yDlj60uCZuyc4W',
       'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');
