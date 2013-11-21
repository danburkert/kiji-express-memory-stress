(defproject memory-stress "0.1.0-SNAPSHOT"
  :repositories [["kiji" "https://repo.wibidata.com/artifactory/kiji"]
                 ["kiji-nightly" "https://repo.wibidata.com/artifactory/kiji-nightly"]]
  :plugins [[lein-scalac "0.1.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.kiji.express/kiji-express "0.14.0"]
                 [org.kiji.schema/kiji-schema "1.3.3"]
                 [org.kiji.schema-shell/kiji-schema-shell "1.3.1"]
                 [org.kiji.platforms/kiji-cdh4-platform "1.3.0"]
                 [org.scala-lang/scala-library "2.9.3"]
                 [com.github.scopt/scopt_2.9.3 "3.2.0"]]
  :scala-source-path "src/scala"
  ;:resource-paths ["/Users/dan/wibi/kiji-bento-chirashi-1.2.7/cluster/lib/hadoop-2.0.0-mr1-cdh4.3.0/conf"]
  :prep-tasks ["scalac"]
  :profiles {:prepare {:main org.kiji.express.memory.TablePreparer}
             :scald {:main com.twitter.scalding.Tool}}
  :aliases {"compile" "scalac"
            "prepare" ["with-profile" "prepare" "run"]
            "scald" ["with-profile" "scald" "run"]})
