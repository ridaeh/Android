package enib.gala;

import android.support.annotation.NonNull;

public class Consumption { //https://o7planning.org/en/10435/android-listview-tutorial#a1453256
    //class consommation
    private String name;
    private Double price;
    private Integer id;
    private Integer amount;
    private String allergen=null;
    private String category=null;
    private Double degreesOfAlcohol=null;

    public Consumption(String name, Double price, Integer id, Integer amount, String allergen, String category, Double degreesOfAlcohol) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.amount = amount;
        this.allergen = allergen;
        this.category = category;
        this.degreesOfAlcohol = degreesOfAlcohol;
    }

    public Double getDegreesOfAlcohol() {
        return degreesOfAlcohol;
    }

    public void setDegreesOfAlcohol(Double degreesOfAlcohol) {
        this.degreesOfAlcohol = degreesOfAlcohol;
    }

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
                ", degreesOfAlcohol=" + degreesOfAlcohol +
                '}';
    }

    public String toStringProper()
    {
        String txt=name;
        txt+="\n";
        txt+=price.toString();
        txt+=" €\n";
        if (degreesOfAlcohol!=null)
        {
            txt+=degreesOfAlcohol.toString();
            txt+="°\n";
        }
        if (allergen!=null)
        {
            txt+=allergen.toString();
            txt+="°\n";
        }
        if(amount>1)
        {
            txt+="amount : "+amount.toString();
            txt+="\n";
        }

        return txt;
    }
}
