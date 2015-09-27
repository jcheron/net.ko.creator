package net.ko.creator.controls;

import java.util.ArrayList;

import net.ko.bean.Range;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

public class SelectedRangesList {
	private KstyledText kstyledText;
	private ArrayList<SelectedRanges> rangesList;
	private SelectedRanges activeRanges;
	//private VerifyKeyListener KeyListener;

	public SelectedRangesList(KstyledText kstyledText) {
		rangesList=new ArrayList<>();
//		this.kstyledText=kstyledText;
//		KeyListener=new VerifyKeyListener() {
//			@Override
//			public void verifyKey(VerifyEvent e) {
//				detectActiveRanges();
//				if(activeRanges!=null){
//					if(e.keyCode==SWT.TAB){
//						Range range=null;
//						e.doit=false;
//						if ((e.stateMask & SWT.SHIFT) != 0)
//							range=activeRanges.selectPrevious();
//						else
//							range=activeRanges.selectNext();
//						showToolTip(range);
//					}
//				}
//			}
//		};
//		kstyledText.addVerifyKeyListener(KeyListener);

	}
//	protected void showToolTip(Range range) {
//		if(range!=null)
//			kstyledText.setToolTipText(range.getToolTip());
//		else
//			kstyledText.setToolTipText("");
//	}
	public void insertTextUpdate(int start,int length){
		for(SelectedRanges range:rangesList)
			range.insertTextUpdate(start,length);
	}
	public void replaceTextUpdate(int start,int length,String replacedText){
		int i=rangesList.size()-1;
		while(i>=0){
			if (rangesList.get(i).replaceTextUpdate(start,length,replacedText))
				rangesList.remove(i--);
			i--;
		}
	}
	public void detectActiveRanges(){
		boolean find=false;
		int i=0;
		while(i<rangesList.size() && !find){
			find=(rangesList.get(i).getActiveRangeIndex()!=-1);
			i++;
		}
		if(find){
			activeRanges=rangesList.get(i-1);
		}
	}
	public void add(String word){
		add(0,new String[]{word});
	}
	public void add(String[] words){
		add(0, words);
	}
	public void add(int start,String[] words){
		add(start, words, new String[]{});
	}
	public void add(int start,String[] words,String[] toolTips){
		SelectedRanges range=new SelectedRanges(kstyledText, start, words,toolTips);
		rangesList.add(range);
	}
}
