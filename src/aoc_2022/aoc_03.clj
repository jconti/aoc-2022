(ns aoc-2022.aoc-03
  (:require
   [aoc-2022.common :as common]
   [clojure.set :as set]))

(def input (common/read-input "aoc-03-input.txt"))

(defn mistake
  [line]
  (as-> line $
    (seq $)
    (split-at (/ (count $) 2) $)
    (map set $)
    (apply set/intersection $)
    (first $)))

(def priorities
 (zipmap
  [\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z
   \A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q \R \S \T \U \V \W \X \Y \Z]
  (range 1 53)))

(def part-1
  "Value is `7903`"
  (->> input
       (map mistake)
       (map priorities)
       (reduce +)))

(defn badge
  [lines]
  {:pre [(= 3 (count lines))]}
  (->> lines
       (map (comp set seq))
       (apply set/intersection)
       (first)))

(def part-2
  "Value is `2548`"
  (->> input
       (partition 3)
       (map badge)
       (map priorities)
       (reduce +)))
