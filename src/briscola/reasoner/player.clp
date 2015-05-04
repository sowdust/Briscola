(import briscola.objects.*)

(defclass Deck briscola.objects.Deck)
(defclass Suit briscola.objects.Suit)
(defclass Card briscola.objects.Card)
(defclass Rank briscola.objects.Rank)
(defclass Hand briscola.objects.Hand)
(defclass Player briscola.Player)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   variabili disponibili per il fetch
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   IO  :   il mio Player
;   AGENT:  il mio PlayerAgent




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   tutti i tipi di fatti che ci sono utili (slot per ora ridondanti)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;   (mio-turno)
;   (mio-turno-numero n)
;   (mio-ruolo ruolo)
;   (giocabile card)



(deftemplate io "chi sono io"
    (slot player)
)

( deftemplate in-mano "carte che posso ancora giocare"
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate in-mazzo "carte ancora da scoprire"
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate in-tavolo "carte sul tavolo"
    (slot card)
    (slot player)
    (slot rank)
    (slot suit)
)

( deftemplate da-giocarsi "carte attivate per il gioco"
    (slot card)
    (slot sal)
)

( deftemplate turno "ordine in cui si prende la mano"
    (slot player)
    (slot posizione)
)

( deftemplate prende "chi, per ora, prende"
    (slot player)
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate nuova-giocata "info sulla carta appena giocata"
    (slot player)
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate lisci-in-mano
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate liscio-piu-basso
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate carichi-in-mano "Le carte da punto che ho in mano"
    (slot card)
    (slot rank)
    (slot suit)
    (slot points)
)

( deftemplate carico-piu-basso 
    (slot card)
    (slot rank)
    (slot suit)
    (slot points)
)

( deftemplate carico-piu-alto 
    (slot card)
    (slot rank)
    (slot suit)
    (slot points)
)

( deftemplate posso-prendere "Carte con le quali posso prendere la mano"
    (slot card)
    (slot rank)
    (slot suit)
)

( deftemplate piu-bassa-che-prende "La meno potente che prende in mano"
    (slot card)
)

( deftemplate giaguaro  (slot player) )
( deftemplate socio (slot player) )
( deftemplate villano   (slot player) )
( deftemplate briscola (slot card) (slot rank) (slot suit) )
( deftemplate seme-mano-fact (slot suit) )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     GLOBAL VARs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; stores the briscola suit (initially null)
(defglobal ?*briscola* = nil)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     UTILITIES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;  if set to true debug statements are printed
( defglobal ?*debug* = TRUE)


