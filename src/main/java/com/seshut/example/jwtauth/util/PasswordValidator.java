package com.seshut.example.jwtauth.util;

public class PasswordValidator {

	public static boolean isValid(String value) {
		boolean flag = true;
		if(value == null)
		{
			flag = false;
		}
		else if(value.length() < 8)
		{
			flag = false;
		}
		else
		{
			boolean upperFlag = false, lowerFlag = false, numberFlag = false;
			for(int i = 0; i < value.length(); i++)
			{
				char ch = value.charAt(i);
				if(Character.isUpperCase(ch))
				{
					upperFlag = true;
				}
				else if(Character.isLowerCase(ch))
				{
					lowerFlag = true;
				}
				else if(Character.isDigit(ch))
				{
					numberFlag = true;
				}
			}
			if(!upperFlag || !lowerFlag || !numberFlag)
			{
				flag = false;
			}
		}
		return flag;
	}

}
