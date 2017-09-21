# shorturl

## Install Dependencies

#### SBT
```
brew install sbt
```

#### DynamoDB

Follow the steps provided here:
http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html#DynamoDBLocal.DownloadingAndRunning
```
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```


## Run Locally

### Using SBT
``` 
sbt run 
```

### Using Docker
```
sbt clean docker:publishLocal
docker run -it shorturl:latest
```
