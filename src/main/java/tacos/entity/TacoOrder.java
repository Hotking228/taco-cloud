package tacos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "taco_order")
@Entity
public class TacoOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date placedAt;

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    @Column(name = "delivery_street")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    private String deliveryState;

    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
             message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    @Column(name = "cc_cvv")
    private String ccCVV;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Taco> tacos = new ArrayList<>();

    @ManyToOne
    private User user;

    public void addTaco(Taco taco){
        this.tacos.add(taco);
    }

    public void setUser(User user){
        if(user == null){
            user = new User();
        }

        this.user = user;
        deliveryName = deliveryName == null ? (
                user.getFullname() == null ?
                    "" :
                    user.getFullname()
                ) : deliveryName;
        deliveryStreet = deliveryStreet == null ? (
                            user.getStreet() == null ?
                                    "" :
                                    user.getStreet()) :
                            deliveryStreet;
        deliveryCity = deliveryCity == null ? (
                user.getCity() == null ?
                "" :
                        user.getCity()) :
                deliveryCity;
        deliveryState = deliveryState == null ? (
                user.getState() == null ?
                "" :
                        user.getState()) :
                deliveryState;
        deliveryZip = deliveryZip == null ? (
                user.getZip() == null ?
                "" :
                        user.getZip()) :
                deliveryZip;
        ccNumber = ccNumber == null ?
                "" :
                ccNumber;
        ccExpiration = ccExpiration == null ?
                "" :
                ccExpiration;;
        ccCVV = "";
    }
}
