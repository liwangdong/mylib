package com.wonder.utils;

import java.util.UUID;

public class UUIDUtil {
	public static String getUUID36() {
		//返回36位UUID 
		return UUID.randomUUID().toString(); 
	}
}
