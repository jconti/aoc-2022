(ns aoc-2022.aoc-06
  (:require
   [aoc-2022.common :as common]))

(def data
  "Data is a seq of characters"
  (-> "aoc-06-input.txt"
      common/read-input
      first
      seq))

(defn start?
  [n cs]
  (-> cs set count (> (dec n))))

(defn sub-sq
  [len start sq]
  (take len (drop start sq)))

(defn find-data-start
  [data len]
  (reduce
   (fn [_ si]
     (when (start? len (sub-sq len si data))
       (reduced (+ si len))))
   nil
   (range)))

(def part-1
  "Value `1707`"
  (find-data-start data 4))

(def part-2
  "Value is `3697`"
  (find-data-start data 14))
