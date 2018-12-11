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
    private String address;
    private String password;
    private Double balance; //solde
    private String token;
    private String admin;
    private Integer permissions;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneIndicative='" + phoneIndicative + '\'' +
                ", city='" + city + '\'' +
                ", postCode=" + postCode +
                ", balance=" + balance +
                ", token='" + token + '\'' +
                ", admin='" + admin + '\'' +
                ", permissions=" + permissions +
                '}';
    }

    public User(Integer id, String email, String password, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public User(Integer id, String firstName, String lastName, String email, String phone, String phoneIndicative, String city, Integer postCode, String address, String password, Double balance, String token, String admin, Integer permissions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.phoneIndicative = phoneIndicative;
        this.city = city;
        this.postCode = postCode;
        this.address = address;
        this.password = password;
        this.balance = balance;
        this.token = token;
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

    public void setPassword(String password) {
        this.password = password;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdmin() {
        return admin;
    }

    public Integer getPermissions() {
        return permissions;
    }

}