;; PRINTS DEBUG MESSAGES IF GLOBAL VAR ?*debug* SET TO TRUE
( deffunction debug (?list)
    (if ?*debug* then
        (printout t ((fetch IO) getName) ": " ?list crlf)
    )
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;      FUNCTIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( deffunction get-piu-alta-seme (?seme)
    (bind ?it (run-query* briscole-in-mano ?seme))
    (bind ?result nil)
    (while (?it next)
        (bind ?card (?it getObject c))
        (if (batte ?card ?result ?seme) then
            (bind ?result ?card)
        )
    ) 
    return ?result
)

( deffunction get-minor-valore (?it)
    (if (?it next) then
        (bind ?min (?it getObject c))
        (while (?it next)
            (if ( < (((?it getObject c)  getRank) getPosition)  ((?min getRank) getPosition) ) then
                (bind ?min (?it getObject c) )
            )
        )
        return ?min
    else
        return nil
    )
)

( deffunction get-punti-in-tavola () "Calcola quanti punti sono stati giocati fin'ora"
    (bind ?it (run-query* carte-in-tavola))
    (bind ?counter 0)
    (while (?it next)
        (bind ?counter (+ ((?it getObject r) getValue) ?counter) )
    )
    return ?counter
)


( deffunction batte (?c ?max ?seme-mano)
    "dice se c batte max"

        (if (not(instanceof ?c briscola.objects.Card)) then
            return FALSE    
        else
        (if (not(instanceof ?max briscola.objects.Card)) then
            return TRUE 
        else

       ; (debug (create$  "confronto fra max, " (?max toString) " e c: " (?c toString)))
        ;;  se c è briscola e max no
        (if (and (= (?c getSuit) ?*briscola*) (<> (?max getSuit) ?*briscola*)) then
            ;(bind ?max ?c)
            ;(debug (create$ (?c toString) " è briscola, " (?max toString) " no"))
            return TRUE
        else
        ;;  se max è briscola e c no
        (if (and (= (?max getSuit) ?*briscola*) (<> (?c getSuit) ?*briscola*)) then
            ;(bind ?max ?max)
            ;(debug (create$ (?max toString) " è briscola, " (?c toString) " no"))
            return FALSE
        else
        ;; se c non è seme di mano nè briscola ha perso sicuro
        (if (and (<> (?c getSuit) ?seme-mano) (<> (?c getSuit) ?*briscola*)) then
            ;(bind ?max ?max)
            ;(debug (create$ (?c toString) " non è seme mano nè briscola"))
            return FALSE
        else
        ;;  o sono tutti e due briscole o seme di mano
        (if (> (?c getPosition) (?max getPosition)) then
            ;(bind ?max ?c)
            ;(debug (create$ "tutti e due" (?c toString) "prende il posto di " (?max toString)))
            return TRUE
        ))))))
)

( deffunction gioca (?c ?int)    "se c non nulla, la metto in memoria per essere giocata"
    (if (instanceof ?c briscola.objects.Card) then
        (store DA-GIOCARE ?c)
        (assert (da-giocarsi (card ?c) (sal ?int)))
        ;;; METTERE QUALCOSA CHE FERMI TUTTO IL RESTO!!
    else
        (debug "provando a giocare una carta nulla. Si lascia al caso")
    )
)



;;;     Funzioni di analisi 
;;;     ( asseriscono fatti prima di giocare ) 

( deffunction analizza-carichi-in-mano ()
    "Asserisco fatti riguardo ai miei carichi"
    (bind ?it (run-query* carichi-in-mano ?*briscola*))
    (bind ?piubasso nil)
    (bind ?piualto nil)
    (while (?it next)
        (if (or (not (instanceof ?piubasso briscola.objects.Card)) (> (?piubasso getValue) ((?it getObject c) getValue)) )then 
            (bind ?piubasso (?it getObject c))
        )
        (if (or (not (instanceof ?piualto briscola.objects.Card)) (< (?piualto getValue) ((?it getObject c) getValue)) )then 
            (bind ?piualto (?it getObject c))
        )
        (assert (carichi-in-mano (card (?it getObject c)) (rank (?it getObject r)) (suit (?it getObject s))    (points ((?it getObject r) getValue)) ))
    )

    (if (<> ?piubasso nil) then
        (assert (carico-piu-basso (card ?piubasso)))
    )
    (if (<> ?piualto nil) then
        (assert (carico-piu-alto (card ?piualto)))
    )
)

( deffunction analizza-lisci-in-mano ()
    "Asserisco fatti riguardo ai miei lisci"
    (bind ?it (run-query* lisci-in-mano ?*briscola*))
    (bind ?piualto nil) ;; sarebbe PIUBASSO in realta'....
    (while (?it next)
        (if (or (not (instanceof ?piualto briscola.objects.Card)) (> (?piualto getValue) ((?it getObject c) getValue)) )then 
            (bind ?piualto (?it getObject c))
        )
        (assert (lisci-in-mano (card (?it getObject c)) (rank (?it getObject r)) (suit (?it getObject s))    ))
        ;(debug (create$ carico in mano: ((?it getObject c) toString)))
    )

    (if (<> ?piualto nil) then
        (assert (liscio-piu-basso (card ?piualto)))
    )
)



( deffunction analizza-possibili-prese (?sta-prendendo ?seme-mano)
    "Asserisco fatti riguardo le carte con le quali posso prendere"
    (bind ?im (run-query* carte-in-mano ))
    (bind ?piubassa nil)
    (while (?im next)
        (if (batte (?im getObject c) ?sta-prendendo ?seme-mano) then
            (if (or (not (instanceof ?piubassa briscola.objects.Card)) (batte ?piubassa  (?im getObject c) ?seme-mano) ) then 
                (bind ?piubassa (?im getObject c))
            )
            (assert (posso-prendere (card (?im getObject c)) (rank (?im getObject r))  (suit (?im getObject s))))
;            (debug (create$ posso prendere con ((?im getObject c) toString) sta prendendo (?sta-prendendo toString))) 
        else
;            (debug (create$ NON posso prendere con ((?im getObject c) toString) sta prendendo (?sta-prendendo toString))) 
            
        )
    )
    (if (instanceof ?piubassa briscola.objects.Card) then
        (assert (piu-bassa-che-prende (card ?piubassa)))
    )
)


;;;;; TODO:
( deffunction piu-bassa-che-prende (?seme-mano)
    "Data una carta, dico se fra quelle in mano è la più bassa che prende"
    (bind ?it (run-query* prendono-in-mano))
    (?it next)
    (bind ?card (?it getObject c))
    (while (?it next)
        (bind ?temp (?it getObject c))
        (if (batte ?card ?temp) then
            (bind ?card ?temp)
        )
    )
    return ?card
)


( deffunction carta-da-giocarsi ()
    "Tra tutte le carte selezionate, scelgo quella con priorità maggiore"
    (bind ?it (run-query* da-giocarsi))
    (?it next)
    (bind ?card (?it getObject c))
    (bind ?sal (?it getObject n))
    (while (?it next)
        (if ( > (?it getObject n) ?sal) then
            (bind ?card (?it getObject c))
            (bind ?sal (?it getObject n))
        )
    )
    return ?card
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     QUERIES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defquery da-giocarsi "Ritorna tutte le carte selezionate da giocare"
    (da-giocarsi (card ?c) (sal ?n))
)

( defquery prendono-in-mano "Ritorna tutte le carte che prendono"
    (posso-prendere (card ?c) (rank ?r) (suit ?s))
)

( defquery briscole-in-mano    "Ritorna tutte le briscole che ho in mano"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?seme) (rank ?r))
)

