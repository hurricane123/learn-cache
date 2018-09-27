package com.hurricane.learn.cache.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {
	
	public static byte[] objToByte(Object obj) {		
		byte[] bytes = null;		
		try {			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();			
			ObjectOutputStream oo = new ObjectOutputStream(bo);			
			oo.writeObject(obj);			
			bytes = bo.toByteArray();			
			bo.close();			
			oo.close();  		
		}catch(Exception e) { 			
			e.printStackTrace();		
		}		
		return bytes;    
	}		
	
	
	public static Object byteToObj(byte[] bytes) {		
		Object object = null;		
		try {			
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);		
			object = ois.readObject();
			bais.close();
			ois.close();
		} catch (Exception e) {		
			e.printStackTrace();	
		}		
		return object;
	}



}
