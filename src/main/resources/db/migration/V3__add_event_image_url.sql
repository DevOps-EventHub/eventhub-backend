ALTER TABLE events ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

UPDATE events SET image_url = 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1200&q=80' WHERE title = 'Global AI Governance Summit';
UPDATE events SET image_url = 'https://images.unsplash.com/photo-1517048676732-d65bc937f952?auto=format&fit=crop&w=1200&q=80' WHERE title = 'Cloud Native Engineering Day';
UPDATE events SET image_url = 'https://images.unsplash.com/photo-1497366811353-6870744d04b2?auto=format&fit=crop&w=1200&q=80' WHERE title = 'UX Strategy Masterclass';
UPDATE events SET image_url = 'https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=1200&q=80' WHERE title = 'Executive Leadership Mixer';
UPDATE events SET image_url = 'https://images.unsplash.com/photo-1559526324-4b87b5e36e44?auto=format&fit=crop&w=1200&q=80' WHERE title = 'Annual Fintech Expo';
UPDATE events SET image_url = 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1f?auto=format&fit=crop&w=1200&q=80' WHERE title = 'Digital Health Forum';
