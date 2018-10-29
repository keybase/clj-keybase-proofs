(set-env!
  :resource-paths #{"src" "test"}
  :dependencies '[[clj-http "3.9.1"]
                  [cheshire "5.8.1"]
                  [adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer [bootlaces! build-jar push-snapshot]])
(def +version+ "0.1.0-SNAPSHOT")
(bootlaces! +version+)

(task-options!
  pom {:project 'clj-keybase-proofs
       :version "0.1.0"})
