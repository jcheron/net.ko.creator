package net.ko.creator.controls;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class KstyledText extends StyledText {
	private SelectedRangesList selectedRangesList;
	private ColorizedItemsList colorizedItems;
	public KstyledText(Composite parent, int style) {
		super(parent, style);
		selectedRangesList=new SelectedRangesList(this);
		colorizedItems=new ColorizedItemsList(this);
		addExtendedModifyListener(new ExtendedModifyListener() {
			
			@Override
			public void modifyText(ExtendedModifyEvent e) {
				KstyledText text=(KstyledText) e.widget;
				if(e.replacedText.length()==0)
					insertTextUpdate(e.start, e.length);
				else
					replaceTextUpdate(e.start, e.length, e.replacedText);
				StyleRange sr=new StyleRange();
				sr.foreground=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
				sr.start=0;
				sr.length=text.getText().length();
				text.setStyleRange(sr);
				colorizedItems.colorize(0, text.getText());
			}
		});
	}
	protected void replaceTextUpdate(int start, int length, String replacedText) {
		selectedRangesList.replaceTextUpdate(start, length, replacedText);
	}
	protected void insertTextUpdate(int start, int length) {
		selectedRangesList.insertTextUpdate(start, length);
	}
	public void selectWord(){
		int posInitiale=this.getCaretOffset();
		int lengthInitiale=this.getSelectionCount();
		String text=this.getText();
		int start=text.substring(0, posInitiale).lastIndexOf("{");
		if(text.substring(0, posInitiale).lastIndexOf("}")>start)
			start=posInitiale;
		int end=posInitiale+text.substring(posInitiale).indexOf("}")+1;
		if(text.substring(posInitiale).indexOf("{")!=-1&&posInitiale+text.substring(posInitiale).indexOf("{")+1<end)
			end=posInitiale;
		if(start!=-1&&end!=0){
			this.setSelectionRange(start, end-start);
			this.setSelection(start, end);
		}else{
			this.setSelection(posInitiale,posInitiale+lengthInitiale);
			this.setSelectionRange(posInitiale,lengthInitiale);
		}
	}
	public Point replaceWord(String newWord){
		if(getSelectionCount()==0)
			selectWord();
		int start=getSelectionRange().x;
		replaceTextRange(start, getSelectionRange().y, newWord);
		setSelectionRange(start, newWord.length());
		return getSelectionRange();
	}
	public Point replaceWord(String newWord,String[] selection){
		return replaceWord(newWord, selection, new String[]{});
	}
	public Point replaceWord(String newWord,String[] selection,String[] toolTips){
		if(selection==null)
			replaceWord(newWord);
		int pos=-1;
		int size=newWord.length();
		if(getSelectionCount()==0)
			selectWord();
		int start=getSelectionRange().x;
		Point result=new Point(start, newWord.length());
		replaceTextRange(start, getSelectionRange().y, newWord);
		if(selection.length>0){
			selectedRangesList.add(start,selection,toolTips);
			String firstWord=selection[0];
			if(!"".equals(firstWord)){
				switch (firstWord) {
				case "_after":
					start+=size;
					size=0;
					break;
				case "_before":
					size=0;
					break;
				default:
					pos=newWord.indexOf(firstWord);
					if(pos!=-1){
						start+=pos;
						size=firstWord.length();
					}
					break;
				}
			}
		}
		setSelectionRange(start, size);
		return result;
	}
	public Point replaceWord(String newWord,String selection){
		return replaceWord(newWord, new String[] {selection});
	}
	public void replace(String regex,String replacement){
		String text=this.getText();
		replacement=Matcher.quoteReplacement(replacement);
		text=text.replaceAll(regex, replacement);
		setText(text);
	}
	public void insertIn(String before,String after,String value){
		String text=this.getText();
		String beforeR=Pattern.quote(before);
		String afterR=Pattern.quote(after);
		if(!text.matches("((\\s|.)*?)"+beforeR+"((\\s|.)*?)"+afterR+"((\\s|.)*?)"))
			text=text+before+after;
		value=Matcher.quoteReplacement(value);
		Pattern p = Pattern.compile(beforeR+"((\\s|.)*?)"+afterR,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		text=m.replaceAll(before+"$1"+value+after);
		setText(text);
	}
	public void setRangeColor(Color systemColor) {
		StyleRange styleRange = new StyleRange();
		styleRange.start = this.getSelectionRange().x;
		styleRange.length = this.getSelectionCount();
		styleRange.foreground = systemColor;
		this.setStyleRange(styleRange);
	}
	public void setRangeColor(Color systemColor,Point selectionRange) {
		StyleRange styleRange = new StyleRange();
		styleRange.start = selectionRange.x;
		styleRange.length = selectionRange.y;
		styleRange.foreground = systemColor;
		this.setStyleRange(styleRange);
	}
	public ColorizedItemsList getColorizedItems() {
		return colorizedItems;
	}
	public void setColorizedItems(ColorizedItemsList colorizedItems) {
		this.colorizedItems = colorizedItems;
	}

}
