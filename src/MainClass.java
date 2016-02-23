import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

// https://bitbucket.org/xerial/sqlite-jdbc/downloads
/*2) Программно создаем БД SQLite(через "CREATE TABLE IF NOT EXISTS Price (ID INT PRIMARY KEY, ProdGroup TEXT ,...);
        3) Данные из текстового прайса забиваем в БД, если забиваемый товар там уже есть - переписываем новую цену(через UPDATE);
        4) В консоли дать возможность пользователю вывести или отдельную группу товара, или конкретный товар.

        Какие могут быть еще варианты:
        а) БД создать через SQLite Studio(или похожую программу)
        б) Чтобы не делать Update цен, можете перед перекидыванием товаров в прайс делать TRUNCATE TABLE
        Обсуждение 10
*/
public class MainClass {
    public static void main(String[] args) {
        Connection c = null;//объект соединения к бд
        Statement stmt = null;//обьект запрос в бд
        try {
            Class.forName("org.sqlite.JDBC");//Инициализирует все статические поля класса
            c = DriverManager.getConnection("jdbc:sqlite:Product.db");//поключение к бд, указан драйвер бд, тип бд и название бд Product.db(это просто файл лежащий в папке с проектом, он созжается при ображении к базе)
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Price (ID INT PRIMARY KEY, ProdGroup TEXT , Brand TEXT, Price INT);";
            stmt.executeUpdate(sql);

            /*
            ResultSet rs = stmt.executeQuery("SELECT Name, Phone FROM MainT;");
            while ( rs.next() ) {
                //int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String phone  = rs.getString("Phone");
                //System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "PHONE = " + phone );
                System.out.println();
            }
            rs.close();*/

            stmt.close();
            c.commit();
            c.close();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
