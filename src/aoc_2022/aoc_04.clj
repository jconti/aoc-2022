(ns aoc-2022.aoc-04
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn range->set
  "Given \"5-7\" return `#{5 6 7}`"
  [r]
  (as-> r $
      (str/split $ #"\-")
      (mapv edn/read-string $)
      (update $ 1 inc)
      (apply range $)
      (set $)))

(defn parse-pair
  "Given \"5-7,1-2\" return `(#{5 6 7} #{1 2})`"
  [line]
  (map range->set (str/split line #",")))

(def data (->> "aoc-04-input.txt"
               common/read-input
               (map parse-pair)))

(defn count-by [pred coll]
  (->> coll
       (map (partial apply pred))
       (filter true?)
       (count)))

(defn fully-contains? [s1 s2]
  (let [i (set/intersection s1 s2)]
    (or (empty? (set/difference s1 i))
        (empty? (set/difference s2 i)))))

(def part-1
  "Value is `305`"
  (count-by fully-contains? data))

(defn overlaps? [s1 s2]
  (some? (seq (set/intersection s1 s2))))

(def part-2
  "Value is `811`"
  (count-by overlaps? data))
