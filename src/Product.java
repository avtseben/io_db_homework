public class Product {
    private String categ;
    private String brand;
    private int price;

    public Product (String _categ, String _brand, int _price) {
	    categ = _categ;
	    brand = _brand;
	    price = _price;
    }
    public String getCateg() {
	return categ;
    }
    public String getBrand() {
	return brand;
    }
    public int getPrice() {
	return price;
    }
    public void showInfo() {
	System.out.println("Category: " + categ);
	System.out.println("Brand: " + brand);
	System.out.println("Price: " + price);
    }
}
