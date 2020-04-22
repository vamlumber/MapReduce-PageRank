package advance.db;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Entry {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// to calculate the time
		Date time = Calendar.getInstance().getTime();
//		Configuration conf = new Configuration();
//		Job job = Job.getInstance(conf, "Page Rank");
//		job.setJarByClass(Entry.class);
//		job.setMapperClass(PGMapper.class);
//		// to minimize the mapper output
//		job.setReducerClass(PGReducer.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(Text.class);
//		FileInputFormat.addInputPath(job, new Path(args[0]));
//		FileOutputFormat.setOutputPath(job, new Path(args[1]));
//		int i = 1;
//		// time calucation
//		// JobClient.runJob(conf);
//
//		if (job.waitForCompletion(true)) {
//			i = 0;
//			Date timeC = Calendar.getInstance().getTime();
//			System.out.println("Total time taken for completion in seconds");
//			System.out.println((timeC.getTime() - time.getTime()) / 1000);
//		}
//		System.exit(i);
		String in=args[0];
		String out=args[1];

		for (int i = 1; i < 30; i++) {
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "Page Rank");
			job.setJarByClass(Entry.class);
			job.setMapperClass(PGMapper.class);
			// to minimize the mapper output
			job.setReducerClass(PGReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.addInputPath(job, new Path(in));
			FileOutputFormat.setOutputPath(job, new Path(out+i));
			in=out+i;
			//int i = 1;
			// time calucation
			// JobClient.runJob(conf);

			if (job.waitForCompletion(true)) {
				//i = 0;
				Date timeC = Calendar.getInstance().getTime();
				System.out.println(" time taken for completion in seconds");
				System.out.println((timeC.getTime() - time.getTime()) / 1000);
			}
			//System.exit(i);
		}
		
		//System.exit(1);
		Date timeC = Calendar.getInstance().getTime();
		System.out.println("total time taken for completion in seconds");
		System.out.println((timeC.getTime() - time.getTime()) / 1000);

	}

}
