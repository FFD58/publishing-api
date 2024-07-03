INSERT INTO users (is_deleted, profile_id, email, "password", "position", "role", username)
VALUES (false, NULL, 'ffd58111@mail.ru', '$2a$10$ZBIP6//GMS62zN0w6r2BXuET/LZQpTH7GTY3h0lAnbZNOGrEmN4/i', NULL,
        'ROLE_ADMIN', 'Admin'),
       (false, NULL, 'qwe11111@qwer.ru', '$2y$10$/YHoMtfwCNdmqlZd1GhwQOutdCmbdB8Z6Tadam3DGuzI3zLlF1Aw2', NULL,
        'ROLE_USER', 'User');

INSERT INTO formats (is_deleted, designation, title)
VALUES (false, '60*84/32', 'А6'),
       (false, '60*84/16', 'А5'),
       (false, '60*84/8', 'А4'),
       (false, '70*100/16', 'Журнал');

INSERT INTO types (is_deleted, title)
VALUES (false, 'Монография'),
       (false, 'Учебное пособие'),
       (false, 'Научно-производственный журнал'),
       (false, 'Учебно-методическое пособие'),
       (false, 'Научный журнал'),
       (false, 'Энцикломедия');

INSERT INTO customers (is_deleted, email, "name", phone)
VALUES (false, 'another@customer.ru', 'Иванов Петр Федорович', '+71234567890'),
       (false, 'ffafurin@mail.ru', 'Юрков Кондратий Николаевич', '+72934561895'),
       (false, 'ffd58@mail.ru', 'Папко Ефрем Никанорович', '+77934119965'),
       (false, 'ur-fac@psu.ru', 'Юридический факультет', '+78412111222'),
       (false, 'cust-test@inbox.ru', 'Синицин Константин Евграфович', '+32134119876');

INSERT INTO books (is_deleted, book_format_id, book_type_id, order_id, title, authors)
VALUES (false, 2, 2, NULL, 'Молодая гвардия', '{"Фадеев Александр Александрович"}'),
       (false, 3, 3, NULL, 'Дети капитана Гранта', '{"Жюль Верн"}'),
       (false, 4, 4, NULL, 'Война и Мир', '{"Толстой Лев Николаевич"}');

INSERT INTO orders (is_deleted, is_reported, "number", book_id, created_at, customer_id, finished_at, updated_at, "comment",
                    deadline, status)
VALUES (false, false, 13, 2, '2024-06-25 14:34:52.416459', 2, NULL, '2024-06-25 19:57:32.612571', '-', '2024-07-30 19:57:32.612571', 'AWAIT'),
       (false, false, 1, 1, '2024-06-17 13:27:22.776338', 1, NULL, '2024-06-27 18:12:32.478443', 'Новый заказ', '2024-11-11 19:57:32.612571',
        'AWAIT'),
       (false, false, 123, 3, '2024-06-25 14:34:25.841799', 3, NULL, '2024-06-27 18:12:49.916552', 'Цветные изображения',
        '2024-07-19 19:57:32.612571', 'AWAIT');

UPDATE books
SET order_id = 1
WHERE id = 1;

UPDATE books
SET order_id = 2
WHERE id = 2;

UPDATE books
SET order_id = 3
WHERE id = 3;

INSERT INTO user_tasks (is_deleted, comment, created_at, finished_at, order_id, updated_at, user_id, status, title)
VALUES (false, 'Много сложных формул', '2024-06-18 22:39:29', NULL, 1, '2024-06-18 22:39:34', 1, 'AWAIT', 'Первая читка'),
       (false, 'Некачественные рисунки', '2024-06-27 16:42:08.16524', NULL, 1, '2024-06-27 16:42:08.166238', 2, 'IN_WORK', 'Первая читка'),
       (false, NULL, '2024-06-27 16:46:00.655001', NULL, 2, '2024-06-27 16:59:31.495273', 1, 'IN_WORK', 'Вторая читка'),
       (true, 'Много широких таблиц', '2024-06-27 16:45:47.146465', NULL, 1, '2024-06-27 18:12:02.523463', 2, 'IN_WORK', 'Вторая верстка'),
       (false, 'Сплошной текст', '2024-06-27 16:47:06.395583', NULL, 3, '2024-06-27 18:12:14.758713', 1, 'IN_WORK', 'Вторая верстка'),
       (false, 'Английский язык', '2024-06-27 18:13:21.386267', NULL, 1, '2024-06-27 18:13:21.386267', 2, 'IN_WORK', '1 верстка'),
       (false, 'Много сложных формул', '2024-06-29 21:29:34.039541', NULL, 3, '2024-06-29 21:29:34.039541', 1, 'IN_WORK', '1 верстка');

INSERT INTO customers_orders (customer_id, orders_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO formats_books (book_format_id, books_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO orders_tasks (order_id, tasks_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (3, 5),
       (3, 6),
       (2, 7);

INSERT INTO types_books (book_type_id, books_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO users_tasks (tasks_id, user_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2),
       (5, 1),
       (6, 2),
       (7, 1);