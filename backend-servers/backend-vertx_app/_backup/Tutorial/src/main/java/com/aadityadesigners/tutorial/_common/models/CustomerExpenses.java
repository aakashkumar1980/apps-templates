package com.aadityadesigners.tutorial._common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerExpenses {
    private Customer customer;
    private Integer customerExpenses;
}
