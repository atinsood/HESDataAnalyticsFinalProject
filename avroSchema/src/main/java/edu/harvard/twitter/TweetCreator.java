package edu.harvard.twitter;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import edu.harvard.twitter.schema.Tweet;

/*
 * Using avro with code generation. Meaning that the Tweet class was generated 
 * from the schema file
 */
public class TweetCreator {

	private Tweet tweet1;
	private Tweet tweet2;
	private Tweet tweet3;

	public static void main(String[] args) {

		TweetCreator tweetCretor = new TweetCreator();

		// create tweets
		tweetCretor.createTweet();

		// write these to a file
		tweetCretor.serializeTweet();

		// read these from file and print
		tweetCretor.desrializeTweet();
	}

	private void serializeTweet() {

		// Create a file to write the objects to
		File tweets = new File("tweets.avro");

		// Create a file writer
		DatumWriter<Tweet> tweetWriter = new SpecificDatumWriter<Tweet>(
				Tweet.class);
		DataFileWriter<Tweet> tweetToFileWriter = new DataFileWriter<Tweet>(
				tweetWriter);

		// write the tweets to a file
		try {
			tweetToFileWriter.create(tweet1.getSchema(), tweets);
			tweetToFileWriter.append(tweet1);
			tweetToFileWriter.append(tweet2);
			tweetToFileWriter.append(tweet3);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				tweetToFileWriter.close();
			} catch (IOException e) {
				System.out.println("Exception while closing the file");
			}
		}

	}

	private void desrializeTweet() {

		File file = new File("tweets.avro");// file where the tweets were
											// serialized

		// Readers to deserialize tweet
		DatumReader<Tweet> userDatumReader = new SpecificDatumReader<Tweet>(
				Tweet.class);
		DataFileReader<Tweet> dataFileReader;
		try {
			dataFileReader = new DataFileReader<Tweet>(file, userDatumReader);

			// Read the tweets and print them
			Tweet tweet = null;
			while (dataFileReader.hasNext()) {
				tweet = dataFileReader.next(tweet);
				System.out.println(tweet);
			}

		} catch (IOException e) {
			// Unable to work with the file
			e.printStackTrace();
		}
	}

	// ------- TWEET CREATION LOGIC -------

	// Creates tweets using different ways available
	public void createTweet() {
		tweet1 = createTweetMethod1();
		tweet2 = createTweetMethod2();
		tweet3 = createTweetMethod3();
	}

	// Plain old way
	private Tweet createTweetMethod1() {
		Tweet tweet = new Tweet();
		tweet.setText("Hello world, even I am tweeting");
		tweet.setUser("user1");
		tweet.setTweetId(1);
		return tweet;
	}

	// Using constructor only
	private Tweet createTweetMethod2() {
		Tweet tweet = new Tweet(2, "user2",
				"Hello world, even I am tweeting using method 2");
		return tweet;
	}

	// Using builder pattern
	private Tweet createTweetMethod3() {
		Tweet tweet = Tweet.newBuilder().setTweetId(1).setUser("user3")
				.setText("Hello world, even I am tweeting using method 3")
				.build();
		return tweet;
	}

}
