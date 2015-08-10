(defproject com.solita/lein-test-refresh-gui "0.10.1-SNAPSHOT"
  :description "Automatically reload code and run clojure.test tests when files change and display graphical report"
  :url "https://github.com/slarba/lein-test-refresh"
  :developer "Marko Lauronen based on work of Jake McCrary"
  :min-lein-version "2.4"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/tools.namespace "0.2.11" :exclusions [org.clojure/clojure]]
                 [leinjacker "0.4.2" :exclusions [org.clojure/clojure]]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [jakemcc/clojure-gntp "0.1.1" :exclusions [org.clojure/clojure]]]
  :deploy-repositories [["snapshots" {:url "https://clojars.org/repo"
                                      :username :gpg :password :gpg}]
                        ["releases" {:url "https://clojars.org/repo"
                                     :username :gpg :password :gpg}]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.6.0"]]}}
  :scm {:name "git"
        :url "git@github.com:slarba/lein-test-refresh.git"})
