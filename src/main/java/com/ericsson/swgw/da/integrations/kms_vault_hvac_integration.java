package com.ericsson.swgw.da.integrations;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.constants.IntegrationConstants;

public class kms_vault_hvac_integration {
	 public static String VAULT_SERVER_PATH = Constants.EMPTY;
	 public static String TOKEN = Constants.EMPTY;
	 
	 static{
		 TOKEN = IntegrationConstants.VAULT_SERVER_PATH ;
		 TOKEN = IntegrationConstants.TOKEN;
	 }
	 
	 public static Vault  getVaultConnection() throws VaultException{
		 final VaultConfig vaultConfig = new VaultConfig().address(VAULT_SERVER_PATH).token(TOKEN).build();
	     final Vault vault = new Vault(vaultConfig);
		 
		return vault;		 
	 }
	 
	 public static String getClientKey() throws VaultException{
		 Vault vault = getVaultConnection();
		 final String userName = vault.logical().read(IntegrationConstants.PATH).getData().get(IntegrationConstants.CLIENT_KEY);
		 
		 return (null !=  userName) ? userName : Constants.EMPTY;		 
	 }
	 
	 public static String getSecretKey() throws VaultException{
		 Vault vault = getVaultConnection();
		 final String password = vault.logical().read(IntegrationConstants.PATH).getData().get(IntegrationConstants.SECRET_KEY);
		 
		return (null !=  password) ? password : Constants.EMPTY;		 
	 }
}
