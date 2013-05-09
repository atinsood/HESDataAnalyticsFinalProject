Harvard Extension School - Big Data Analytics - Final Project
==============================================================

This a an umbrella project which consists of submodules highlighting various
aspects of Avro.

- As a serialization/deserialization framework
- As an RPC framework
- As a cross language communication framework
- As a map reduce input output data framework

This is how the project structure looks like

```
asood@starbuck-2 [~/work/opensource/college]
> tree -L 1
.
├── README.md
├── avroMapReduce
├── avroSchema
└── javaXPython
```

> ### avroSchema ###
This project basically highlights avro being used as a seralization/deserialtion framework and basic RPC

```
asood@starbuck-2 [~/work/opensource/college/avroSchema]
> tree
.
├── pom.xml
├── src
│   ├── main
│   │   ├── avro
│   │   │   ├── tweet.avsc
│   │   │   └── tweetWithAvdl.avdl
│   │   ├── java
│   │   │   └── edu
│   │   │       └── harvard
│   │   │           ├── avro
│   │   │           │   └── twitter
│   │   │           │       ├── SendError.java
│   │   │           │       ├── TweetRecord.java
│   │   │           │       ├── TwitterService.java
│   │   │           │       ├── impl
│   │   │           │       │   ├── EchoTwitterServiceImpl.java
│   │   │           │       │   └── TwitterClient.java
│   │   │           │       └── rpc
│   │   │           │           ├── RPCTwitterClient.java
│   │   │           │           ├── project1
│   │   │           │           └── project2
│   │   │           │               └── RPCTwitterServiceImplProject2.java
│   │   │           └── twitter
│   │   │               ├── TweetCreator.java
│   │   │               ├── TweetCreatorNoCodeGeneration.java
│   │   │               └── schema
│   │   │                   └── Tweet.java
│   │   ├── python
│   │   │   ├── tweet.avsc
│   │   │   ├── tweetExample.py
│   │   │   └── tweets.avro
│   │   └── resources
│   └── test
│       └── java
├── tweets.avro
└── tweetsWithoutCodeGen.avro
```

Note that src/main/avro/tweet.avsc file is what describes the schema.
```
> cat avroSchema/src/main/avro/tweet.avsc
{"namespace": "edu.harvard.twitter.schema",
 "type": "record",
 "name": "Tweet",
 "fields": [
     {"name": "tweetId", "type": "int"},
     {"name": "user", "type": "string"},
     {"name": "text", "type": "string"}
 ]
}
```

The pom.xml has been customized to perform code generation. 
Running mvn clean package generates src/main/java/edu/harvard/twitter/schema/Tweet.java

TweeterCreator.java uses this generated Tweet.java to demonstrate how to use avro genrated POJO . Similarly TweetCreatorNoCodeGeneration.java demonstrates how to use avro without the need to generate Tweet.java and how the tweet.avsc file itself can be used to create a record that conforms to a schema. 

tweets.avro and tweetsWithoutCodeGen.avro are the files that get generated when the code in TweeterCreator.java and TweetCreatorNoCodeGeneration.java is run respectively. 

src/main/python/tweetExample.py contains code to do serializing/deserializing in python while conforming to the same tweet.avsc schema.

Similarly tweetWithAvdl.avdl is a avro file which describes service to do RPC in addition to schema.

```
> cat avroSchema/src/main/avro/tweetWithAvdl.avdl
@namespace ("edu.harvard.avro.twitter")
protocol TwitterService{

    record TweetRecord{
        int tweetId;
        string text;
        string username;
        array<string> hashtags;
        }

    error SendError{
        string message;
        }

    void sendTweet(TweetRecord tweet) throws SendError;

    }
```
The file basically has an avro schema which describes the object TweetRecord and has a service called TwitterService which has a method called sendTweet. 
    
Running mvn clean pacakage generates the folder structure
```
src/main/java/edu/harvard/avro/twitter/
├── SendError.java
├── TweetRecord.java
├── TwitterService.java
```
TwitterService.java is the service class. src/main/java/edu/harvard/avro/twitter/impl/EchoTwitterServiceImpl.java is a sample implementation of this class. Refer to  src/main/java/edu/harvard/avro/twitter/impl/TwitterClient.java to see how objects generated via avro can be used for exchanging data. 

src/main/java/edu/harvard/avro/twitter/impl/rpc/RPCTwitterServiceImplProject2.java and src/main/java/edu/harvard/avro/twitter/impl/rpc/RPCTwitterClient.java demonstrates how data can be transferred over RPC
