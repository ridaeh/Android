package enib.gala;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String phoneIndicative;
    private String city;
    private String postCode;
    private String address;
    private String password;
    private Double balance; //solde
    private String token;
    private String admin;
    private Integer permissions;
    private String qrCode;

    public User(Integer id, String firstName, String lastName, String email, String phone, String phoneIndicative, String city, String postCode, String address, String password, Double balance, String token, String admin, Integer permissions, String qrCode) {
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
        this.qrCode = qrCode;
    }

    public User(Integer id, String email, String password, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

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
                ", postCode='" + postCode + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", token='" + token + '\'' +
                ", admin='" + admin + '\'' +
                ", permissions=" + permissions +
                ", qrCode='" + qrCode + '\'' +
                '}';
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public boolean connect()
    {
        return false;
    }

    public void update(String firstName, String lastName, String email, String phone, String phoneIndicative, String city, String postCode) {
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

    public String getPostCode() {
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

    private String getAdmin() {
        return admin;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public boolean isAdmin()
    {
        if (this.getAdmin()==null){return false;}
        String sAdmin =this.getAdmin();
        Integer admin = Integer.parseInt(sAdmin);
        return (admin==1);
    }
}
