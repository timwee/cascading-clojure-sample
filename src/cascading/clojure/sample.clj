(ns cascading.clojure.sample
  (:use [clojure.contrib map-utils])
  (:require [clojure.contrib.str-utils2 :as s]))

(defn split-line [line] 
  (let [data (s/split line #"\t")]
    (cond (= 3 (count data)) (list data)
	  :otherwise (list (conj data "dummycontent")))))
(defn identity-each [& line]
  [line])

(defn second-of-line [line]
  [[(second (s/split line #"\t"))]])

(defn filter-dummycontent-name [name id]
  (not (= "dummycontent" name)))

(defn test-with-fields []
  {:operations {:each {:using split-line :reader identity :writer str :outputFields ["name" "id" "content"]}
		:each {:using identity-each :reader identity :writer str :inputFields ["name" "id"] :outputFields ["name" "id"]}
		:filter {:using filter-dummycontent-name :reader identity :writer str :inputFields ["name" "id"] :outputFields ["name" "id"]}}})
	
	

(defn append-str [acc nxt]
  (let [seen (first acc)]
    [(str seen nxt)]))	
				
(defn groupby-with-fields []
  {:operations {:groupBy {:groupby (fn [x] (rand-int 5)) :using second-of-line :reader identity :writer str :outputFields ["key" "second"]}
		:everygroup {:using append-str :reader identity :init (fn [] [""]) :writer str :inputFields ["second"] :outputFields ["combined"]}}})

