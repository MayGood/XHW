package com.maygood.xhw.data;

public class MessageFormater {

	//6λ��ʾ��ǰ��λΪ��ʼλ��   ĩ��λΪ����λ��
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
