
CREATE TYPE public.Application_Type AS ENUM
    ('low-power', 'high-power', 'mid-power');

CREATE TABLE IF NOT EXISTS public.user
(
    id uuid NOT NULL default gen_random_uuid(),
    name character varying(100) ,
    display_name character varying(100) ,
    mailId character varying(255) NOT NULL,
    phoneNumber character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    timestamp timestamp with time zone NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.power
(
    id uuid NOT NULL default gen_random_uuid(),
    userId uuid NOT NULL,
    from_time timestamp with time zone NOT NULL,
    to_time timestamp with time zone NOT NULL,
    duration character varying(255) ,
    unit_consumed character varying(255),
    application Application_Type not null ,
    CONSTRAINT power_pkey PRIMARY KEY (id),
    CONSTRAINT fk_power_user FOREIGN KEY (userId)
        REFERENCES public.power (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_power_users FOREIGN KEY (userId)
        REFERENCES public.user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.authorization_token
(
    jwt_token character varying(500) NOT NULL,
    key character varying(255) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT authorization_token_pkey PRIMARY KEY (jwt_token)
)
    TABLESPACE pg_default;



