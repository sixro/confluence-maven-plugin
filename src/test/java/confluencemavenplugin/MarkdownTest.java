package confluencemavenplugin;

import static org.junit.Assert.*;

import org.junit.Test;

public class MarkdownTest {

	@Test public void toHtml() {
		Markdown markdown = new Markdown("## header2");
		assertEquals("<h2>header2</h2>\n", markdown.toHtml());
	}

}
