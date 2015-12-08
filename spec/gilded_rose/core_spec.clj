(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer :all]))

(defn update-item-quality [item] (first (update-quality [item])))

(defn expected-update [item attribute update]
  (should= (update (attribute item)) (attribute (update-item-quality item))))

(describe "gilded rose"

  (describe "update-quality"

    (it "never decreases quality past zero"
      (pending "Should pass, but doesn't due to bug")
      (let [vest (assoc vest :quality 0) elixir (assoc elixir :quality 0)]
        (expected-update vest :quality identity)
        (expected-update elixir :quality identity)))

    (context "when item is legendary"

      (it "leaves quality and sell-in date unchanged"
        (expected-update sulfuras :sell-in identity)
        (expected-update sulfuras :quality identity))
    )

    (context "when item is not legendary"

      (it "decrements sell-in date by one"
        (expected-update vest :sell-in dec)
        (expected-update elixir :sell-in dec)
        (expected-update brie :sell-in dec)
        (expected-update passes :sell-in dec))

      (it "increments brie quality by one"
        (expected-update brie :quality inc))

      (it "never increases brie quality above fifty"
        (let [brie (assoc brie :quality 50)]
          (expected-update brie :quality identity)))

      (context "when item sell-in date has not been reached"

        (it "decrements vest quality by one"
          (let [vest (assoc vest :sell-in 10)]
            (expected-update vest :quality dec)))

        (it "decrements elixir quality by one"
          (let [elixir (assoc elixir :sell-in 10)]
            (expected-update elixir :quality dec)))

        (context "when sell-in date is greater than 10"

          (it "increments backstage passes quality by one"
            (let [passes (assoc passes :sell-in 15)]
              (expected-update passes :quality inc)))

          (it "never increases passes quality above fifty"
            (let [passes (assoc (assoc passes :quality 50) :sell-in 15)]
              (expected-update passes :quality identity)))
        )

        (context "when sell-in date is greater than 5 and less than or equal to 10"

          (it "increments backstage passes quality by two"
            (let [passes (assoc passes :sell-in 10)]
              (expected-update passes :quality (partial + 2))))

          (it "never increases passes quality above fifty"
            (pending "Should pass, but doesn't due to bug")
            (let [passes (assoc (assoc passes :quality 50) :sell-in 10)]
              (expected-update passes :quality identity)))
        )

        (context "when sell-in date is greater than 0 and less than or equal to 5"

          (it "increments backstage passes quality by three"
            (let [passes (assoc passes :sell-in 5)]
              (expected-update passes :quality (partial + 3))))

          (it "never increases passes quality above fifty"
            (pending "Should pass, but doesn't due to bug")
            (let [passes (assoc (assoc passes :quality 50) :sell-in 5)]
              (expected-update passes :quality identity)))
        ))

      (context "when sell-in date has passesed"

        (it "decrements vest quality by two"
          (let [vest (assoc vest :sell-in 0)]
            (expected-update vest :quality (comp dec dec))))

        (it "decrements elixir quality by two"
          (let [elixir (assoc elixir :sell-in 0)]
            (expected-update elixir :quality (comp dec dec))))

        (it "sets backstage passes quality to zero"
          (let [passes (assoc passes :sell-in 0)]
            (expected-update passes :quality (constantly 0))))
      ))))
