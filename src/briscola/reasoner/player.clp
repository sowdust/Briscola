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

( deftemplate giaguaro  (slot player) )
( deftemplate socio (slot player) )
( deftemplate villano   (slot player) )
( deftemplate briscola (slot card) (slot rank) (slot suit) )



;;  if set to true debug statements are printed
( defglobal ?*debug* = FALSE)

;; stores the briscola suit (initially null)
;( defglobal ?*briscola* = nil)
( defglobal ?*da-giocare* = nil)


;; PRINTS DEBUG MESSAGES IF GLOBAL VAR ?*debug* SET TO TRUE
( deffunction debug (?list)
    (if ?*debug* then
        (printout t ?list crlf)
    )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;      RULES
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


( defrule gioca "quando è il mio turno, meglio che giochi!"
    ?mio-turno <- (mio-turno)
    (in-mano (card ?c))
=>
    (printout t "potrei giocare " (?c toString) crlf)
    (store DA-GIOCARE ?c)
    (retract ?mio-turno)
)