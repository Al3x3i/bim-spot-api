# bim-spot-api
A simple app using the technology of the public IUCN Red List API.

### The root context of the API `/api/*`
Ex:

`
localhost:8080/api/preview
`

### Utils
Convert JSON to POJO Objects in Java Online

`
https://json2csharp.com/json-to-pojo
`

### Build project
```
./gradlew build
```

### Run 
```
$ ./gradlew build
```

### API documentation
`
http://localhost:8080/swagger-ui.html
`

### Build Docker file
```
$ ./gradlew build
$ docker build -t al3x3i/bimspot .
$ docker run -p 8080:8080 al3x3i/bimspot