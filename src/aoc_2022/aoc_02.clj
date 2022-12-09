(ns aoc-2022.aoc-02
  (:require
   [aoc-2022.common :as common]))

(def input (common/read-input "aoc-02-input.txt"))

(def scoring-part-1   ; them,me,base,score
  (hash-map "A X" 4   ; rock,rock,1,3
            "B X" 1   ; paper,rock,1,0
            "C X" 7   ; scissors,rock,1,6
            "A Y" 8   ; rock,paper,2,6
            "B Y" 5   ; paper,paper,2,3
            "C Y" 2   ; scissors,paper,2,0
            "A Z" 3   ; rock,scissors,3,0
            "B Z" 9   ; paper,scissors,3,6
            "C Z" 6)) ; scissors,scissors,3,3

(def part-1
  "Value is `12772`"
  (->> input
       (map scoring-part-1)
       (reduce +)))

(def scoring-part-2   ; them,result,me,base,score
  (hash-map "A X" 3   ; rock,lose,scissors,3,0
            "B X" 1   ; paper,lose,rock,1,0
            "C X" 2   ; scissors,lose,paper,2,0
            "A Y" 4   ; rock,draw,rock,1,3
            "B Y" 5   ; paper,draw,paper,2,3
            "C Y" 6   ; scissors,draw,scissors,3,3
            "A Z" 8   ; rock,win,paper,2,6
            "B Z" 9   ; paper,win,scissors,3,6
            "C Z" 7)) ; scissors,win,rock,1,6

(def part-2
  "Value is `11618`"
  (->> input
       (map scoring-part-2)
       (reduce +)))
