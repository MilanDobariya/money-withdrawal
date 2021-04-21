package com.moneywithdrawal.serviceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.moneywithdrawal.model.Withdrawn;
import com.moneywithdrawal.service.WithdrawalService;  


@Service
public class WithdrawalServiceImpl implements WithdrawalService {

	@Override
	public Withdrawn getInternationalWithdrawal(Withdrawn withdrawal) {
		String fileName = "currency-rate.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
		 Object obj = null;
		try {
			FileReader reader = new FileReader(file);
			obj = new JSONParser().parse(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        JSONObject jo = (JSONObject) obj;
        JSONObject rate = (JSONObject) jo.get("conversion_rates");
           double conRate =  Double.parseDouble(rate.get(withdrawal.getCurrencyType())+"");
	       double total = (1/conRate)*withdrawal.getNoteType()*withdrawal.getQuantity();
	       double charge = ((total*2)/2);
	       withdrawal.setAmount(total+ charge);
	    return withdrawal;
	}

}
