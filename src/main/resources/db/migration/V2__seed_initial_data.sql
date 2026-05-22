WITH constants AS (
  SELECT
    'ROLE_USER'::text AS role_user,
    'ROLE_ADMIN'::text AS role_admin,
    'PUBLICADO'::text AS status_publicado,
    'RASCUNHO'::text AS status_rascunho
),
category_seed AS (
  SELECT * FROM (
    VALUES
      ('T', 'Tecnologia', 'Eventos de tecnologia e inovacao'),
      ('N', 'Negocios', 'Eventos de lideranca e estrategia'),
      ('D', 'Design', 'Eventos sobre produto e design')
  ) AS c(code, name, description)
)
INSERT INTO users (name, email, password, role, created_at, updated_at)
SELECT 'teste', 'teste@teste.com', crypt('123456', gen_salt('bf', 10)), c.role_user, NOW(), NOW()
FROM constants c
UNION ALL
SELECT 'admin', 'admin@admin.com', crypt('123456', gen_salt('bf', 10)), c.role_admin, NOW(), NOW()
FROM constants c
ON CONFLICT (email) DO NOTHING;

WITH category_seed AS (
  SELECT * FROM (
    VALUES
      ('T', 'Tecnologia', 'Eventos de tecnologia e inovacao'),
      ('N', 'Negocios', 'Eventos de lideranca e estrategia'),
      ('D', 'Design', 'Eventos sobre produto e design')
  ) AS c(code, name, description)
)
INSERT INTO categories (name, description)
SELECT c.name, c.description
FROM category_seed c
ON CONFLICT (name) DO NOTHING;

WITH constants AS (
  SELECT
    'PUBLICADO'::text AS status_publicado,
    'RASCUNHO'::text AS status_rascunho
),
category_map AS (
  SELECT * FROM (
    VALUES
      ('T', 'Tecnologia'),
      ('N', 'Negocios'),
      ('D', 'Design')
  ) AS m(code, category_name)
),
event_seed AS (
  SELECT * FROM (
    VALUES
      ('Global AI Governance Summit', 'Discussao sobre IA responsavel e regulacao global.', 'T', 'Sao Paulo, BR', 10, 4, 220, 'P'),
      ('Cloud Native Engineering Day', 'Praticas de arquitetura moderna com containers e observabilidade.', 'T', 'Campinas, BR', 14, 6, 180, 'P'),
      ('UX Strategy Masterclass', 'Experiencias digitais orientadas a resultados de negocio.', 'D', 'Rio de Janeiro, BR', 18, 3, 120, 'P'),
      ('Executive Leadership Mixer', 'Networking com liderancas de produto e operacoes.', 'N', 'Belo Horizonte, BR', 21, 3, 140, 'P'),
      ('Annual Fintech Expo', 'Tendencias de pagamento, open finance e risco.', 'N', 'Curitiba, BR', 27, 5, 260, 'R'),
      ('Digital Health Forum', 'Transformacao digital na saude com foco em escala.', 'T', 'Porto Alegre, BR', 34, 4, 200, 'P')
  ) AS e(title, description, category_code, location, start_days, duration_hours, capacity, status_code)
)
INSERT INTO events (title, description, category_id, location, start_at, end_at, capacity, status)
SELECT
  e.title,
  e.description,
  c.id,
  e.location,
  NOW() + make_interval(days => e.start_days),
  NOW() + make_interval(days => e.start_days, hours => e.duration_hours),
  e.capacity,
  CASE
    WHEN e.status_code = 'P' THEN k.status_publicado
    ELSE k.status_rascunho
  END
FROM event_seed e
JOIN category_map cm ON cm.code = e.category_code
JOIN categories c ON c.name = cm.category_name
CROSS JOIN constants k
WHERE NOT EXISTS (
  SELECT 1 FROM events ev WHERE ev.title = e.title
);
