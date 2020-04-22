package advance.db;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PGReducer extends Reducer<Text, Text, Text, Text>{

	private Text value=new Text();
	@Override
	protected void reduce(Text arg0, Iterable<Text> arg1,
			Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
		String vertexList="";
		double pageRank=0.0;
		Iterator<Text> iterator = arg1.iterator();
		while(iterator.hasNext()) {
			String next = iterator.next().toString();
			String[] split=next.split(" ");
			
			if(split.length==1) {
				pageRank+=Double.parseDouble(split[0]);
			}else {
				vertexList=split[1];
			}
			System.out.println("Reducer - "+arg0.toString()+" -- "+next.toString());
		
		}
		//pageRank=((1-0.85)/560273)+(0.85)*pageRank;
		value.set(pageRank+" "+vertexList);
		context.write(arg0, value);
		
	}

}
