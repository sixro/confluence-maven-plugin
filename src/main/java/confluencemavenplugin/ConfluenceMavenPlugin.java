package confluencemavenplugin;

import java.io.*;
import java.util.Collections;

import org.apache.commons.io.*;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * Represents the {@code confluence-maven-plugin} main class (but not the {@code Mojo}s).
 * 
 * <p>
 * All the things done by this plugin starts here. Instead, if what you need is to take a look to
 * {@code Mojo}s provided, you can see them under package {@code confluencemavenplugin.mojo}. 
 * </p>
 */
public class ConfluenceMavenPlugin {

	public void generate(File file, MavenProject project, File outputDirectory) throws FileNotFoundException, IOException {
		if (! outputDirectory.exists())
			outputDirectory.mkdirs();
		
		Markdown markdown = new Markdown(file);
		String html = markdown.toHtml();
		html = replaceProjectProperties(html, project);
		
		String htmlFilename = FilenameUtils.getBaseName(file.getName()) + ".html";
		File outputFile = new File(outputDirectory, htmlFilename);
		
		PrintWriter writer = new PrintWriter(outputFile);
		writer.write(html);
		writer.close();
	}

	private String replaceProjectProperties(String html, MavenProject project) {
		StringWriter writerOnText = new StringWriter();
		boolean evaluated = Velocity.evaluate(
				new VelocityContext(Collections.singletonMap("project", project)), 
				writerOnText, 
				getClass().getName(), 
				html
		);
		if (! evaluated)
			throw new RuntimeException("Unable to replace project properties inside content '" + html + "'");
		
		return writerOnText.toString();
	}

	public void deploy(Confluence confluence, File outputDirectory, String parentTitle) throws DeployException {
		if (! confluence.existPage(parentTitle))
			throw new DeployException("Unable to find any page with title '" + parentTitle + "' to use as parent");
		
		try {
			confluence.addOrUpdatePage(parentTitle, new File(outputDirectory, "README.html"));
		} catch (IOException e) {
			throw new DeployException("Unable to deploy a page", e);
		}
	}

}
