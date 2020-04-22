
package bigdata;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DB2_2019 {
	// tt0000091;short;The House of the Devil;1896;Horror,Short

	private static final String[] genreArray = { "Comedy;Romance", "Action;Thriller", "Adventure;Sci-Fi" };

	static String constructKey(String[] values) {

		int year = Integer.parseInt(values[3]);
		StringBuilder builder = new StringBuilder();
		if (year >= 2001 && year <= 2005) {
			builder.append("[2001-2005]");
		} else if (year >= 2006 && year <= 2010) {
			builder.append("[2006-2010]");
		} else if (year >= 2011 && year <= 2015) {
			builder.append("[2011-2015]");
		}else {
			return null;
		}
		String genreKey = null;
		for (String genre : genreArray) {
			String[] each = genre.split(";");
			if (values[4].contains(each[0]) && values[4].contains(each[1])) {
				genreKey = genre;
			}
		}
		if (genreKey == null) {
			return null;
		}
		builder.append(",");
		builder.append(genreKey);
		return builder.toString();
	}

	public static class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
		// to store the key
		private Text keyText = new Text();
		// to store value
		private final static IntWritable one = new IntWritable(1);

		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			try {

				String[] split = value.toString().split(";");
				String type = split[1];
				// to avoid bad data
				if (!"movie".equals(type) || !split[3].matches("^[0-9]{4}$") || "\\N".equals(split[4]))
					return;
				int year = Integer.parseInt(split[3]);
				String genKey;
				if ((genKey=constructKey(split))!=null) {
	
						keyText.set(genKey);
						context.write(keyText, one);
					

				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		// to set the value
		private IntWritable result = new IntWritable();

		@Override
		protected void reduce(Text arg0, Iterable<IntWritable> arg1,
				Reducer<Text, IntWritable, Text, IntWritable>.Context arg2) throws IOException, InterruptedException {
			try {

				int sum = 0;
				// add all the values
				for (IntWritable intWritable : arg1) {
					sum += intWritable.get();
				}
				result.set(sum);// set to result
				arg2.write(arg0, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// to calculate the time
		Date time = Calendar.getInstance().getTime();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(DB2_2019.class);
		job.setMapperClass(WordCountMapper.class);
		// to minimize the mapper output
		job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(WordCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		int i = 1;
		// time calucation
		if (job.waitForCompletion(true)) {
			i = 0;
			Date timeC = Calendar.getInstance().getTime();
			System.out.println("Total time taken for completion in seconds");
			System.out.println((timeC.getTime() - time.getTime()) / 1000);
		}
		System.exit(i);

	}

}
