package edu.harvard.avro.twitter.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.avro.AvroRemoteException;

import edu.harvard.avro.twitter.SendError;
import edu.harvard.avro.twitter.TweetRecord;
import edu.harvard.avro.twitter.TwitterService;

public class TwitterClient {

	public static void main(String[] args) {

		TwitterClient client = new TwitterClient();
		client.simplesend();
	}

	// Simple example using avdl
	private void simplesend() {

		// Created a record
		TweetRecord record = new TweetRecord();
		record.setTweetId(1);
		record.setUsername("user1");
		record.setText("Tweeting via avdl files");
		List<CharSequence> hashtags = new ArrayList<CharSequence>();
		hashtags.add("hello");
		hashtags.add("avdl");
		record.setHashtags(hashtags);

		TwitterService service = new EchoTwitterServiceImpl();
		try {
			service.sendTweet(record);
		} catch (SendError e) {
			e.printStackTrace();
		} catch (AvroRemoteException e) {
			e.printStackTrace();
		}

	}
}
