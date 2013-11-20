(defproject memory-stress "0.1.0-SNAPSHOT"
  :repositories [["kiji" "https://repo.wibidata.com/artifactory/kiji"]
                 ["kiji-nightly" "https://repo.wibidata.com/artifactory/kiji-nightly"]]
  :plugins [[lein-scalac "0.1.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.kiji.express/kiji-express "0.13.0"]
                 [org.kiji.schema/kiji-schema "1.3.3"]
                 [org.kiji.schema-shell/kiji-schema-shell "1.3.1"]
                 [org.kiji.platforms/kiji-cdh4-platform "1.3.0"]
                 [org.scala-lang/scala-library "2.9.2"]]
  :scala-source-path "src/scala"
  :prep-tasks ["scalac"]
  :main com.twitter.scalding.Tool)
