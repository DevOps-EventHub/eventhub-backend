WITH constants AS (
  SELECT
    'ROLE_USER'::text AS role_user,
    'ROLE_ADMIN'::text AS role_admin,
    'PUBLICADO'::text AS status_publicado,
    'RASCUNHO'::text AS status_rascunho,
    'Tecnologia'::text AS category_tecnologia,
    'Negocios'::text AS category_negocios,
    'Design'::text AS category_design
)
INSERT INTO users (name, email, password, role, created_at, updated_at)
SELECT 'teste', 'teste@teste.com', crypt('123456', gen_salt('bf', 10)), c.role_user, NOW(), NOW()
FROM constants c
UNION ALL
SELECT 'admin', 'admin@admin.com', crypt('123456', gen_salt('bf', 10)), c.role_admin, NOW(), NOW()
FROM constants c
ON CONFLICT (email) DO NOTHING;

INSERT INTO categories (name, description)
VALUES
  ('Tecnologia', 'Eventos de tecnologia e inovacao'),
  ('Negocios', 'Eventos de lideranca e estrategia'),
  ('Design', 'Eventos sobre produto e design')
ON CONFLICT (name) DO NOTHING;

WITH constants AS (
  SELECT
    'PUBLICADO'::text AS status_publicado,
    'RASCUNHO'::text AS status_rascunho,
    'Tecnologia'::text AS category_tecnologia,
    'Negocios'::text AS category_negocios,
    'Design'::text AS category_design
)
INSERT INTO events (title, description, category_id, location, start_at, end_at, capacity, status)
SELECT * FROM (
  SELECT
    'Global AI Governance Summit',
    'Discussao sobre IA responsavel e regulacao global.',
    (SELECT id FROM categories WHERE name = c.category_tecnologia),
    'Sao Paulo, BR',
    NOW() + INTERVAL '10 days',
    NOW() + INTERVAL '10 days 4 hours',
    220,
    c.status_publicado
  FROM constants c

  UNION ALL

  SELECT
    'Cloud Native Engineering Day',
    'Praticas de arquitetura moderna com containers e observabilidade.',
    (SELECT id FROM categories WHERE name = c.category_tecnologia),
    'Campinas, BR',
    NOW() + INTERVAL '14 days',
    NOW() + INTERVAL '14 days 6 hours',
    180,
    c.status_publicado
  FROM constants c

  UNION ALL

  SELECT
    'UX Strategy Masterclass',
    'Experiencias digitais orientadas a resultados de negocio.',
    (SELECT id FROM categories WHERE name = c.category_design),
    'Rio de Janeiro, BR',
    NOW() + INTERVAL '18 days',
    NOW() + INTERVAL '18 days 3 hours',
    120,
    c.status_publicado
  FROM constants c

  UNION ALL

  SELECT
    'Executive Leadership Mixer',
    'Networking com liderancas de produto e operacoes.',
    (SELECT id FROM categories WHERE name = c.category_negocios),
    'Belo Horizonte, BR',
    NOW() + INTERVAL '21 days',
    NOW() + INTERVAL '21 days 3 hours',
    140,
    c.status_publicado
  FROM constants c

  UNION ALL

  SELECT
    'Annual Fintech Expo',
    'Tendencias de pagamento, open finance e risco.',
    (SELECT id FROM categories WHERE name = c.category_negocios),
    'Curitiba, BR',
    NOW() + INTERVAL '27 days',
    NOW() + INTERVAL '27 days 5 hours',
    260,
    c.status_rascunho
  FROM constants c

  UNION ALL

  SELECT
    'Digital Health Forum',
    'Transformacao digital na saude com foco em escala.',
    (SELECT id FROM categories WHERE name = c.category_tecnologia),
    'Porto Alegre, BR',
    NOW() + INTERVAL '34 days',
    NOW() + INTERVAL '34 days 4 hours',
    200,
    c.status_publicado
  FROM constants c
) AS v(title, description, category_id, location, start_at, end_at, capacity, status)
WHERE NOT EXISTS (
  SELECT 1 FROM events e WHERE e.title = v.title
);
