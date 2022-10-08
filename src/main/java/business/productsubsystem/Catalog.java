package business.productsubsystem;

import business.externalinterfaces.ICatalog;

public class Catalog implements ICatalog {
	private String catalogid;
	private String catalogname;
	public Catalog(String id, String name) {
		this.catalogid = id;
		this.catalogname = name;
	}

	public Catalog() {
	}

	public String getId() {
		return catalogid;
	}

	public String getCatalogname() {
		return catalogname;
	}

	public void setId(String id) {
		catalogid = id;
		
	}

	public void setCatalogname(String catalogname) {
		this.catalogname = catalogname;
		
	}

	public String getCatalogid() {
		return catalogid;
	}

	public void setCatalogid(String catalogid) {
		this.catalogid = catalogid;
	}
}
