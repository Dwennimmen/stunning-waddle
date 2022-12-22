package de.uk.java.feader.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.uk.java.feader.data.Entry;
import de.uk.java.feader.data.Feed;

public class AppIO implements IAppIO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Feed> loadSubscribedFeeds(File feedsFile) {
		List<Feed> feeds = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(feedsFile));
			Object o = ois.readObject();
			if (List.class.isAssignableFrom(o.getClass())) {
				feeds = (List<Feed>) o;
			}
			ois.close();
			return feeds;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Could not load search index: " + e.getLocalizedMessage());
		}
		return new LinkedList<Feed>();
	}

	@Override
	public void saveSubscribedFeeds(List<Feed> feeds, File feedsFile) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(feedsFile));
			oos.writeObject(feeds);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			System.err.println("Could not save subscribed feeds: " + e.getLocalizedMessage());
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Properties loadConfig(File configFile) {
		Properties config = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
			config.load(fis);
			return config;
		} catch (IOException e) {
			System.err.println("Could not load properties: " + e.getLocalizedMessage());
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}

	@Override
	public void saveConfig(Properties config, File configFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(configFile);
			config.store(fos, "");
			fos.flush();
			fos.close();
		} catch (IOException e) {
			System.err.println("Could not store properties: " + e.getLocalizedMessage());
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void exportAsHtml(List<Entry> entries, File htmlFile) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(htmlFile);
			fw.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><title></title></head><body><ul>");
			for (Entry e : entries) {
				fw.write("<li>");
				fw.write("<h1>" + e.getTitle() + "</h1>");
				fw.write("<p>" + e.getContent() + "</p>");
				fw.write("</li>");
			}
			fw.write("</ul></body></html>");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			
		} finally {
			if (fw!=null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		};
	}

}
