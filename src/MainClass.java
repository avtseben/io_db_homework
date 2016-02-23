import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.ArrayList;



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
	ArrayList<Product> a = fromTextToList(); 
	Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:../assets/Product.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Price (ID INTEGER PRIMARY KEY AUTOINCREMENT, ProdGroup TEXT , Brand TEXT, Price INT);";
            stmt.executeUpdate(sql);
	    fromListToDB(stmt,a);

	    String sqlsel = "SELECT * FROM Price;";
	    queryAll(stmt,sqlsel);

            stmt.close();
            c.commit();
            c.close();


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
    private static Product fromLineToObject(String s) {
	String [] as = s.split(",");
	Integer price = new Integer(as[2].replace(" ",""));//TODO нужно убирать пробелы перед и после.
	Product p = new Product(as[0],as[1],price.intValue());
	return p;
    }
    //Read from file and packing product to Arralist as Object Product
    private static ArrayList<Product> fromTextToList() {
	try {
	    BufferedReader in = new BufferedReader(new FileReader("../assets/technik.txt"));
	    ArrayList<Product> a = new ArrayList<>();
	    String line;
	    while((line = in.readLine()) != null) {
		try {
		    a.add(fromLineToObject(line));
		} catch (Exception e) {
		    System.out.println(e);
		    System.out.println("В исходном файле есть плохие строчки!!!");
	    }
	    }
	    in.close();
	    return a;
	} catch (IOException e) {
	    System.out.println(e);
	    System.out.println("Какая то прблема с исходным файлом, не могу его открыть!!");
	}
	return null;
    }
    public static void fromListToDB(Statement stmt, ArrayList<Product> a) {
	for(Product pr : a) {
	    String _c = pr.getCateg();
	    String b = pr.getBrand();
	    int p = pr.getPrice();
	    int id = queryByProduct(stmt,_c,b);
	    String sql = "";
	    System.out.println("id = " + id);
	    if(id == 0) {
		sql = "INSERT INTO Price (ProdGroup,Brand,Price) VALUES ('" + _c + "','"+ b + "','" + p + "');";
	    } else {
		sql = "UPDATE Price SET Price ='" + p + "' WHERE ID = '" + id + "';";
		System.out.println(sql);
	    }
	    //System.out.println(sqlins);
	    try {
		stmt.executeUpdate(sql);
	    } catch (Exception e) {
	         System.out.println(e);
	         System.out.println("SQL запись  не удалась!!");
	    }

	}
    }
    public static int queryByProduct(Statement stmt, String _categ, String _brand) {
	try {
	    String sql= "SELECT ID FROM Price where ProdGroup ='" + _categ + "' AND Brand ='" + _brand + "';";//TODO в поле Brand перед названием попадается пробле
	    ResultSet rs = stmt.executeQuery(sql);
	    while ( rs.next() ) {
		int id  = rs.getInt("ID");
		return id; 
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println(e);
	    System.out.println("SQL запрос по продукту не прошел!!");
	}
	return 0;
    }
    public static void queryAll(Statement stmt, String sql) {
	try {
	    ResultSet rs = stmt.executeQuery(sql);
	    while ( rs.next() ) {
		String id  = rs.getString("ID");
		String categ = rs.getString("ProdGroup");
		String brand = rs.getString("Brand");
		String price  = rs.getString("Price");
		System.out.println( "ID = " + id );
		System.out.println( "CATEG = " + categ );
		System.out.println( "BRAND = " + brand );
		System.out.println( "PRICE = " + price );
		System.out.println();
	    }
	    rs.close();
	} catch (Exception e) {
	    System.out.println(e);
	    System.out.println("SQL запрос не прошел!!");
	}
    }
//    public static void fromListToDB(ArrayList<Product> ap) {
//	String sql = "INSERT INTO Price (ProdGroup,Brand,Price) VALUES ('test','test','400');"
//	stmt.executeUpdate(sql);
//	
  //  }
}
