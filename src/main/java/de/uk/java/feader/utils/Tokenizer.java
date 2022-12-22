package de.uk.java.feader.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer implements ITokenizer {

	@Override
	public List<String> tokenize(String text) {
		text = text.replaceAll("<[^>]*?>", " ");
		
		List<String> tokens = new LinkedList<String>();
		StringTokenizer tokenizer = new StringTokenizer(text, " \t\n\r\f!?.()[]{}<>,");
		while (tokenizer.hasMoreTokens())
			tokens.add(tokenizer.nextToken());
		return tokens;
	}

}
