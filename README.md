This is a sample Springboot Application built on Cassandra with REST APIs to

1) Aggregate product data from multiple sources and return it to the client in a JSON format. 

   GET http://localhost:8080/product-api/v1/products/13860428

2) Update the price of a product

    PUT http://localhost:8080/product-api/v1/products/13860428

    Headers:
    Content-Type: application/json

    Request body:
    {"price":"34.99","currency":"USD"}


To run the application:

1) Checkout the code from the main branch of this repository
2) Install and run Apache Cassandra
3) Run the DDL commands provided under src/main/resources/cql

Postman Collection for the APIs:
https://www.getpostman.com/collections/62c767ee5f6ae434a87f





