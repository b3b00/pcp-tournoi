--
-- PostgreSQL database dump
--

-- Dumped from database version 10.10 (Ubuntu 10.10-1.pgdg18.04+1)
-- Dumped by pg_dump version 10.10 (Ubuntu 10.10-1.pgdg18.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: group_table; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.group_table (
    id integer NOT NULL,
    name character varying(255)
);


ALTER TABLE public.group_table OWNER TO pcpuser;

--
-- Name: group_table_teams; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.group_table_teams (
    group_id integer NOT NULL,
    teams_id integer NOT NULL
);


ALTER TABLE public.group_table_teams OWNER TO pcpuser;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: pcpuser
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO pcpuser;

--
-- Name: options; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.options (
    id integer NOT NULL,
    is_preset boolean,
    label character varying(255),
    mode integer,
    set_length integer NOT NULL,
    winning_sets integer NOT NULL
);


ALTER TABLE public.options OWNER TO pcpuser;

--
-- Name: player; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.player (
    id integer NOT NULL,
    is_licensed boolean,
    name character varying(255)
);


ALTER TABLE public.player OWNER TO pcpuser;

--
-- Name: team; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.team (
    id integer NOT NULL,
    player1_id integer,
    player2_id integer
);


ALTER TABLE public.team OWNER TO pcpuser;

--
-- Name: tournament; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.tournament (
    id integer NOT NULL,
    date timestamp without time zone,
    name character varying(255),
    options_id integer
);


ALTER TABLE public.tournament OWNER TO pcpuser;

--
-- Name: tournament_groups; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.tournament_groups (
    tournament_id integer NOT NULL,
    groups_id integer NOT NULL
);


ALTER TABLE public.tournament_groups OWNER TO pcpuser;

--
-- Name: tournament_players; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.tournament_players (
    tournament_id integer NOT NULL,
    players_id integer NOT NULL
);


ALTER TABLE public.tournament_players OWNER TO pcpuser;

--
-- Name: tournament_teams; Type: TABLE; Schema: public; Owner: pcpuser
--

CREATE TABLE public.tournament_teams (
    tournament_id integer NOT NULL,
    teams_id integer NOT NULL
);


ALTER TABLE public.tournament_teams OWNER TO pcpuser;

--
-- Data for Name: group_table; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.group_table (id, name) FROM stdin;
149	0
150	1
151	2
152	3
\.


--
-- Data for Name: group_table_teams; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.group_table_teams (group_id, teams_id) FROM stdin;
149	147
149	143
149	146
150	141
150	138
150	139
151	145
151	148
151	140
152	137
152	144
152	142
\.


--
-- Data for Name: options; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.options (id, is_preset, label, mode, set_length, winning_sets) FROM stdin;
76	t	simple	0	11	3
77	t	double	1	50	1
\.


--
-- Data for Name: player; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.player (id, is_licensed, name) FROM stdin;
80	f	basile
86	f	genevieve
88	f	odilon
89	f	edouard
90	f	épipahnie
79	t	raymond
81	t	lucien
82	t	alix
83	t	guilaume
84	t	paulin
85	t	tatiana
87	t	yvettes
115	f	nina
116	t	remi
117	t	marcel
118	t	roseline
119	t	prisca
120	f   marius
121	f	sebastien
122	f	agnes
133	f	vincent
134	f	bernard
135	f	francois
136	f	ananie
\.


--
-- Data for Name: team; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.team (id, player1_id, player2_id) FROM stdin;
137	118	133
138	82	90
139	119	136
140	116	135
141	87	88
142	117	115
143	81	120
144	83	80
145	84	134
146	85	122
147	79	121
148	89	86
\.


--
-- Data for Name: tournament; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.tournament (id, date, name, options_id) FROM stdin;
78	\N	poules	77
\.


--
-- Data for Name: tournament_groups; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.tournament_groups (tournament_id, groups_id) FROM stdin;
78	149
78	150
78	151
78	152
\.


--
-- Data for Name: tournament_players; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.tournament_players (tournament_id, players_id) FROM stdin;
78	80
78	86
78	88
78	89
78	90
78	79
78	81
78	82
78	83
78	84
78	85
78	87
78	115
78	116
78	117
78	118
78	119
78	120
78	121
78	122
78	133
78	134
78	135
78	136
\.


--
-- Data for Name: tournament_teams; Type: TABLE DATA; Schema: public; Owner: pcpuser
--

COPY public.tournament_teams (tournament_id, teams_id) FROM stdin;
78	137
78	138
78	139
78	140
78	141
78	142
78	143
78	144
78	145
78	146
78	147
78	148
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: pcpuser
--

SELECT pg_catalog.setval('public.hibernate_sequence', 152, true);


--
-- Name: group_table group_table_pkey; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.group_table
    ADD CONSTRAINT group_table_pkey PRIMARY KEY (id);


--
-- Name: options options_pkey; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.options
    ADD CONSTRAINT options_pkey PRIMARY KEY (id);


--
-- Name: player player_pkey; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.player
    ADD CONSTRAINT player_pkey PRIMARY KEY (id);


--
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: tournament tournament_pkey; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT tournament_pkey PRIMARY KEY (id);


--
-- Name: tournament_players uk_cfgmecnw6x0fm6jh5y506b0cc; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT uk_cfgmecnw6x0fm6jh5y506b0cc UNIQUE (players_id);


--
-- Name: tournament_teams uk_kkir011p65vyipc4gxw266q0f; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_teams
    ADD CONSTRAINT uk_kkir011p65vyipc4gxw266q0f UNIQUE (teams_id);


--
-- Name: group_table_teams uk_liy8q81ogutnppfw9br9a57hi; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.group_table_teams
    ADD CONSTRAINT uk_liy8q81ogutnppfw9br9a57hi UNIQUE (teams_id);


--
-- Name: tournament_groups uk_tdqg082frgt4l2f0wbqpe0lct; Type: CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_groups
    ADD CONSTRAINT uk_tdqg082frgt4l2f0wbqpe0lct UNIQUE (groups_id);


--
-- Name: tournament_teams fk685bov1vc7bklfxuqm3t9djq8; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_teams
    ADD CONSTRAINT fk685bov1vc7bklfxuqm3t9djq8 FOREIGN KEY (teams_id) REFERENCES public.team(id);


--
-- Name: tournament_groups fk893swvvyt06var25ldhrtg7pd; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_groups
    ADD CONSTRAINT fk893swvvyt06var25ldhrtg7pd FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);


