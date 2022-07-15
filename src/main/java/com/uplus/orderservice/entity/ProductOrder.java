package com.uplus.orderservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long customerId;

    @Column
    private Long phoneId;

    @Column
    private Long planId;

    @Column
    private String orderNumber;

    @Column
    private int monthPrice;

    // @Column(insertable=false, updatable = false)
	// private String name;

    // @Column(insertable=false, updatable = false)
	// private String phoneNumber;

    

    // @ManyToOne(optional=false)
    // @JoinColumn(name="name", name="", insertable=false, updatable = false)
    // private Customer customer;

    // @ManyToOne
    // @JoinTable(name = "customer",
    //         joinColumns = @JoinColumn(name = "customer_id"),
    //         inverseJoinColumns = @JoinColumn(name = "id")
    // )
    // private Customer customer; //custoemr one : order: many 가 여러개

    // @ManyToOne
    // private Customer customer;

    @Builder
    public ProductOrder(Long customerId, Long phoneId, Long planId, String orderNumber, int monthPrice) {
        this.customerId = customerId;
        this.phoneId = phoneId;
        this.planId = planId;
        this.orderNumber = orderNumber;
        this.monthPrice = monthPrice;
    }


}
