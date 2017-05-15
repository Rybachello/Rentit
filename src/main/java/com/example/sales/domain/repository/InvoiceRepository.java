package com.example.sales.domain.repository;

import com.example.sales.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stepan on 15/05/2017.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
}
