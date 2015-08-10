(defproject lein2 "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/test.check "0.7.0"]]
  :plugins [[com.solita/lein-test-refresh #=(eval (nth (read-string (slurp "../test-refresh/project.clj")) 2))]]
  :test-selectors {:integration :integration
                   :ns-metadata :ns-metadata
                   :unit (complement :integration)}
  :test-refresh {;;:notify-command ["say" "-v" "Agnes"]
                 :notify-on-success false
                 :quiet true})
