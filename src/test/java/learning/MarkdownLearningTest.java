package learning;

import static org.junit.Assert.*;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Test;

public class MarkdownLearningTest {

	@Test public void test() {
		Parser parser = Parser.builder().build();
		Node document = parser.parse("## title");
		HtmlRenderer renderer = HtmlRenderer.builder().build();

		assertEquals("<h2>title</h2>\n", renderer.render(document));
	}

}
