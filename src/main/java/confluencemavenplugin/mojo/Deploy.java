package confluencemavenplugin.mojo;

import java.io.File;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.settings.*;

import confluencemavenplugin.*;

@Mojo(
		name="deploy",
		defaultPhase=LifecyclePhase.DEPLOY,
		requiresOnline=true,
		requiresProject=true
)
public class Deploy extends AbstractMojo {

	@Parameter(defaultValue="${settings}", readonly=true)
	private Settings settings;
	
	@Parameter(name="outputDirectory", defaultValue="${project.build.directory}/confluence")
	private File outputDirectory;

	@Parameter(name="serverId", required=true)
	private String serverId;
	
	@Parameter(name="endpoint", required=true)
	private String endpoint;

	@Parameter(name="spaceKey", required=true)
	private String spaceKey;

	@Parameter(name="parentTitle", required=true)
	private String parentTitle;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(getClass().getName() + "execute***");

		Server server = settings.getServer(serverId);
		if (server == null)
			throw new MojoFailureException("Unable to find any server identified by \"" + serverId + "\" in your settings.xml");

//		Confluence confluence = new Confluence(
//				new Confluence.Credentials(server.getUsername(), server.getPassword()), 
//				endpoint, 
//				spaceKey
//		);

		ConfluenceMavenPlugin plugin = new ConfluenceMavenPlugin();
		
		Confluence confluence = null;
		try {
			confluence = new Confluence(
					endpoint, 
					new Confluence.Credentials(server.getUsername(), server.getPassword()), 
					spaceKey
			);
			confluence.login();

			plugin.deploy(confluence, outputDirectory, parentTitle);
		} catch (DeployException e) {
			throw new MojoExecutionException("Unable to deploy to confluence", e);
		} catch (ConfluenceException e) {
			throw new MojoExecutionException("Unable to deploy to confluence", e);
		} finally {
			if (confluence != null)
				confluence.logout(); 
		}
	}

}
