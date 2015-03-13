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

( deftemplate mio-ruolo "io che sono?"
    (slot ruolo)
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;     UTILITIES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;  if set to true debug statements are printed
( defglobal ?*debug* = TRUE)


;; PRINTS DEBUG MESSAGES IF GLOBAL VAR ?*debug* SET TO TRUE
( deffunction debug (?list)
    (if ?*debug* then
        (printout t ?list crlf)
    )
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
    (assert (mano-numero ?number))
    (debug (create$ "inizializzando mano" ?number))
)


( defrule nuova-giocata "Ricevo una giocata: aggiorno la situa"
    ?w <- (nuova-giocata (player ?p) (card ?c) (rank ?r) (suit ?s))
=>
    (debug (create& "ricevuta giocata " (?p toString) " " (?c toString) ))
    (retract ?w)
)

( defrule gioca "quando è il mio turno, meglio che giochi!"
    ?mio-turno <- (mio-turno)
    (in-mano (card ?c))
=>
    (printout t "potrei giocare " (?c toString) crlf)
    (store DA-GIOCARE ?c)
    (retract ?mio-turno)
)
