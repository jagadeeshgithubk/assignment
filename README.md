
# moneytransfer

Money Transfer webservice supporting parallel calls

Application starts webserver on http://localhost:9090 by default

 - **Jetty** - as a server layer
 - **Jersey** - as a JAX-RS implementation
 - **JUnit 5** - as a unit test framework
 - **Rest Assured** - for API tests

Application may be started from standalone jar:
```sh
java -jar money-transfer-between-accounts-1.0-SNAPSHOT.jar
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
        "accountId": "573582b24911",
        "balance": 80.1
    },
    {
        "accountId": "62002b0f686d",
        "balance": 15
    },
    {
        "accountId": "37d3807795f7",
        "balance": 130.1
    },
    {
        "accountId": "9de1807c1727",
        "balance": 100.1
    }
]
```
---
**POST** - persists new account 
**Request Body** - Account object

Sample request:
```javascript
{
	"accountId":"456",
	"balance":"5.0"
}
```

Sample response:
**Status: 200 OK**
```javascript
{
	"accountId":"2",
	"balance":"5.0"
}
```
Duplicated account response:
**Status: 400 Bad Request**
```javascript
Account with ID:2 already exists. 
Duplicates are not allowed.
```
---
**/{accountId}** - account id
**GET** - retrieves all accounts from database

Response:
**Status: 200 OK**
```javascript
{
    "accountId": "37d3807795f7",
    "balance": 55.3
}
```
Account doesn't exist:
**Status: 204 No Content**

## Transaction API - `/transactions`

**POST** - submit new transaction

**Request Body** - MoneyTransfer object

Sample request:
```javascript
{
	"source":"1",
	"target":"2",
	"balance":"5.0"
}
```

Sample response:
**Status: 200 OK**
```javascript
[
    {
        "accountId": "1",
        "balance": "45"
    },
    {
        "accountId": "2",
        "balance": "10"
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