( defquery non-briscole-in-mano "Ritorna tutte le carte non briscole"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?s&:(<> ?seme ?s)) (rank ?r))
)

( defquery lisci-in-mano "I lisci che ho in mano"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?s&:(<> ?seme ?s)) (rank ?r&:(= 0 (?r getValue))))
)

( defquery carichi-in-mano "I carichi che ho in mano"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?s&:(<> ?seme ?s)) (rank ?r&:(< 0 (?r getValue))))
)

( defquery carte-in-tavola "Ritorna tutte le carte in tavola"
    (in-tavolo (card ?c) (rank ?r) (suit ?s))
)

( defquery carte-in-mano "Ritorna tutte le carte che ho in mano"
    (in-mano (card ?c) (suit ?s) (rank ?r))
)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;      RULES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defrule niente
    (initial-fact)
=>
    (debug "Reasoner ha inizio")
    (assert (mano-numero -1))
)

( deffunction init-mano (?number)
    (remove in-tavolo)
    (remove turno)
    (remove mano-numero)
    (remove prende)
    (remove giocata-numero)
    (remove seme-mano-fact)
    (remove punti-in-tavola)
    (remove posso-prendere)
    (remove analizza-giocate)
    (remove calcola-giocata)
    (remove carichi-in-mano)
    (remove carico-piu-alto)
    (remove posso-prendere)
    (remove mio-turno-numero)

    (assert (seme-mano-fact (suit nil)))
    (assert (mano-numero ?number))
    (assert (prende (player nil) (card nil) (rank nil) (suit nil)))
    (assert (giocata-numero -1))
    (store DA-GIOCARE nil)
    ;(debug (create$ "inizializzando mano" ?number))

)


