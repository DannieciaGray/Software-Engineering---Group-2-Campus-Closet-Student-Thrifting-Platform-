-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.items (
  item_id bigint NOT NULL DEFAULT nextval('items_item_id_seq'::regclass),
  title text NOT NULL,
  description text,
  price numeric NOT NULL,
  status text NOT NULL DEFAULT 'AVAILABLE'::text,
  seller_id bigint NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT items_pkey PRIMARY KEY (item_id),
  CONSTRAINT items_seller_id_fkey FOREIGN KEY (seller_id) REFERENCES public.users(user_id)
);
CREATE TABLE public.orders (
  order_id bigint NOT NULL DEFAULT nextval('orders_order_id_seq'::regclass),
  item_id bigint NOT NULL,
  buyer_id bigint NOT NULL,
  status text NOT NULL DEFAULT 'PENDING'::text,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT orders_pkey PRIMARY KEY (order_id),
  CONSTRAINT orders_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(item_id),
  CONSTRAINT orders_buyer_id_fkey FOREIGN KEY (buyer_id) REFERENCES public.users(user_id)
);
CREATE TABLE public.users (
  user_id bigint NOT NULL DEFAULT nextval('users_user_id_seq'::regclass),
  name text NOT NULL,
  email text NOT NULL UNIQUE,
  password_hash text NOT NULL,
  role text NOT NULL DEFAULT 'USER'::text,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT users_pkey PRIMARY KEY (user_id)
);