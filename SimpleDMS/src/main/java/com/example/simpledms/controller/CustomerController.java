package com.example.simpledms.controller;

import com.example.simpledms.model.Customer;
import com.example.simpledms.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : com.example.jpaexam.controller.exam07
 * fileName : CustomerController
 * author : ds
 * date : 2022-10-21
 * description : 고객 컨트롤러 쿼리 메소드
 * 요약 :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-10-21         ds          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    CustomerService customerService; // @Autowired : 스프링부트가 가동될때 생성된 객체를 하나 받아오기

    @GetMapping("/customer")
    public ResponseEntity<Object> getCustomerAll(@RequestParam(required = false) String email,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size
    ) {

        try {
//            Pageable 객체 정의 ( page, size 값 설정 )
            Pageable pageable = PageRequest.of(page, size);

//            Page 객체 정의
            Page<Customer> customerPage;

//            findAll() 생략 해도 전체검색이 됨 :
//            why? like 검색시 고객명 매개변수가 ""이더라도 전체검색이 됨
            customerPage = customerService.findAllByDnameContaining(email, pageable);

//            맵 자료구조에 넣어서 전송
            Map<String, Object> response = new HashMap<>();
            response.put("customer", customerPage.getContent());
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());

            if (customerPage.isEmpty() == false) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/customer/all")
    public ResponseEntity<Object> removeAll() {

        try {
            customerService.removeAll();

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {

        try {
            Customer customer2 = customerService.save(customer);

            return new ResponseEntity<>(customer2, HttpStatus.OK);

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/customer/{cid}")
    public ResponseEntity<Object> getCustomerId(@PathVariable int cid) {

        try {
            Optional<Customer> optionalCustomer = customerService.findById(cid);

            if (optionalCustomer.isPresent() == true) {
//                데이터 + 성공 메세지 전송
                return new ResponseEntity<>(optionalCustomer.get(), HttpStatus.OK);
            } else {
//                데이터 없음 메세지 전송(클라이언트)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            // 서버에러 발생 메세지 전송(클라이언트)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/customer/{cid}")
    public ResponseEntity<Object> updateCustomer(@PathVariable int cid,
                                            @RequestBody Customer customer) {

        try {
            Customer customer2 = customerService.save(customer);

            return new ResponseEntity<>(customer2, HttpStatus.OK);

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping ("/customer/deletion/{cid}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable int cid) {

        try {
            boolean bSuccess = customerService.removeById(cid);

            if (bSuccess == true) {
//                데이터 + 성공 메세지 전송
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
//                데이터 없음 메세지 전송(클라이언트)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            // 서버에러 발생 메세지 전송(클라이언트)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}










