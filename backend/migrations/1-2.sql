/*** Add User and todolist tables in database ***/

CREATE TABLE "user"
(
    id text primary key,
    first_name text NOT NULL,
    last_name text NOT NULL,
    email text NOT NULL,
    password bytea NOT NULL,
    CONSTRAINT "user_email_key" UNIQUE (email)
);

CREATE TABLE "todo"
(
    id text primary key,
    title text NOT NULL,
    description text NOT NULL,
    due_by timestamp without time zone NOT NULL,
    user_id text NOT NULL,
    status text NOT NULL,
    completed_at timestamp without time zone,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT "todolist_user_id_fkey" FOREIGN KEY (user_id)
        REFERENCES "user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
