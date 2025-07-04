package sailpoint.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sailpoint.exception.InvalidConfigException;
import sailpoint.object.config.Config;

public class ConfigUtils {
    // 02/07/2025: Added Log4J
    private static Logger LOG = LogManager.getLogger(ConfigUtils.class);

    public static Config ReadConfigFile(String path) throws InvalidConfigException {
        LOG.debug(String.format("ReadConfigFile: %s", path));
        Gson gson = new Gson();
        try (Reader reader = new FileReader(path)) {
            // Convert JSON to Config object and return
            return gson.fromJson(reader, Config.class);

        } catch (IOException _exc) {
            LOG.error(_exc.getMessage(), _exc);
            throw new InvalidConfigException("Error opening config file, see exception for details", _exc);
        }
    }
    
    public static void WriteConfigFile(String path, Config config) throws InvalidConfigException {
        LOG.debug(String.format("WriteConfigFile: %s", path));
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println(config);
            gson.toJson(config, writer);
        } catch (IOException _exc) {
            LOG.error(_exc.getMessage(), _exc);
            throw new InvalidConfigException("Error writing config file, see exception for details", _exc);
        }
    }
    
    public static String DecodeConfigItem(Config config, String encodedString) throws InvalidConfigException {
        LOG.debug(String.format("DecodeConfigItem: %s", encodedString));
    	String ks = config.getKs();
    	byte[] decodedKey = Base64.getDecoder().decode(config.getKs());
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        
        try {
        	GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(config.getIv()));
			return EncryptionUtils.decrypt(EncryptionUtils.ALGORITHM, encodedString, secretKey, gcmParameterSpec);
		} catch (Exception _exc) {
            LOG.error(_exc.getMessage(), _exc);
			throw new InvalidConfigException("Error decoding secret key", _exc);
		}
    }

    public static Config EncodeSecrets(Config config) throws InvalidConfigException {
        LOG.debug(String.format("EncodeSecrets"));
        String cSecret = config.getTenant().getClientSecret();
        String pxPass = config.getProxy().getPassword();
        boolean proxyEnabled = config.getProxy().isEnabled();
        String ks = config.getKs();
        String iv = config.getIv();
        
        try {
        	GCMParameterSpec gcmParameterSpec = null;
        	
        	if (iv == null || iv.length() == 0) {
                LOG.debug(String.format("Generating IV"));
        		gcmParameterSpec = EncryptionUtils.generateInitVector();
//        		System.out.println(gcmParameterSpec.getTLen());
        		config.setIv(new String(Base64.getEncoder().encode(gcmParameterSpec.getIV())));
        	} else {
        		gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(config.getIv()));
        	}
        	
        	
        	if (ks == null || ks.length() == 0) {
                LOG.debug(String.format("Generating Key"));
            	SecretKey key = EncryptionUtils.generateKey(128);
            	
            	byte[] rawData = key.getEncoded();
                String encodedKey = Base64.getEncoder().encodeToString(rawData);
            	config.setKs(encodedKey);
            	
//            	gcmParameterSpec = EncryptionUtils.generateInitVector();
                
            }
        	
        	byte[] decodedKey = Base64.getDecoder().decode(config.getKs());
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            
            if (cSecret != null && cSecret.length() > 0 && !cSecret.startsWith("%%") && !cSecret.equalsIgnoreCase("env")) {
                LOG.debug(String.format("Encrypting Client Secret"));
            	String encodedSecret = EncryptionUtils.encrypt(EncryptionUtils.ALGORITHM, cSecret, secretKey, gcmParameterSpec);
            	config.getTenant().setClientSecret("%%" + encodedSecret);
            }
            
            if (proxyEnabled && pxPass != null && pxPass.length() > 0 && !pxPass.startsWith("%%") && !pxPass.equalsIgnoreCase("env")) {
                LOG.debug(String.format("Encrypting Proxy Password"));
            	String encodedSecret = EncryptionUtils.encrypt(EncryptionUtils.ALGORITHM, pxPass, secretKey, gcmParameterSpec);
            	config.getProxy().setPassword("%%" + encodedSecret);
            }
            return config;
        } catch (Exception _exc) {
            LOG.error(_exc.getMessage(), _exc);
        	throw new InvalidConfigException("Error while encrypting config secrets: " + _exc.getMessage(), _exc);
        	
        }
    }
}
