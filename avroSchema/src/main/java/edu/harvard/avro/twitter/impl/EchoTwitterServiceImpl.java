package edu.harvard.avro.twitter.impl;

import org.apache.avro.AvroRemoteException;

import edu.harvard.avro.twitter.SendError;
import edu.harvard.avro.twitter.TweetRecord;
import edu.harvard.avro.twitter.TwitterService;


public class EchoTwitterServiceImpl implements TwitterService {

	@Override
	public Void sendTweet(TweetRecord tweet) throws AvroRemoteException,
			SendError {

		//Echos back the contents
		System.out.println("Received message with id: " + tweet.getTweetId()
				+ " from user: " + tweet.getUsername() + " and has text: "
				+ tweet.getText() + " with hashtags: " + tweet.getHashtags());
		
		return null;
	}

}
