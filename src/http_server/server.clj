(ns http-server.server
  (use [clojure.java.io :as io]
       [http_server.clack.core :as clack]
       [http_server.static.core :as static])
  (import [java.net ServerSocket]))

(defn receive [socket]
  (.readLine (io/reader socket)))

(defn send-sock [socket msg]
  (let [writer (io/output-stream socket)]
      (.write writer msg)
      (.flush writer)))

(defn serve [port handler]
  (loop []
    (with-open [server-sock (ServerSocket. port)
                sock (.accept server-sock)]
      (let [msg (receive sock)]
        (send-sock sock (handler msg))))
    (recur)))

(defn start [port public-dir]
  (static/initialize-static public-dir)
  (clack/use-middleware static/app)
  (serve port clack))
