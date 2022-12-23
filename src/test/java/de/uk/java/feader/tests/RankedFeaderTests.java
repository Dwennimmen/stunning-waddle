package de.uk.java.feader.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.tidy.Tidy;

import de.uk.java.feader.Feader;
import de.uk.java.feader.data.Feed;
import de.uk.java.feader.io.FeedDownloader;
import de.uk.java.feader.io.IAppIO;
import de.uk.java.feader.search.IRankedSearchEngine;
import de.uk.java.feader.search.ISearchEngine;
import de.uk.java.feader.utils.ITokenizer;


public class RankedFeaderTests {
	
	private static final String TEST_FEED_URL = "https://feader.test.de/feed.rss";
	private static final String TEST_FEED_TITLE = "Feader Test Feed";
	private static final String TEST_FEED_DESC = "This is a test feed for the Feader project";
	
	private static final File CONFIG_FILE = new File("feader.test.config");
	private static final File HTML_FILE = new File("feader.test.html");
	private static final File FEEDS_FILE = new File("feader.test.feeds");
	private static final File INDEX_FILE = new File("feader.test.index");
	
	private static final IAppIO IO = Feader.getAppIO();
	private static final IRankedSearchEngine SEARCH = (IRankedSearchEngine) Feader.getSearchEngine();
	private static final ITokenizer TOKENIZER = Feader.getTokenizer();
	private static final FeedDownloader DOWNLOADER = new FeedDownloader();
	private static final List<Feed> FEEDS = new ArrayList<Feed>();
	private static final List<Feed> RANKEDFEEDS = new ArrayList<Feed>();


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("[TEST PREP] Deleting test files ...");
		deleteTestFiles();
		System.out.println("[TEST PREP] Preparing test data...");
		FEEDS.add(DOWNLOADER.readFeed(TEST_FEED_URL, ClassLoader.getSystemResource("test.rss").openStream()));
		System.out.println(FEEDS);
		System.out.println("[TEST PREP] Prepared test feeds: " + FEEDS);
		SEARCH.setTokenizer(TOKENIZER);
		
		System.out.println("[TEST PREP] Preparing ranked test data...");
		RANKEDFEEDS.add(DOWNLOADER.readFeed(TEST_FEED_URL, ClassLoader.getSystemResource("ranked-test.rss").openStream()));
		System.out.println(FEEDS);
		System.out.println("[TEST PREP] Prepared ranked test feeds: " + FEEDS);
		
		SEARCH.createSearchIndex(RANKEDFEEDS);

		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("[TEST CLEANUP] Deleting test files ...");
		deleteTestFiles();
	}
	
	private static void deleteTestFiles() {
		if (FEEDS_FILE.exists()) FEEDS_FILE.delete();
		if (CONFIG_FILE.exists()) CONFIG_FILE.delete();
		if (HTML_FILE.exists()) HTML_FILE.delete();
		if (INDEX_FILE.exists()) INDEX_FILE.delete();
	}

	
	@Test
	public void testCaseSensitivityToggle() {
		System.out.println("[TEST] IRankedSearchEngine ci toggle implementation");		

		SEARCH.setCaseSensitive(false);
		assertEquals("search not case insensitive", 4, SEARCH.search("feader").size());		
		assertEquals("search not case insensitive", 4, SEARCH.search("Tests").size());		
		SEARCH.setCaseSensitive(true);
		assertEquals("search not case sensitive", 0, SEARCH.search("feader").size());		
		assertEquals("search not case insensitive", 1, SEARCH.search("Tests").size());		

	}

	@Test
	public void testRankedSearch() {
		System.out.println("[TEST] IRankedSearchEngine search implementation");		

		SEARCH.setCaseSensitive(false);

		assertTrue("Search engine does not implement IRankedSearchEngine", SEARCH instanceof IRankedSearchEngine);
		
		assertEquals("wrong number of search results!", 4, SEARCH.search("Feader").size());
		assertEquals("wrong number of search results!", 2, SEARCH.search("word").size());
		assertEquals("search results not in correct order", "Test Title 3", SEARCH.search("word").get(0).getTitle());
		assertEquals("search results not in correct order", "Test Title 2", SEARCH.search("word").get(1).getTitle());
	}
	@Test
	public void testWildCard(){
		String word = "ga umlage".toLowerCase();
		String searchTerm = "gas*";
		boolean result = word.matches(searchTerm.replaceAll("\\*", ".*"));
		Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(word);
		boolean matchFound = matcher.find();
		assertTrue(matchFound);
	}
	

}
