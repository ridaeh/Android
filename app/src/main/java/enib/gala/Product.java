package enib.gala;

public class Product {
    //{"Id":"1","Price":"250","Name":"Coca Cola","Size":"33","SizeUnit":"cl","Available":"1"}
    private Integer id;
    private Integer price;
    private String name;
    private Integer size;
    private String sizeUnit;
    private boolean available;

    public Product(Integer id, Integer price, String name, Integer size, String sizeUnit, boolean available) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.size = size;
        this.sizeUnit = sizeUnit;
        this.available = available;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", sizeUnit='" + sizeUnit + '\'' +
                ", available=" + available +
                '}';
    }
}
