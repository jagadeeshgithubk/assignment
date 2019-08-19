
# money transfer between accounts

Money Transfer webservice supporting parallel calls

Application starts webserver on http://localhost:9090 by default

 - **Java & MultiThreading**
 - **Jetty**
 - **Jersey** 
 - **JUnit 5**
 

Main class for running application:
```sh
MoneyTransferStarter.java
```
or as a maven goal

```sh
mvn exec:java
```
## Account API - `/accounts`

**GET** - retrieves all accounts from database

Response:
**Status: 200 OK**
```javascript
[
    {
        "accountId": "123",
        "balance": "10000.0"
    }
]
```
---
**POST** - Add a new account 
**Request Body** - Account object

Sample request:
```javascript
{
	"accountId":"456",
	"balance":"50000.0"
}
```

Sample response:
**Status: 200 OK**
```javascript
{
	"accountId":"456",
	"balance":"50000.0"
}
```
Duplicated account response:
**Status: 400 Bad Request**
```javascript
Account with ID:456 already exists. 
Duplicates are not allowed.
```
---
**/{accountId}** - account id
**GET** - retrieves all accounts from data store

Response:
**Status: 200 OK**
```javascript
{
    "accountId": "123",
    "balance": "10000.0"
}
```
Account doesn't exist:
**Status: 204 No Content**

## Transfer Money API - `/transferMoney`

**POST** - Transfer money from one account to another account 

**Request Body** - MoneyTransferDTO object

Sample request:
```javascript
{
	"source":"123",
	"target":"456",
	"balance":"5000"
}
```

Sample response:
**Status: 200 OK**
```javascript
[
    {
        "accountId": "123",
        "balance": "5000"
    },
    {
        "accountId": "456",
        "balance": "10000"
    }
]
```

Insufficient balance on source account:
**Status: 409 Conflict**
```javascript
Money Transfer cant be performed due to lack of funds on the account.
```

One of the party accounts doesn't exist:
**Status: 400 Bad Request**
```javascript
Account(s) doesnt exist. | Source: null, Target: Account{accountId=2, balance=10}
```
