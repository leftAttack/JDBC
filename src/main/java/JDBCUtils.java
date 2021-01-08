import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Properties;

/**
 * @author Yue_
 * @create 2021-01-08-10:02
 */
public class JDBCUtils {

    public static Connection getConnection() throws Exception {
        //1.加载配置文件
        InputStream resourceAsStream = JDBCUtils.class.getClassLoader().getResourceAsStream("JDBC.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        //2.读取配置信息
        String user = properties.getProperty("username");
        String password = properties.getProperty("password");
        String driver = properties.getProperty("driverName");
        String url = properties.getProperty("url");
        //3.加载驱动
        Class.forName(driver);
        //4.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static void closeResource(Connection connection, PreparedStatement preparedStatement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void updata(String sql, Object... args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(connection, preparedStatement);
        }
    }

    public static <T> T query(Class<T> clazz,String sql,Object... args){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
             connection = getConnection();
             preparedStatement = connection.prepareStatement(sql);
            for(int i = 0;i < args.length; i++){
                preparedStatement.setObject(i+1,args[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if(resultSet.next()){
                T t = clazz.newInstance();
                for(int i = 1;i <= columnCount;i ++){
                    Object columnValue = resultSet.getObject(i);
                    String columnLabel = metaData.getColumnLabel(i);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,preparedStatement);
        }
        return null;
    }
}
