ProductResource has no mutable fields because it should remain stateless,allowing multiple requests to be handled safely and concurrently without sharing or modifying request-specific data.


## Testing
borinpowiney@Borins-MacBook-Pro shop-rest % curl -i localhost:8080/api/products
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 220

[{"id":1,"name":"Kettle","price":24.90,"sku":"KT-01","stock":12,"version":1},{"id":2,"name":"Mug","price":6.50,"sku":"MG-01","stock":40,"version":1},{"id":3,"name":"Tea","price":4.20,"sku":"TE-01","stock":0,"version":1}]%                                                    
borinpowiney@Borins-MacBook-Pro shop-rest % curl -i localhost:8080/api/products/2
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 71

{"id":2,"name":"Mug","price":6.50,"sku":"MG-01","stock":40,"version":1}%                   
borinpowiney@Borins-MacBook-Pro shop-rest % curl -i localhost:8080/api/products/99
HTTP/1.1 404 Not Found
Content-Length: 0

borinpowiney@Borins-MacBook-Pro shop-rest % curl -i localhost:8080/api/products/abc
HTTP/1.1 404 Not Found
Content-Length: 0

