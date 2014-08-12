package confluencemavenplugin;

import java.io.*;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.codehaus.swizzle.confluence.*;

public class Confluence {

	@SuppressWarnings("unused")
	private final String endpoint;
	private final Credentials credentials;
	private final String spaceKey;
	
	private org.codehaus.swizzle.confluence.Confluence server; 
	
	public Confluence(String endpoint, Credentials credentials, String spaceKey) {
		super();
		this.endpoint = endpoint;
		this.credentials = credentials;
		this.spaceKey = spaceKey;
		
		this.server = newConfluence(endpoint);
	}

	private org.codehaus.swizzle.confluence.Confluence newConfluence(String endpoint) {
		try {
			return new org.codehaus.swizzle.confluence.Confluence(endpoint);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL specified for the endpoint: " + endpoint, e);
		}
	}

	public void login() throws ConfluenceException {
		try {
			server.login(credentials.username, credentials.password);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to login using credentials " + credentials, e);
		}
	}
	
	public void logout() {
		try { server.logout(); } 
		catch (SwizzleException ignore) { }
	}

	public boolean existPage(String title) {
		try {
			server.getPage(spaceKey, title);
			return true;
		} catch (org.codehaus.swizzle.confluence.ConfluenceException e) {
			return ! e.getMessage().contains("You're not allowed to view that page, or it does not exist");
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to check if page '" + title + "' exists", e);
		}
	}

	public void addPage(String parentTitle, String title, String content) {
		String parentId = pageId(parentTitle);
		Page page = new Page();
		page.setSpace(spaceKey);
		page.setParentId(parentId);
		page.setTitle(title);
		page.setContent(content);

		try {
			server.storePage(page);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to add page '" + title + "' to parent page with title '" + parentTitle + "' (ID: " + parentId + ")", e);
		}
	}

	public void addPage(String parentTitle, File file) throws IOException {
		String content = FileUtils.readFileToString(file);
		String title = tagText(content, "h1");
		addPage(parentTitle, title, content);
	}

	public void addOrUpdatePage(String parentTitle, File file) throws IOException {
		String content = FileUtils.readFileToString(file);
		String title = tagText(content, "h1");
		if (! existPage(title))
			addPage(parentTitle, title, content);
		else
			updatePage(title, content);
	}

	protected String tagText(String content, String tag) {
		String tagBegin = "<" + tag + ">";
		String tagEnd = "</" + tag + ">";
		try {
			String substring = content.substring(0, content.indexOf(tagEnd));
			return substring.substring(content.indexOf(tagBegin) + tagBegin.length());
		} catch (StringIndexOutOfBoundsException e) {
			throw new IllegalStateException("Unable to retrieve text of tag '" + tag + "' in content '" + content + "'", e);
		}
	}

	public void deletePage(String title) {
		String pageId = pageId(title);
		try {
			server.removePage(pageId);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to delete page with title '" + title + "' (ID: " + pageId + ")", e);
		}
	}

	public void updatePage(String title, String content) {
		Page page = null;
		try {
			page = server.getPage(spaceKey, title);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to find any page with '" + title + "'", e);
		}
		page.setContent(content);

		try {
			server.storePage(page);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to update page '" + title + "'", e);
		}
	}

	public String pageId(String title) {
		try {
			return server.getPage(spaceKey, title).getId();
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to retrieve page ID of a page with title '" + title + "'", e);
		}
	}

	public String pageContent(String title) {
		try {
			return server.getPage(spaceKey, title).getContent();
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to retrieve page ID of a page with title '" + title + "'", e);
		}
	}

	public static class Credentials {
		
		public final String username;
		public final String password;
		
		public Credentials(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
		
	}

}
