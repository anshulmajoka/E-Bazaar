
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;

import middleware.DatabaseException;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.DbConfigKey;

/**
 * @author pcorazza
 * @since Nov 10, 2004
 * Class Description:
 * 
 * 
 */
public class DataAccessUtil {
	
	public static ResultSet runQuery(SimpleConnectionPool pool, String dbUrl, String query) throws DatabaseException {
        System.out.println("query = "+query);
        Connection con = pool.getConnection(dbUrl);
        ResultSet rs = SimpleConnectionPool.doQuery(con,query);
        pool.returnToPool(con,dbUrl);
        return rs;
    }

    protected static SimpleConnectionPool getPool() throws DatabaseException {
		DbConfigProperties props = new DbConfigProperties();
		return SimpleConnectionPool.getInstance(
	            props.getProperty(DbConfigKey.DB_USER.getVal()), 
	            props.getProperty(DbConfigKey.DB_PASSWORD.getVal()), 
	            props.getProperty(DbConfigKey.DRIVER.getVal()),
	            Integer.parseInt(props.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal())));
	    
	}
    public static Integer runUpdate(SimpleConnectionPool pool, String dbUrl, String query) throws DatabaseException {        	
        Connection con = pool.getConnection(dbUrl);
        Integer generatedKey = SimpleConnectionPool.doUpdate(con,query);  
        pool.returnToPool(con,dbUrl);
        return generatedKey; 
    }
    
}
