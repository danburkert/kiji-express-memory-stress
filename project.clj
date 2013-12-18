(defproject memory-stress "0.1.0-SNAPSHOT"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :repositories [["kiji" "https://repo.wibidata.com/artifactory/kiji"]
                 ["kiji-nightly" "https://repo.wibidata.com/artifactory/kiji-nightly"]]
  :plugins [[lein-scalac "0.1.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.kiji.express/kiji-express "0.15.0-SNAPSHOT"]
                 [org.kiji.schema/kiji-schema "1.3.4"]
                 [org.kiji.schema-shell/kiji-schema-shell "1.3.2"]
                 [org.kiji.platforms/kiji-cdh4-platform "1.3.0"]
                 [org.kiji.mapreduce/kiji-mapreduce "1.2.3"]
                 [org.scala-lang/scala-library "2.9.2"]
                 [com.github.scopt/scopt_2.9.3 "3.2.0"]]
  :scala-source-path "src/scala"
  :prep-tasks ["scalac"]
  :profiles {:prepare {:main org.kiji.express.memory.TablePreparer}
             :scald {:main com.twitter.scalding.Tool}}
  :aliases {"compile" "scalac"
            "prepare" ["with-profile" "prepare" "run"]
            "scald" ["with-profile" "scald" "run"]})
