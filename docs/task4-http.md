JAX-RS filters and writer interceptors handle cross-cutting concerns—such as API key authentication, CORS headers, correlation ID tracing, and Gzip response compression—without cluttering resource endpoints.

## Testing
pc@PCs-MacBook-Air Soft_Chaos-SE401-lab-2026 % curl -i -X POST localhost:8080/api/products \
     -H 'Content-Type: application/json' \
     -d '{"name":"Cup","sku":"CP-01","price":5.00,"stock":10}'
HTTP/1.1 401 Unauthorized
WWW-Authenticate: ApiKey realm="shop"
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET,POST,PUT,PATCH,DELETE
Access-Control-Allow-Headers: Content-Type,X-Api-Key,X-Correlation-Id,If-Match
Access-Control-Expose-Headers: Location,ETag,Link,X-Total-Count,X-Correlation-Id
Content-Type: application/problem+json
Content-Length: 125

{"detail":"Missing or invalid X-Api-Key","instance":"/api/products","status":401,"title":"Unauthorized","type":"about:blank"}
pc@PCs-MacBook-Air Soft_Chaos-SE401-lab-2026 % curl -sv --compressed localhost:8080/api/products -o /dev/null 2>&1 | grep -i -E 'encoding|vary'
> Accept-Encoding: deflate, gzip
< Content-Encoding: gzip
< Vary: Accept-Encoding