package com.aadityadesigners.tutorial.promisefuture;

import java.util.HashMap;

import com.aadityadesigners.tutorial._common.models.Customer;
import com.aadityadesigners.tutorial._common.models.CustomerExpenses;
import com.aadityadesigners.tutorial._common.models.CustomerIncome;
import com.aadityadesigners.tutorial._common.utils.IncomeTaxUtility;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class TaxVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOGGER.debug(String.format("%s::verticle started...", TaxVerticle.class.getSimpleName()));
        startPromise.complete();

        var incomeTaxUtility = new IncomeTaxUtility();
        var customer = Customer.builder()
            .customerId(1)
            .customerName("Aaditya Kumar")
            .customerAge(62)
        .build();
        var customerIncome = CustomerIncome.builder()
            .customer(customer)
            .customerSalary(76000)
        .build();   
        var customerExpenses = CustomerExpenses.builder()
            .customer(customer)
            .customerExpenses(40000)
        .build();           
        
        // Get Tax Rate
        final Promise<Integer> taxRatePromise = Promise.promise();
        final Future<Integer> taxRateFuture = taxRatePromise.future();
        taxRateFuture.compose(handler -> {
            LOGGER.debug(String.format("Tax Rate fetched."));
            return Future.succeededFuture(handler);
        });
        vertx.executeBlocking(handler -> {
            try {
                Integer taxRate = incomeTaxUtility.getTaxRate(customer.getCustomerAge());
                LOGGER.debug(String.format("Tax Rate: %d", taxRate));
                taxRatePromise.complete(taxRate);
            } catch (Exception e) {
                taxRatePromise.fail(e);
            }
        }, false);

        // Get Taxable Income
        final Promise<Integer> taxableIncomePromise = Promise.promise();
        final Future<Integer> taxableIncomeFuture = taxableIncomePromise.future();
        taxableIncomeFuture.compose(handler -> {
            LOGGER.debug(String.format("Taxable Income fetched."));
            return Future.succeededFuture(handler);
        });        
        vertx.executeBlocking(handler -> {
            try {
                Integer taxableIncome = incomeTaxUtility.getTaxableIncome(
                    customerExpenses.getCustomerExpenses(), 
                    customerIncome.getCustomerSalary()
                );                
                LOGGER.debug(String.format("Taxable Income: %d", taxableIncome));
                taxableIncomePromise.complete(taxableIncome);
            } catch (Exception e) {
                taxableIncomePromise.fail(e);
            }
        }, false);        
                
        // Finally, compute Tax
        Future.all(taxRateFuture, taxableIncomeFuture)
           .onSuccess(handler -> {
                Integer taxAmount = incomeTaxUtility.computeTaxAmount(
                    (Integer) handler.list().get(0), 
                    (Integer) handler.list().get(1)    
                );
                LOGGER.info(String.format("Computed Tax Amount is: %d", taxAmount));
           })
           .onFailure(handler -> {
                LOGGER.error(String.format("Exception: %s", handler.getCause().getMessage()));
           });

        
    }

}
