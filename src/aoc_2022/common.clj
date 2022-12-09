(ns aoc-2022.common
  (:require
   [clojure.java.io :as io]))

(defn read-input [filename]
  (->> filename io/resource io/reader line-seq))
