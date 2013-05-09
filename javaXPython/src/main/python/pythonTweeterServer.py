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

