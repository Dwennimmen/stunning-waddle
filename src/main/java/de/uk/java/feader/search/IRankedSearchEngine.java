package de.uk.java.feader.search;

import java.util.List;

import de.uk.java.feader.data.Entry;

public interface IRankedSearchEngine extends ISearchEngine {

	/**
	 * Searches the search index for <code>searchTerm</code> and returns a
	 * <code>List</code> of <code>Entry</code> objects whose title or content text
	 * contains <code>searchTerm</code>. If no results are found in the search
	 * process, this method should return an empty list. If, on the other hand, the
	 * search index is empty (e.g. at first run of application), this method should
	 * return <code>null</code>.
	 * 
	 * The results in the list are sorted according to the frequency of the search
	 * term in the document. The entry with the most frequent uses of the search
	 * term are placed at the top (i.e., in descending order).
	 * 
	 * Note that the signature of the method is the same as in {@link ISearchEngine}.
	 * 
	 * @param searchTerm
	 * @return A list of search results (Entry instances), an empty list (if no
	 *         results), or <code>null</code> (if no index).
	 */
	public List<Entry> search(String searchTerm);

	/**
	 * The next search that is conducted will be done in case sensitive or case insensitive way. 
	 * If <code>false</code> is specified, the next search will be case insensitive. 
	 * @param cs
	 */
	void setCaseSensitive(boolean cs);

	/**
	 * Returns the current setting of the case sensitivity toggle.
	 * @return
	 */
	boolean isCaseSensitive();


}
