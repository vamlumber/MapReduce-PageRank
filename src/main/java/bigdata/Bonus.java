package bigdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.http.util.Args;

public class Bonus {

	//this is to read actors data
	public static class MapImdbActors extends Mapper<Object, Text, Text, Text> {
		//to set the key
		private Text keyContext = new Text();
		//to set the value
		private Text valueContext = new Text();

		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String[] split = value.toString().split(";");
			// creates and sets the first pair for every record <actorId,titleId>
			keyContext.set(split[1]);
			valueContext.set(split[0]);
			context.write(keyContext, valueContext);
			// creates and sets the second pair for every record <actorId,actorName>
			keyContext.set(split[1]);
			valueContext.set("ACT"+split[2]);//appended ACT so that value can be identified in the reducer
			context.write(keyContext, valueContext);

		}

	}

	public static class MapImdbDirectors extends Mapper<Object, Text, Text, Text> {

		//to set the key
		private Text keyContext = new Text();
		//to set the value
		private Text valueContext = new Text();

		@Override
		protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			String[] split = value.toString().split(";");
			//creates key valur pair <directorId,titleId>
			keyContext.set(split[1]);
			valueContext.set(split[0]);
			context.write(keyContext, valueContext);

		}

	}

	public static class BonusReducer extends Reducer<Text, Text, Text, Text> {
		//to set the value
		private Text val = new Text();

		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1, Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			//used hashmap to find repetative titles because has based comparison is always fast
			Map<String,String> movieMap=new HashMap<String, String>();
			//to set the first value
			String actorName = null;
			int i=0;
			boolean b = true;
			for (Text t : arg1) {
				String value=t.toString();
				if(value.startsWith("ACT")) {//if the value is actor name then store
					if(b) {// this will ignore repetitive actors name
					actorName=value.substring(3);
					b=false;// to exit the condition
					}
				}else if (movieMap.containsKey(value)) {
					i++;//if same imdb title is found then increment the counter else put the value in map
				}else {
					movieMap.put(value, null);
				}

			}
			//if counter is greater than or = to 100 then write to output file
			if (i>=100) {
				val.set(actorName+" "+i);
				arg2.write(arg0, val);
			}

		}

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//to calulate the time
		Date time = Calendar.getInstance().getTime();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Bonus");
		job.setJarByClass(Bonus.class);

		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, MapImdbActors.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, MapImdbDirectors.class);
		job.setReducerClass(BonusReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		int i = 1;
		//total time calculation
		if (job.waitForCompletion(true)) {
			i = 0;
			Date timeC = Calendar.getInstance().getTime();
			System.out.println("Total time taken for completion ");
			System.out.println((timeC.getTime() - time.getTime())/1000);
		}
		System.exit(i);
	}

}
