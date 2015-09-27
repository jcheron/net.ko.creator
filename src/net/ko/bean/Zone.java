package net.ko.bean;

public class Zone {
	private String id;
	private String shortDesc;
	private String desc;
	private String defaultValue="";
	private ZoneType type=ZoneType.ztZone;
	public Zone(String id, String desc) {
		this(id,desc,ZoneType.ztZone);
	}
	public Zone(String id, String desc,ZoneType type) {
		super();
		this.id = id;
		this.type=type;
		String[] values=desc.split("\\|");
		if(values.length>0)
			this.shortDesc = values[0];
		if(values.length>1)
			this.desc = values[1];
		if(values.length>2)
			this.defaultValue = values[2];
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return ((Zone)obj).getId().equals(this.id);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public ZoneType getType() {
		return type;
	}
	public void setType(ZoneType type) {
		this.type = type;
	}
	public String getValue(){
		String result="{"+id+"}";
		if(type.equals(ZoneType.ztFunc)){
			result="{#"+id+"#}";
			if(!"".equals(defaultValue))
				result="{#"+id+":"+defaultValue+"#}";
		}else if(type.equals(ZoneType.ztHTML))
			result=shortDesc;
		return result;
	}
}
