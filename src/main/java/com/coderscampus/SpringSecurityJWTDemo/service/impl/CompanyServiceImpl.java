package com.coderscampus.SpringSecurityJWTDemo.service.impl;

import com.coderscampus.SpringSecurityJWTDemo.domain.Company;
import com.coderscampus.SpringSecurityJWTDemo.dto.UserDto;
import com.coderscampus.SpringSecurityJWTDemo.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements com.coderscampus.SpringSecurityJWTDemo.service.CompanyService {

    private CompanyRepository companyRepository;

    @Override
    public Company findByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName).orElse(null);
    }

    @Override
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company findById(Long companyId) {
        return companyRepository.findById(companyId).orElse(null);

    }

}
