package miniORM;
import miniORM.entities.Connector;
import miniORM.entities.EntityManager;
import miniORM.entities.User;

import java.sql.SQLException;
import java.util.Date;

public class DemoORM {
    public static void main(String[] args) {


        try {
            Connector.initConnection("mysql","localhost","3306", "school", "root", "root" );

            EntityManager em = new EntityManager(Connector.getConnection());

            User user = new User("Pesho", "12345", 28, new Date());

            //TODO if there is no table - create, if obj exists - update else create it
            em.persist(user);

        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
