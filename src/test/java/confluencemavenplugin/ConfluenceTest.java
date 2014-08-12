package confluencemavenplugin;

import static org.junit.Assert.*;

import org.junit.*;

public class ConfluenceTest {

	private Confluence confluence;

	@Before public void setup() {
		confluence = new Confluence(
				"http://confluence:9090/rpc/xmlrpc", 
				new Confluence.Credentials("maven", "Gennaio2010."), 
				"VONTGC"
		);
		confluence.login();
	}
	
	@Test public void returns_false_when_a_page_does_not_exists() {
		assertFalse(confluence.existPage("This page cannot exists"));
	}

	@Test public void add_page_then_update_it_and_last_delete_it() {
		String title = "My page";
		assertFalse(confluence.existPage(title));

		confluence.addPage("Projects", title, "this is a test");
		assertTrue(confluence.existPage(title));
		
		String newContent = "The content is changed";
		confluence.updatePage(title, newContent);
		assertEquals(newContent, confluence.pageContent(title));
		
		confluence.deletePage(title);
	}

	@Test public void returns_tag_text() {
		assertEquals("Hello World", confluence.tagText("<body><h1>Hello World</h1></body>", "h1"));
	}

	@After public void teardown() {
		confluence.logout();
	}
	
}
