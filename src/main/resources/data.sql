-- Insert some initial users
INSERT INTO APP_USER (username, password, roles) VALUES ('user1', '$2a$10$gfMWNqmM654M4G6QkZw9RukpiaQncqvjTdxugvOf9jodSm2/XEy.e', 'ROLE_USER'); -- user11
INSERT INTO APP_USER (username, password, roles) VALUES ('user2', '$2a$10$lJMxx5J8kqaUoTg4cUadXuZhWTlixHGFYZdOMssQIezeemPq9WzBW', 'ROLE_USER'); -- user22
INSERT INTO APP_USER (username, password, roles) VALUES ('admin', '$2a$10$eQqLNn41uTQK3Omnqgm9w.CzlBkFoXDjHCs5IpDK2vUlt5lvn3dse', 'ROLE_ADMIN'); -- adminadmin

-- Insert some initial recipes for user1
INSERT INTO recipe (name, is_vegetarian, servings, instructions, user_id) VALUES
('Vegetarian Salad', TRUE, 2, 'To make a refreshing and healthy salad, start by washing and chopping some fresh tomatoes, crisp lettuce, and crunchy carrots. Combine these colorful ingredients in a large bowl. For added flavor, drizzle with olive oil, a splash of lemon juice, and a pinch of salt and pepper. Toss everything together to ensure the dressing evenly coats the vegetables. This simple yet delicious salad makes a perfect side dish or a light meal on its own, bursting with the fresh flavors of the garden.', 1),
('Tomato Basil Soup', TRUE, 4, 'In a pot, heat olive oil over medium heat. Add chopped onions and garlic, and sauté until translucent. Add chopped tomatoes and cook until soft. Pour in vegetable broth and bring to a boil. Reduce heat and simmer for 20 minutes. Blend the soup until smooth and stir in chopped basil before serving.', 1),
('Vegetable Stir-Fry', TRUE, 3, 'In a large wok, heat sesame oil over high heat. Add sliced carrots, bell peppers, and broccoli, and stir-fry for 5-7 minutes. Add soy sauce and a pinch of salt and pepper. Serve hot over steamed rice.', 1),
('Pasta and red meat', FALSE, 1, 'Cook pasta according to package instructions. Sauté minced garlic in olive oil in oven until translucent. Add ground red meat, season with salt, pepper, and Italian herbs. Cook until browned. Mix in tomato sauce and simmer for 10 minutes. Combine with pasta and top with grated Parmesan cheese. Serve hot.', 1),
('Fruit Smoothie', TRUE, 2, 'Blend together a banana, a handful of strawberries, a cup of spinach, and almond milk until smooth. Serve immediately for a refreshing drink.', 1);

-- Insert some initial recipes for user2
INSERT INTO recipe (name, is_vegetarian, servings, instructions, user_id) VALUES
('Grilled Chicken', FALSE, 4, 'Marinate chicken breasts in olive oil, lemon juice, garlic, and herbs for at least 30 minutes. Grill on medium-high heat until fully cooked, about 6-7 minutes per side. Serve hot.', 2),
('Beef Stew', FALSE, 6, 'In a large pot, brown beef chunks in olive oil. Add chopped onions, garlic, and carrots, and cook until softened. Pour in beef broth, diced tomatoes, and potatoes. Simmer on low heat for 2 hours or until beef is tender.', 2),
('Chicken Caesar Salad', FALSE, 2, 'Grill chicken breasts and slice them thinly. Toss with romaine lettuce, Caesar dressing, croutons, and Parmesan cheese.', 2),
('Spaghetti Bolognese', FALSE, 4, 'Cook spaghetti according to package instructions. In a large skillet, cook ground beef with chopped onions and garlic until browned. Add tomato sauce and simmer for 20 minutes. Serve over cooked spaghetti with grated Parmesan cheese.', 2),
('Roasted Vegetables', TRUE, 3, 'Preheat oven to 400°F (200°C). Toss chopped potatoes, carrots, bell peppers, and zucchini with olive oil, salt, and pepper. Spread on a baking sheet and roast for 25-30 minutes until tender and golden.', 2);

-- Insert some initial ingredients
INSERT INTO ingredient (name) VALUES
('Potato'),('Chicken'),('Tomato'),('Lettuce'),('Carrot'),('Basil'),('Onion'),('Garlic'),
('Bell Pepper'),('Zucchini'),('Broccoli'),('Banana'),('Strawberries'),('Spinach'),
('Almond Milk'),('Beef'),('Parmesan Cheese'),('Chicken2');

-- Link ingredients with recipes for user1
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
(1, 3), (1, 4), (1, 5),
(2, 3), (2, 7), (2, 8), (2, 6),
(3, 5), (3, 9), (3, 11),
(4, 16),(4, 3), (4, 9), (4, 17),
(5, 12),(5, 13),(5, 14),(5, 15);

-- Link ingredients with recipes for user2
INSERT INTO recipe_ingredient (recipe_id, ingredient_id) VALUES
(6, 2), (6, 8),
(7, 16),(7, 1), (7, 7), (7, 8), (7, 5),
(8, 2), (8, 4), (8, 17),
(9, 2), (9, 7), (9, 8), (9, 3), (9, 17),
(10, 1),(10, 5),(10, 9),(10, 10);