--
-- Name: team fk962rmngjyud0ijcw4w7d9vfs0; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT fk962rmngjyud0ijcw4w7d9vfs0 FOREIGN KEY (player2_id) REFERENCES public.player(id);


--
-- Name: group_table_teams fkbn9jokn3k7eh5w7m4bnd1lwuy; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.group_table_teams
    ADD CONSTRAINT fkbn9jokn3k7eh5w7m4bnd1lwuy FOREIGN KEY (group_id) REFERENCES public.group_table(id);


--
-- Name: group_table_teams fke3s2eor337p4x92p3tef81crr; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.group_table_teams
    ADD CONSTRAINT fke3s2eor337p4x92p3tef81crr FOREIGN KEY (teams_id) REFERENCES public.team(id);


--
-- Name: tournament_teams fki2xkpjk2bkpniv5lp7pq3ushl; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_teams
    ADD CONSTRAINT fki2xkpjk2bkpniv5lp7pq3ushl FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);


--
-- Name: tournament_players fkii29dd8t832rgf2gmvkp56f1h; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT fkii29dd8t832rgf2gmvkp56f1h FOREIGN KEY (tournament_id) REFERENCES public.tournament(id);


--
-- Name: tournament_groups fkkdok7fv9exqvdkkndlx4a1os3; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_groups
    ADD CONSTRAINT fkkdok7fv9exqvdkkndlx4a1os3 FOREIGN KEY (groups_id) REFERENCES public.group_table(id);


--
-- Name: team fkphn10d6c8k5b758iklivfyn5y; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT fkphn10d6c8k5b758iklivfyn5y FOREIGN KEY (player1_id) REFERENCES public.player(id);


--
-- Name: tournament fkr5wl47o23ydsvlbkhbsps4an8; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament
    ADD CONSTRAINT fkr5wl47o23ydsvlbkhbsps4an8 FOREIGN KEY (options_id) REFERENCES public.options(id);


--
-- Name: tournament_players fkr86wgja4whf90bb3547g9sbdy; Type: FK CONSTRAINT; Schema: public; Owner: pcpuser
--

ALTER TABLE ONLY public.tournament_players
    ADD CONSTRAINT fkr86wgja4whf90bb3547g9sbdy FOREIGN KEY (players_id) REFERENCES public.player(id);


--
-- PostgreSQL database dump complete
--

