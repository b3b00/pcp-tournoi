/*
    OPTIONS
*/
INSERT INTO options (id,is_preset,label,mode,set_length,winning_sets) VALUES 
(0,true,'simple',0,11,3)
,(1,true,'double',1,50,1)
;


/*
    TOURNAMENT
*/
INSERT INTO tournament (id,date,name,options_id) VALUES 
(0,'1977-03-30 16:06:58.206','test tournament',0)
;

/*
    PLAYERS
*/
INSERT INTO player (id,is_licensed,name,tournament_id) VALUES 
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
,(15,false,'u',0)
,(16,false,'i',0)
,(17,false,'o',0)
,(18,false,'p',0)
,(19,false,'a',0)
,(20,false,'z',0)
,(21,false,'e',0)
,(22,false,'r',0)
,(23,false,'t',0)
,(24,false,'y',0)
,(25,false,'u',0)
,(26,false,'i',0)
,(27,false,'o',0)
,(28,false,'p',0)
,(29,false,'z',0)
,(30,false,'e',0)
,(31,false,'r',0)

;
