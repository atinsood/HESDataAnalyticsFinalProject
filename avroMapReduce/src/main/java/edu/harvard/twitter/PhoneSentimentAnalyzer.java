package edu.harvard.twitter;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.mapred.AvroCollector;
import org.apache.avro.mapred.AvroJob;
import org.apache.avro.mapred.AvroMapper;
import org.apache.avro.mapred.AvroReducer;
import org.apache.avro.mapred.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.harvard.twitter.schema.Tweet;

/*
 * Analyzes how many people like/hate their phone/brand
 */
public class PhoneSentimentAnalyzer extends Configured implements Tool {

	// Mapper class
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

	@Override
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Provide input and output directory");
			return -1;
		}

		JobConf conf = new JobConf(getConf(), PhoneSentimentAnalyzer.class);
		conf.setJobName("phoneSentimentAnalyzer");

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		AvroJob.setMapperClass(conf, PhoneSentimentMapper.class);
		AvroJob.setReducerClass(conf, PhoneSentimentReducer.class);

		AvroJob.setInputSchema(conf, Tweet.SCHEMA$);
		AvroJob.setOutputSchema(
				conf,
				Pair.getPairSchema(Schema.create(Type.STRING),
						Schema.create(Type.INT)));

		JobClient.runJob(conf);

		return 0;
	}

	public static void main(String args[]) throws Exception {
		int result = ToolRunner.run(new Configuration(),
				new PhoneSentimentAnalyzer(), args);
		System.exit(result);
	}
}
