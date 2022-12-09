(ns aoc-2022.aoc-05
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]
   [clojure.string :as str]))

(defn ->init-state
  "Create a vector of vectors of characters. [0,0] is character of
   the lowest crate of the first stack."
  [lines]
  (let [[column-number-line & data-lines] (reverse lines)]
    (vec
     (for [ci (map (partial str/index-of column-number-line)
                   ["1" "2" "3" "4" "5" "6" "7" "8" "9"])]
       (vec
        (for [line data-lines
              :let [c (get line ci)]
              :while (not= c \space)]
          c))))))

(defn ->moves
  "Turn each move into a map of string to number, either a vector
   index or quantity of crates to move."
  [lines]
  (->> lines
       (map #(str/split % #"\s"))
       (map (partial apply array-map))
       (map #(update % "move" edn/read-string))
       (map #(update % "to" common/->vec-i))
       (map #(update % "from" common/->vec-i))))

(defn parse-input
  "Input file is initial state definition text, followed by a blank line,
   followed by text describing one move per line. Return a map with data."
  [input]
  (let [[init-state-lines _ move-lines]
        (partition-by common/blank-line? input)]
    {:init-state (->init-state init-state-lines)
     :moves (->moves move-lines)}))

(defn apply-move
  "Given a state and a move map, return a new state with the move applied."
  [state {:strs [move from to]}]
  (reduce
   (fn [state _]
     (let [crate (-> state
                     (nth from)
                     last)]
       (-> state
           (update to conj crate)
           (update from pop))))
   state
   (range move)))

(def data (-> "aoc-05-input.txt" common/read-input parse-input))

(defn top-crates [state]
  (apply str (map last state)))

(def part-1
  "Value \"VGBBJCRMN\""
  (let [{:keys [init-state moves]} data]
    (top-crates (reduce apply-move init-state moves))))

(defn apply-9001-move
  [state {:strs [move from to]}]
  (let [crates (take-last move (nth state from))]
    (-> state
        (update to into crates)
        (update from (comp vec (partial drop-last move))))))

(def part-2
  "Value \"LBBVJBRMH\""
  (let [{:keys [init-state moves]} data]
    (top-crates (reduce apply-9001-move init-state moves))))
