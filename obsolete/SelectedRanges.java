package net.ko.creator.controls;

import java.util.ArrayList;

import net.ko.bean.Range;

import org.eclipse.swt.graphics.Point;

public class SelectedRanges {
	private KstyledText kstyledText;
	private ArrayList<Range> ranges;
	private int activeRangeIndex=-1;
	public SelectedRanges(KstyledText kstyledText,String word){
		this(kstyledText,0,new String[]{word});
	}
	public SelectedRanges(KstyledText kstyledText,String[] words){
		this(kstyledText, 0,words);
	}
	public SelectedRanges(KstyledText kstyledText,int start,String[] words){
		this(kstyledText, start, words, new String[]{});
	}
	public SelectedRanges(KstyledText kstyledText,int start,String[] words,String[] toolTips){
		ranges=new ArrayList<>();
		this.kstyledText=kstyledText;
		String text=kstyledText.getText().substring(start);
		for(int i=0;i<words.length;i++){
			int pos=text.indexOf(words[i]);
			if(pos!=-1){
				String toolTip="";
				if(toolTips.length>i)
					toolTip=toolTips[i];
				ranges.add(new Range(start+pos, words[i].length(),toolTip));
			}
		}
	}
	public int getActiveRangeIndex(){
		int result=-1;
		boolean find=false;
		int i=0;
		while(i<ranges.size() && !find){
			find=(ranges.get(i).equals(kstyledText.getSelectionRange()));
			i++;
		}
		if(find){
			result=i-1;
			activeRangeIndex=result;
		}
		return result;
	}
	public void setRange(int index,Range point){
		if(index>-1 && index<ranges.size())
			ranges.set(index, point);
	}
	public void setRange(Range point){
		int index=-1;
		if(activeRangeIndex!=-1)
			index=activeRangeIndex;
		else
			index=getActiveRangeIndex();
		setRange(index, point);
	}
	public int getNextIndex(){
		int result=-1;
		if(activeRangeIndex!=-1){
			result=activeRangeIndex+1;
			if(result>=ranges.size())
				result=0;
		}
		return result;
	}
	public int getPreviousIndex(){
		int result=-1;
		if(activeRangeIndex!=-1){
			result=activeRangeIndex-1;
			if(result<0)
				result=ranges.size()-1;
		}
		return result;
	}
	public Range getNext(){
		Range result=new Range(kstyledText.getSelectionRange());
		int index=getNextIndex();
		if(index!=-1)
			result=ranges.get(index);
		return result;
	}
	public Range getPrevious(){
		Range result=new Range(kstyledText.getSelectionRange());
		int index=getPreviousIndex();
		if(index!=-1)
			result=ranges.get(index);
		return result;
	}
	public Range selectNext(){
		Range result=getNext();
		selectRange(result);
		return result;
	}
	public Range selectPrevious(){
		Range result=getPrevious();
		selectRange(result);
		return result;
	}
	public void selectRange(int index){
		if(index>-1 && index<ranges.size()){
			Range range=ranges.get(index);
			kstyledText.setSelectionRange(range.x, range.y);
		}
	}
	public void selectRange(Range range){
		kstyledText.setSelectionRange(range.x, range.y);
	}
	public void clear(){
		ranges.clear();
	}
	public int size(){
		return ranges.size();
	}
	public void setActiveRangeIndex(int activeRangeIndex) {
		this.activeRangeIndex = activeRangeIndex;
	}
	public int getRange(Point point){
		int result=-1;
		boolean find=false;
		int i=0;
		while(i<ranges.size() && !find){
			Range range=ranges.get(i);
			find=(range.x<point.x && range.x+range.y>point.x+point.y);
			i++;
		}
		if(find){
			result=i-1;
		}
		return result;
	}
	public void setKstyledText(KstyledText kstyledText) {
		this.kstyledText = kstyledText;
	}
	public void insertTextUpdate(int start, int length) {
		for(Range range:ranges){
			if(start>=range.x && start<=range.x+range.y)
				range.y+=length;
			else if(start<range.x)
				range.x+=length;
		}
		
	}
	public boolean replaceTextUpdate(int start, int length, String replacedText) {
		boolean toDelete=false;
		for(Range range:ranges){
			if((start<range.x&&start+replacedText.length()>range.x)||
			(start<range.x+range.y&&start+replacedText.length()>range.x+range.y)){
				toDelete=true;
				break;
			}else{
				if(start>=range.x && start+replacedText.length()<=range.x+range.y)
					range.y+=length-replacedText.length();
				else if(start<range.x)
					range.x+=length-replacedText.length();
			}
		}
		return toDelete;
	}
}
