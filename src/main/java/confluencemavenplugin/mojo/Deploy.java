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

	@Parameter(name="serverId")
	private String serverId = null;
	
	@Parameter(name="endpoint", required=true)
	private String endpoint;

	@Parameter(name="spaceKey", required=true)
	private String spaceKey;

	@Parameter(name="parentTitle", required=true)
	private String parentTitle;
	
	@Parameter(name="readme", defaultValue="${project.basedir}/README.md")
	private File readme;

	@Parameter(name="username")
	private String username = null;

	@Parameter(name="password")
	private String password = null;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(getClass().getName() + "execute***");

		setUsernameAndPassword();

		ConfluenceMavenPlugin plugin = new ConfluenceMavenPlugin();
		Confluence confluence = null;
		try {
			confluence = new Confluence(endpoint, new Confluence.Credentials(username, password), spaceKey);
			confluence.login();

			plugin.deploy(confluence, outputDirectory, parentTitle, readme);
		} catch (DeployException e) {
			throw new MojoExecutionException("Unable to deploy to confluence", e);
		} catch (ConfluenceException e) {
			throw new MojoExecutionException("Unable to deploy to confluence", e);
		} finally {
			if (confluence != null)
				confluence.logout(); 
		}
	}

	private void setUsernameAndPassword() throws MojoFailureException, MojoExecutionException {
		if(serverId != null){
			Server server = settings.getServer(serverId);
			if (server == null) {
				throw new MojoFailureException("Unable to find any server identified by \"" + serverId + "\" in your settings.xml");
			} else {
				username = server.getUsername();
				password = server.getPassword();
			}
		} else if (username == null || password == null){
			throw new MojoExecutionException("Nether serverId or username and password for confluence defined");
		}
	}

}
