#!/usr/bin/python

import avro.schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter


def readAndWriteAvro():
    """ Unlike java, avro does not let you generate
        code for Tweet in python. So only way to read and write
        data is without using code generation"""

    #Read the schema
    schema = avro.schema.parse(open("tweet.avsc").read())


    #write some data
    writer = DataFileWriter(open("tweets.avro", "w"), DatumWriter(), schema)
    writer.append({"tweetId": 5, "user": "user5", "text" : "Tweeting from python as well"})
    writer.close()

    #read the same data
    tweets = DataFileReader(open("tweets.avro", "r"), DatumReader())
    for tweet in tweets:
        print tweet
    tweets.close()


if __name__ == "__main__":
    readAndWriteAvro()

