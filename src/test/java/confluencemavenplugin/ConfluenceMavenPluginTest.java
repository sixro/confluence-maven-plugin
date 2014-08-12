package confluencemavenplugin;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

public class ConfluenceMavenPluginTest {

	private static final File OUTPUT_DIR  = new File("target/unit-tests-output");
	private static final File README_MD   = new File("src/test/resources/README.md");
	private static final File README_HTML = new File(OUTPUT_DIR, "README.html");

	@Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
		setThreadingPolicy(new Synchroniser());
	}};
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

	/*
	@Test public void deploy_asks_to_confluence_to_upload_README_when_it_exists() throws FileNotFoundException, IOException {
		final String pageID = "myReadmeId";
		
		context.checking(new Expectations() {{ 
			oneOf(confluence).existPage(pageID);
				will(returnValue(true));
			oneOf(confluence).updatePage(pageID, README_HTML);
		}});
		
		plugin.deploy(confluence, OUTPUT_DIR, pageID);
	}*/

}
