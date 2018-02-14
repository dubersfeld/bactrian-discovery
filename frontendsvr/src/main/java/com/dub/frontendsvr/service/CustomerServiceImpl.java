package com.dub.frontendsvr.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dub.frontendsvr.clients.CustomerFeignClient;
import com.dub.frontendsvr.domain.Customer;
import com.dub.frontendsvr.domain.CustomerList;
import com.dub.frontendsvr.exceptions.CustomerAccessDeniedException;
import com.dub.frontendsvr.exceptions.CustomerNotFoundException;
import com.dub.frontendsvr.exceptions.DuplicateCustomerException;
import com.dub.frontendsvr.exceptions.UnauthorizedException;

import feign.FeignException;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerFeignClient customerFeignClient;
	
	
	@Override
	public List<Customer> allCustomers() {
			
		CustomerList customerResponse = customerFeignClient.getCustomerList();
		
		List<Customer> list = customerResponse.getCustomers();
			
		for (Customer customer : list) {
			System.out.println(customer);
		}
	
		return list;
	}


	@Override
	public URI createCustomer(Customer customer) {
			
		try {
			ResponseEntity<Customer> response 
						= customerFeignClient.createCustomer(customer);
				
			HttpHeaders headers = response.getHeaders();	
			URI uri = headers.getLocation();
			return uri;

		} catch (FeignException e) {
			
			if (e.status() == HttpStatus.NOT_ACCEPTABLE.value()) {
				throw new DuplicateCustomerException();
			} else {
				throw new RuntimeException();
			}	
		}
	}


	@Override
	public Customer getCustomer(long id) {
		
		try {
			ResponseEntity<Customer> response = 
					customerFeignClient.getCustomer(id);
									
			return response.getBody();
				
		} catch (FeignException e) {	
		
    		if (e.status() == HttpStatus.NOT_FOUND.value()) {
    			throw new CustomerNotFoundException();
    		} else if (e.status() == HttpStatus.UNAUTHORIZED.value() 
    								|| e.status() == HttpStatus.FORBIDDEN.value()) {
    			throw new UnauthorizedException();
    		} else {
    			throw new RuntimeException();	
    		}
		}		
	}


	@Override
	public void updateCustomer(Customer customer) {
						
		try {
			
			customerFeignClient.updateCustomer(customer.getId(), customer);
			
		} catch (FeignException e) {
			if (e.status() == HttpStatus.UNAUTHORIZED.value() 
					|| e.status() == HttpStatus.FORBIDDEN.value()) {
				throw new CustomerAccessDeniedException();
			} else {
				throw new RuntimeException();
			}
		}
	}


	@Override
	public void deleteCustomer(long id) {
		
		try {
			
			customerFeignClient.deleteCustomer(id);
		
		} catch (FeignException e) {
			if (e.status() == HttpStatus.NOT_FOUND.value()) {
				throw new CustomerNotFoundException();
			} else if (e.status() == HttpStatus.UNAUTHORIZED.value()
					|| e.status() == HttpStatus.FORBIDDEN.value()) {
				throw new CustomerAccessDeniedException();
			}	
		}
	}

}
