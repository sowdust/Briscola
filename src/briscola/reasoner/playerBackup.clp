(import briscola.objects.*)

(defclass Deck briscola.objects.Deck)
(defclass Suit briscola.objects.Suit)
(defclass Card briscola.objects.Card)
(defclass Rank briscola.objects.Rank)
(defclass Hand briscola.objects.Hand)
(defclass Player briscola.Player)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   tutti i tipi di fatti che ci sono utili (slot per ora ridondanti)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( deftemplate io "chi sono io"
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
        ))))
)



( deffunction gioca (?c)    "se c non nulla, la metto in memoria per essere giocata"
    (if (instanceof ?c briscola.objects.Card) then
        (store DA-GIOCARE ?c)
    else
        (debug "provando a giocare una carta nulla. Si lascia al caso")
    )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     QUERIES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defquery briscole-in-mano    "Ritorna tutte le briscole che ho in mano"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?seme) (rank ?r))
)

( defquery non-briscole-in-mano "Ritorna tutte le carte non briscole"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?s&:(<> ?seme ?s)) (rank ?r))
)

( defquery carte-in-tavola "Ritorna tutte le carte in tavola"
    (in-tavolo (card ?c) (rank ?r) (suit ?s))
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
        (if (not(instanceof ?c briscola.objects.Card) ) then
            (debug c card nulla!!!!)
        )
        (if (not(instanceof ?prende-card briscola.objects.Card) ) then
            (debug (create$ prende card nulla!!!! Contatore: ?counter-giocata))
        )

    (if (batte ?c ?prende-card ?seme-mano )  then
        (if (not(instanceof ?c briscola.objects.Card) ) then
            (debug prende card nulla!!!!)
        )
        ;(debug (create$ "non prende più " (?prende-player toString) " con " (?prende-card toString) " bensì " (?p toString) " con " (?c toString) " seme mano: " (?seme-mano toString)))
        (remove prende)
        (assert (prende (player ?p)  (suit (?c getSuit)) (rank (?c getRank))))

    ))

    (bind ?new-counter-giocata (+ ?counter-giocata 1))

    (retract ?w)
    (retract ?y)

    (assert (giocata-numero ?new-counter-giocata))
)

( defrule tocca-a-me "quando è il mio turno, meglio che giochi!"
    ?mio-turno <- (mio-turno)
    ;(in-mano (card ?c))
=>
    (assert (analizza-situa))
    (assert (punti-in-tavola (get-punti-in-tavola)))
    (retract ?mio-turno)
)

( defrule analizza-situazione
    (analizza-situa)
=>

    ;;  COMINCIAMO CON LO STUDIARE L'APPETITOSITÀ DELLA TAVOLA
    ;;      * da-prendere
    ;;      * da-prendere-assolutamente
    ;;      * da-lasciare
    (bind ?punti (get-punti-in-tavola))
    (debug (create$ ci sono ?punti punti in tavola) )
    (if (> ?punti 6) then
        (assert (da-prendere))
    )
    (if (= ?punti 0) then
        (assert (da-lasciare))
    )
    (if (> ?punti 30) then
        (assert (da-prendere-assolutamente))
    )


    ;;  VEDIAMO CIÒ CHE HO IN MANO
    (assert (calcola-giocata))
)

;( defrule analizza-situa-mano
;    (in-mano (rank ?r) (suit ?s) (card ?c))
;    (seme-mano (suit ?s))




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
    (gioca ?da-giocare)

    (retract ?w)
)



( defrule gioca1 "quando è il mio turno, meglio che giochi!"
    ?w <- (calcola-giocata)
    (mio-ruolo socio)
    (mano-numero 0)
=>
    (debug "sono il socio e gioco la più bassa che ho. ")
    (retract ?w)
)

( defrule gioca2 "quando è il mio turno, meglio che giochi!"
    ?w <- (calcola-giocata)
    (mio-ruolo villano)
    (mano-numero 0)
=>
    (debug "sono il villano e gioco la più bassa che ho. ")
    (retract ?w)
)
