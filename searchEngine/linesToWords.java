package searchEngine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class linesToWords {

	public static void splitWords() throws FileNotFoundException, IOException {

		ArrayList<String> words = new ArrayList<>();

		String filePath = "src//TxtFiles//output.txt";
		try (FileReader fileReader = new FileReader(filePath)) {
			StringBuffer sb = new StringBuffer();
			while (fileReader.ready()) {
				char c = (char) fileReader.read();
				if (c == ' ' || c == '\n' || c == ',' || c == '.' || c == ';' || c == ':' || c == '&' || c == '|') {
					words.add(sb.toString());
					sb = new StringBuffer();
				} else {
					sb.append(c);
				}
			}
			if (sb.length() > 0) {
				words.add(sb.toString());
			}
		}

		FileWriter writer = new FileWriter("src//TxtFiles//output.txt");
		for (String word : words) {
			writer.write(word + System.lineSeparator());
		}
		writer.close();
	}
}
