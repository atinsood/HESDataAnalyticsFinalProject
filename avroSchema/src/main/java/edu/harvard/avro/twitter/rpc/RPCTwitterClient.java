package edu.harvard.avro.twitter.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;

import edu.harvard.avro.twitter.TweetRecord;
import edu.harvard.avro.twitter.TwitterService;
import edu.harvard.avro.twitter.rpc.project2.RPCTwitterServiceImplProject2;

public class RPCTwitterClient {

	public static void main(String[] args) {

		// project 1 sending message to project 2

		// Start server for project 2
		int portForProject2 = 9900;
		RPCTwitterServiceImplProject2 project2 = new RPCTwitterServiceImplProject2(
				portForProject2);
		if (!RPCTwitterServiceImplProject2.isServerStarted) {
			project2.startServer();
		}

		// create a client for project 1 and send a message to project 2
		NettyTransceiver client = null;
		try {
			client = new NettyTransceiver(
					new InetSocketAddress(portForProject2));
			TwitterService proxyForClient1 = (TwitterService) SpecificRequestor
					.getClient(TwitterService.class, client);

			TweetRecord tweetFromClient1 = new TweetRecord();
			tweetFromClient1.setTweetId(1);
			tweetFromClient1.setUsername("client1");
			tweetFromClient1.setText("Tweeting from client 1 to project 2");
			List<CharSequence> hashtags = new ArrayList<CharSequence>();
			hashtags.add("client1");
			hashtags.add("project2");
			tweetFromClient1.setHashtags(hashtags);

			proxyForClient1.sendTweet(tweetFromClient1);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (client != null) {
				client.close();
			}
			project2.closeServer();
		}

	}
}
