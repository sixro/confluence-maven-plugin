package confluencemavenplugin;

import java.io.*;
import java.util.Arrays;

import org.apache.commons.io.*;

import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;



public class Markdown {

	private static final String CHARACTER_ENCODING = System.getProperty("markdown.characterencoding", "UTF-8");
	
	private String text;
	private File file;


	Parser parser = Parser.builder().extensions(Arrays.asList(TablesExtension.create())).build();
	HtmlRenderer renderer = HtmlRenderer.builder().extensions(Arrays.asList(TablesExtension.create())).build();

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
			return new InputStreamReader(new FileInputStream(file), CHARACTER_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to create a Reader on file " + file + " due to an unsupported encoding", e);
		}
	}

	public String toHtml() {
		Node document = parser.parse(text);
		return renderer.render(document);
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
