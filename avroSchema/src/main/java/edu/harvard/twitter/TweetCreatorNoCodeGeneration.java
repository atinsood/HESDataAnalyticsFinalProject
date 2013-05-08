package edu.harvard.twitter;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

/*
 * Using avro without schema generation. Meaning that the Tweet class 
 * was not generated from the schema and the raw schema was used 
 */
public class TweetCreatorNoCodeGeneration {

	public static void main(String[] args) {

		TweetCreatorNoCodeGeneration tweetCreator = new TweetCreatorNoCodeGeneration();

		// Use the avro schema file
		Schema schema;
		try {
			StringBuilder path = new StringBuilder();
			path.append("src").append(File.separator).append("main")
					.append(File.separator).append("avro")
					.append(File.separator).append("tweet.avsc");
			schema = new Parser().parse(new File(path.toString()));
			tweetCreator.createTweetAndSerialize(schema);
			tweetCreator.deserailizeGenericData(schema);
		} catch (IOException e) {
			// Failure to read schema file
			e.printStackTrace();
		}

	}

	private void createTweetAndSerialize(Schema schema) {

		// Notice how we are not using the Tweet class
		// but rather using GenericRecord
		GenericRecord tweet1 = new GenericData.Record(schema);
		tweet1.put("tweetId", 4);
		tweet1.put("user", "user4");
		tweet1.put("text", "Generated this tweet without schema");

		serializeGenericData(schema, tweet1);

	}

	/*
	 * @param schema Schema that is being used while writing the records
	 */
	private void serializeGenericData(Schema schema, GenericRecord tweet) {
		// Serialization of data generated without code generation
		// is similar to data generated with code generation

		// file where to write data
		File file = new File("tweetsWithoutCodeGen.avro");
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(
				schema);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(
				datumWriter);
		try {
			dataFileWriter.create(schema, file);
			dataFileWriter.append(tweet);
		} catch (IOException e) {
			// Problems while writing to the file
			e.printStackTrace();
		} finally {
			try {
				dataFileWriter.close();
			} catch (IOException e) {
				// Problems while closing the file
				e.printStackTrace();
			}
		}

	}

	private void deserailizeGenericData(Schema schema) {

		File file = new File("tweetsWithoutCodeGen.avro");
		// Deserializing is similar to deserializing with code generation
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(
				schema);
		DataFileReader<GenericRecord> dataFileReader;
		try {
			dataFileReader = new DataFileReader<GenericRecord>(file,
					datumReader);
			GenericRecord tweet = null;
			while (dataFileReader.hasNext()) {
				tweet = dataFileReader.next(tweet);
				System.out.println(tweet);
			}
		} catch (IOException e) {
			// Failure to read the file
			e.printStackTrace();
		}

	}
}
