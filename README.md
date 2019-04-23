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
(require '[io.keybase.proofs :as keybase-proofs])
```
