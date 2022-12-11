(ns aoc-2022.aoc-07
  (:require
   [aoc-2022.common :as common]
   [clojure.edn :as edn]
   [clojure.string :as str]))

(defn apply-input
  "Alter `state` the command in the `line` vector of string."
  [{:keys [path sizes] :as state} [p1 p2 p3 :as line]]

  (cond
    ;; cd to root
    (and (= p1 "$") (= p2 "cd") (= p3 "/"))
    (assoc state :path [])

    ;; cd to a sub-dir
    (and (= p1 "$") (= p2 "cd") (not= p3 ".."))
    (update state :path conj p3)

    ;; cd to parent dir
    (and (= p1 "$") (= p2 "cd") (= p3 ".."))
    (update state :path pop)

    ;; accumulate names of dirs, initialize size
    (= p1 "dir")
    (update-in state (concat [:sizes] path [p2]) #(or % {:size 0}))

    ;; add file size into directory
    (re-find #"^\d+$" p1)
    (update-in state (concat [:sizes] path [:size]) #(+ (or % 0) (edn/read-string p1)))

    :else state))

(defn compute-sizes
  [sizes-map]
  (reduce-kv (fn [m k v]
               (if (= k :size)
                 m
                 (let [{sz :size :as sm} (compute-sizes v)]
                   (-> m (assoc k sm) (update :size + sz)))))
             sizes-map
             sizes-map))

(defn size-seq
  [{:keys [size] :as sizes-map}]
  (cons size (mapcat (comp size-seq second) (dissoc sizes-map :size))))

(def base-state {:path [] :sizes {}})

(def data
  "Data is a seq of lines broken into vectors of string tokens."
  (->> "aoc-07-input.txt"
       common/read-input
       (map #(str/split % #"\s+"))))

(def result-state
  (update
   (reduce apply-input base-state data)
   :sizes
   compute-sizes))

(def part-1
  "Value is `1427048`"
  (->> result-state
       (:sizes)
       (size-seq)
       (filter #(<= % 100000))
       (reduce +)))

(def part-2
  "Value is `2940614`"
  (let [disk-space 70000000
        needed-space 30000000
        used-space (-> result-state :sizes :size)
        free-space (- disk-space used-space)
        min-to-delete (- needed-space free-space)]
    (->> result-state
         :sizes
         size-seq
         sort
         (drop-while (partial > min-to-delete))
         first)))
