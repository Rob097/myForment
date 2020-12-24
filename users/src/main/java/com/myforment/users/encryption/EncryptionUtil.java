package com.myforment.users.encryption;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.StringFixedIvGenerator;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class EncryptionUtil {
	private static StandardPBEStringEncryptor standardEncryptor = null;
	
	public EncryptionUtil(
			@Value("${secret.password}") String encryptKey,
			@Value("${secret.algorithm}") String encryptAlgorithm,
			@Value("${secret.salt}") String encryptSalt,
			@Value("${secret.iv}") String encryptIv
			) throws Exception{
		
		standardEncryptor = new StandardPBEStringEncryptor();
		standardEncryptor.setPassword(encryptKey);
		standardEncryptor.setSaltGenerator(new StringFixedSaltGenerator(encryptSalt));
		standardEncryptor.setIvGenerator(new StringFixedIvGenerator(encryptIv));
		standardEncryptor.setKeyObtentionIterations(10000);
		standardEncryptor.setAlgorithm(encryptAlgorithm);
	}
	
	public String encrypt(String plainTenxt) {
		//PER ABILITARE L'ENCRYPTING DECOMMENTARE QUESTO METODO
		/*String cipherText = plainTenxt;
    	try{
    		cipherText = standardEncryptor.encrypt(plainTenxt);
    	}catch(Exception e) {}
        return cipherText;*/
		return plainTenxt;
	}
	public String decrypt(String cipherText) {
		//PER ABILITARE IL DECRYPTING DECOMMENTARE QUESTO METODO
		/*String plainTenxt = cipherText;
        try{
        	plainTenxt = standardEncryptor.decrypt(cipherText);
        }catch(Exception e) {/*Per i campi non de-criptabili}
        return plainTenxt;*/
		return cipherText;
	}
    
}
