package confluencemavenplugin;

import java.io.*;


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

	public void deploy(Confluence confluence, File outputDirectory, String parentTitle) throws DeployException {
		if (! confluence.existPage(parentTitle))
			throw new DeployException("Unable to find any page with title '" + parentTitle + "' to use as parent");
		
		try {
			confluence.addPage(parentTitle, new File(outputDirectory, "README.html"));
		} catch (IOException e) {
			throw new DeployException("Unable to deploy a page", e);
		}
	}

}
