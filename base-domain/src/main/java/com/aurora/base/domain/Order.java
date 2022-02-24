package com.aurora.base.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {
    private Long id;
    private Long customerId;
    private Long productId;
    private int productCount;
    private int price;
    private String status;
    private String source;

}
