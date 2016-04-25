package com.ginkgocap.tongren.base;

public class ForMatJSONStr {
	 
	  public static void main(String[] args) {
	    String jsonStr = "{\"logo\":\"TVRRME5qVXlPRE0xTVRJMk1UQXdNREEyTWpNME9EVXo=\",\"identityCard\":\"TVRRME5qVXlPREkzTlRVeU1EQXdNREF5TkRJNE5qUXk=\",\"legalPersonMobile\":\"15811189392\",\"organizationId\":3905029794955769,\"legalPerson\":\"��������\",\"businessLicense\":\"TVRRME5qVXlPRE16TnpnM05EQXdNREEwTnpnNU9UazU=\",\"fullName\":\"��֯ȫ��update\",\"introduction\":\"��֯���update\",\"organizationType\":123,\"verifyCode\":\"8720\"}";
	    String fotmatStr = ForMatJSONStr.format(jsonStr);
//			fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		//	fotmatStr = fotmatStr.replaceAll("\t", "");
	    System.out.println(fotmatStr);
	  } 
	  
	  /**
	   * �õ���ʽ��json���  �˸���\t ������\r
	   */
	  public static String format(String jsonStr) {
	    int level = 0;
	    StringBuffer jsonForMatStr = new StringBuffer();
	    for(int i=0;i<jsonStr.length();i++){
	      char c = jsonStr.charAt(i);
	      if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
	        jsonForMatStr.append(getLevelStr(level));
	      }
	      switch (c) {
	      case '{': 
	      case '[':
	        jsonForMatStr.append(c+"\n");
	        level++;
	        break;
	      case ',': 
	        jsonForMatStr.append(c+"\n");
	        break;
	      case '}':
	      case ']':
	        jsonForMatStr.append("\n");
	        level--;
	        jsonForMatStr.append(getLevelStr(level));
	        jsonForMatStr.append(c);
	        break;
	      default:
	        jsonForMatStr.append(c);
	        break;
	      }
	    }
	    
	    return jsonForMatStr.toString();

	  }
	  
	  private static String getLevelStr(int level){
	    StringBuffer levelStr = new StringBuffer();
	    for(int levelI = 0;levelI<level ; levelI++){
	      levelStr.append("\t");
	    }
	    return levelStr.toString();
	  }

	}