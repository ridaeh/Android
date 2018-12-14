package enib.gala;

public class Product {
    private Integer id;
    private Double price;
    private String name;
    private Integer size;
    private String sizeUnit;
    private boolean available;
    private Integer count;

    public Product(Integer id, Double price, String name, Integer size, String sizeUnit, boolean available) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.size = size;
        this.sizeUnit = sizeUnit;
        this.available = available;
        this.count = 1;
    }

    public Integer getId() {
        return id;
    }

    public Double getPrice() {
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void removeOneCount()
    {
        this.count --;
    }

    public void addOneCount()
    {
        this.count ++;
    }

}
