package com.dub.frontendsvr.clients;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dub.frontendsvr.domain.Customer;
import com.dub.frontendsvr.domain.CustomerList;


@FeignClient("customers-service")
public interface CustomerFeignClient {
	
	// List all customers
    @RequestMapping(
            method= RequestMethod.GET,
            value = "${customerListUriPath}",
            consumes="application/json")
    CustomerList getCustomerList();
    
    
    // get customer 
    @RequestMapping(
            method= RequestMethod.GET,
            value="${customerUriPath}",
            consumes="application/json")
    ResponseEntity<Customer> getCustomer(@PathVariable("customerId") long id);
    
    
    // create a new customer
    @RequestMapping(
            method= RequestMethod.POST,
            value="${customerCreateUriPath}",
            consumes="application/json")
    ResponseEntity<Customer> createCustomer(@RequestBody Customer customer);
    
    // update customer 
    @RequestMapping(
            method= RequestMethod.PUT,
            value="${customerUpdateUriPath}",
            consumes="application/json")
    ResponseEntity<?> updateCustomer(
    							@PathVariable("customerId") long id, 
    							@RequestBody Customer customer);
    
    // delete customer 
    @RequestMapping(
            method= RequestMethod.DELETE,
            value="${customerDeleteUriPath}",
            consumes="application/json")
    ResponseEntity<?> deleteCustomer(@PathVariable("customerId") long id);
    
    
}