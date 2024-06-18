package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchSaleByPeriodAndSellerName(String strMinDate, String strMaxDate, String sellerName, Pageable pageable) {
		LocalDate maxDate = getMaxDateFromString(strMaxDate);
		LocalDate minDate = getMinDateFromStringAndMaxDate(strMinDate, maxDate);
		return repository.searchSaleByPeriodAndSellerName(minDate, maxDate, sellerName, pageable);
	}

	public List<SaleSummaryDTO> getSaleSummaryBySeller(String strMinDate, String strMaxDate) {
		LocalDate maxDate = getMaxDateFromString(strMaxDate);
		LocalDate minDate = getMinDateFromStringAndMaxDate(strMinDate, maxDate);
		return repository.getSaleSummaryBySeller(minDate, maxDate);
	}

	private LocalDate getMaxDateFromString(String strMaxDate) {
		if(strMaxDate.isBlank()) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}
		return LocalDate.parse(strMaxDate);
	}

	private LocalDate getMinDateFromStringAndMaxDate(String strMinDate, LocalDate maxDate) {
		if(strMinDate.isBlank()) {
			return maxDate.minusYears(1L);
		}
		return LocalDate.parse(strMinDate);
	}
}
