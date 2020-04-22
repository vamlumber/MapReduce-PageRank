package advance.db;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PGMapper extends Mapper<Object, Text, Text, Text>{
	
	private Text keyText=new Text();
	private Text valueText=new Text();

	@Override
	protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String[] split = value.toString().split("\\s+");
			double pageRank=Double.parseDouble(split[1]);
		if(split.length<3) {
			keyText.set(split[0]);
			valueText.set(pageRank+"");
			return;
		}
		String[] vertex=split[2].split(",");
		int totalsize=vertex.length;
		double dividedPageRank=pageRank/(double)totalsize;
		for(int i=0; i< vertex.length;i++) {
			keyText.set(vertex[i]);
			valueText.set(""+dividedPageRank);
			System.out.println("Mapper - "+keyText.toString()+" -- "+valueText.toString());
			context.write(keyText, valueText);
		}
		
		
		keyText.set(split[0]);
		valueText.set(pageRank+" "+split[2]);
		System.out.println("Mapper - "+keyText.toString()+" -- "+valueText.toString());
		context.write(keyText, valueText);	
		
	}

}
