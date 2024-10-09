
### Testing comm between client and server on grpc. 


## Client
Client uses the following ports 
* 8001 for grpc (netty)
* 8002 for rest (tomcat)
## Server
Server uses port 
* 8000 for grpc communications 
* one time message over blocking stub

## POSTMAN Calls - on the client to trigger grpc comm
```
localhost:8002/send?client=postman2&msg=hello
```

* 100 messages over blocking stub
```
localhost:8002/non-async?client=andra&msg=ciao
```
* 100 messages over async / non-blocking stub
```
localhost:8002/async?client=andra&msg=ciao
```

