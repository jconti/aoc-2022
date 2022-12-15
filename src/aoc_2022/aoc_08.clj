(ns aoc-2022.aoc-08
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [clojure.test :refer [with-test is are]]))

(defn char->int [c] (- (int c) (int \0)))

;; This data structure is the result of a silly blunder in the final count
;; that inspired me to add a coordinate system to make sure I was only counting
;; each tree once. When this approach changed nothing I realized I had made a
;; simply silly blunder. But it is interesting how extra info can be helpful.

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

(defn view-distance
  "Given `sq` where the tree house is located at the head position
   and the rest is the tree data in one direction, return the sq
   of trees that are in view from the house, ie. up until the first
   tree that is <= the house height."
  [[[_ _ house-h :as house] & trees]]
  (reduce (fn [sq [_ _ tree-h :as tree]]
            (let [result (conj sq tree)]
             (if (> house-h tree-h)
               result
               (reduced result))))
          []
          trees))

;; (view-distance [[0 0 5] [0 1 4] [0 2 5] [0 3 5]])
;; (view-distance [[0 0 5] [0 1 7]])
;; (view-distance [[0 0 5]])

(defn we-dir
  [x y]
  (second (split-at y (nth grid x))))

(defn ns-dir
  [x y]
  (for [row (second (split-at x grid))]
    (nth row y)))

(defn ew-dir
  [x y]
  (reverse (first (split-at (inc y) (nth grid x)))))

(defn sn-dir
  [x y]
  (reverse
   (for [row (first (split-at (inc x) grid))]
     (nth row y))))

(def part-2
  "Value is `385112`"
  (->> (for [x (range grid-size) y (range grid-size)]
         ((juxt we-dir ns-dir ew-dir sn-dir) x y))
       (map (partial map view-distance))
       (map (partial map count))
       (map (partial apply *))
       (reduce max)))

part-2
;; => 385112
