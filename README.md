#### Used technologies
 - Java 19
 - Spring-boot
 - Caffeine
 - Swagger
 - TestContainers
 - Mockito

#### User interface

Swagger UI:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


Curl examples:
```
curl GET http://localhost:8080/api/percentage/percentage/all | jq
curl GET http://localhost:8080/api/percentage/percentage/ruby-server-sdk | jq
```

Example of response:
```
{
  "repositoryName": "ruby-server-sdk",
  "languagesInfo": [
    {
      "language": "Ruby",
      "percentage": 99.85
    },
    {
      "language": "Shell",
      "percentage": 0.15
    }
  ]
}
```

&nbsp;
##### GtiHub possible language responses which have to be handled
```
{"Ruby":722926,"HTML":2490,"Shell":562}
{}
```

&nbsp;
#### Steps to generate new UserToken:
```
username -> 
  settings -> 
    Developer settings -> 
      personal access tokens 
        -> generate new token
```

&nbsp;
#### Currently token generated with 30 days of expiration:
```
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
```

&nbsp;
#### Configuration parameters

```
client.request.itemPerPage=30

Example:
https://api.github.com/users/productboard/repos?page=1&per_page=30

```
- _Used for initial downloading gitGub list of repositories._
- _**per_page** http GET query parameter._ 

&nbsp;
```
client.retry.attempts=5
```
- _Count of retry attempts in case of an error occurred during gitHub API call._

&nbsp;
```
client.retry.delaySec=1
```
- _Delay between retries._

&nbsp;
```
client.request.timeoutSec=10
```
- _Delay for each specific reauest to gitHub. If response isn't received within the timeout then an Exception is thrown._

&nbsp;
```
client.api.token: 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
```
- _Token used as part of gitHub request reduces rate limits restrictions._

&nbsp;
```
execution.intervalHours=24
```
- _Job to download actual data from gitHub execute every 24 hours._

