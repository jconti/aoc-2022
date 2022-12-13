(ns aoc-2022.aoc-08
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]
   [clojure.string :as str]))

(defn char->int [c] (- (int c) (int \0)))

(def grid
  (vec (map-indexed
        (fn [x row]
          (vec (map-indexed
                (fn [y h]
                  (vector x y (char->int h)))
                row)))
        (common/read-input "aoc-08-input.txt"))))

(def grid-size (count (first grid)))

(def west->east grid)
(def north->south (partition grid-size (apply interleave grid)))
(def east->west (map reverse grid))
(def south->north (partition grid-size (apply interleave (reverse grid))))

(defn visible
  ([sq]
   (let [top (first sq)]
     (cons top (visible top (rest sq)))))
  ([[_ _ height :as top] [[_ _ cur-height :as cur] & trees]]
   (cond
     (nil? cur-height) nil
     (> cur-height height) (cons cur (visible cur trees))
     :else (visible top trees))))

;; (visible [[nil nil 1] [nil nil 2] [nil nil 2] [nil nil 1] [nil nil 4] [nil nil 3]])
;; => ([nil nil 1] [nil nil 2] [nil nil 4])

;; (mapcat visible grid)
;; (visible (second grid))
;; (distinct (map last (second grid)))

;; (+ 99 98 98 97)
;; => 392

(def part-1
;; => 1820
  (->> [west->east north->south east->west south->north]
       (mapcat (partial mapcat visible))
       (distinct)
       (count)))
