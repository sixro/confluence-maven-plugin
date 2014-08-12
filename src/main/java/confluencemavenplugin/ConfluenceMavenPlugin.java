package confluencemavenplugin;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.codehaus.swizzle.confluence.*;


/**
 * Represents the {@code confluence-maven-plugin} main class (but not the {@code Mojo}s).
 * 
 * <p>
 * All the things done by this plugin starts here. Instead, if what you need is to take a look to
 * {@code Mojo}s provided, you can see them under package {@code confluencemavenplugin.mojo}. 
 * </p>
 */
public class ConfluenceMavenPlugin {

	public void generate(File readme, File outputDirectory) throws FileNotFoundException, IOException {
		if (! outputDirectory.exists())
			outputDirectory.mkdirs();
		
		Markdown markdown = new Markdown(readme);
		markdown.toHtmlFile(outputDirectory);
	}

	public void deploy(Confluence confluence, String spaceKey, File outputDirectory, String readmePageId) throws DeployException {
		try {
			Page page = confluence.getPage(spaceKey, readmePageId);
			page.setContent(IOUtils.toString(new FileReader(new File(outputDirectory, "README.html"))));
			confluence.storePage(page);
		} catch (Exception e) {
			throw new DeployException("Unable to deploy confluence pages", e);
		}
	}

}
