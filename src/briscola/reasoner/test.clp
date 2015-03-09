(import briscola.objects.*)

;;  if set to true debug statements are printed
(defglobal ?*debug* = FALSE)

(defclass Deck briscola.objects.Deck)
(defclass Suit briscola.objects.Suit)
(defclass Card briscola.objects.Card)
(defclass Rank briscola.objects.Rank)
(defclass Player briscola.objects.Rank)


(deffunction debug (?list)
    (if ?*debug* then
        (printout t ?list crlf)
    )
)


(deffunction calcolapresa (?lista ?briscola)
    "Calcola chi prende la mano"
    (bind ?c1 (nth$ 1 ?lista))
    (bind ?seme-mano (?c1 getSuit))
    (debug (create$ "seme di mano: " (?seme-mano toString)))
    (bind ?max ?c1)
    (foreach ?c ?lista
       (debug (create$  "confronto fra max, " (?max toString) " e c: " (?c toString)))
        ;;  se c è briscola e max no
        (if (and (= (?c getSuit) ?briscola) (<> (?max getSuit) ?briscola)) then
            (bind ?max ?c)
            (debug (create$ (?c toString) " è briscola, " (?max toString) " no"))
        else
        ;;  se max è briscola e c no
        (if (and (= (?max getSuit) ?briscola) (<> (?c getSuit) ?briscola)) then
            (bind ?max ?max)
            (debug (create$ (?max toString) " è briscola, " (?c toString) " no"))
        else
        ;; se c non è seme di mano nè briscola ha perso sicuro
        (if (and (<> (?c getSuit) ?seme-mano) (<> (?c getSuit) ?briscola)) then
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



;   non viene cancellato a reset
;(defglobal ?*briscola* = "valore briscola")
;   creazione di una lista piana
;(bind ?lista (create$ "el1" "el2" "el3" (create$ "el4" "el5"))
;   accesso a elemento n
;(bind ?secondo-elemento (nth$ 2 ?lista))
; foreach
;(foreach ?el ?lista
;    (printout t ?el crlf)
;)
; while e progn (progn serve a valutare più espressione dove ce ne può stare una sola)
;(while (progn (bind ?x 5) FALSE) do
;    (printout t "non mi stampo" crlf)
;)

(defrule iniziale
    (initial-fact)
=>
    (assert (do-it))
)

(defrule test-presa
    (do-it)
=>
    ;(printout t "ricomincia da 10000" crlf)

    (bind ?repetition 15)
    (printout t crlf "--------------------------" crlf)

    (while (>= ?repetition 0)

        (bind ?deck (new Deck))

        (printout t "Mischiando il mazzo" crlf)
        (?deck shuffle)
        (printout t "Carta #1: " ((?deck get 0) toString) crlf)
        (printout t "Carta #2: " ((?deck get 1) toString) crlf)
        (printout t "Carta #3: " ((?deck get 2) toString) crlf)
        (printout t "Carta #4: " ((?deck get 3) toString) crlf)
        (printout t "Carta #5: " ((?deck get 4) toString) crlf)
        (bind ?briscola ((?deck get 10) getSuit))
        (printout t "Briscola: " (?briscola toString) crlf)
        (bind ?lista (create$ (?deck get 0) (?deck get 1) (?deck get 2) (?deck get 3) (?deck get 4)))

        (bind ?prende (calcolapresa ?lista  ((?deck get 10) getSuit)))
        (printout t "Prende: " (?prende toString) crlf)
        (assert (do-it))

        (bind ?repetition (- ?repetition 1))
    )

)