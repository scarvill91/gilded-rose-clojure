(ns gilded-rose.core)

(defn set-type [type item]
  (assoc item :type type))

(defmulti update-item (fn [item] (:type item)))

(defmethod update-item :default [item] item)

(defmethod update-item ::normal [{:keys [sell-in quality] :as item}]
  (assoc item :quality (max 0 (if (<= sell-in 0) (- quality 2) (- quality 1)))
              :sell-in (dec sell-in)))

(defmethod update-item ::cheese [{:keys [sell-in quality] :as item}]
  (assoc item :quality (min 50 (inc quality))
              :sell-in (dec sell-in)))

(defmethod update-item ::passes [{:keys [sell-in quality] :as item}]
  (assoc item :quality (min 50 (cond (< 10 sell-in)  (+ 1 quality)
                                     (< 5 sell-in)   (+ 2 quality)
                                     (< 0 sell-in)   (+ 3 quality)
                                     :else           0))
              :sell-in (dec sell-in)))

(defmethod update-item ::legendary [item] item)

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(def vest (set-type ::normal (item "+5 Dexterity Vest" 10 20)))
(def brie (set-type ::cheese (item "Aged Brie" 2 0)))
(def elixir (set-type ::normal (item "Elixir of the Mongoose" 5 7)))
(def sulfuras (set-type ::legendary (item "Sulfuras, Hand of Ragnaros" 0 80)))
(def passes (set-type ::passes (item "Backstage passes to a TAFKAL80ETC concert" 15 20)))

(defn update-current-inventory[]
  (let [inventory [vest, brie, elixir, sulfuras passes]] (map update-item inventory)))
