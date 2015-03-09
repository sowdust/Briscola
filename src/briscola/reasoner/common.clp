(import briscola.objects.*)

(defclass Deck briscola.objects.Deck)
(defclass Suit briscola.objects.Suit)
(defclass Card briscola.objects.Card)
(defclass Rank briscola.objects.Rank)
(defclass Hand briscola.objects.Hand)
(defclass Player briscola.Player)

;;  if set to true debug statements are printed
(defglobal ?*debug* = FALSE)

;; stores the briscola suit (initially null)
(defglobal ?*briscola* = nil)







;;; PRINTS DEBUG MESSAGES IF GLOBAL VAR ?*debug* SET TO TRUE
(deffunction debug (?list)
    (if ?*debug* then
        (printout t ?list crlf)
    )
)

;;; GIVEN A LIST OF CARDS AND THE BRISCOLA, RETURNS THE WINNING CARD
(deffunction calcola_presa (?lista)
    "Calcola chi prende la mano"
    (bind ?c1 (nth$ 1 ?lista))
    (bind ?seme-mano (?c1 getSuit))
    (debug (create$ "seme di mano: " (?seme-mano toString)))
    (bind ?max ?c1)
    (foreach ?c ?lista
       (debug (create$  "confronto fra max, " (?max toString) " e c: " (?c toString)))
        ;;  se c è briscola e max no
        (if (and (= (?c getSuit) ?*briscola*) (<> (?max getSuit) ?*briscola*)) then
            (bind ?max ?c)
            (debug (create$ (?c toString) " è briscola, " (?max toString) " no"))
        else
        ;;  se max è briscola e c no
        (if (and (= (?max getSuit) ?*briscola*) (<> (?c getSuit) ?*briscola*)) then
            (bind ?max ?max)
            (debug (create$ (?max toString) " è briscola, " (?c toString) " no"))
        else
        ;; se c non è seme di mano nè briscola ha perso sicuro
        (if (and (<> (?c getSuit) ?seme-mano) (<> (?c getSuit) ?*briscola*)) then
            (bind ?max ?max)
            (debug (create$ (?c toString) " non è seme mano nè briscola"))
        else
        ;;  o sono tutti e due briscole o seme di mano
        (if (> (?c getPosition) (?max getPosition)) then
            (bind ?max ?c)
            (debug (create$ "tutti e due" (?c toString) "prende il posto di " (?max toString)))
        ))))
    )
    return ?max
)