(ns queen-of-hearts.core
  (:require [clojure.core.async :as async]
            [clojure.string :as str]))

(def flowers ["white carnation"
              "yellow daffodil"
              "yellow rose"
              "red rose"
              "white rose"
              "purple lily"
              "pink carnation"])

(defn paint-it-red [s]
  (str "red "
       (last (str/split s #"\s"))))

(defn is-a-rose? [s]
  (= "rose"
     (last (str/split s #"\s"))))

(def fix-for-the-queen-xform
  (comp (map paint-it-red)
        (filter is-a-rose?)))

;;; usage examples

(into [] fix-for-the-queen-xform flowers)

(sequence fix-for-the-queen-xform flowers)

(transduce fix-for-the-queen-xform
           (completing #(str %1 %2 ":"))
           ""
           flowers)

(def flower-chan (async/chan 1 fix-for-the-queen-xform))

(def result-chan (async/reduce (completing #(str %1 %2 ":"))
                               ""
                               flower-chan))

(async/onto-chan flower-chan flowers)

(def flowers-for-the-queen (async/<!! result-chan))
