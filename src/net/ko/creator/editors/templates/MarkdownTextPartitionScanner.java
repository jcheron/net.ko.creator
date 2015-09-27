package net.ko.creator.editors.templates;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

public class MarkdownTextPartitionScanner extends RuleBasedPartitionScanner implements IWordDetector{
	public final static String MARKDOWN_H1 = "__markdown_h1";
	public final static String MARKDOWN_H2 = "__markdown_h2";
	public final static String MARKDOWN_FIELD = "__markdown_field";
	public final static String MARKDOWN_HTML = "__markdown_html";
	
	public final static String[] LEGAL_CONTENT_TYPES = {MARKDOWN_H1, MARKDOWN_H2, MARKDOWN_FIELD, MARKDOWN_HTML};
	
	public MarkdownTextPartitionScanner() {
			IToken heading1 = new Token(MARKDOWN_H1);
			IToken heading2 = new Token(MARKDOWN_H2);
			IToken field = new Token(MARKDOWN_FIELD);
			IToken html = new Token(MARKDOWN_HTML);
			
			// ORDER is important - try to switch order of '=' and '==' !
			List<IPredicateRule> rules = new ArrayList<>();
			//IPredicateRule[] rules = new IPredicateRule[NUMBER_OF_RULES];
			rules.add(new SingleLineRule("\"", "\"", Token.UNDEFINED, '\0', true));
			//rules[1] =new SingleLineRule("'", "'", Token.UNDEFINED, '\0', true);
			rules.add(new MultiLineRule("{#", "#}", heading2, (char) 0, false));
			rules.add(new SingleLineRule("{_", "}", heading1, (char) 0, false));
			rules.add(new SingleLineRule("{/_", "}", heading1, (char) 0, false));
			rules.add(new WordPatternRule(this, "{", "}", field));
			rules.add(new MultiLineRule("<", ">", html));
			setPredicateRules((IPredicateRule[])rules.toArray(new IPredicateRule[rules.size()]));
	}

	@Override
	public boolean isWordPart(char c) {
		return !Character.isWhitespace(c)&&c!='#'&&c!='_';
	}

	@Override
	public boolean isWordStart(char c) {
		return !Character.isWhitespace(c);
	}
}