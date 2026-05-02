package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid  @RequestBody Company companyPost){
        Company newCompany = this.companyService.handleCreateNewCompany(companyPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
            ) {

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(spec, pageable));
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateACompany(@Valid @PathVariable("id") long id, @RequestBody Company company){
        return ResponseEntity.ok().body(this.companyService.updateCompany(id, company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteACompany(@PathVariable("id") long id){
        this.companyService.deleteACompany(id);
        return ResponseEntity.ok(null);
    }
}
