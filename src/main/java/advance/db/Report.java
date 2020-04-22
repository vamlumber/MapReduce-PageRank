package advance.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Report {
	 
	public static void main(String[] args) throws IOException {
		
		FileReader file=new FileReader("/Users/hsingh/Downloads/soc-pokec-profiles.txt");
		FileReader file1=new FileReader("/Users/hsingh/Downloads/hashmap.txt");
		BufferedReader hmreader=new BufferedReader(file1);
		File fout = new File("/Users/hsingh/Downloads/final_pokec.txt");
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		HashMap<String, Character> hashMap=new HashMap<String, Character>();
		String line;
		while((line=hmreader.readLine())!=null) {
			hashMap.put(line, 'o');
		}

		BufferedReader fileInputStream=new BufferedReader(file);
		int i=0;
		while((line=fileInputStream.readLine())!=null && i<999) {
			if(hashMap.containsKey(line.split("\t")[0])) {
				//System.out.println(line.split("\t")[0]);
				//System.out.println(++i+" => "+line);
				bw.write(line);
				bw.newLine();
				i++;
			}
		}
		fileInputStream.close();
		hmreader.close();
		bw.close();
	}

}
