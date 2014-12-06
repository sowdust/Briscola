(import briscola.objects.*)


(defclass Deck briscola.objects.Deck)
(defclass Suit briscola.objects.Suit)
(defclass Card briscola.objects.Card)
(defclass Rank briscola.objects.Rank)

(bind ?deck (new Deck))
(bind ?card (?deck get 0))
(printout t "card: " (?card toString) crlf)
(?deck shuffle)
(bind ?card (?deck get 0))
(printout t "card: " (?card toString) crlf)




(defrule ciao
    (initial-fact)
=>
    (printout t "ricomincia da 10000" crlf)

)
