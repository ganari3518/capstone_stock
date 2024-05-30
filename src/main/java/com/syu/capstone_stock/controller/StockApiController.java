package com.syu.capstone_stock.controller;

import com.syu.capstone_stock.service.PyService;
import com.syu.capstone_stock.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockApiController {
    private final PyService pyService;

    @GetMapping("/caps/info/top10")
    public String findStockTop10() {
        return pyService.findStockTop10();
    }

    @GetMapping("/caps/graph/{Code}")
    public void saveGraph(@PathVariable String Code) {
        pyService.saveGraph(Code);
    }

    @GetMapping("/caps/info/{Code}")
    public String findStockInfo(@PathVariable String Code) {
        return pyService.findStockInfo(Code);
    }

    @GetMapping("/caps/info/graph/{Code}/{filename}")
    public ResponseEntity<?> getGraphImg(@PathVariable String Code, @PathVariable String filename){
        return FileUtil.getResource(Code, filename);
    }

    @GetMapping("/caps/info/stocks")
    public String findAllStock() {
        return pyService.findAllStock();
    }

    @GetMapping("/caps/info/exchanges")
    public String findExchange(){
        return pyService.findExchange();
    }

//    @PostMapping("/caps/stocks/krx")
//    public ApiResponse<?> saveStockKRX(@RequestBody StockRequestDto jsonString){
//        return ApiResponse.createSuccess(pyService.saveStockKRX(jsonString));
//    }
//
//    @PostMapping("/caps/stocks/price")
//    public ApiResponse<?> saveDailyPrice(@RequestBody DailyPriceRequestDto dailyPriceRequestDto){
//        return ApiResponse.createSuccess(pyService.saveDailyPrice(dailyPriceRequestDto));
//    }
}
