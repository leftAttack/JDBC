import entities.Employee;
import org.junit.Test;

import java.sql.Connection;

/**
 * @author Yue_
 * @create 2021-01-08-10:13
 */
public class JDBCTest<T>{

    @Test
    public void testConnection() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testupdata(){
        String sql = "insert into e_user value(?,?,?,?)";
        JDBCUtils.updata(sql,3,"haha",19,0);
    }

    @Test
    public void testQuery()  {
        String sql = "select * from e_user where id = ?";
        Employee employee = JDBCUtils.query(Employee.class, sql, 1);
        System.out.println(employee);
    }


}
