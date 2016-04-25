package com.ginkgocap.tongren.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转化为拼音的工具
 * 
 * @author hanxifa
 * 
 */
@SuppressWarnings("rawtypes")
public class ChineseToEnglish {

	// 转换单个字符
	private static String getCharacterPinYin(char c) {
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
			// 如果c不是汉字，toHanyuPinyinStringArray会返回null
			if (pinyin == null) {
				return null;
			} else {// 只取一个发音，如果是多音字，仅取第一个发音
				return pinyin[0];
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;

	}

	// 转换一个字符串
	public static String convertToSpell(String str) {
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = getCharacterPinYin(str.charAt(i));
			if (tempPinyin == null) {
				// 如果str.charAt(i)非汉字，则保持原样
				sb.append(str.charAt(i));
			} else {
				sb.append(tempPinyin);
			}
		}
		return sb.toString();
	}

	public static String getSpellByWord(char word) {
		return convertToSpell(word + "");
	}

}
