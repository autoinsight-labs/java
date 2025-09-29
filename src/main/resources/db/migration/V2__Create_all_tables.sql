-- V2: Criação de tabelas e triggers

-- Criação da tabela roles
CREATE TABLE roles (
  id CHAR(24),
  name VARCHAR(50) NOT NULL,
  acronym VARCHAR(10) NOT NULL,
  CONSTRAINT role_pk PRIMARY KEY (id),
  CONSTRAINT role_acronym_uq UNIQUE (acronym),
  CONSTRAINT role_name_uq UNIQUE (name)
);

-- Criação da tabela users
CREATE TABLE users (
  id CHAR(24),
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role_id CHAR(24),
  CONSTRAINT user_pk PRIMARY KEY (id),
  CONSTRAINT user_email_uq UNIQUE (email),
  CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- Criação da tabela vehicles
CREATE TABLE vehicles (
  id CHAR(24),
  plate VARCHAR(7) NOT NULL,
  model_id CHAR(24) NOT NULL,
  user_id CHAR(24) NOT NULL,
  CONSTRAINT vehicle_pk PRIMARY KEY (id),
  CONSTRAINT vehicle_user_fk FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT plate_uq UNIQUE (plate)
);

-- Criação da tabela bookings
CREATE TABLE bookings (
  id CHAR(24),
  occurs_at TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
  cancelled_at TIMESTAMP,
  vehicle_id CHAR(24) NOT NULL,
  yard_id CHAR(24) NOT NULL,
  CONSTRAINT booking_pk PRIMARY KEY (id),
  CONSTRAINT booking_vehicle_fk FOREIGN KEY (vehicle_id) REFERENCES vehicles (id)
);

-- Trigger para definir role padrão 'USR' quando não informado
CREATE OR REPLACE TRIGGER trg_users_default_role
BEFORE INSERT ON users
FOR EACH ROW
DECLARE
  v_default_role_id CHAR(24);
BEGIN
  IF :NEW.role_id IS NULL THEN
    SELECT id INTO v_default_role_id FROM roles WHERE acronym = 'USR';
    :NEW.role_id := v_default_role_id;
  END IF;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    NULL;
END;
/
