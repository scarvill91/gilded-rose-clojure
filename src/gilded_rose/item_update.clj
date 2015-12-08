(ns gilded-rose.item-update)

(defmulti update-item (fn [item] (:type item)))

(defmethod update-item :normal [{:keys [sell-in quality] :as item}]
  (assoc item :sell-in (dec sell-in)
              :quality (max 0 (if (< 0 sell-in) (- quality 1) (- quality 2)))))

(defmethod update-item :cheese [{:keys [sell-in quality] :as item}]
  (assoc item :sell-in (dec sell-in)
              :quality (min 50 (inc quality))))

(defmethod update-item :passes [{:keys [sell-in quality] :as item}]
  (assoc item :sell-in (dec sell-in)
              :quality (min 50 (cond (< 10 sell-in)  (+ 1 quality)
                                     (< 5 sell-in)   (+ 2 quality)
                                     (< 0 sell-in)   (+ 3 quality)
                                     :else           0))))

(defmethod update-item :legendary [item] item)
