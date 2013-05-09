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

Note that src/main/avro/tweet.avsc file is what describes the schema. The pom.xml has been customized to perform code generation. 
Running mvn clean package generates src/main/java/edu/harvard/avro/twitter/schema/Tweet.java

TweeterCreator.java uses this generated Tweet.java to demonstrate how to use avro genrated POJO . Similarly TweetCreatorNoCodeGeneration.java demonstrates how to use avro without the need to generate Tweet.java and how the tweet.avsc file itself can be used to create a record that conforms to a schema. 

tweets.avro and tweetsWithoutCodeGen.avro are the files that get generated when the code in TweeterCreator.java and TweetCreatorNoCodeGeneration.java is run respectively. 

src/main/python/tweetExample.py contains code to do serializing/deserializing in python while conforming to the same tweet.avsc schema.
