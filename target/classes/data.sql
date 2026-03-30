-- Campagnes
INSERT INTO campagne (id, titre, objectif, debut, fin, statut) VALUES
(1, 'Aide aux réfugiés 2025',  500000.00, '2025-01-01', '2025-12-31', 'ACTIVE'),
(2, 'École pour tous',          200000.00, '2025-03-01', '2025-09-30', 'ACTIVE'),
(3, 'Santé communautaire',      150000.00, '2024-06-01', '2024-12-31', 'TERMINEE');

-- Donateurs
INSERT INTO donateur (id, nom, email, type) VALUES
(1, 'Ahmed Benali',     'ahmed@example.com',  'PARTICULIER'),
(2, 'Société Atlas SA', 'contact@atlas.ma',   'ENTREPRISE'),
(3, 'Fatima Zahra',     'fz@example.com',     'PARTICULIER'),
(4, 'ONG Solidarité',   'info@solidarite.ma', 'ASSOCIATION');

-- Dons
INSERT INTO don (id, montant, date_don, moyen, campagne_id, donateur_id) VALUES
(1, 5000.00,  '2025-02-10', 'VIREMENT', 1, 2),
(2, 1500.00,  '2025-02-15', 'CARTE',    1, 1),
(3, 3000.00,  '2025-03-05', 'CASH',     2, 3),
(4, 10000.00, '2025-03-10', 'VIREMENT', 1, 4),
(5, 800.00,   '2025-03-20', 'CHEQUE',   2, 1);
