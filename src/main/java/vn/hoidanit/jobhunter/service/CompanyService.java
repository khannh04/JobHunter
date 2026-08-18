package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateNewCompany(Company company){
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company updateCompany(long id, Company company){
        Optional<Company> currentCom = this.companyRepository.findById(id);
        if (currentCom.isPresent()){
            Company updateCom = currentCom.get();
            updateCom.setName(company.getName());
            updateCom.setDescription(company.getDescription());
            updateCom.setAddress(company.getAddress());
            updateCom.setLogo(company.getLogo());
            return this.companyRepository.save(updateCom);
        }
        return null;
    }

    public void deleteACompany(long id){
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()){
            Company com = companyOptional.get();
            // fetch all users belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }

    public Optional<Company> findById(long id){
        return this.companyRepository.findById(id);
    }

}
