package enib.gala;

public class Consumption { //https://o7planning.org/en/10435/android-listview-tutorial#a1453256
    private String mName;
    private Double mPrice;
    private Integer mId;

    public Consumption(String name,Double price,Integer id)
    {
        mName=name;
        mPrice=price;
        mId=id;
    }

    public String getName()
    {
        return mName;
    }

    public Double getPrice()
    {
        return mPrice;
    }

    public Integer getId()
    {
        return mId;
    }


}
