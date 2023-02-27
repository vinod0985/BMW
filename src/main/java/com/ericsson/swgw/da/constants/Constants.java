package com.ericsson.swgw.da.constants;

/*###################################################################################
#  @Name           :   Constants.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To receive payload from SW Gateway.
#
#  @Programmer     :   Eswar D, Rajni Kumari
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      25-Aug-2022            Created Constants for custom response message.
######################################################################################*/

public class Constants {
	
	public Constants() {
	    throw new IllegalStateException("Constant class");
	  }
	
	public static final String MESSAGE = "Message";
	
	public static final String SUCCESS_MESSAGE = "Payload request is Received";

	public static final String TEMPLATE_FILE = "template.ini";

	public static final String JSON_EXT = ".json";

	public static final String XML_EXT = ".xml";

	public static final String APPLICATION_JSON = "application/json";
	
	public static final String USER_DIR = "user.dir";

	public static final String TICKET = "Ticket";
	
	public static final String TICKETHEADER = "TicketHeader";
	
	public static final String EUFT = "EUFT";
	
	public static final String NULL = "null";
	
	
	public static final String EUFTGROUPNAME = "EUFTGroupName";
	
	public static final String FUNCTIONDESIGNATION = "FunctionDesignation";
	
	public static final String EXTERNALACCESS = "ExternalAccess";
	
	public static final String PRODUCTS = "Products";
	
	public static final String NUMBER = "Number";
	
	public static final String VERSION = "Version";
	
	public static final String CHECKSUM = "Checksum";
	
	public static final String SHA256 = "SHA256";
	
	public static final String MD5 = "MD5";
	
	public static final String ERROR_TICKET = "Faulty notification - payload request has no Ticket";
	
	public static final String ERROR_EUFT_GROUPNAME = "Faulty notification - payload request has no EUFTGroupName";
	
	public static final String ERROR_TICKET_HEADER = "Faulty notification - payload request has no TicketHeader";
	
	public static final String ERROR_EUFT_NO = "Faulty notification - payload request has no EUFT Number";
	
	public static final String ERROR_PRODUCT_NO = "Faulty notification - payload request has no Product Number";
	
	public static final String ERROR_PRODUCT_VERSION = "Faulty notification - payload request has no Product Version";
	
	public static final String ERROR_FUNCTION_DESIGNATION = "Faulty notification - payload request has no FunctionDesignation";
	
	public static final String ERROR_EXTERNAL_ACCESS = "Faulty notification - payload request has no ExternalAccess";
	
	public static final String ERROR_SHA256 = "Faulty notification - payload request has no SHA256";
	
	public static final String ERROR_MD5 = "Faulty notification - payload request has no MD5";
	
	public static final String ERROR_CHECKSUM = "Faulty notification - payload request has no Checksum";
	
	public static final String ERROR_NO_PRODCUTS = "Faulty notification - payload request does not have products";

	public static final String APPLICATION_XML = "application/xml";

	public static final String AUTH_BASIC = "Basic ";

	public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String AUTH_CONTENT = "grant_type=client_credentials&scope=%s/.default";

	public static final String COLON = ":";

	public static final String BEARER = "Bearer %s";

	public static final String TEXT_XML = "text/xml";

	public static final String AUTHORIZATION = "Authorization";

	public static final String PULL_PRODUCT_URL = "/pull/pullProduct?";

	public static final String PRODUCT_NO_EQUAL_TO = "productNumber=";

	public static final String AMPERSAND = "&";

	public static final String RSTATE_NO_EQUAL_TO = "rstate=";

	public static final String TICKET_ID_EQUAL_TO = "ticket=";

	public static final String EUFT_NO_EQUAL_TO = "euftNumber=";
	
	public static final String BACK_SLASH = "/";
	
	public static final String ACCESS_TOKEN = "access_token";

	public static final String CHECKSUM_MD5 = "md5";

	public static final String CHECKSUM_CRC32 = "crc32";

	public static final String CHECKSUM_SHA1 = "sha1";

	public static final String CHECKSUM_SHA256 = "sha256";
	
	public static final String TICKET_XML_URL = "/customer/downloadquery?search=ticketxml";
	
	public static final String REF_DOC_URL = "/customer/downloadquery?search=referencedocurls";
	
	public static final String EUFT_EQUAL_TO = "EUFT=";
	
	public static final String EUFT_NUMBER_EQUAL_TO = "euftNumber=";
	
	public static final String TICKET_EQUAL_TO = "Ticket=";
	
	public static final String BASIC_AUTH = "Basic %s";

	public static final String TRUST_ANCHOR_FILE = "software_gateway_integrity_trust_anchor_a1.pem";
	
	public static final String TRUST_ANCHOR_FILE_URL = "https://www.ericsson.com/49e6e8/assets/content/ceda8a51bc5e4057a6bbbc560035d861/software_gateway_integrity_trust_anchor_a1.pem";

	public static final String REF_DOC_URLS = "/customer/downloadquery?search=referencedocurls";
	
	public static final int DEFAULT_PORT = 4444;
	
	public static final String PRAGMA_HEADER_KEY_VALUE = "akamai-x-cache-on, akamai-x-cache-remote-on, akamai-x-check-cacheable, akamai-x-get-cache-key, akamai-x-get-true-cache-key,akamai-x-get-extracted-values, akamai-x-get-ssl-client-session-id, akamai-x-serial-no, akamai-x-get-request-id";
	
	public static final String SIGN_File = "Signature File";
	
	public static final String MANIFEST_File = "Manifest File";
	
	public static final String SMO_File = "SMO Document";
	
	public static final String NMO_File = "NMO Document";
	
	public static final String PRODUCT_BOX_File = "Product Box File";
	
	public static final String TICKET_XML_File = "Ticket XML";
	
	public static final String CLIENT_KEY = "client_key";
	
	public static final String SECRET_KEY = "secret_key";
	
	public static final String SEMI_COLON = ";";
	
	public static final String Euft = "euft";
	
	public static final String ERROR_CLIENT_KEY = "client_key does not exist";
	
	public static final String ERROR_SECRET_KEY = "secret_key does not exist";
	
	public static final String ERROR_EUFT = "euft does not exist and should contain only Digits";
	
	public static final String SCAN_MODE = "scanning";
	
	public static final String SWGW_NOTIFICATION_MODE = "swgw notification";

	public static final String ADDITIONAL_PROPERTIES = "additional.properties";

	public static final String READ_SECRETS = "readSecrets.json";
	
	public static final String COMMA = ",";
	
	public static final String EMPTY = "";
	
	public static final String TRUE = "True";
	
	public static final String FALSE = "False";
	
	public static final String RELATIVE_STORAGE_PATH = "Relative_storage_path";
	
	public static final String SPINNAKER_POLLING_ENABLED = "Spinnaker_polling_enabled";
	
	public static final String MANIFEST_SHA256 = "MANIFEST_SHA256";
	
	public static final String MANIFEST_SHA256_EXTN = ".manifest.sha256";
	
	public static final String APPLICATION = "Application";
	
}
