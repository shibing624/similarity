package org.xm.similarity.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 加载词典用的类
 * 
 * @author ansj
 */
public class DicReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(DicReader.class);
	public static BufferedReader getReader(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		try {
			return new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("不支持的编码", e);
		}
		return null;
	}

	public static InputStream getInputStream(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		return in;
	}
}
