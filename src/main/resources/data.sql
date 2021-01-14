INSERT INTO apps (id, name, domain , version) VALUES
  ('0', 'lala', 'ww.ww', '1.0');


  INSERT INTO users (id, username, first_name, last_name, email, password, authorities) VALUES
  ('0', 'admin', 'admin', 'admin', 'admin@admin.com', 'admin', 'ROLE_ADMIN'),  ('1', 'user', 'user', 'user', 'user@user.com', 'user', 'ROLE_USER');

  INSERT INTO appusers (id_user, id_app) VALUES
  ('0', '0');

--   INSERT INTO roles (id, name) VALUES
--   (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

