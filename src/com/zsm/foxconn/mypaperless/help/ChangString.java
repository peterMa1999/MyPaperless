package com.zsm.foxconn.mypaperless.help;

import java.io.IOException;

import taobe.tec.jcc.JChineseConvertor;

public class ChangString {

	/**
	 * 简体转成繁体
	 */
	public static String change(String changeText) {
		try {
			JChineseConvertor jChineseConvertor = JChineseConvertor
					.getInstance();
			changeText = jChineseConvertor.s2t(changeText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return changeText;
	}

	/**
	 * 繁体转成简体
	 */
	public static String change1(String changeText) {
		try {
			JChineseConvertor jChineseConvertor = JChineseConvertor
					.getInstance();
			changeText = jChineseConvertor.t2s(changeText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return changeText;
	}
}
