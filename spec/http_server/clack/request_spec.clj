(ns http_server.clack.request_spec
  (use [speclj.core]
       [http_server.clack.request]
       [http_server.clack.methods]))

(defn request-reader [string]
  (clojure.java.io/reader (char-array string)))

(describe "parsing"
  (context "method"
    (it "parses get"
      (should= GET (:method (parse (request-reader "GET / HTTP/1.1"))))))

    (it "parses post"
      (should= POST (:method (parse (request-reader "POST / HTTP/1.1"))))) 

  (it "parses root"
    (should= "/" (:path (parse (request-reader "GET / HTTP/1.1")))))

  (it "parses path"
    (should= "/hello/world" (:path (parse (request-reader "GET /hello/world HTTP/1.1")))))

  (it "parses http version"
    (should= "1.1" (:http_version (parse (request-reader "GET /hello/world HTTP/1.1")))))

  (it "parses a header"
    (should= {"From" "uku.taht@gmail.com"} (:headers (parse (request-reader "GET /hello/world HTTP/1.1\r\nFrom:uku.taht@gmail.com\r\n")))))

  (it "parses multiple headers"
    (should= {"From" "uku.taht@gmail.com"
              "Content-Length" "5"} (:headers (parse (request-reader "GET /hello/world HTTP/1.1\r\nFrom:uku.taht@gmail.com\r\nContent-Length:5\r\n")))))

  (it "trims whitespace from headers"
    (should= {"From" "uku.taht@gmail.com" } (:headers (parse (request-reader "GET /hello/world HTTP/1.1\r\nFrom  :   uku.taht@gmail.com\r\n"))))))
