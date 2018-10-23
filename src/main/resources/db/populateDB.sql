DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories) VALUES
  (100000, '23.10.2018 8:00', 'Завтрак', 800),
  (100000, '23.10.2018 13:00', 'Обед', 1000),
  (100000, '23.10.2018 18:00', 'Ужин', 800),
  (100000, '24.10.2018 8:00', 'Завтрак', 400),
  (100000, '24.10.2018 13:00', 'Обед', 800),
  (100000, '24.10.2018 18:00', 'Ужин', 600),

  /* У админа - диета */
  (100001, '23.10.2018 9:00', 'Завтрак', 800),
  (100001, '23.10.2018 12:00', 'Обед', 1000),
  (100001, '23.10.2018 20:00', 'Ужин', 800);
