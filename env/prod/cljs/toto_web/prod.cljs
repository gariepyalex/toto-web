(ns toto-web.prod
  (:require [toto-web.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
