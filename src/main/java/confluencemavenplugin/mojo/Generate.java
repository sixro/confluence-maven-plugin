package confluencemavenplugin.mojo;

import java.io.*;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import confluencemavenplugin.ConfluenceMavenPlugin;

@Mojo(
		name="generate",
		defaultPhase=LifecyclePhase.PROCESS_RESOURCES,
		requiresOnline=false,
		requiresProject=true
)
public class Generate extends AbstractMojo {

	@Parameter(defaultValue="${project}", readonly=true)
	private MavenProject project;
	
	@Parameter(name="readme", defaultValue="${project.basedir}/README.md")
	private File readme;
	
	@Parameter(name="outputDirectory", defaultValue="${project.build.directory}/confluence")
	private File outputDirectory;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(getClass().getName() + ".execute***");
		getLog().info(getClass().getName() + " readme: " + readme);
		getLog().info(getClass().getName() + " outputDirectory: " + outputDirectory);
		
		if (! readme.exists() || !readme.isFile() || !readme.canRead())
			throw new MojoFailureException("README file \"" + readme + "\" does not exist or it does not seem to be readable");

		try {
			ConfluenceMavenPlugin plugin = new ConfluenceMavenPlugin();
			plugin.generate(readme, project, outputDirectory);
		} catch (IOException e) {
			throw new RuntimeException("Unable to generate README", e);
		}
	}

}
