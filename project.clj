(defproject matcher-starter "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojars.cognesence/breadth-search "0.9.0"]
                 [org.clojars.cognesence/matcher "1.0.1"]
                 [org.clojars.cognesence/ops-search "1.0.1"]
                 [org.clojure/tools.trace "0.7.10"]
                 [com.clojure-goes-fast/clj-memory-meter "0.1.2"]
                 [com.clojure-goes-fast/clj-memory-meter "0.1.2"]
                ]
  :jvm-opts ["-Djdk.attach.allowAttachSelf"]
)
