package helper;

import core.AppServiceObj;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moiz.p on 24/09/16.
 */
public class DBOperations
{
    public static void closeConnection()
    {
        DB.closeConnection();
    }

    public ArrayList<ArrayList<String>> getArrayListFromDB(String sql) throws SQLException
    {
        try (Statement st = DB.getConnection().createStatement())
        {
            try (ResultSet rs = st.executeQuery(sql))
            {
                ResultSetMetaData rsmd = rs.getMetaData();
                int cols = rsmd.getColumnCount();
                ArrayList<ArrayList<String>> list = new ArrayList<>();
                while (rs.next())
                {
                    ArrayList<String> row = new ArrayList<>();

                    for (int i = 1; i <= cols; i++)
                    {
                        row.add(rs.getString(i));
                    }
                    list.add(row);
                }

                return list;
            }
        }
    }

    public static ArrayList<String> getListFromDB(String sql) throws SQLException
    {
        try (Statement st = DB.getConnection().createStatement())
        {
            try (ResultSet rs = st.executeQuery(sql))
            {
                ArrayList<String> list = new ArrayList<>();
                while (rs.next())
                {
                    list.add(rs.getString(1));
                }
                return list;
            }
        }
    }

    public void saveAppService(AppServiceObj appServiceObj) throws SQLException
    {
        try (Connection connection = DBOperations.DB.getConnection())
        {
            if (!agentExists(appServiceObj.userId))
            {
                String sql = "insert into agent (flockUserid, flockUsertoken, flockName, companyId) values(?,?,?,?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
                {
                    preparedStatement.setString(1, appServiceObj.userId);
                    preparedStatement.setString(2, appServiceObj.userToken);
                    preparedStatement.setString(3, appServiceObj.name);
                    preparedStatement.setInt(4, 1);
                    int i = preparedStatement.executeUpdate();
                    System.out.println(i + " records inserted");
                }
            }
        }
    }

    private boolean agentExists(String userId) throws SQLException
    {
        try (Connection connection = DBOperations.DB.getConnection())
        {
            String sql = "SELECT count(*) FROM agent WHERE flockUserid=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    return "1".equals(resultSet.getString(1));
                }
                System.out.println(userId + "exists");
            }
        }
        return false;
    }

    public void getCompanyServiceParams(String companyId, String serviceId)
    {
        //TODO if required
    }

    public void uninstallAppService(String userId) throws SQLException
    {
        try (Connection connection = DBOperations.DB.getConnection())
        {
            String sql = "UPDATE agent SET status='InActive' WHERE flockUserid= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setString(1, userId);
                int i = preparedStatement.executeUpdate();
                System.out.println(i + " record updated");
            }
        }
    }

    public void getSaveFacebookMessage(String companyId, String serviceId, String sender_id, String message)
    {

    }

    public static List<String> getQueuedCustomerList() throws SQLException {
        return getListFromDB("SELECT * FROM customer");
    }

    public static class DB
    {
        private static final Logger LOGGER = Logger.getLogger(DB.class.getName());
        private static Connection c = null;
        public static final String jdbcDriver = "com.mysql.jdbc.Driver";
        public static final String dbUrl = "jdbc:mysql://localhost:3306/Flockathon";
        public static String DBUSER = "root", DBPASS = "bruteforce";

        public static Connection getConnection()
        {
            if (c == null)
            {
                synchronized (DB.class)
                {
                    if (c == null)
                    {
                        try
                        {
                            Class.forName(jdbcDriver);
                            c = DriverManager.getConnection(dbUrl, DBUSER, DBPASS);
                        } catch (Exception e)
                        {
                            LOGGER.error("Unable to connect to Database!!", e);
                        }
                    }
                }
            }
            return c;
        }

        public static void executeNonQuery(String sql) throws SQLException
        {
            try (Statement stmt = c.createStatement())
            {
                stmt.executeUpdate(sql);
            }
        }

        private static void closeConnection()
        {
            try
            {
                c.close();
            } catch (SQLException e)
            {
                LOGGER.error("From closeConnection", e);
            }
        }
    }

}