-- V3: Inserção de dados iniciais

-- Inserção de roles padrão
INSERT INTO roles (id, name, acronym) VALUES ('clxyz123000000000000001', 'Administrator', 'ADM');
INSERT INTO roles (id, name, acronym) VALUES ('clxyz123000000000000002', 'User', 'USR');
INSERT INTO roles (id, name, acronym) VALUES ('clxyz123000000000000003', 'Manager', 'MGR');

-- Inserção de usuário administrador padrão
-- Senha: admin123
INSERT INTO users (id, name, email, password, role_id) 
VALUES (
  'clxyz123000000000000010', 
  'System Administrator', 
  'admin@autoinsight.com', 
  '$2a$10$rqCUNBQr6HZtYZZQGGa8zO4/FjFN5.aK5s5Yx8qS3mM4K6L7N8O9P', 
  'clxyz123000000000000001'
);

-- Inserção de usuário comum para testes
-- Senha: user123
INSERT INTO users (id, name, email, password, role_id) 
VALUES (
  'clxyz123000000000000011', 
  'Test User', 
  'user@autoinsight.com', 
  '$2a$10$sQCVOCQr7IauZaAQHHb9aP5/GkGO6.bL6t6Zy9rT4nN5L7M8O9Q0R', 
  'clxyz123000000000000002'
);

-- Inserção de veículo de exemplo
INSERT INTO vehicles (id, plate, model_id, user_id) 
VALUES (
  'clxyz123000000000000020', 
  'ABC1234', 
  'clxyz123000000000000100', 
  'clxyz123000000000000011'
);

-- Inserção de booking de exemplo (agendamento futuro)
INSERT INTO bookings (id, occurs_at, vehicle_id, yard_id) 
VALUES (
  'clxyz123000000000000030', 
  SYSTIMESTAMP + INTERVAL '1' DAY, 
  'clxyz123000000000000020', 
  'clxyz123000000000000200'
);