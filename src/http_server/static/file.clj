(ns http_server.static.file)

(defprotocol File
  (contents [this]))

