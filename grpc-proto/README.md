# Protocol Buffer with proto3


To build the project and generate Client & Server codes use following command in project folder:

```bash
# For generating, packaging, and installing client and server code to local maven repository
mvn clean install 
```

# Running apps


### Server 
```
run GRPC SERVER -> Localhost:8000
```


### Client 
```
Run GRPC CLient -> localhost:8001 (netty, for grpc communication) 
                   localhost:8002 (tomcat, for REST communication) 
```

### Description
*On startup the client sends a msg to server, and return, this is done on the blocking stub; the server 
responds to the client;* 



```
In postman we have the following endpoints

localhost:8002/non-async?client=andra&msg=ciao

-> here we send different messages to the server, and we receive the response that we display 
communication between client and server via blocking stub

localhost:8002/async?client=andra&msg=ciao

->here we send different messages to the server, and we receive the response that we display
communication via grpc's non blocking stub, for more async functionality
