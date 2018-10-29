# clj-keybase-proofs

`clj-keybase-proofs` is a library intended to help you integrate
[Keybase](https://keybase.io) proofs with your service. Full documentation is
available at the [Proof Integration
Guide](https://keybase.io/docs/proof_integration_guide). You may also be interested
in the [Django library and example application](https://github.com/keybase/django-keybase-proofs).

If you like, you can look at the example Clojure application
[Colorbase](https://github.com/keybase/colorbase) to see how this library can
be used in a real website using Ring, Compojure, HugSQL, and Hiccup.

## Quickstart
Add to your `build.boot` or `project.clj`:

```
clj-keybase-proofs "0.1.0-SNAPSHOT"
```

And require into your namespace:

```
(require '[org.keybase.proofs :as keybase-proofs])
```

### Checking the validity of a proof
Do this when a user wants to post a proof to your service before adding it to
your database.

```
(keybase-proofs/valid-proof? identity-service-domain username keybase-username sig-hash)

(keybase-proofs/valid-proof? "mysocialnetwork.com" "alice" "alice_on_keybase" "8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f")
; true/false
```
If it returns true, associate the `keybase-username` and `sig-hash` with
`username`. Remember that users can make more than one Keybase proof, so make
sure they can upload multiple such proofs. If it returns false, you should reject
the proof and ask your user to try again.

### Checking if a proof is live
Do this after you've added the proof to your database and expose it via the `check_url` API endpoint on your service. If it fails, you should delete it or mark it as deleted and not show the proof on the user's profile page. You can also call this function for all your users with Keybase proofs every so often in order to check for updates to the Keybase proof, e.g., if the user later revoked it on Keybase, this function will return `false` so you can stop showing the proof.

```
(keybase-proofs/valid-proof? identity-service-domain username keybase-username sig-hash)

(keybase-proofs/valid-proof? "mysocialnetwork.com" "alice" "alice_on_keybase" "8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f")
; true/false
```

### Making a link to a Keybase profile
When linking to a user's Keybase profile on their profile page on your website, use
```
(keybase-proofs/make-profile-link keybase-username sig-hash)

(keybase-proofs/make-profile-link "alice_on_keybase" "8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f")
; https://keybase.io/alice_on_keybase/sigs/8514ae2f9083a3c867318437845855f702a4154d1671a19cf274fb2e6b7dec7c0f
```

This is a special link particular to the user's keybase proof. If it ever fails for some reason, the link will indicate that, but when it succeeds it goes to the user's Keybase profile as normal.