( defrule nuova-giocata "Ricevo una giocata: aggiorno la situa"
    ?w <- (nuova-giocata (player ?p) (card ?c) (rank ?r) (suit ?s))
    (prende (player ?prende-player) (card ?prende-card))
    ?y <- (giocata-numero ?counter-giocata)
    (seme-mano-fact (suit ?seme-mano))
=>

    ;(debug (create$ "ricevuta giocata " ?counter-giocata (?p toString) " " (?c toString) ))

    ;;  Aggiorniamo chi prende
    (if (= ?counter-giocata -1) then
        (remove prende)
        (assert (prende (player ?p) (card ?c) (suit (?c getSuit)) (rank (?c getRank))))
        (remove seme-mano-fact)
        (assert (seme-mano-fact (suit ?s)))
        (bind ?seme-mano ?s)
    else

    (if (batte ?c ?prende-card ?seme-mano )  then
        ;(debug (create$ "non prende più " (?prende-player toString) " con " (?prende-card toString) " bensì " (?p toString) " con " (?c toString) " seme mano: " (?seme-mano toString)))
        (remove prende)
        (assert (prende (player ?p) (card ?c) (suit (?c getSuit)) (rank (?c getRank))))

    ))

    (bind ?new-counter-giocata (+ ?counter-giocata 1))

    (retract ?w)
    (retract ?y)

    (assert (giocata-numero ?new-counter-giocata))
)

( defrule tocca-a-me "quando è il mio turno, meglio che giochi!"
    ?mio-turno <- (mio-turno)
    (in-mano (card ?c))
=>
    ;(printout t "potrei giocare " (?c toString) crlf)
    ;(store IOOCARE ?c)
    (assert (analizza-giocate))
    (retract ?mio-turno)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   ANALISI DELLA SITUAZIONE
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defrule analisi-propria-mano "Che carte ho?"

    ?w <- (analizza-giocate)
    (giocata-numero ?counter-giocata)
    (seme-mano-fact (suit ?seme-mano))
    (prende (player ?prende-player) (card ?prende-card) (suit ?prende-suit) (rank ?prende-rank))

=>
    ;;  quanti punti ci sono in tavola
    ;;  (punti-in-tavola n)
    (bind ?punti-in-tavola (get-punti-in-tavola))
    (assert (punti-in-tavola ?punti-in-tavola))

    ;;  ho dei carichi?
    ;;  (carichi-in-mano (card) (rank) (suit) (points)
    (analizza-carichi-in-mano)

    ;;  posso prendere?
    ;;  (posso-prendere (card) (rank) (suit))
    (if (> ?counter-giocata -1) then
        (analizza-possibili-prese ?prende-card ?seme-mano)
    )


    ;(debug (create$ in tavola ci sono ?punti-in-tavola))
    (assert (calcola-giocata))
    (retract ?w)

)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   REGOLE GENERALI
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defrule se-posso-prendere-punti-gratis-villano-prendo "Se ho un carico che può prendere tutto"
    ;;  importante!
    ;(declare (salience 100))
    (mio-ruolo villano)
    (mano-numero ?m)
    ?w <- (calcola-giocata)
    (mio-turno-numero 5)
    (briscola (card ?b))
    (posso-prendere (card ?c&:(= ?c (get-piu-alta-seme ?s))) (suit ?s&:(<> ?s ?*briscola*)))
    (punti-in-tavola ?punti&:(> ?punti 0))
    (giaguaro (player ?g))
    (turno (player ?player&:(= ?player ?g)) (posizione ?pos&:(<> ?pos 1)))
=>
    (gioca ?c 90)
    (debug (create$ sono ultimo di mano, prendo gratis perchè ci sono punti e non lascio giaguaro ultimo! (?c toString) ?m ))
    (retract ?w)
)

