package edu.harvard.avro.twitter.rpc.project2;

import java.net.InetSocketAddress;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;

import edu.harvard.avro.twitter.SendError;
import edu.harvard.avro.twitter.TweetRecord;
import edu.harvard.avro.twitter.TwitterService;

public class RPCTwitterServiceImplProject2 implements TwitterService {

	private final int port;
	public RPCTwitterServiceImplProject2(int port) {
		this.port = port;
	}
	public static boolean isServerStarted;
	private static Server server;

	@Override
	public Void sendTweet(TweetRecord tweet) throws AvroRemoteException,
			SendError {

		System.out.println("===============");
		System.out.println("Project 2 received message with id: " + tweet.getTweetId()
				+ " from user: " + tweet.getUsername() + " and has text: "
				+ tweet.getText() + " with hashtags: " + tweet.getHashtags());
		System.out.println("===============");
		
		return null;
	}

	/*
	 * Start netty server to receive incoming requests from project 2
	 */
	public void startServer() {

		try {
			server = new NettyServer(new SpecificResponder(
					TwitterService.class, this), new InetSocketAddress(port));
			server.start();
			isServerStarted = true;
		} catch (Exception e) {
			isServerStarted = false;
			server.close();
			e.printStackTrace();
		}
		
	}
	
	public void closeServer(){
		server.close();
	}

}
