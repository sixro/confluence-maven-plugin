package confluencemavenplugin;

import org.apache.maven.plugin.*;

/**
 * Goal which update a confluence space with documentation
 * found whitin your code.
 * 
 * @goal update
 * @phase deploy
 */
public class UpdateMojo extends AbstractMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("WARNING: does nothing! This is a work in progress!");
	}

}
