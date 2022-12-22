package de.uk.java.feader.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.java.feader.data.Entry;
import de.uk.java.feader.data.Feed;
import de.uk.java.feader.utils.ITokenizer;

public class SearchEngine implements ISearchEngine {

	ITokenizer tokenizer;
	Map<String, Set<Entry>> index = new HashMap<String, Set<Entry>>();

	@Override
	public List<Entry> search(String searchTerm) {
		if (index.isEmpty())
			return null;
		
		searchTerm = searchTerm.toLowerCase();
		if (!index.containsKey(searchTerm))
			return new ArrayList<Entry>();
		if (index.get(searchTerm).isEmpty())
			return new ArrayList<Entry>();
		return new ArrayList<Entry>(index.get(searchTerm));
	}

	@Override
	public void createSearchIndex(List<Feed> feeds) {
		this.index = new HashMap<String, Set<Entry>>();
		for (Feed feed : feeds) {
			this.addToSearchIndex(feed);
		}
	}

	@Override
	public void addToSearchIndex(Feed feed) {
		for (Entry entry : feed.getEntries()) {

			List<String> tokens = this.tokenizer.tokenize(entry.getTitle());
			tokens.addAll(this.tokenizer.tokenize(entry.getContent()));

			for (String token : tokens) {
				token = token.toLowerCase();
				if (!index.containsKey(token))
					index.put(token, new HashSet<Entry>());
				index.get(token).add(entry);
			}
		}
	}

	@Override
	public void setTokenizer(ITokenizer tokenizer) {
		this.tokenizer = tokenizer;

	}

	@Override
	public void saveSearchIndex(File indexFile) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(indexFile));
			oos.writeObject(index);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			System.err.println("Could not save search index: " + e.getLocalizedMessage());
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadSearchIndex(File indexFile) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFile));
			Object o = ois.readObject();
			if (HashMap.class.isAssignableFrom(o.getClass())) {
				this.index = (Map<String, Set<Entry>>) o;
			}
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Could not load search index: " + e.getLocalizedMessage());
		}
	}

	@Override
	public boolean indexExists() {
		return index != null;
	}

}
