(ns org.keybase.proofs
  "Helper functions for integrating services with Keybase proofs.
  Full documentation available at https://keybase.io/docs/proof_integration_guide."
  (require [clj-http.client :as http-client]))

(defn make-profile-link
  "Returns a URL pointing to a Keybase profile on https://keybase.io and a
  particular proof. The profile will indicate if the proof is valid and has not been revoked.
  Valid example:   https://keybase.io/t_alice/sigs/8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f
  Invalid example: https://keybase.io/t_alice/sigs/cafefeed9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f
  `keybase-username` is the user's username on Keybase.
  `sig-hash` is a hash provided when the user first posted the proof to the identity service."
  [keybase-username sig-hash]
  (let [url-template "https://keybase.io/%s/sigs/%s"]
    (format url-template keybase-username sig-hash)))

(defn- get-proof-status [identity-service-domain username keybase-username sig-hash]
  "Helper function that returns the status of a proof using the Keybase API.
  If the API returned a 200 OK, returns a map structured like
  {:valid_proof true, :proof_live false}
  :valid_proof indicates whether a proof is in the correct format.
  :proof_live indicates whether the proof is live and has not been deleted or revoked on both
  the identity service and Keybase.
  If the API call does not succeed, returns nil.
  `identity-service-domain` is the domain configured in the config.json file sent to Keybase.
  `username` is the user's username on the identity service.
  `keybase-username` is the user's username on Keybase.
  `sig-hash` is a hash provided when the user first posted the proof to the identity service."
  (let [check-url "https://keybase.io/_/api/1.0/sig/check_proof.json"
        params {:domain identity-service-domain
                :username username
                :kb_username keybase-username
                :sig_hash sig-hash}
        options {:query-params params
                 :as :json-strict
                 :throw-exceptions :false}
        resp (http-client/get check-url options)]
    (when (and (= 200 (:status resp))
               (zero? (get-in resp [:body :status :code])))
      (:body resp))))

(defn valid-proof?
  "Returns true if a proof is in the correct format, is for the right identity service,
  and is for the right identity service user and Keybase user.
  Check this function *before* inserting the proof into a database, and return an error
  if it returns false.
  `identity-service-domain` is the domain configured in the config.json file sent to Keybase.
  `username` is the user's username on the identity service.
  `keybase-username` is the user's username on Keybase.
  `sig-hash` is a hash provided when the user first posted the proof to the identity service."
  [identity-service-domain username keybase-username sig-hash]
  (boolean (:valid_proof (get-proof-status identity-service-domain username keybase-username sig-hash))))

(defn proof-live?
  "Returns true if a proof is live and can be seen and verified by the Keybase servers and clients.
  This might fail if, for example, the user decides to revoke the proof on Keybase or delete the proof
  on the identity service.
  Check this function *after* inserting the proof into your database. The Keybase servers will check
  the identity service's API to make sure the proof exists and is correct. If it isn't, the user
  might have already revoked the proof or there may be a configuration issue with the identity service's
  API. In this case, the proof should not be shown on the user's profile.
  You may opt to periodically check this function for your users with Keybase proofs to be updated on
  whether the proof is still valid, and stop or start showing the proof on the user's profile if it changes.
  `identity-service-domain` is the domain configured in the config.json file sent to Keybase.
  `username` is the user's username on the identity service.
  `keybase-username` is the user's username on Keybase.
  `sig-hash` is a hash provided when the user first posted the proof to the identity service."
  [identity-service-domain username keybase-username sig-hash]
  (boolean (:proof_live (get-proof-status identity-service-domain username keybase-username sig-hash))))
