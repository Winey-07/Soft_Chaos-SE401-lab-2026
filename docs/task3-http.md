Bean validation and custom parameter converters ensure incoming query and body data are validated cleanly at the API boundary before reaching domain logic, returning standardized RFC 9457 problem details on error.

## Testing
pc@PCs-MacBook-Air Soft_Chaos-SE401-lab-2026 % curl -i 'localhost:8080/api/products?minPrice=5%20EUR&inStock=true&sort=price,desc'
HTTP/1.1 200 OK
X-Correlation-Id: 8e4f12a0-9b3c-4d1e-8a2f-1c5d6e7f8a9b
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET,POST,PUT,PATCH,DELETE
Access-Control-Allow-Headers: Content-Type,X-Api-Key,X-Correlation-Id,If-Match
Access-Control-Expose-Headers: Location,ETag,Link,X-Total-Count,X-Correlation-Id
Content-Type: application/json
Content-Length: 112

[{"id":1,"name":"Kettle","price":24.90,"sku":"KT-01","stock":12},{"id":2,"name":"Mug","price":6.50,"sku":"MG-01","stock":40}]
pc@PCs-MacBook-Air Soft_Chaos-SE401-lab-2026 % curl -i -X POST localhost:8080/api/products \
     -H 'X-Api-Key: demo-123' \
     -H 'Content-Type: application/json' \
     -d '{"name":"","sku":"kettle","price":0,"stock":-1}'
HTTP/1.1 400 Bad Request
X-Correlation-Id: 1b621a77-ce70-465d-90a0-98c7c2449277
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET,POST,PUT,PATCH,DELETE
Access-Control-Allow-Headers: Content-Type,X-Api-Key,X-Correlation-Id,If-Match
Access-Control-Expose-Headers: Location,ETag,Link,X-Total-Count,X-Correlation-Id
Content-Type: application/problem+json
Connection: close
Content-Length: 367

{"detail":"Validation failed","errors":[{"field":"price","message":"must be greater than or equal to 0.01"},{"field":"sku","message":"must match \"[A-Z]{2}-\\d{2}\""},{"field":"stock","message":"must be greater than or equal to 0"},{"field":"name","message":"must not be blank"}],"instance":"/api/products","status":400,"title":"Invalid request","type":"about:blank"}