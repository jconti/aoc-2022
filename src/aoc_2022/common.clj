(ns aoc-2022.common
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(def blank-line? (comp some? seq))

(def read-input (comp line-seq io/reader io/resource))

(def ->vec-i (comp dec edn/read-string))
