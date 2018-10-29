(set-env!
  :resource-paths #{"src" "test"}
  :dependencies '[[clj-http "3.9.1"]
                  [cheshire "5.8.1"]])

(task-options!
  pom {:project 'clj-keybase-proofs
       :version "0.1.0"})
