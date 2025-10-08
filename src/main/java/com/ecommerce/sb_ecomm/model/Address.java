package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, max = 50, message = "address should be at least 5 characters ")
    private String addressName;
    @NotBlank
    @Size(min = 5, max = 50, message = "Street name should be at least 5 characters ")
    private String streetName;
    @NotBlank
    @Size(min = 5, max = 50, message = "City should be at least 5 characters ")
    private String city;
    @NotBlank
    @Size(min = 2, max = 50, message = "State should be at least 6 characters ")
    private String state;
    @NotBlank
    @Size(min = 6, max = 50, message = "Zip code should be at least 5 characters ")
    private String zipCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<User>();

    public Address(String addressName, String streetName, String city, String state, String zipCode) {
        this.addressName = addressName;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
}
