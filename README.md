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
├── README.md
├── avroMapReduce
├── avroSchema
└── javaXPython
```
------------------------------------
### avroSchema ###
------------------------------------

This project basically highlights avro being used as a seralization/deserialtion framework and basic RPC

```
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

------------------------------------
### javaXPython ###
------------------------------------
This project highlights how avro can be used for communicating across multiple programming languages

```
├── pom.xml
└── src
    ├── main
    │   ├── avro
    │   │   └── tweetWithAvdl.avdl
    │   ├── java
    │   │   └── edu
    │   │       └── harvard
    │   │           └── avro
    │   │               └── twitter
    │   │                   ├── SendError.java
    │   │                   ├── TweetRecord.java
    │   │                   ├── TwitterService.java
    │   │                   └── impl
    │   │                       └── TwitterServiceImpl.java
    │   └── python
    │       ├── avro-tools-1.7.4.jar
    │       ├── pythonTweeterServer.py
    │       ├── tweetWithAvdl.avdl
    │       └── tweetWithAvr.avr
```

tweetWithAvdl.avdl file describes both schema and the service interface.  Running mvn clean package generates SendError.java , TweetRecord.java and TwitterService.java

TwitterServiceImpl.java implements TwitterService.java and starts an http server on 9090 port.
```
	@Override
	public Void sendTweet(TweetRecord tweet) throws AvroRemoteException,
			SendError {

		// Echos back the contents
		System.out.println("Java server received message with id: "
				+ tweet.getTweetId() + " from user: " + tweet.getUsername()
				+ " and has text: " + tweet.getText());

		return null;
	}

	public void startServer() {

		try {
			server = new HttpServer(new SpecificResponder(TwitterService.class,
					this), port);

			server.start();
			isServerStarted = true;
		} catch (Exception e) {
			isServerStarted = false;
			server.close();
			e.printStackTrace();
		}
	}
```

src/main/python/pythonTweeterServer.py acts as the client for the above server and sends a RPC call over HTTP to sendTweet method containing TweetRecord.

```
> cat javaXPython/src/main/python/pythonTweeterServer.py
#!/usr/bin/python

import avro.ipc as ipc
import avro.protocol as protocol

avroProtocol = protocol.parse(open("tweetWithAvr.avr").read())

java_rpc_server_address = ("localhost", 9090)

if __name__ == "__main__":

    client = ipc.HTTPTransceiver(java_rpc_server_address[0], java_rpc_server_address[1])
    requestor = ipc.Requestor(avroProtocol, client)

    tweet = {"tweetId": 1, "username": "pythonUser", "text": "This is a tweet from python"}

    params = {"tweet": tweet}
    requestor.request("sendTweet", params)
    client.close()
```

This example highlights the fact that how a typed schema like avro can be useful when communicating across multiple services written in different programming languages. 

Note : tweetWithAvr.avr file  used in the python client is generated using 
java -jar avro-tools-1.7.4.jar idl tweetWithAvdl.avdl tweetWithAvr.avr

------------------
avroMapReduce
------------------
This example demonstrates how avro can be useful with map reduce. This project generates a number of sample tweets and then write them to a input/tweets.avro file which can be later used as an input for map reduce. The schema used is defined by src/main/avro/tweets.avsc . Running mvn clean package generates the schema as src/main/java/edu/harvard/twitter/schema/Tweet.java

```
├── avro-tools-1.7.4.jar
├── dependency-reduced-pom.xml
├── input
│   └── tweets.avro
├── pom.xml
└── src
    ├── main
    │   ├── avro
    │   │   └── tweet.avsc
    │   └── java
    │       └── edu
    │           └── harvard
    │               └── twitter
    │                   ├── GenerateTweets.java
    │                   ├── PhoneSentimentAnalyzer.java
    │                   └── schema
    │                       └── Tweet.java
```

The code to generate random tweets can be run using mvn exec:java -q -Dexec.mainClass=edu.havrvard.twitter.GenerateTweets

Generate tweets randomly picks a phone brand name and an emotion and then generates a tweet with text "I <emotion> my new <phone brand> phone. Additionaly the emotion and phone brand are also added as hash tags to the tweet, which are later used to figure out the number of people associated with a given emotion for a phone brand. 

The code needed for mapper, reducer and running the job is present in PhoneSentimentAnalyzer.java

Mapper

```
public static class PhoneSentimentMapper extends
			AvroMapper<Tweet, Pair<CharSequence, Integer>> {

		@Override
		public void map(Tweet tweet,
				AvroCollector<Pair<CharSequence, Integer>> collector,
				Reporter reporter) throws IOException {

			// TODO Possible bug. Currently assumes emotion hashtag will follow
			// brand hashtag
			StringBuilder hashtags = new StringBuilder();
			// Concatenate all hashtags
			for (CharSequence tag : tweet.getHashtags()) {
				hashtags.append(tag).append(" ");
				// FIXME There will always be a trailing space
			}

			collector.collect(new Pair<CharSequence, Integer>(hashtags
					.toString(), 1));
		}
	}
```

Mapper takes the schema object Tweet as input and emits out hashtags and a count of 1. 

Reducer
```
public static class PhoneSentimentReducer extends
			AvroReducer<CharSequence, Integer, Pair<CharSequence, Integer>> {

		@Override
		public void reduce(CharSequence key, Iterable<Integer> values,
				AvroCollector<Pair<CharSequence, Integer>> collector,
				Reporter reporter) throws IOException {

			int sum = 0;
			for (Integer count : values) {
				sum += count;
			}

			collector.collect(new Pair<CharSequence, Integer>(key, sum));
		}
	}
```
Reducer takes the hashtags emitted by mapper and key and count of 1 everytime a mapper found a hashtag as value. The reducer then counts all the 1's corresponding to a given set of hashtags and emits out the hashtag followed total as the result. 

You can run the job either locally using

mvn exec:java -q -Dexec.mainClass=edu.harvard.twitter.PhoneSentimentAnalyzer -Dexec.args="input output"

or on hadoop by uploading the tweets.avro to HDFS and then running

hadoop jar target/avroMapReduce-1.0-SNAPSHOT.jar <location of tweets.avro on HDFS> <location where you want the output>

The output generated with either approach is in avro format which can be made human readable by 

java -jar avro-tools-1.7.4.jar tojson part-00000.avro
