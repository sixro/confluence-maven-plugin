package confluencemavenplugin;

import java.io.*;

import org.apache.commons.io.*;

import com.github.rjeschke.txtmark.Processor;

public class Markdown {

	private String text;
	private File file;

	public Markdown(String text) {
		this.text = text;
		this.file = null;
	}
	
	public Markdown(File file) throws FileNotFoundException, IOException {
		this(IOUtils.toString(new FileReader(file)));
		this.file = file;
	}

	public String toHtml() {
		return Processor.process(text);
	}
	
	public void toHtmlFile(File directory) throws IOException {
		toHtmlFile(directory, FilenameUtils.getBaseName(file.getName()) + ".html");
	}

	public void toHtmlFile(File directory, String filename) throws IOException {
		File html = new File(directory, filename);
		Writer writer = new FileWriter(html);
		IOUtils.write(toHtml(), writer);
		writer.close();
	}

}
