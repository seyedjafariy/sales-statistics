sales-statistics
==========
How to use the service : 

Sending the sales like this :

* curl -X POST http://localhost:8080/sales -H 'Content-Type: application/x-www-form-urlencoded' -d 'sales_amount=10.00'

And getting sales info like this : 

*  curl -X GET http://localhost:8080/statistics
