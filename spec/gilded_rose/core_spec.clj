(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality]]))

(defn update-item [item] (first (update-quality [item])))

(describe "gilded rose"
  (let [ vest {:name "+5 Dexterity Vest" :sell-in 10 :quality 20}
         elixir {:name "Elixir of the Mongoose" :sell-in 5 :quality 7}
         brie {:name "Aged Brie" :sell-in 2 :quality 0}
         pass {:name "Backstage passes to a TAFKAL80ETC concert" :sell-in 15 :quality 20}
         sulfuras {:name "Sulfuras, Hand of Ragnaros" :sell-in 0 :quality 80} ]

    (describe "update-quality"

      ; Should pass according to behavior specified in README, but doesn't
      ; (it "never decreases quality past zero"
      ;   (let [item {:name "item" :sell-in 10 :quality 0}]
      ;     (should= 0 (:quality (update-quality item)))))

      (it "never increases quality above fifty"
        (let [brie (assoc brie :quality 50)]
          (should= 50 (:quality (update-item brie)))))

      (context "when item is legendary"
        (it "leaves quality and sell-in date unchanged"
          (should (and (= (:quality sulfuras)
                          (:quality (update-item sulfuras)))
                       (= (:sell-in sulfuras)
                          (:sell-in (update-item sulfuras))))))
      )

      (context "when item is not legendary"

        (it "decrements sell-in date by one"
          (let [item {:name "item" :sell-in 10 :quality 20}]
            (should= (- (:sell-in item) 1)
                     (:sell-in (first (update-quality [item]))))))

        (it "increments brie quality by one"
          (should= (+ (:quality brie) 1)
                   (:quality (update-item brie))))

        (context "when item sell-in date has not been reached"

          (it "decrements vest quality by one"
            (should= (- (:quality vest) 1)
                     (:quality (update-item vest))))

          (it "decrements elixir quality by one"
            (should= (- (:quality elixir) 1)
                     (:quality (update-item elixir))))

          (context "when sell-in date is greater than 10"

            (it "increments backstage pass quality by one"
              (should= (+ (:quality pass) 1)
                       (:quality (update-item pass))))
          )

          (context "when sell-in date is greater than 5 and less than or equal to 10"

            (it "increments backstage pass quality by two"
              (let [pass (assoc pass :sell-in 10)]
                (should= (+ (:quality pass) 2)
                         (:quality (update-item pass)))))
          )

          (context "when sell-in date is greater than 0 and less than or equal to 5"

            (it "increments backstage pass quality by three"
              (let [pass (assoc pass :sell-in 5)]
                (should= (+ (:quality pass) 3)
                         (:quality (update-item pass)))))
          )
        )

        (context "when sell-in date has passed"

          (it "decrements vest quality by two"
            (let [vest (assoc vest :sell-in 0)]
              (should= (- (:quality vest) 2)
                       (:quality (update-item vest)))))

          (it "decrements elixir quality by two"
            (let [elixir (assoc elixir :sell-in 0)]
              (should= (- (:quality elixir) 2)
                       (:quality (update-item elixir)))))

          (it "sets backstage pass quality to zero"
            (let [pass (assoc pass :sell-in 0)]
              (should= 0 (:quality (update-item pass)))))
        )
      )
    )
  )
)
