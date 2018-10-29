(ns org.keybase.test.proofs-test
  (:require [org.keybase.proofs :as keybase-proofs]
            [clojure.test :refer :all]))

(def ^:constant t-alice-good-sig-hash "8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f")
(def ^:constant t-alice-bad-sig-hash "cafefeed9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f")

(deftest profile-link
  (is (= (str "https://keybase.io/t_alice/sigs/" t-alice-good-sig-hash)
         (keybase-proofs/make-profile-link "t_alice" t-alice-good-sig-hash))))

(deftest bad-api-arguments
  (is (not (keybase-proofs/valid-proof? "twitter" "t_alice_" "t_alice" nil)))
  (is (not (keybase-proofs/proof-live? "twitter" "t_alice_" "t_alice" nil))))

(deftest proof-not-valid
  (is (not (keybase-proofs/valid-proof? "twitter" "t_alice_" "t_alice" t-alice-bad-sig-hash)))
  (is (not (keybase-proofs/proof-live? "twitter" "t_alice_" "t_alice" t-alice-bad-sig-hash))))
