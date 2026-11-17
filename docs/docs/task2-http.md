borinpowiney@Borins-MacBook-Pro shop-rest % curl -i -X POST localhost:8080/api/products \
-H 'Content-Type: application/json' \
-d '{"name":"Teapot","sku":"TP-01","price":31.00,"stock":5}'
HTTP/1.1 201 Created
Location: http://localhost:8080/api/products/4
Content-Type: application/json
Content-Length: 62

{"id":4,"name":"Teapot","price":31.00,"sku":"TP-01","stock":5}%                                          
borinpowiney@Borins-MacBook-Pro shop-rest % curl -i localhost:8080/api/products/4 \
-H 'Accept: application/xml'
HTTP/1.1 200 OK
Content-Type: application/xml
Content-Length: 155

<?xml version="1.0" encoding="UTF-8" standalone="yes"?><product><id>4</id><name>Teapot</name><sku>TP-01</sku><price>31.00</price><stock>5</stock></product>%                                                      
borinpowiney@Borins-MacBook-Pro shop-rest % curl -i -X PATCH localhost:8080/api/products/3/stock \
-H 'Content-Type: application/json' \
-d '{"delta":-1}'
HTTP/1.1 409 Conflict
Content-Length: 0

borinpowiney@Borins-MacBook-Pro shop-rest % curl -i -X DELETE localhost:8080/api/products/4
HTTP/1.1 204 No Content

borinpowiney@Borins-MacBook-Pro shop-rest % curl -i -X POST localhost:8080/api/products \
-H 'Content-Type: text/plain' \
-d 'Teapot'
HTTP/1.1 415 Unsupported Media Type
Content-Length: 0