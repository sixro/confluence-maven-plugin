package learning;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.rjeschke.txtmark.Processor;

public class MarkdownLearningTest {

	@Test public void test() {
		assertEquals("<h2>title</h2>\n", Processor.process("## title"));
	}

}
