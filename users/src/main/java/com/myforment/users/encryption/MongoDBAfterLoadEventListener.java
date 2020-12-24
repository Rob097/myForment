package com.myforment.users.encryption;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;

import com.google.gson.Gson;
import com.myforment.users.models.Address;
import com.myforment.users.security.configuration.Properties;

@SuppressWarnings("unused")
public class MongoDBAfterLoadEventListener extends AbstractMongoEventListener<Object> {

	private static Logger logger = LogManager.getLogger(MongoDBAfterLoadEventListener.class);
    
    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {

    	try {
        Document eventObject = event.getDocument();

        /*
        We'll skip these because:
        * We don't want to encrypt the ID as we'll be querying on this most of the times in our business logic.
        * The '_class' field is a meta-data field added by SpringBoot which really is not very important in terms of
            being encrypted or not.
         */
        List<String> keysNotToDecrypt = Properties.ENCRYPT_EXCEPTIONS;

        for ( String key : eventObject.keySet() ) {
            if (!keysNotToDecrypt.contains(key) && !(eventObject.get(key) instanceof Date) && !(eventObject.get(key) instanceof Boolean)) {
            	//System.out.println("CLASS: " + eventObject.get(key).toString() + "  -->  " + eventObject.get(key).getClass().getName());
            	
            	if (eventObject.get(key) instanceof Document) {
					Document d = (Document) eventObject.get(key);
					if(d.containsKey("addressLineOne")) {
						Address a = new Address(
								this.encryptionUtil.decrypt(d.getString("addressLineOne")),
								this.encryptionUtil.decrypt(d.getString("addressLineTwo")),
								this.encryptionUtil.decrypt(d.getString("cap")),
								this.encryptionUtil.decrypt(d.getString("city")),
								this.encryptionUtil.decrypt(d.getString("province")),
								this.encryptionUtil.decrypt(d.getString("country"))
								);
						eventObject.put(key, a);
					}
            	}else {
            		//Non togliere questo try catch!
            		try {
            			eventObject.put(key, this.encryptionUtil.decrypt(eventObject.get(key).toString()));
            		}catch(Exception e) {
            			eventObject.put(key,eventObject.get(key));
            		}
            	}
            }
        }

        //logger.info("DB Object: " + new Gson().toJson(eventObject));

        super.onAfterLoad(event);
        
    	}catch(Exception e) {
			e.printStackTrace();
		}
    	
    }
    
}
