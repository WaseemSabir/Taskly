/*** Add User and todolist tables in database ***/

CREATE TABLE "user"
(
    id character varying(100) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password bytea NOT NULL,
    CONSTRAINT "user_pkey" PRIMARY KEY (id),
    CONSTRAINT "user_email_key" UNIQUE (email)
) WITH ( OIDS = FALSE );

CREATE TABLE "todo"
(
    id character varying(100) NOT NULL,
    title character varying(100) NOT NULL,
    description character varying(1000) NOT NULL,
    due_by timestamp without time zone NOT NULL,
    user_id character varying(100) NOT NULL,
    status character varying(100) NOT NULL,
    completed_at timestamp without time zone,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT "todolist_pkey" PRIMARY KEY (id),
    CONSTRAINT "todolist_user_id_fkey" FOREIGN KEY (user_id)
        REFERENCES "user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
) WITH ( OIDS = FALSE );
