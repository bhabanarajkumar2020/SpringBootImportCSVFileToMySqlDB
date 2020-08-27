package com.nit.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.nit.batch.model.Product;

public class ProductProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		// Perform Validation
		var prodCost = item.getProdCost();
		var discount = prodCost * 25 / 100.0;
		var gst = prodCost * 18 / 100.0;
		// Set the discount and gst
		item.setProdDisc(discount);
		item.setProdGst(gst);
		return item;
	}      
}
