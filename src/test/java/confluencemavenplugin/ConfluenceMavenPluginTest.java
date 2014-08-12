package confluencemavenplugin;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;

public class ConfluenceMavenPluginTest {

	private static final File OUTPUT_DIR  = new File("target/unit-tests-output");
	private static final File README_MD   = new File("src/test/resources/README.md");
	private static final File README_HTML = new File(OUTPUT_DIR, "README.html");

	private ConfluenceMavenPlugin plugin;

	@Before public void setup() throws IOException {
		if (OUTPUT_DIR.exists())
			FileUtils.deleteDirectory(OUTPUT_DIR);

		plugin = new ConfluenceMavenPlugin();
	}
	
	@Test public void generate_create_an_html_in_output_directory() throws FileNotFoundException, IOException {
		assertFalse(README_HTML.exists());

		plugin.generate(README_MD, OUTPUT_DIR);
		assertTrue(README_HTML.exists());
	}

}
