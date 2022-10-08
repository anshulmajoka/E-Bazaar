package business.productsubsystem;

import middleware.DatabaseException;
import middleware.dataaccess.SimpleConnectionPool;
import middleware.externalinterfaces.IDbClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbClassCatalog implements IDbClass {

    BeanListHandler<Catalog> beanListHandler = new BeanListHandler<>(Catalog.class);
    QueryRunner runner = new QueryRunner();
    private String query;
    private String queryType;
    private final String SAVE = "Save";

    public void saveNewCatalog(String name) throws DatabaseException {
        //IMPLEMENT
    }


    public void buildQuery() throws DatabaseException {
        if (queryType.equals(SAVE)) {
            buildSaveQuery();
        }

    }

    void buildSaveQuery() throws DatabaseException {
        //IMPLEMENT
        query = "";
    }

    public String getDbUrl() {
        //IMPLEMENT
        return "";
    }

    public String getQuery() {
        //IMPLEMENT
        return "";
    }

    public void populateEntity(ResultSet resultSet) throws DatabaseException {
        // do nothing

    }


    public List<Catalog> getCatalogNames() throws DatabaseException {
        try {
            return runner.query(SimpleConnectionPool.productConnection(), "select * from catalogtype", beanListHandler);
        } catch (SQLException throwables) {
            throw new DatabaseException("Database exception");
        }
    }

}
