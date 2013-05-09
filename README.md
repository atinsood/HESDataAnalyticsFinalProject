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
