INSERT INTO users (name, email, password, role, created_at, updated_at)
VALUES
  ('teste', 'teste@teste.com', crypt('123456', gen_salt('bf', 10)), 'ROLE_USER', NOW(), NOW()),
  ('admin', 'admin@admin.com', crypt('123456', gen_salt('bf', 10)), 'ROLE_ADMIN', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

INSERT INTO categories (name, description)
VALUES
  ('Tecnologia', 'Eventos de tecnologia e inovacao'),
  ('Negocios', 'Eventos de lideranca e estrategia'),
  ('Design', 'Eventos sobre produto e design')
ON CONFLICT (name) DO NOTHING;

INSERT INTO events (title, description, category_id, location, start_at, end_at, capacity, status)
SELECT * FROM (
  VALUES
  ('Global AI Governance Summit', 'Discussao sobre IA responsavel e regulacao global.', (SELECT id FROM categories WHERE name = 'Tecnologia'), 'Sao Paulo, BR', NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 4 hours', 220, 'PUBLICADO'),
  ('Cloud Native Engineering Day', 'Praticas de arquitetura moderna com containers e observabilidade.', (SELECT id FROM categories WHERE name = 'Tecnologia'), 'Campinas, BR', NOW() + INTERVAL '14 days', NOW() + INTERVAL '14 days 6 hours', 180, 'PUBLICADO'),
  ('UX Strategy Masterclass', 'Experiencias digitais orientadas a resultados de negocio.', (SELECT id FROM categories WHERE name = 'Design'), 'Rio de Janeiro, BR', NOW() + INTERVAL '18 days', NOW() + INTERVAL '18 days 3 hours', 120, 'PUBLICADO'),
  ('Executive Leadership Mixer', 'Networking com liderancas de produto e operacoes.', (SELECT id FROM categories WHERE name = 'Negocios'), 'Belo Horizonte, BR', NOW() + INTERVAL '21 days', NOW() + INTERVAL '21 days 3 hours', 140, 'PUBLICADO'),
  ('Annual Fintech Expo', 'Tendencias de pagamento, open finance e risco.', (SELECT id FROM categories WHERE name = 'Negocios'), 'Curitiba, BR', NOW() + INTERVAL '27 days', NOW() + INTERVAL '27 days 5 hours', 260, 'RASCUNHO'),
  ('Digital Health Forum', 'Transformacao digital na saude com foco em escala.', (SELECT id FROM categories WHERE name = 'Tecnologia'), 'Porto Alegre, BR', NOW() + INTERVAL '34 days', NOW() + INTERVAL '34 days 4 hours', 200, 'PUBLICADO')
) AS v(title, description, category_id, location, start_at, end_at, capacity, status)
WHERE NOT EXISTS (
  SELECT 1 FROM events e WHERE e.title = v.title
);
