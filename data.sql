
COPY public.tournament (id, date, name, options_id) FROM stdin;
78	\N	poules	77
\.





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
120	f	marius
121	f	sebastien
122	f	agnes
133	f	vincent
134	f	bernard
135	f	francois
136	f	ananie
\.



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