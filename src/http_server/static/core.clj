(ns http_server.static.core
  (use [http_server.static.get]
       [http_server.clack.methods]
       [http_server.static.disk_file])
  (import http_server.static.disk_file.DiskFile))

(def files (atom {}))

(defn add-file [java-file dirname]
  (let [abs-path (.getAbsolutePath java-file)
        rel-path (subs abs-path (count dirname))
        file     (DiskFile. abs-path)]
    (swap! files assoc rel-path file)))

(defn initialize-static [dirname]
  (doseq [file  (file-seq (clojure.java.io/file dirname))]
    (add-file file dirname)))

(defn app [_next env]
  (map println @files)
  (cond 
    (= (:method env) GET) (do-get env @files)))
