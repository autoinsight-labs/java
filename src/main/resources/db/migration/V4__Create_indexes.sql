-- V4: Criação de índices

-- Índices para a tabela users
CREATE INDEX idx_users_role_id ON users (role_id);

-- Índices para a tabela vehicles
CREATE INDEX idx_vehicles_user_id ON vehicles (user_id);
CREATE INDEX idx_vehicles_model_id ON vehicles (model_id);

-- Índices para a tabela bookings
CREATE INDEX idx_bookings_vehicle_id ON bookings (vehicle_id);
CREATE INDEX idx_bookings_yard_id ON bookings (yard_id);
CREATE INDEX idx_bookings_occurs_at ON bookings (occurs_at);
CREATE INDEX idx_bookings_cancelled_at ON bookings (cancelled_at);

-- Índice composto para consultas de bookings ativos por veículo
CREATE INDEX idx_bookings_active_vehicle ON bookings (vehicle_id, occurs_at);

-- Índice para consultas de bookings por período
CREATE INDEX idx_bookings_period ON bookings (occurs_at, cancelled_at);
