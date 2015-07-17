package org.haldokan.edge.guava;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GuavaTable {
	public static void main(String[] args) throws Exception {
		// close prices for securities for biz dates
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Table<String, Date, Double> pxs = HashBasedTable.create();
		
		pxs.put("GOOG", df.parse("20150202"), 500.3434d);
		pxs.put("AMAZ", df.parse("20150202"), 300.42d);
		pxs.put("GOOG", df.parse("20150201"), 501.3434d);
		pxs.put("AMAZ", df.parse("20150201"), 301.42d);
		pxs.put("GOOG", df.parse("20150130"), 502.3434d);
		pxs.put("AMAZ", df.parse("20150130"), 302.42d);
		pxs.put("GOOG", df.parse("20150129"), 503.3434d);
		pxs.put("AMAZ", df.parse("20150129"), 303.42d);
		pxs.put("GOOG", df.parse("20150128"), 504.3434d);
		pxs.put("AMAZ", df.parse("20150128"), 304.42d);
		pxs.put("MSFT", df.parse("20150202"), 500.3434d);
		pxs.put("ORAC", df.parse("20150201"), 300.42d);

		System.out.println(pxs);
		
		System.out.println(pxs.row("GOOG"));
		System.out.println(pxs.column(df.parse("20150202")));
		System.out.println(pxs.get("AMAZ", df.parse("20150130")));
	}
}