( defrule se-posso-prendere-gratis-villano-prendo-senza-avv-giaguaro "Se ho un carico che può prendere tutto"
    ;;  importante!
    ;(declare (salience 100))
    (mio-ruolo villano)
    (mano-numero ?m)
    ?w <- (calcola-giocata)
    (mio-turno-numero 5)
    (punti-in-tavola ?punti&:(> ?punti 9))
    (briscola (card ?b))
    (posso-prendere (card ?c&:(= ?c (get-piu-alta-seme ?s))) (suit ?s&:(<> ?s ?*briscola*)))
=>
    (gioca ?c 100)
    (debug (create$ sono ultimo di mano, prendo gratis perchè non più di 9 punti! (?c toString) ?m ))
    (retract ?w)
)

( defrule se-villano-non-ultimo-dopo-chiamante-non-prendo "Se villano e non ultimo, lascio il giaguaro prima di me"
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mio-turno-numero ?n&:(<> ?n 5))
    (giaguaro (player ?g))
    (turno (player ?io&:(= ?io (fetch IO))) (posizione ?n))
    (turno (player ?player&:(= ?player ?g)) (posizione ?pos&:(= ?pos (- ?n 1))))
    (liscio-piu-basso (card ?c))
=>
    (gioca ?c 80)
    (debug (create$ gioco prima del chiamante, lascio))
    (retract ?w)
)

( defrule socio-tiene-giaguaro-ultimo "lasciamo il giaguaro ultimo nelle mani finali"
    ?w <- (calcola-giocata)
    (mano-numero ?mano)
    (mio-ruolo socio)
    (socio (player ?socio))
    (socio-forte ?forza)
    (giaguaro (player ?g))
    (turno (player ?io&:(= ?io (fetch IO))) (posizione ?n))
    (turno (player ?player&:(= ?player ?g)) (posizione ?pos&:(= ?pos (+ ?n 1))))
    (briscola (card ?b))
    (posso-prendere (card ?c&:(or (> ?mano 4) (or (> ?forza 20) (or (<> ?c ?b) (= ?socio (fetch IO)))))))
=>
    (gioca ?c 50)
    (debug (create$ sono socio -scoperto o forte: ?forza- per lasciare al giaguaro ultimo alla mano gioco (?c toString)))
    (retract ?w)
)

( defrule villano-prende-prima-giaguaro "villano prova a prendere prima del giaguaro"
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (giaguaro (player ?g))
    (turno (player ?io&:(= ?io (fetch IO))) (posizione ?n))
    (turno (player ?player&:(= ?player ?g)) (posizione ?pos&:(= ?pos (- ?n 1))))
    ;(turno (player ?g) (posizione ?pos));&:(= ?pos (- ?n 1))))
    ;(turno (player ?g) (posizione ?m));&:(= ?m (- 1 ?m))))
    ;(punti-in-tavola ?x)
    ;(posso-prendere (card ?c))
    (seme-mano-fact (suit ?seme-mano))
    (posso-prendere (card ?c));&:(= ?c (piu-bassa-che-prende ?seme-mano))) )  
=>
    (debug (create$ gioco la più bassa che prende (?c toString), per tenere giaguaro ultimo))
    (gioca ?c 0)
    (retract ?w)
)


( defrule villano-carica-se-villano-prende
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (giaguaro (player ?gia))
    (socio (player ?soc))
    (briscola (suit ?briscola))
    (turno (player ?io&:(= ?io (fetch IO))) (posizione ?n))
    (turno (player ?player&:(= ?player ?gia)) (posizione ?pos&:(< ?pos ?n)))
    (turno (player ?player2&:(= ?player2 ?soc)) (posizione ?pos2&:(< ?pos2 ?n))) 
    (prende (player ?prende&:(and (<> ?prende ?soc) (<> ?prende ?gia) )))
    ;(in-mano (card ?carico));&:(= ?carico (get-maggior-carico ?briscola))))
    (carico-piu-alto (card ?carico))
    ;(carichi-in-mano (card ?carico&:(= ?carico (get-maggior-carico))))
