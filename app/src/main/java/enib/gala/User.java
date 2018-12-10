package enib.gala;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String phoneIndicative;
    private String city;
    private Integer postCode;
    private String password;
    private Double balance; //solde
    private String tokensConnexion;
    private String admin;
    private Integer permissions;

    public User(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String firstName, String lastName, String email, String phone, String phoneIndicative, String city, Integer postCode, String password, Double balance, String tokensConnexion, String admin, Integer permissions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.phoneIndicative = phoneIndicative;
        this.city = city;
        this.postCode = postCode;
        this.password = password;
        this.balance = balance;
        this.tokensConnexion = tokensConnexion;
        this.admin = admin;
        this.permissions = permissions;
    }

    public boolean connect()
    {
        return false;
    }

    public void update(String firstName, String lastName, String email, String phone, String phoneIndicative, String city, Integer postCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.phoneIndicative = phoneIndicative;
        this.city = city;
        this.postCode = postCode;
    }

    public User get()
    {
        return User.this;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneIndicative() {
        return phoneIndicative;
    }

    public String getCity() {
        return city;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public String getPassword() {
        return password;
    }

    public Double getBalance() {
        return balance;
    }

    public String getTokensConnexion() {
        return tokensConnexion;
    }

    public String getAdmin() {
        return admin;
    }

    public Integer getPermissions() {
        return permissions;
    }
}
