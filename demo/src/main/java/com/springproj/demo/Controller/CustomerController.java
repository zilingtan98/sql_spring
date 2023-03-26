package com.springproj.demo.Controller;
import com.springproj.demo.Model.Transaction;
import com.springproj.demo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class CustomerController  {

    @Autowired
    CustomerRepository customerRepository;
    @GetMapping("/retrieve")
    public Page<Transaction> retrieveTransactions(
            @RequestParam(name = "customer_id", required = false) String customerID,
            @RequestParam(name = "account_number", required = false) String accountNum,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(customerRepository.findTransactions(customerID, accountNum, description, pageable)).getBody();

    }
    @PutMapping("/updateDescription/{id}")
    public ResponseEntity<String> updateTransactionDescription(@PathVariable("id") String id, @RequestBody Transaction transaction) {
        Transaction _transaction = customerRepository.findById(id);

        if (_transaction != null) {
            _transaction.setDescription(transaction.getDescription());

            try{
                customerRepository.updateDescription(_transaction,id);
                return new ResponseEntity<>("Description for transaction id=" + id +" was updated successfully.", HttpStatus.OK);
            } catch (OptimisticLockingFailureException ex) {
                // Handle optimistic locking exception
                return new ResponseEntity<>("Description to update account number=" + id + " due to concurrent update.", HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>("Cannot find account number=" + id, HttpStatus.NOT_FOUND);
        }
    }

}
