package confluencemavenplugin;

import org.apache.maven.plugin.*;

/**
 * Deploy documentation to a confluence space.
 * 
 * @goal deploy
 * @phase process-resources
 * 
 * @execute goal="generate"
 */
public class DeployMojo extends AbstractMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("[FAKE] deploying to confluence...");
	}

}
