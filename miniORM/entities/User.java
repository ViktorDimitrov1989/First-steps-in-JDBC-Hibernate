package miniORM.entities;

import miniORM.persitance.Column;
import miniORM.persitance.Entity;
import miniORM.persitance.Id;

import java.util.Date;

@Entity(name = "users")
public class User {
    @Id
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "age")
    private int age;
    @Column(name = "registration_date")
    private Date registrationDate;

    public User(String name, String password, int age, Date registrationDate) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public String getPassword() {
        return this.password;
    }

    public int getAge() {
        return this.age;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }
}
