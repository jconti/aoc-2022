(ns aoc-2022.aoc-01
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]))

(def input-filename "aoc-01-input.txt")

(def totals
  "Total calories of every elf sorted from greatest to least."
  (->> input-filename
       common/read-input
       (partition-by (comp some? seq))
       (map (partial filter seq))
       (filter seq)
       (map (partial map edn/read-string))
       (map (partial reduce +))
       (sort)
       (reverse)))

(def part-1 (first totals))
(def part-2 (apply + (take 3 totals)))
