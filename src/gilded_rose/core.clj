(ns gilded-rose.core
  (:require [gilded-rose.item-update :refer :all]))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn with-type [type item]
  (assoc item :type type))

(def vest (with-type :normal (item "+5 Dexterity Vest" 10 20)))
(def brie (with-type :cheese (item "Aged Brie" 2 0)))
(def elixir (with-type :normal (item "Elixir of the Mongoose" 5 7)))
(def sulfuras (with-type :legendary (item "Sulfuras, Hand of Ragnaros" 0 80)))
(def passes (with-type :passes (item "Backstage passes to a TAFKAL80ETC concert" 15 20)))

(defn update-current-inventory[]
  (let [inventory [vest, brie, elixir, sulfuras passes]] (map update-item inventory)))
