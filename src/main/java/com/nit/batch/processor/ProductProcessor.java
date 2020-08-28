package com.nit.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.nit.batch.model.Product;

public class ProductProcessor implements ItemProcessor<Product, Product> {

	private static final Logger log = LoggerFactory.getLogger(ProductProcessor.class);

	@Override
	public Product process(Product item) throws Exception {
		try {
			log.info("STARTED");
			// Perform Validation
			var prodCost = item.getProdCost();
			var discount = prodCost * 25 / 100.0;
			var gst = prodCost * 18 / 100.0;
			// Set the discount and gst
			item.setProdDisc(discount);
			item.setProdGst(gst);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("ENDED");
		return item;
	}
}
