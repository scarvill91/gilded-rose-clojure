(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer :all]
          [gilded-rose.item-update :refer :all]))

(describe "gilded rose"

  (describe "update-quality"

    (it "never decreases quality past zero"
      (let [vest (assoc vest :quality 0) elixir (assoc elixir :quality 0)]
        (should= 0 (:quality (update-item vest)))
        (should= 0 (:quality (update-item elixir)))))

    (context "when item is legendary"

      (it "leaves quality and sell-in date unchanged"
        (should= (:quality sulfuras) (:quality (update-item sulfuras)))
        (should= (:sell-in sulfuras) (:sell-in (update-item sulfuras))))
    )

    (context "when item is not legendary"

      (it "decrements sell-in date by one"
        (should= (- (:sell-in vest) 1) (:sell-in (update-item vest)))
        (should= (- (:sell-in elixir) 1) (:sell-in (update-item elixir)))
        (should= (- (:sell-in brie) 1) (:sell-in (update-item brie)))
        (should= (- (:sell-in passes) 1) (:sell-in (update-item passes))))

      (it "increments brie quality by one"
        (should= (+ (:quality brie) 1) (:quality (update-item brie))))

      (it "never increases brie quality above fifty"
        (let [brie (assoc brie :quality 50)]
          (should= 50 (:quality (update-item brie)))))

      (context "when item sell-in date has not been reached"

        (it "decrements vest quality by one"
          (let [vest (assoc vest :sell-in 10)]
            (should= (- (:quality vest) 1) (:quality (update-item vest)))))

        (it "decrements elixir quality by one"
          (let [elixir (assoc elixir :sell-in 10)]
            (should= (- (:quality elixir) 1) (:quality (update-item elixir)))))

        (context "when sell-in date is greater than 10"

          (it "increments backstage passes quality by one"
            (let [passes (assoc passes :sell-in 15)]
              (should= (+ (:quality passes) 1) (:quality (update-item passes)))))

          (it "never increases passes quality above fifty"
            (let [passes (assoc (assoc passes :quality 50) :sell-in 15)]
              (should= 50 (:quality (update-item passes)))))
        )

        (context "when sell-in date is greater than 5 and less than or equal to 10"

          (it "increments backstage passes quality by two"
            (let [passes (assoc passes :sell-in 10)]
              (should= (+ (:quality passes) 2) (:quality (update-item passes)))))

          (it "never increases passes quality above fifty"
            (let [passes (assoc (assoc passes :quality 50) :sell-in 10)]
              (should= 50 (:quality (update-item passes)))))
        )

        (context "when sell-in date is greater than 0 and less than or equal to 5"

          (it "increments backstage passes quality by three"
            (let [passes (assoc passes :sell-in 5)]
              (should= (+ (:quality passes) 3) (:quality (update-item passes)))))

          (it "never increases passes quality above fifty"
            (let [passes (assoc (assoc passes :quality 50) :sell-in 5)]
              (should= 50 (:quality (update-item passes)))))
        ))

      (context "when sell-in date has passesed"

        (it "decrements vest quality by two"
          (let [vest (assoc vest :sell-in 0)]
            (should= (- (:quality vest) 2) (:quality (update-item vest)))))

        (it "decrements elixir quality by two"
          (let [elixir (assoc elixir :sell-in 0)]
            (should= (- (:quality elixir) 2) (:quality (update-item elixir)))))

        (it "sets backstage passes quality to zero"
          (let [passes (assoc passes :sell-in 0)]
            (should= 0 (:quality (update-item passes)))))
      ))))
