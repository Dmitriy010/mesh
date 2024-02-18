CREATE TABLE IF NOT EXISTS public.users (
                                            id            BIGINT PRIMARY KEY,
                                            date_of_birth DATE,
                                            name          VARCHAR(500),
                                            password      VARCHAR(500) CHECK (length(password) >= 8)
);

CREATE SEQUENCE IF NOT EXISTS public.users_id_seq START 1;

CREATE TABLE IF NOT EXISTS public.account (
                                              id      BIGINT PRIMARY KEY,
                                              balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
                                              actual_balance DECIMAL(19, 2) NOT NULL CHECK (actual_balance >= 0),
                                              user_id BIGINT NOT NULL UNIQUE,
                                              CONSTRAINT ACCOUNT_USER_ID_FK FOREIGN KEY (user_id)
                                                  REFERENCES public.users
);

CREATE SEQUENCE IF NOT EXISTS public.account_id_seq START 1;

CREATE TABLE IF NOT EXISTS public.email_data (
                                                 id      BIGINT PRIMARY KEY,
                                                 email   VARCHAR(200) UNIQUE,
                                                 user_id BIGINT,
                                                 CONSTRAINT EMAIL_DATA_USER_ID_FK FOREIGN KEY (user_id)
                                                     REFERENCES public.users
);

CREATE SEQUENCE IF NOT EXISTS public.email_data_id_seq START 1;

CREATE TABLE IF NOT EXISTS public.phone_data (
                                                 id      BIGINT PRIMARY KEY,
                                                 phone   VARCHAR(13) UNIQUE,
                                                 user_id BIGINT,
                                                 CONSTRAINT PHONE_DATA_USER_ID_FK FOREIGN KEY (user_id)
                                                     REFERENCES public.users
);

CREATE SEQUENCE IF NOT EXISTS public.phone_data_id_seq START 1;

INSERT INTO public.users (id, date_of_birth, name, password)
-- useruser1 пароль
VALUES (1, NOW(), 'user1', '$2a$10$bq5WCX4N6PInuISz3yfNHe1rl4/YXYAC.T.hmoNF7/0n01TDhQ.Gm');
INSERT INTO public.users (id, date_of_birth, name, password)
-- useruser2 пароль
VALUES (2, NOW(), 'user2', '$2a$10$Ea0uLlRxIHZay7r.L.GhxO.7CpgED4.8e4kqTO9j1Y.GjgaCMPRYK');
INSERT INTO public.users (id, date_of_birth, name, password)
-- useruser3 пароль
VALUES (3, NOW(), 'user3', '$2a$10$MuIlH9tVJoM2dFPF.HdUQepp9N1/tPvbRZMxMI1DhAwPDuRp/8OBO');

SELECT setval('public.users_id_seq', (SELECT MAX(id) FROM public.users));

INSERT INTO public.account (id, balance, actual_balance, user_id)
VALUES (1, 0, 0, 1);
INSERT INTO public.account (id, balance, actual_balance, user_id)
VALUES (2, 100, 100, 2);
INSERT INTO public.account (id, balance, actual_balance, user_id)
VALUES (3, 500, 500, 3);

SELECT setval('public.account_id_seq', (SELECT MAX(id) FROM public.account));

INSERT INTO public.email_data (id, email, user_id)
VALUES (1, 'user1@mail.ru', 1);
INSERT INTO public.email_data (id, email, user_id)
VALUES (2, 'user2@mail.ru', 2);
INSERT INTO public.email_data (id, email, user_id)
VALUES (3, 'user3@mail.ru', 3);

SELECT setval('public.email_data_id_seq', (SELECT MAX(id) FROM public.email_data));

INSERT INTO phone_data (id, phone, user_id)
VALUES (1, '79005456511', 1);
INSERT INTO phone_data (id, phone, user_id)
VALUES (2, '79005456522', 2);
INSERT INTO phone_data (id, phone, user_id)
VALUES (3, '79005456533', 3);

SELECT setval('public.phone_data_id_seq', (SELECT MAX(id) FROM public.phone_data));
