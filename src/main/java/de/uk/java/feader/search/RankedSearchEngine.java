package de.uk.java.feader.search;

import de.uk.java.feader.data.Entry;
import de.uk.java.feader.data.Feed;
import de.uk.java.feader.utils.ITokenizer;

import java.io.*;
import java.util.*;

public class RankedSearchEngine implements IRankedSearchEngine {
    private boolean caseSensitiveCheck;
    ITokenizer tokenizer;
    HashMap<String, Map<Entry, Integer>> index = new HashMap<>();

    @Override
    public List<Entry> search(String searchTerm) {
        if (index.isEmpty())
            return null;

        boolean isChecked = false;
        String key = "";
        HashMap<Entry, Integer> result = new HashMap<>();
        if(this.isCaseSensitive()) {
            if(index.containsKey(searchTerm)) {
                isChecked = true;
                key = searchTerm;
                result.putAll(index.get(key));
            }
        }
        else {
            for(String k : index.keySet()) {
                if(searchTerm.equalsIgnoreCase(k)) {
                    key = k;
                    isChecked = true;
                    result.putAll(index.get(key));
                }
            }
        }
        if(isChecked) {
            if (index.get(key).isEmpty())
                return new ArrayList<Entry>();
//			sorting entries in descending order
            List<Map.Entry<Entry, Integer> > list = new LinkedList<Map.Entry<Entry, Integer>>(result.entrySet());
            Collections.sort(list, (item1, item2) -> item2.getValue().compareTo(item1.getValue()));
            ArrayList<Entry> entryList = new ArrayList<Entry>();
            for(Map.Entry<Entry, Integer> e : list) {
                entryList.add(e.getKey());
            }

            return entryList;
        }
        else {
            return new ArrayList<Entry>();
        }
    }

    @Override
    public void createSearchIndex(List<Feed> feeds) {
        this.index = new HashMap<String, Map<Entry, Integer>>();
        for (Feed feed : feeds) {
            this.addToSearchIndex(feed);
        }
    }

    @Override
    public void addToSearchIndex(Feed feed) {
        for (Entry entry : feed.getEntries()) {

            List<String> tokens = this.tokenizer.tokenize(entry.getTitle());
            tokens.addAll(this.tokenizer.tokenize(entry.getContent()));

            String textPlain = (entry.getTitle() + " " + entry.getContent()).replace(".", "").replace("\n", "");
            String[] text = textPlain.split(" ");

            for (String token : tokens) {
                int count = 0;
                for (String s : text) {
                    if (token.equals(s)) {
                        count++;
                    }
                }
                if (!index.containsKey(token))
                    index.put(token, new HashMap<Entry, Integer>());
                index.get(token).put(entry, count);
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

    @Override
    public void loadSearchIndex(File indexFile) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(indexFile));
            Object o = ois.readObject();
            if (HashMap.class.isAssignableFrom(o.getClass())) {
                this.index = (HashMap<String, Map<Entry, Integer>>) o;
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

    @Override
    public void setCaseSensitive(boolean cs) {
        this.caseSensitiveCheck = cs;
    }
    @Override
    public boolean isCaseSensitive() {
        return this.caseSensitiveCheck;
    }
}
