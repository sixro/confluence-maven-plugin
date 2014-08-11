package confluencemavenplugin;

import org.apache.maven.plugin.*;

/**
 * Generate documentation from markdown files.
 *
 * @goal generate
 * @phase generate-resources
 */
public class GenerateMojo extends AbstractMojo {

	
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("[FAKE] generating confluence pages...");
		
		
	}

}
