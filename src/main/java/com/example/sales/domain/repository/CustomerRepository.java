package com.example.sales.domain.repository;

import com.example.sales.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    @Query("Select customer from Customer customer where customer.token = ?1")
    Customer findByToken(String token);
    Customer findByEmail(String email);
}
