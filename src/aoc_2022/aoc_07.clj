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

    ;; cd to parent dir, add sub-dir's size into parent
    (and (= p1 "$") (= p2 "cd") (= p3 ".."))
    (-> state (update-in [:sizes (pop path)] #(+ (or % 0) (sizes path))) (update :path pop))

    ;; accumulate dir size
    (re-find #"^\d+$" p1)
    (update-in state [:sizes path] #(+ (or % 0) (edn/read-string p1)))

    ;; other commands un-needed
    :else state))

(def base-state {:path [] :sizes {}})

(def data
  "Data is a seq of lines broken into vectors of string tokens."
  (->> "aoc-07-input.txt"
       common/read-input
       (map #(str/split % #"\s+"))))

;; (take 5 data)
;; => (["$" "cd" "/"] ["$" "ls"] ["dir" "ctd"] ["80649" "mwcj.pmh"] ["212527" "nbb.ztq"])

(def result-state
  (reduce apply-input base-state data))

(def part-1
  "Value is `1427048`"
  (->> result-state
       (:sizes)
       (vals)
       (filter #(<= % 100000))
       (reduce +)))

(def part-2
  (let [disk-space 70000000
        needed-space 30000000
        free-space (- disk-space (-> result-state :sizes (get [])))
        min-to-delete (- needed-space free-space)]
    #p free-space
    #p min-to-delete
    #_(->> result-state
           :sizes
           vals
           sort
           #_(drop-while (partial < min-to-delete)))))
