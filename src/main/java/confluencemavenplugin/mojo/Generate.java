package confluencemavenplugin.mojo;

import java.io.*;

import org.apache.maven.plugin.*;

import confluencemavenplugin.ConfluenceMavenPlugin;

/**
 * Generate documentation from markdown files.
 *
 * @goal generate
 * @phase generate-resources
 */
public class Generate extends AbstractMojo {

	/**
	 * README.md file path.
	 * 
	 * @parameter expression="${confluence.plugin.readme}" default-value="${project.basedir}/README.md"
	 */
	private File readme;
	
	/**
	 * Output directory.
	 * 
	 * @parameter expression="${confluence.plugin.outputDirectory}" default-value="${project.build.outputDirectory}/confluence"
	 */
	private File outputDirectory;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (! readme.exists() || !readme.isFile() || !readme.canRead())
			throw new MojoFailureException("README file \"" + readme + "\" does not exist or it does not seem to be readable");
		
		try {
			ConfluenceMavenPlugin plugin = new ConfluenceMavenPlugin();
			plugin.generate(readme, outputDirectory);
		} catch (IOException e) {
			throw new RuntimeException("Unable to generate README", e);
		}
	}

}
