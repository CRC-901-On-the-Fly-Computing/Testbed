package de.upb.crc901.testbed.otfproviderregistry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.upb.crc901.otftestbed.commons.telemetry.es.TelemetryService;
import de.upb.crc901.testbed.otfproviderregistry.gui.GUIEvent;

/**
 * 
 * @author schwicht
 *
 */
public class ElasticSearchUtil {

	public static void log(GUIEvent event, String source, String target, Color domain, String edgetype) {
		
		//[1544195591874, "NEW_EDGE", "6", "MP", "-65536", "SUB_SUP"],
		List<String> message = new ArrayList<>();
		message.add(new Date().getTime()+"");
		message.add(event.toString());
		message.add(source);
		message.add(target);
		message.add(domain.hashCode()+"");
		message.add(edgetype);

		/*
		StringBuilder trace = new StringBuilder();
		
		trace.append("[");
		trace.append(new Date().getTime());
		trace.append(", ");
		trace.append("\"");
		trace.append(this.eventType.toString());
		trace.append("\"");
		trace.append(", ");
		for(int i=0; i< data.length;i++) {
			
			Object x = data[i];
			trace.append("\"");
			if(x instanceof Color) {
				trace.append(x.hashCode());
			}
			else {
				
				trace.append(x.toString());
			}
			
			trace.append("\"");
			if(i+1<data.length) {
				trace.append(", ");
			}
			
		}
		trace.append("],");
		System.out.println(trace);
		
		*/
		
		
		
		TelemetryService.apiFromOwnSpringContext().logOtfProviderNetworkMessage(message);
	}
}
