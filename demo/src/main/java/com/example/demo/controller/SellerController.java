package com.example.demo.controller;

import com.example.demo.dto.SellerDTO;
import com.example.demo.service.SellerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("api/v1/seller")
@RestController
public class SellerController {
    private SellerService sellerService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<SellerDTO> showAllSellers(){
        return sellerService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SellerDTO getSellerById(@PathVariable Long id){
        return sellerService.getById(id);
    }
}
