package edu.harvard.twitter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import edu.harvard.twitter.schema.Tweet;

/*
 * Class to generate a random number of tweets saying wether 
 * people like or hate their new phone of a particular brand
 */
public class GenerateTweets {

	private static final int NUM_OF_TWEETS_TO_GENERATE = 1000000;
	private static final String[] HASH_TAGS_BRAND = { "samsung", "htc",
			"nokia", "apple" };
	private static final String[] HASH_TAGS_EMOTION = { "love", "hate" };

	public static void main(String[] args) {

		File inputFile = new File("input/tweets.avro");
		DatumWriter<Tweet> writer = new SpecificDatumWriter<Tweet>(Tweet.class);
		DataFileWriter<Tweet> fileWriter = new DataFileWriter<Tweet>(writer);
		try {
			fileWriter.create(Tweet.SCHEMA$, inputFile);
			Random random = new Random();
			for (int i = 0; i < NUM_OF_TWEETS_TO_GENERATE; i++) {

				// generate a emotion hash tag and a brand hash tag
				String brand = HASH_TAGS_BRAND[random
						.nextInt(HASH_TAGS_BRAND.length)];
				String emotion = HASH_TAGS_EMOTION[random
						.nextInt(HASH_TAGS_EMOTION.length)];

				String[] hashTags = { brand, emotion };

				Tweet tweet = generateTweet(i, "I " + emotion + " my new "
						+ brand, hashTags);
				fileWriter.append(tweet);

//				System.out.println("Tweet with id: " + tweet.getTweetId()
//						+ " from user: " + tweet.getUser() + " and has text: "
//						+ tweet.getText() + " with hashtags: "
//						+ tweet.getHashtags());

			}

			fileWriter.close();
		} catch (IOException e) {
			// Issues opening the file
			e.printStackTrace();
		}

	}

	private static Tweet generateTweet(int count, String tweetText,
			CharSequence[] hashTags) {
		Tweet tweet = new Tweet();
		tweet.setUser("user " + count);
		tweet.setTweetId(count);
		tweet.setText(tweetText);
		tweet.setHashtags(Arrays.asList(hashTags));
		return tweet;
	}
}
