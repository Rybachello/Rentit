package com.example.sales.application.services;

import com.example.common.application.exceptions.CustomerNotFoundException;
import com.example.inventory.infrastructure.IdentifierFactory;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.repository.CustomerRepository;
import com.example.sales.application.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public CustomerDTO createCustomer(String email) {

        Customer customer = Customer.of(IdentifierFactory.nextID(),IdentifierFactory.nextID(),email,null);
        customerRepository.save(customer);
        return CustomerDTO.of(customer.getId(),customer.getToken(),customer.getEmail());
    }

    public Customer findByToken(String token) throws CustomerNotFoundException{
        Customer customer = customerRepository.findByToken(token);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        } else {
            return customer;
        }
    }
}
