package advance.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MakeInputFile {
	public static void main(String[] args) {
		try {
			long i=0;
			File file = new File("/Users/hsingh/Downloads/input.txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/hsingh/Downloads/input1.txt"));
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String l=line.replace("\\],", "");
				writer.write(l);
				writer.write("\n");
				System.out.println(i++);
			}
			writer.close();
			fileReader.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
