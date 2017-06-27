package me.bodyash.commandcode.utils;

import org.apache.commons.lang.RandomStringUtils;

public final class Codegenerator {
	
	public static String generatecode(int lenth){
		  return RandomStringUtils.randomAlphanumeric(lenth);
	}

}
