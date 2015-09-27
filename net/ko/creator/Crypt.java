package net.ko.creator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypt
{
    public static String encode(String password,String algo)
    {
        byte[] uniqueKey=null;
		try {
			uniqueKey = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        byte[] hash      = null;

        try
        {
            hash = MessageDigest.getInstance(algo).digest(uniqueKey);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Error("Algo non supporté");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
                hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
}
