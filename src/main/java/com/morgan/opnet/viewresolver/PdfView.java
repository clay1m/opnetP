package com.morgan.opnet.viewresolver;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.morgan.opnet.model.Pizza;

public class PdfView extends AbstractIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Pizza pizza = (Pizza) model.get("pizza");

		PdfPTable table = new PdfPTable(3);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell("Name");
		table.addCell("Flavor");
		table.addCell("Toppings");

		table.addCell(pizza.getName());
		table.addCell(pizza.getFlavor());

		StringBuffer toppings = new StringBuffer("");
		for (String topping : pizza.getToppings()) {
			toppings.append(topping);
			toppings.append(" ");
		}
		table.addCell(toppings.toString());
		document.add(table);

	}

}