=>
    
    (debug (create$ carico al massimo perchè prende il mio compagno (?prende toString)  (?carico toString)))
    (gioca ?carico 0)
    (retract ?w)
    ;(debug (create$ il mio maggior carico e (get-maggior-carico ?briscola)))
)



( defrule villano-prima-di-giaguaro "Se sono villano e gioco per primo, giag secondo"
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mio-turno-numero 0)
    (giaguaro (player ?gia))
    (turno (player ?player&:(= ?player ?gia)) (posizione ?pos&:(= ?pos 1)))
    ;(in-mano (card ?c&:(( and (> (?c getValue) 0)) (< (?c getValue) 5)) ) (suit ?s&:(<> ?s ?*briscola*)))
    (carico-piu-basso (card ?c))
=>
    (debug (create$ essendo io primo e giaguaro secondo, gioco una carta alta da k a j))
    (gioca ?c 80)
    (retract ?w)
)







;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   PRIMA MANO  #0
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




( defrule gioca-mano-0-giaguaro "Gioca la carta minore che ha"
    ?w <- (calcola-giocata)
    (mano-numero 0)
    (mio-ruolo giaguaro)
=>
    (bind ?it (run-query* non-briscole-in-mano ?*briscola*))
    (bind ?da-giocare  (get-minor-valore ?it))
    (debug  (create$ "Sono il giaguaro. Gioco la più bassa: " (?da-giocare toString)))
    (gioca ?da-giocare 0)
    (retract ?w)
)


( defrule gioca-mano-0-socio "quando è il mio turno, meglio che giochi!"
    ?w <- (calcola-giocata)
    (mio-ruolo socio)
    (mano-numero 0)
    (carico-piu-basso (card ?c))
    (mio-turno-numero ?n&:(< ?n 4))
=>
    (debug (create$ sono il socio e gioco un piccolo carico per confondere le idee. (?c toString) ))
    (gioca ?c 70)
    (retract ?w)
)

( defrule gioca-mano-0-socio-ultimo
    ?w <- (calcola-giocata)
    (mio-ruolo socio)
    (mano-numero 0)
    (mio-turno-numero 5)
    (posso-prendere (card ?c) (suit ?s&:(<> ?s ?*briscola*)))
=>
    (debug (create$ sono il socio,ultimo, e prendo senza una briscola ))
    (gioca ?c 70)
    (retract ?w)
)




( defrule gioca-villano-0-carico "quando è il mio turno, meglio che giochi!"
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mano-numero 0)
    (mio-turno-numero ?n&:(< ?n 3))
    (carico-piu-alto (card ?c))
=>
    (debug "sono il villano, ho compagni dietro, gioco un carico alto. ")
    (gioca ?c 50)
    (retract ?w)
)

( defrule gioca-villano-0-sciallo
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mano-numero 0)
    (mio-turno-numero ?n&:(= ?n 3))
    (carico-piu-basso (card ?c))
=>
    (debug "sono il villano, forse ho compagni dietro, gioco un carico basso. ")
    (gioca ?c 50)
    (retract ?w)
)

( defrule gioca-villano-0-prende
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mano-numero 0)
    (mio-turno-numero 4)
    (briscola (card ?briscola))
    (piu-bassa-che-prende (card ?c&:(<> ?c ?briscola)))
    (punti-in-tavola ?n&:(> ?n 0))
=>
    (debug "sono il villano da ultimo e prendo. ")
    (gioca ?c 50)
    (retract ?w)
)
    




