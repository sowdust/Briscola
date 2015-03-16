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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     QUERIES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defquery briscole-in-mano    "Ritorna tutte le briscole che ho in mano"
    (declare (variables ?seme))
    (in-mano (card ?c) (suit ?seme) (rank ?r))
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
    (assert (seme-mano-fact (suit nil)))
    (assert (mano-numero ?number))
    (assert (prende (player nil) (card nil)))
    (assert (giocata-numero -1))
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
        (assert (prende (player ?p) (card ?c)))
        (remove seme-mano-fact)
        (assert (seme-mano-fact (suit ?s)))
        (bind ?seme-mano ?s)
    else

    (if (batte ?c ?prende-card ?seme-mano )  then
        ;(debug (create$ "non prende più " (?prende-player toString) " con " (?prende-card toString) " bensì " (?p toString) " con " (?c toString) " seme mano: " (?seme-mano toString)))
        (remove prende)
        (assert (prende (player ?p) (card ?c)))

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
    (store DA-GIOCARE ?c)
    (assert (calcola-giocata))
    (retract ?mio-turno)
)

( defrule gioca "quando è il mio turno, meglio che giochi!"
    ?w <- (calcola-giocata)
    (mio-ruolo giaguaro)
    (mano-numero 0)
=>
    (debug "sono il giaguaro e gioco la più bassa che ho. ")
    (bind ?it (run-query* briscole-in-mano ?*briscola*))
    (while (?it next)
        (printout t ((?it getObject c) toString)  crlf)
    )
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
