(ns com.solita.gui-frame
  (:require  [clojure.test :refer :all]
             [clojure.core.async :refer [>! <! chan go-loop go]])
  (:import [javax.swing JFrame JButton JList JPanel BoxLayout JScrollPane JTextArea Box JProgressBar]
           [java.awt Color BorderLayout Dimension]))

(def +RESULT-WIDTH+ 680)
(def +RESULT-HEIGHT+ 80)

(def +WINDOW-WIDTH+ 700)
(def +WINDOW-HEIGHT+ 250)

(def frame (JFrame. "Test report"))
(def panel (JPanel.))
(def scrollpane (JScrollPane. panel
                              JScrollPane/VERTICAL_SCROLLBAR_ALWAYS
                              JScrollPane/HORIZONTAL_SCROLLBAR_NEVER))

(def resultbox-dims (Dimension. +RESULT-WIDTH+ +RESULT-HEIGHT+))

(def progressbar-running (atom false))

(defn- progressbar []
  (let [pbar (JProgressBar.)]
    (doto pbar
      (.setPreferredSize resultbox-dims)
      (.setIndeterminate true))))

(defn- test-result-box [item]
  (let [type (:type item)
        color (if (= type :pass)
                Color/GREEN
                Color/RED)
        label-text (if (= type :pass)
                     (str "\n\n              Tests OK!")
                     (str (if (= type :error) "ERROR" "FAIL")
                          "    " (:file item) ":" (:line item) "\nexpected: " (:expected item) "\nactual: " (:actual item)))
        label (JTextArea. label-text)]
    (doto label
      (.setPreferredSize resultbox-dims)
      (.setMaximumSize resultbox-dims)
      (.setLineWrap true)
      (.setBackground color)
      (.setEditable false))))

(defn- init-report-frame []
  (.setLayout panel (BoxLayout. panel BoxLayout/Y_AXIS))
  (doto frame
    (.setDefaultCloseOperation JFrame/HIDE_ON_CLOSE)
    (.add scrollpane)
    (.setLocationRelativeTo nil)
    (.setSize +WINDOW-WIDTH+ +WINDOW-HEIGHT+)
    (.setVisible true)))

(def test-result-channel (chan))

(defn- revalidate-results []
  (doto panel
    (.revalidate)
    (.repaint)))

(defn- add-test-result [result]
  (when @progressbar-running
    (reset! progressbar-running false)
    (.removeAll panel))
  (doto panel
    (.add (test-result-box result))
    (.add (Box/createRigidArea (Dimension. 0 5))))
  (revalidate-results))

(defn- add-progress-bar []
  (reset! progressbar-running true)
  (doto panel
    (.removeAll)
    (.add (progressbar)))
  (revalidate-results))

(defn send-result
  "Send test result. Map with :clear key true will clear the result list, otherwise
  it should contain clojure.test report map"
  [result]
  (go (>! test-result-channel result)))

(defn init []
  (init-report-frame)
  (go-loop []
    (let [msg (<! test-result-channel)]
      (if (:clear msg)
        (add-progress-bar)
        (add-test-result msg)))
    (recur)))
