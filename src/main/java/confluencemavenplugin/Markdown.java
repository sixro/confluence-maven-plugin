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
		this(IOUtils.toString(newReader(file)));
		this.file = file;
	}

	private static Reader newReader(File file) throws FileNotFoundException {
		try {
			return new InputStreamReader(new FileInputStream(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to create a Reader on file " + file + " due to an unsupported encoding", e);
		}
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
