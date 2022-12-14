package application;

import application.gui.*;
import business.*;
import business.externalinterfaces.*;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.RulesShoppingCart;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import middleware.DatabaseException;
import middleware.EBazaarException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class BrowseAndSelectController implements CleanupControl {
	private static final Logger LOG = Logger
			.getLogger("BrowseAndSelectController.class.getName()");

	// ///////// EVENT HANDLERS -- new code goes here ////////////

	//Online Purchase Menue
	// control of mainFrame
	class PurchaseOnlineActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			catalogListWindow = new CatalogListWindow();
			IProductSubsystem pss = new ProductSubsystemFacade();
			try {
				List<String[]> catalogNames = pss.getCatalogNames();
				catalogListWindow.updateModel(catalogNames);
				mainFrame.getDesktop().add(catalogListWindow);
				catalogListWindow.setVisible(true);
			} catch(DatabaseException ex) {
				String errMsg = "Database unavailable: " + ex.getMessage();
				JOptionPane.showMessageDialog(catalogListWindow, errMsg,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}

	class LoginListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				LoginControl loginControl = new LoginControl(mainFrame,
						mainFrame);
				loginControl.startLogin();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class RetrieveCartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(cartItemsWindow == null){
                cartItemsWindow = new  CartItemsWindow();
            }
			EbazaarMainFrame.getInstance().getDesktop().add(cartItemsWindow);
			cartItemsWindow.setVisible(true);
		}
	}

	// control of CatalogListWindow
	class SelectCatalogListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			JTable table = catalogListWindow.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {				
				String type = (String) table.getValueAt(selectedRow, 0);
				LOG.info("Selected type: " + type);
				catalogListWindow.setVisible(false);
				productListWindow  = new ProductListWindow(type);
				mainFrame.getDesktop().add(productListWindow);
				productListWindow.setVisible(true);
			}
			// If arriving here, the value of selectedRow is -1,
			// which means no row was selected
			else {
				String errMsg = "Please select a row.";
				JOptionPane.showMessageDialog(catalogListWindow, errMsg,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	class BackToMainFrameListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			mainFrame.setVisible(true);
			catalogListWindow.setVisible(false);
		}
	}

	// Control of ProductListWindow
	class SelectProductListener implements ActionListener {
		HashMap<String, String[]> readProductDetailsData() {
			DefaultData productData = DefaultData.getInstance();
			return productData.getProductDetailsData();
		}

		boolean useDefaultData;

		public SelectProductListener(boolean useDefData) {
			useDefaultData = useDefData;
		}

		/* Returns, as a String array, the product details based on the type */
		String[] readProductDetailsData(String type) {
			if (useDefaultData) {
				DefaultData productData = DefaultData.getInstance();
				return productData.getProductDetailsData(type);
			} 
			return null;
		}

		public void actionPerformed(ActionEvent evt) {
			JTable table = productListWindow.getTable();
			int selectedRow = table.getSelectedRow();

			if (selectedRow >= 0) {
				String type = (String) table.getValueAt(selectedRow, 0);
				String[] productParams = readProductDetailsData(type);
				productDetailsWindow = new ProductDetailsWindow(productParams);
				mainFrame.getDesktop().add(productDetailsWindow);
				productListWindow.setVisible(false);
				productDetailsWindow.setVisible(true);
			}
			// Value of selectedRow is -1, which means no row was selected
			else {
				String errMsg = "Please select a row.";
				JOptionPane.showMessageDialog(productListWindow, errMsg,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class BackToCatalogListListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			catalogListWindow.setVisible(true);
			productListWindow.setVisible(false);
		}
	}

	// ///// control ProductDetails
	class AddCartItemListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			productDetailsWindow.setVisible(false);
			quantityWindow = new QuantityWindow(false, null);
			EbazaarMainFrame.getInstance().getDesktop().add(quantityWindow);
			quantityWindow.setVisible(true);
			quantityWindow.setParentWindow(productDetailsWindow);
		}
	}

	class QuantityOkListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			String productSelection = productDetailsWindow.getItem();
			String quantityDesired = quantityWindow.getQuantityDesired();

			DbClassQuantity dbq = new DbClassQuantity();
			Quantity qty = new Quantity(quantityDesired);
			dbq.setQuantity(qty);
			try {
				dbq.readQuantityAvail(productSelection);
				System.out.println(qty.getQuantityAvailable());
			}catch(DatabaseException e){
				e.printStackTrace();
			}

			boolean rulesOk = true;
			IRules rules = new RulesQuantity(qty);
			try {
				rules.runRules();
			} catch (EBazaarException e) {
				rulesOk = false;
				String message = e.getMessage();
				System.out.println(message);
				displayError(quantityWindow, message);
			}

			if (rulesOk) {
				quantityWindow.dispose();
				cartItemsWindow = new CartItemsWindow();
				EbazaarMainFrame.getInstance().getDesktop().add(cartItemsWindow);
				cartItemsWindow.setVisible(true);
			}
		}
	}

	void displayError(Component w, String msg) {
		JOptionPane.showMessageDialog(w, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	class BackToProductListListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			productDetailsWindow.setVisible(false);
			productListWindow.setVisible(true);
		}
	}

	// /// control CartItemsWindow

	class ContinueShoppingListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			cartItemsWindow.setVisible(false);

			// user has been looking at this product list
			if (productListWindow != null) {
				productListWindow.setVisible(true);
			}
			// user has just retrieved saved cart
			else {
				if (catalogListWindow != null) {
					catalogListWindow.dispose();
				}
				catalogListWindow = new CatalogListWindow();				
				EbazaarMainFrame.getInstance().getDesktop()
						.add(catalogListWindow);
				catalogListWindow.setVisible(true);
			}
		}
	}

	class SaveCartListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			// implement
			// here's the logic:
			// require login if not logged in
			// if current live cart does not have a cartid
			// then it's new;
			// save cart level data, read the auto-generated
			// cart id, then loop
			// and get lineitemid's for each line, inserting
			// the relevant cartid, and save each
			// line
			// If current cart does have an id, we just save
			// cart items that are not flagged as "hasBeenSaved"
			// (a boolean in that class)
			// no need to save at the cart level, just save the
			// not-so-far-saved cart items
			// postcondition: the live cart has a cartid
			// and all cart items are flagged as "hasBeenSaved"
			SessionContext context = SessionContext.getInstance();
			Boolean loggedIn = (Boolean) context.get(CustomerConstants.LOGGED_IN);
			if (!loggedIn.booleanValue()) {
				LoginControl loginControl = new LoginControl(cartItemsWindow, mainFrame);
				loginControl.startLogin();
			} else {
				updateCart();
			}
		}

		public void updateCart(){
			SessionContext context = SessionContext.getInstance();
			ICustomerSubsystem customer = (ICustomerSubsystem) context.get(CustomerConstants.CUSTOMER);
			ICustomerProfile profile = customer.getCustomerProfile();

			IShoppingCartSubsystem shoppingCartSubsystem = ShoppingCartSubsystemFacade.getInstance();
			shoppingCartSubsystem.setCustomerProfile(profile);

			IShoppingCart currentCart = shoppingCartSubsystem.getLiveCart();

			boolean error = false;
			IRules rulesShoppingCart = new RulesShoppingCart(currentCart);
			try {
				rulesShoppingCart.runRules();
			} catch (RuleException ruleException) {
				JOptionPane.showMessageDialog(quantityWindow, ruleException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				error = true;
			} catch (EBazaarException eBazaarException) {
				JOptionPane.showMessageDialog(quantityWindow, eBazaarException.getMessage(), "Error",						JOptionPane.ERROR_MESSAGE);
				error = true;
			}
			if (error == false){
				shoppingCartSubsystem.saveLiveCart();
			}

			JOptionPane.showMessageDialog(cartItemsWindow, "Cart Save Success", "Message", JOptionPane.PLAIN_MESSAGE);
		}
	}

	// /////// PUBLIC INTERFACE -- for getting instances of listeners ///
	// EbazaarMainFrame
	public ActionListener getNewOnlinePurchaseListener(EbazaarMainFrame f) {
		return (new PurchaseOnlineActionListener());
	}

	public LoginListener getLoginListener(EbazaarMainFrame f) {
		return new LoginListener();
	}

	public ActionListener getRetrieveCartActionListener(EbazaarMainFrame f) {
		return (new RetrieveCartActionListener());
	}

	// CatalogListWindow
	public ActionListener getSelectCatalogListener(CatalogListWindow w) {
		return new SelectCatalogListener();
	}

	public ActionListener getBackToMainFrameListener(CatalogListWindow w) {
		return new BackToMainFrameListener();
	}

	// ProductListWindow
	public ActionListener getSelectProductListener(ProductListWindow w,
			boolean useDefData) {
		return new SelectProductListener(useDefData);
	}

	public ActionListener getBackToCatalogListListener(ProductListWindow w) {
		return new BackToCatalogListListener();
	}

	// ProductDetails Window

	public ActionListener getAddToCartListener(ProductDetailsWindow w) {
		return new AddCartItemListener();
	}


	public ActionListener getBackToProductListListener(ProductDetailsWindow w) {
		return new BackToProductListListener();
	}

	// CartItemsWindow

	public ActionListener getContinueShoppingListener(CartItemsWindow w) {
		return (new ContinueShoppingListener());
	}

	public ActionListener getSaveCartListener(CartItemsWindow w) {
		return (new SaveCartListener());
	}

	public ActionListener getQuantityOkListener(QuantityWindow w, boolean edit,
			Integer posOfEdit) {
		return new QuantityOkListener();
	}

	// ////// PUBLIC ACCESSORS to register screens controlled by this class////

	public void setCatalogList(CatalogListWindow w) {
		catalogListWindow = w;
	}

	public void setMainFrame(EbazaarMainFrame m) {
		mainFrame = m;
	}

	public void setProductListWindow(ProductListWindow p) {
		productListWindow = p;
	}

	public void setProductDetailsWindow(ProductDetailsWindow p) {
		productDetailsWindow = p;
	}

	public void setCartItemsWindow(CartItemsWindow w) {
		cartItemsWindow = w;
	}

	public void setSelectOrderWindow(SelectOrderWindow w) {
		selectOrderWindow = w;
	}

	public void setMaintainCatalogTypes(MaintainCatalogTypes w) {
		maintainCatalogTypes = w;
	}

	public void setMaintainProductCatalog(MaintainProductCatalog w) {
		maintainProductCatalog = w;
	}

	public void setQuantityWindow(QuantityWindow w) {
		quantityWindow = w;
	}

	// private boolean edit;

	// ///// screens -- private references
	private EbazaarMainFrame mainFrame;
	private ProductListWindow productListWindow;
	private CatalogListWindow catalogListWindow;
	private ProductDetailsWindow productDetailsWindow;
	private CartItemsWindow cartItemsWindow;
	private SelectOrderWindow selectOrderWindow;
	private MaintainCatalogTypes maintainCatalogTypes;
	private MaintainProductCatalog maintainProductCatalog;
	private QuantityWindow quantityWindow;
	private Window[] allWindows = {};
	private JInternalFrame[] internalFrames = { productListWindow,
			catalogListWindow, productDetailsWindow, quantityWindow,
			cartItemsWindow, selectOrderWindow, maintainCatalogTypes,
			maintainProductCatalog };

	public void cleanUp() {
		ApplicationUtil.cleanup(allWindows);
		ApplicationUtil.cleanup(internalFrames);
	}

	// ///// make this class a singleton
	private static final BrowseAndSelectController instance = new BrowseAndSelectController();

	public static BrowseAndSelectController getInstance() {
		return instance;
	}

	private BrowseAndSelectController() {
	}

	// /////// communication with other controllers
	public void makeMainFrameVisible() {
		if (mainFrame == null) {
			mainFrame = new EbazaarMainFrame();
		}
		mainFrame.setVisible(true);
	}
}
