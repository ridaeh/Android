package enib.gala;

public class Consumption { //https://o7planning.org/en/10435/android-listview-tutorial#a1453256
    private String name;
    private Double price;
    private Integer id;
    private Integer amount;
    private String allergen=null;
    private String category=null;

    public Consumption(String name, Double price, Integer id) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.amount = 1;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAllergen() {
        return allergen;
    }

    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Consumption{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", id=" + id +
                ", amount=" + amount +
                ", allergen='" + allergen + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
