/*
    OPTIONS
*/
INSERT INTO public."options" (id,is_preset,"label","mode",set_length,winning_sets) VALUES 
(0,true,'simple',0,11,3)
,(1,true,'double',1,50,1)
;


/*
    TOURNAMENT
*/
INSERT INTO public.tournament (id,"date",name,options_id) VALUES 
(0,'2019-08-26 16:06:58.206','test tournament',0)
;

/*
    PLAYERS
*/
INSERT INTO public.player (id,is_licensed,name,tournament_id) VALUES 
 (0,false,'a',0)
,(1,false,'z',0)
,(2,false,'e',0)
,(3,false,'r',0)
,(4,false,'t',0)
,(5,false,'y',0)
,(6,false,'u',0)
,(7,false,'i',0)
,(8,false,'o',0)
,(9,false,'p',0)
,(10,false,'z',0)
,(11,false,'e',0)
,(12,false,'r',0)
,(13,false,'t',0)
,(14,false,'y',0)
,(6,false,'u',0)
,(7,false,'i',0)
,(8,false,'o',0)
,(9,false,'p',0)
;
