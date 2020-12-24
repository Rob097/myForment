package com.myforment.users.encryption;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import com.google.gson.Gson;
import com.myforment.users.security.configuration.Properties;

@SuppressWarnings("unused")
public class MongoDBBeforeSaveEventListener extends AbstractMongoEventListener<Object> {

	private static Logger logger = LogManager.getLogger(MongoDBBeforeSaveEventListener.class);

	@Autowired
	EncryptionUtil encryptionUtil;

	@Override
	public void onBeforeSave(BeforeSaveEvent<Object> event) {

		try {
			Document eventObject = event.getDocument();

			/*
			 * We'll skip these because: We don't want to encrypt the ID as we'll be
			 * querying on this most of the times in our business logic. The '_class' field
			 * is a meta-data field added by SpringBoot which really is not very important
			 * in terms of being encrypted or not.
			 */
			List<String> keysNotToEncrypt = Properties.ENCRYPT_EXCEPTIONS;

			for (String key : eventObject.keySet()) {
				if (!keysNotToEncrypt.contains(key) && !(eventObject.get(key) instanceof Date) && !(eventObject.get(key) instanceof Boolean)) {
					
					if (eventObject.get(key) instanceof Document) {
						Document d = (Document) eventObject.get(key);
						if(d.containsKey("addressLineOne")) {
							Document c = new Document();
							c.put("addressLineOne", this.encryptionUtil.encrypt(d.getString("addressLineOne")));
							c.put("addressLineTwo", this.encryptionUtil.encrypt(d.getString("addressLineTwo")));
							c.put("cap", this.encryptionUtil.encrypt(d.getString("cap")));
							c.put("city", this.encryptionUtil.encrypt(d.getString("city")));
							c.put("province", this.encryptionUtil.encrypt(d.getString("province")));
							c.put("country", this.encryptionUtil.encrypt(d.getString("country")));
							eventObject.put(key, c);
						}
					}else {
						//Non togliere questo try catch!
						try {
	            			eventObject.put(key, this.encryptionUtil.encrypt(eventObject.get(key).toString()));
	            		}catch(Exception e) {
	            			eventObject.put(key,eventObject.get(key));
	            		}
					}
				}
			}

			//logger.info("DB Object: " + new Gson().toJson(eventObject));

			super.onBeforeSave(event);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
