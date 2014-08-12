package learning;

import java.net.MalformedURLException;

import org.codehaus.swizzle.confluence.*;
import org.junit.*;

public class SwizzleConfluenceLearningTest {

	@Ignore("used only to learn Swizzle Confluence API")
	@Test public void create_new_page() throws MalformedURLException, ConfluenceException, SwizzleException {
		Confluence confluence = new Confluence("http://confluence:9090/rpc/xmlrpc");
		confluence.login("maven", "Gennaio2010.");
		
		Page parentPage = confluence.getPage("VONTGC", "Projects");
		
		Page page = new Page();
		page.setSpace("VONTGC");
		page.setParentId(parentPage.getId());
		page.setTitle("my page");
		page.setContent("This is an example");
		page.setCreator("maven");
		confluence.storePage(page);
		
		confluence.logout();
	}

}
