package com.syu.capstone_stock.service;

import com.syu.capstone_stock.util.PythonExec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PyService {
//    private final PyStockRepository pyStockRepository;
//    private final PyDailyPriceRepository pyDailyPriceRepository;
    private List<String> result = null;

    public String findStockTop10() {
        result = PythonExec.exec("top_10_stock.py");
        return iterList(result);
    }

    public void saveGraph(final String Code) {
        PythonExec.exec("graph.py",Code);
    }

    public String findStockInfo(String Code) {
        result = PythonExec.exec("stock_info.py", Code);
        return iterList(result);
    }

    public String findAllStock() {
        return PythonExec.execByzt("all_stock_code_name.py");
    }

    public String findExchange(){
        result =  PythonExec.exec("exchange_rate.py");
        return iterList(result);
    }

    private String iterList(List<String> arg) {
        String res = null;
        for(String s : arg){
            res = s+"\n";
        }
        return res;
    }

//    public List<Stock> saveStockKRX(StockRequestDto stockRequestDto){
//        return pyStockRepository.saveAll(stockRequestDto.toArrays());
//    }
//
//    public List<DailyPrice> saveDailyPrice(DailyPriceRequestDto dailyPriceRequestDto){
//        return pyDailyPriceRepository.saveAll(dailyPriceRequestDto.toArrays());
//    }
}