/*
 * Created on Mar 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package business.productsubsystem;

import java.util.ArrayList;
import java.util.List;

import business.DbClassQuantity;
import business.Quantity;
import business.util.*;
import business.externalinterfaces.IProductFromDb;
import business.externalinterfaces.IProductFromGui;
import business.externalinterfaces.IProductSubsystem;
import middleware.DatabaseException;

public class ProductSubsystemFacade implements IProductSubsystem {
	final String DEFAULT_PROD_DESCRIPTION="New Product";
	CatalogTypes types;
	
    public TwoKeyHashMap<Integer,String,IProductFromDb> getProductTable() throws DatabaseException {
        DbClassProduct dbClass = new DbClassProduct();
        return dbClass.readProductTable();
        
    }
	public TwoKeyHashMap<Integer,String,IProductFromDb> refreshProductTable() throws DatabaseException {
		DbClassProduct dbClass = new DbClassProduct();
        return dbClass.refreshProductTable();		
	}
	
	//This is needed by ComboListener in ManageProductsController. When you have implemented this,
	//you can remove comments from body of ComboListener.
	public List<IProductFromDb> getProductList(String catType) {
		List<IProductFromDb> list = new ArrayList<IProductFromDb>();
		DbClassProduct dbClassProduct = new DbClassProduct();
		try {
			list = dbClassProduct.readProductList(getCatalogIdFromType(catType));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public Integer getCatalogIdFromType(String catType) throws DatabaseException {
		if(catType.equals("Books")) return 1;
		else if(catType.equals("Clothing")) return 2;
		else return 3;
		
	}
	public void saveNewProduct(IProductFromGui product, String catalogType) throws DatabaseException {
		//get catalogid
		Integer catalogid = getCatalogIdFromType(catalogType); 
				//invent description
		String description = DEFAULT_PROD_DESCRIPTION;
		DbClassProduct dbclass = new DbClassProduct();
		dbclass.saveNewProduct(product, catalogid,description);
		
	}
	/* reads quantity avail and stores in the Quantity argument */
	public void readQuantityAvailable(String prodName, Quantity quantity) throws DatabaseException {
		DbClassQuantity dbclass = new DbClassQuantity();
		dbclass.setQuantity(quantity);
		dbclass.readQuantityAvail(prodName);
				
	}
	@Override
	public List<String[]> getCatalogNames() throws DatabaseException {
		DbClassCatalog dbClassCatalog = new DbClassCatalog();
		List<Catalog> catalogTypes = dbClassCatalog.getCatalogNames();
		List<String[]> list = new ArrayList<String[]>();
		catalogTypes.forEach(t->{
			list.add(new String[]{t.getCatalogname()});
		});
//		list.add(new String[]{"Books"});
//		list.add(new String[]{"Clothing"});
		return list;
		
	}
	@Override
	public List<String[]> refreshCatalogNames() throws DatabaseException {
		return new ArrayList<String[]>();
	}
	@Override
	public List<IProductFromDb> refreshProductList(String catType)
			throws DatabaseException {
		IProductFromDb prod =  new Product(1, "coat", "hi", "joe", "tom", 2, "other");
		List<IProductFromDb> list = new ArrayList<IProductFromDb>();
		list.add(prod);
		return list;
	}
	@Override
	public Integer getProductIdFromName(String prodName)
			throws DatabaseException {
		if(prodName.startsWith("Mess")) return 1;
		else if(prodName.startsWith("Gone")) return 2;
		if(prodName.startsWith("Garden")) return 3;
		if(prodName.startsWith("Pants")) return 4;
		if(prodName.startsWith("T-")) return 5;
		if(prodName.startsWith("Skirts")) return 6;
		return 7;
		
	}
	@Override
	public IProductFromDb getProduct(String prodName) throws DatabaseException {
		return new Product(1, "coat", "hi", "joe", "tom", 2, "other");
	}
	@Override
	public IProductFromDb getProductFromId(Integer prodId)
			throws DatabaseException {
		return new Product(1, "coat", "hi", "joe", "tom", 2, "other");
	}
	@Override
	public void saveNewCatalogName(String name) throws DatabaseException {
		//do nothing yet
		
	}
	@Override
	public IProductFromGui createProduct(String name, String date,
			String numAvail, String unitPrice) {
		return new Product(1, "coat", "hi", "joe", "tom", 2, "other");
	}
	

	

}
