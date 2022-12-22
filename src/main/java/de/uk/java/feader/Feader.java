package de.uk.java.feader;

import de.uk.java.feader.gui.FeaderGUI;
import de.uk.java.feader.io.AppIO;
import de.uk.java.feader.io.IAppIO;
import de.uk.java.feader.search.IRankedSearchEngine;
import de.uk.java.feader.search.RankedSearchEngine;
import de.uk.java.feader.utils.ITokenizer;
import de.uk.java.feader.utils.Tokenizer;


public class Feader {
	
	
	/*
	 * main method starts the application
	 */
	public static void main(String[] args) {
		IAppIO io = getAppIO();
		IRankedSearchEngine search = getSearchEngine();
		ITokenizer tokenizer = getTokenizer();
		new FeaderGUI(io, search, tokenizer);
	}
	
	/**
	 * Returns a new IFeaderIO instance
	 * @return the new IFeaderIO instance
	 */
	public static IAppIO getAppIO() {
		return new AppIO();
	}
	
	/**
	 * Returns a new IFeaderIO instance
	 * @return the new IFeaderIO instance
	 */
	public static IRankedSearchEngine getSearchEngine() {
		return new RankedSearchEngine();
	}
	
	/**
	 * Returns a new ITokenizer instance
	 * @return the new IFeaderIO instance
	 */
	public static ITokenizer getTokenizer() {
		return new Tokenizer();
	}

}
