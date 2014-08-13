package confluencemavenplugin;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.*;
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
		String title = titleOf(content);
		addPage(parentTitle, title, content);
	}

	/**
	 * Add or updated specific page returning the title of current page in order to create trees.
	 * 
	 * @param parentTitle title of parent page
	 * @param file an HTML file suitable for confluence
	 * @return title of added (or updated) page
	 * @throws IOException if an error occurs
	 */
	public String addOrUpdatePage(String parentTitle, File file) throws IOException {
		String content = FileUtils.readFileToString(file);
		String title = titleOf(content);
		if (! existPage(title))
			addPage(parentTitle, title, content);
		else
			updatePage(title, content);
		return title;
	}

	public void deletePage(String title) {
		String pageId = pageId(title);
		try {
			server.removePage(pageId);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to delete page with title '" + title + "' (ID: " + pageId + ")", e);
		}
	}

	public void deletePages(Iterable<String> titles) {
		for (String title : titles) deletePage(title);
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

	@SuppressWarnings("unchecked")
	public void sync(File[] files, String parentTitle) {
		Map<String, File> titlesToFiles = mapTitlesToFiles(files);
		
		try {
			List<PageSummary> children = server.getChildren(pageId(parentTitle));
			Map<String, PageSummary> titlesToRemotePages = mapTitlesToPages(children);
			
			Collection<String> titlesOfPagesToRemove = CollectionUtils.removeAll(
					titlesToRemotePages.keySet(), 
					titlesToFiles.keySet()
			);
			deletePages(titlesOfPagesToRemove);
			for (File file : files)
				addOrUpdatePage(parentTitle, file);
		} catch (SwizzleException e) {
			throw new ConfluenceException("Unable to sync files " + Arrays.toString(files) + " as children of page with title '" + parentTitle + "'", e);
		} catch (IOException e) {
			throw new ConfluenceException("Unable to add or update a page found in files " + Arrays.toString(files) + " as children of page with title '" + parentTitle + "'", e);
		}
	}

	private Map<String, File> mapTitlesToFiles(File[] files) {
		Map<String, File> map = new LinkedHashMap<>();
		for (File file : files) {
			String title = titleOf(file);
			map.put(title, file);
		}
		return map;
	}

	private Map<String, PageSummary> mapTitlesToPages(List<PageSummary> pages) {
		Map<String, PageSummary> map = new LinkedHashMap<>();
		for (PageSummary page : pages)
			map.put(page.getTitle(), page);
		return map;
	}
	
	private String titleOf(File file) {
		try {
			return titleOf(FileUtils.readFileToString(file));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load title of file '" + file + "'", e);
		}
	}

	private String titleOf(String content) {
		return tagText(content, "h1");
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
