(ns com.jakemccrary.gui-frame
  (:require  [clojure.test :refer :all]
             [clojure.core.async :refer [>! <! chan go-loop go]])
  (:import [javax.swing JFrame JButton JList JLabel JPanel BoxLayout JScrollPane]
           [java.awt Color BorderLayout Dimension]))

(def +WINDOW-WIDTH+ 300)
(def +WINDOW-HEIGHT+ 500)
(def +RESULT-HEIGHT+ 30)

(defn test-result-box [item]
  (let [panel (JPanel.)
        color (if (:error item) Color/RED Color/GREEN)]
    (doto panel
      (.setBackground color)
      (.setPreferredSize (Dimension. +WINDOW-WIDTH+ +RESULT-HEIGHT+))
      (.add (JLabel. (:title item))))))

(def sample-results [{:error false :title "Onnistunut 1"}
                     {:error false :title "Onnistunut 2"}
                     {:error true :title "Epäonnistunut 1"}
                     {:error false :title "Onnistunut 3"}
                     {:error false :title "Onnistunut 4"}
                     {:error false :title "Onnistunut 4"}
                     {:error false :title "Onnistunut 4"}
                     {:error false :title "Onnistunut 4"}
                     {:error false :title "Onnistunut 4"}])

(def frame (JFrame. "Harja testit"))
(def panel (JPanel.))
(def scrollpane (JScrollPane. panel
                              JScrollPane/VERTICAL_SCROLLBAR_ALWAYS
                              JScrollPane/HORIZONTAL_SCROLLBAR_NEVER))

(defn init-report-frame []
  (.setPreferredSize scrollpane (Dimension. +WINDOW-WIDTH+ +WINDOW-HEIGHT+))
  (.setLayout panel (BoxLayout. panel BoxLayout/Y_AXIS))
  (doto frame
    (.setDefaultCloseOperation JFrame/HIDE_ON_CLOSE)
    (.setLayout (BorderLayout.))
    (.add scrollpane BorderLayout/CENTER)
    (.setLocationRelativeTo nil)
    (.setSize +WINDOW-WIDTH+ +WINDOW-HEIGHT+)
    (.setVisible true)))

(def test-result-channel (chan))

(defn update-test-results [results]
  (.removeAll panel)
  (doseq [i results]
    (.add panel (test-result-box i)))
  (doto panel
    (.revalidate)
    (.repaint)))

(defn init []
  (init-report-frame)
  (go-loop []
    (update-test-results (<! test-result-channel))))

(defn show-results [results]
  (go (>! test-result-channel results)))
