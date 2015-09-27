package net.ko.creator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;

public class SwtUtils {
	public static String RGBToHex(RGB rgb) {
		int red = rgb.red;
		int green = rgb.green;
		int blue = rgb.blue;
		String r = Integer.toHexString(red);
		String g = Integer.toHexString(green);
		String b = Integer.toHexString(blue);
		if (r.length() == 1) r = "0" + r;
		if (g.length() == 1) g = "0" + g;
		if (b.length() == 1) b = "0" + b;
		return "#" + r + g + b;
	}
	public static Color hex2RGB(Device device, String colorStr) {
		colorStr=colorStr.replace("#", "");
		return new Color(device,
		Integer.valueOf( colorStr.substring( 0, 2 ), 16 ),
		Integer.valueOf( colorStr.substring( 2, 4 ), 16 ),
		Integer.valueOf( colorStr.substring( 4, 6 ), 16 ) );
	}	
}
