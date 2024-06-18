package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(s.id,s.date,s.amount,s.seller.name) " +
    "FROM Sale s WHERE s.date BETWEEN :minDate AND :maxDate " +
    "and UPPER(s.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))")
    Page<SaleReportDTO> searchSaleByPeriodAndSellerName(LocalDate minDate, LocalDate maxDate, String sellerName, Pageable pageable);
    
    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.seller.name, sum(s.amount)) " +
    "FROM Sale s " +
    "WHERE s.date BETWEEN :minDate AND :maxDate " +
    "GROUP BY s.seller"
    )
    List<SaleSummaryDTO> getSaleSummaryBySeller(LocalDate minDate, LocalDate maxDate);
}
