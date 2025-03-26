-- Supprimer les données existantes pour éviter les doublons lors des redémarrages (optionnel, utile en développement)
-- Attention : Ne pas utiliser en production sans précaution !
-- DELETE FROM comments; -- S'il y a une table comments
-- DELETE FROM posts;
-- DELETE FROM subjects;
-- DELETE FROM users WHERE id > 1; -- Garder l'utilisateur 1 par exemple

-- Insérer les Thèmes (Subjects)
INSERT INTO subjects (id, nom) VALUES
(1, 'Développement Web Frontend'),
(2, 'Développement Web Backend'),
(3, 'Bases de Données'),
(4, 'DevOps & Cloud'),
(5, 'Intelligence Artificielle')
ON DUPLICATE KEY UPDATE nom=nom; -- Ignore si l'ID existe déjà (utile si IDs non auto-incrémentés ou pour re-run)
-- Si votre ID est auto-incrémenté, vous pouvez simplifier :
-- INSERT INTO subjects (nom) VALUES
-- ('Développement Web Frontend'),
-- ('Développement Web Backend'),
-- ('Bases de Données'),
-- ('DevOps & Cloud'),
-- ('Intelligence Artificielle');
-- Mais il est plus sûr de spécifier les IDs pour les jointures ci-dessous

-- Insérer les Articles (Posts)
-- Assurez-vous qu'un utilisateur avec user_id = 1 existe !

-- Articles pour Thème 1: Développement Web Frontend (subject_id = 1)
INSERT INTO posts (titre, contenu, date, user_id, subject_id) VALUES
('Introduction à React 18', 'Découvrez les nouveautés de React 18, notamment le rendu concurrent et les transitions. Cet article couvre les bases pour démarrer.', NOW(), 1, 1),
('Maîtriser CSS Grid Layout', 'Un guide complet sur CSS Grid Layout pour créer des mises en page web complexes et responsives facilement. Exemples pratiques inclus.', NOW(), 1, 1);

-- Articles pour Thème 2: Développement Web Backend (subject_id = 2)
INSERT INTO posts (titre, contenu, date, user_id, subject_id) VALUES 
('Construire une API REST avec Spring Boot', 'Apprenez étape par étape comment développer une API RESTful robuste en utilisant Java et le framework Spring Boot. Gestion des erreurs et sécurité abordées.', NOW(), 1, 2),
('Node.js et Express pour les débutants', 'Un tutoriel pour commencer avec Node.js et le framework Express. Idéal pour créer rapidement des serveurs web et des API.', NOW(), 1, 2);

-- Articles pour Thème 3: Bases de Données (subject_id = 3)
INSERT INTO posts (titre, contenu, date, user_id, subject_id) VALUES
('SQL vs NoSQL : Quand utiliser quoi ?', 'Comprendre les différences fondamentales entre les bases de données SQL relationnelles et les bases NoSQL. Avantages, inconvénients et cas d''usage.', NOW(), 1, 3),
('Optimisation des requêtes SQL : Les Index', 'Comment les index fonctionnent dans les bases de données SQL et comment les utiliser efficacement pour accélérer vos requêtes.', NOW(), 1, 3);

-- Articles pour Thème 4: DevOps & Cloud (subject_id = 4)
INSERT INTO posts (titre, contenu, date, user_id, subject_id) VALUES
('Introduction à Docker et aux conteneurs', 'Les bases de Docker : images, conteneurs, Dockerfiles. Apprenez à conteneuriser votre première application.', NOW(), 1, 4),
('Déploiement Continu avec GitHub Actions', 'Mettre en place un pipeline de CI/CD simple mais efficace en utilisant GitHub Actions pour automatiser les tests et le déploiement.', NOW(), 1, 4);

-- Articles pour Thème 5: Intelligence Artificielle (subject_id = 5)
INSERT INTO posts (titre, contenu, date, user_id, subject_id) VALUES
('Les grands principes du Machine Learning', 'Une vue d''ensemble des concepts clés du Machine Learning : apprentissage supervisé, non supervisé, renforcement, et évaluation des modèles.', NOW(), 1, 5),
('Comprendre les Réseaux de Neurones Convolutifs (CNN)', 'Introduction aux CNN, leur architecture typique (convolution, pooling) et leur application principale en vision par ordinateur.', NOW(), 1, 5);

-- Ajoutez d'autres insertions si nécessaire (ex: abonnements initiaux, commentaires...)
-- INSERT INTO user_subscriptions (user_id, subject_id) VALUES (1, 1), (1, 3); -- Exemple d'abonnement pour l'utilisateur 1