package com.maygood.xhw.data;

public class MessageFormater {

	//6位表示：前三位为起始位置   末三位为结束位置
	public static int locateBq(String originalMsg, int start) {
		int loc = start;
		int length = originalMsg.length();
		while(loc<length) {
			if(originalMsg.charAt(loc)=='[') {
				start = loc;
				while(loc<length) {
					if(originalMsg.charAt(loc)==']') {
						return start*1000 + loc;
					}
					loc++;
				}
				loc = start;
			}
			loc++;
		}
		return 0;
	}
}
