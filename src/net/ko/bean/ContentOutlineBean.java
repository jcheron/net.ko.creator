package net.ko.bean;

import java.util.ArrayList;

import net.ko.creator.editors.images.Images;

public abstract class ContentOutlineBean {
	protected String name;
	protected String img;
	protected ArrayList<ContentOutlineBean> childs;
	protected ContentOutlineBean parent;
	public ContentOutlineBean(String name, String img) {
		super();
		this.name = name;
		this.img = img;
		parent=null;
		childs=new ArrayList<ContentOutlineBean>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		String result=Images.BULLET_GRAY;
		if(img!=null&&!"".equals(img))
			result=img;
		return result;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public ContentOutlineBean getParent() {
		return parent;
	}
	public void setParent(ContentOutlineBean parent) {
		this.parent = parent;
	}
	public ArrayList<ContentOutlineBean> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<ContentOutlineBean> childs) {
		this.childs = childs;
		for(ContentOutlineBean cob:childs)
			cob.setParent(this);
	}
	public String getId(){
		return name;
	}
	public abstract void select();
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ContentOutlineBean)
			return ((ContentOutlineBean) obj).getName().equals(this.name);
		return super.equals(obj);
	}
}
