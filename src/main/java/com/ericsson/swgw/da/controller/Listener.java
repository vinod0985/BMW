package com.ericsson.swgw.da.controller;

/*###################################################################################
 #  @Name           :   Listener.java
 #
 #  @Created        :   Sep Drop 2022
 #
 #  @Description    :   To receive webhook payload request from SW Gateway.
 #
 #  @Programmer     :   Eswar D
 #
 #  @Organization   :   HCL
 #
 #  @Release        :   MR2209
 #
 #  @History        :
 #      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
 #      ZDARESW(Eswar)      25-Aug-2022            Implemented webhook reciver
 ######################################################################################*/

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.swgw.da.actions.Scanning;
import com.ericsson.swgw.da.actions.TriggerDownloadNotification;
import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.database.DerbyDBService;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.services.DASftpConnection;
import com.ericsson.swgw.da.utils.FileUtils;
import com.ericsson.swgw.da.utils.StringUtils;
import com.ericsson.swgw.da.validations.PayloadValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

@SpringBootApplication
@RestController
@RequestMapping("/json")
public class Listener {
	
	@Autowired(required=true)
	private Listener listner;

	private static final String NUM_REGEX = "[0-9]+";
	private static  Logger logger = LogManager.getLogger(Listener.class);
	private static int port = Constants.DEFAULT_PORT;
	private static String distributionMode = getDistributionMode();
	
	
	public static void main(String[] args) {
		
		logger = LogManager.getLogger(Listener.class); 
		//System.out.println("::::::::::::"+System.getenv(distributionMode)+"$$$$$$$$$$$$$$$$$");
		if(args.length > 0 && args[0].equalsIgnoreCase("--debug")){
			//logger.setLevel(Level.DEBUG);
			Configurator.setLevel(logger, Level.DEBUG);
			Configurator.setRootLevel(Level.DEBUG);
			
			/*LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration config = context.getConfiguration();
			LoggerConfig rootConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
			rootConfig.setLevel(Level.DEBUG);
			
			// This causes all Loggers to refetch information from their LoggerConfig.
			context.updateLoggers();*/
			
			logger.debug("This statement for debug porpose");
		}
		
		try {
			IniConfig iniConfig = new IniConfig();
			getPortInfo(iniConfig);
			 distributionMode = getDistributionMode();
			logger.debug(".......DA Before creating Template,Test Resorces File.......");
			FileUtils.createTemplateFile();
			FileUtils.createSSLTemplate();
			FileUtils.createlogs();
			FileUtils.createTestResorces();
			//FileUtils.createAnchorFile();
			logger.debug(".......DA END creating Template,Test Resorces File......");

			DerbyDBService.initializeDB();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("IOException :::::"+e.getMessage());
		}
				
        SpringApplication.run(Listener.class, args);
        logger.debug("This statement for debug porpose");
        logger.error("This ststement for error porpose");
        logger.info(".......DA Application Started.......");
        logger.debug("This statement for debug porpose...............");
	}
	
	public static class DistributionAgentMode {
		public static void scanMode(){
			//if(distributionMode.equalsIgnoreCase(Constants.SCAN_MODE)){
				logger.info("Running DA with distribution_method :::"+ distributionMode + " mode" );
	        	DASftpConnection dasftp = new DASftpConnection();
	        	ChannelSftp sftpChannel = null;
	        	Session session = null;
	        	int millstimeOut = 0;
	    		try {
					sftpChannel = dasftp.sftpConnection();
					Scanning scan = new Scanning(sftpChannel);
		    		if(sftpChannel != null){
		    			String casTimeout = IniConfig.getCascTimeout();
		    			if(!StringUtils.isOnlyWhitespaceOrEmpty(casTimeout)){
		    				millstimeOut = (int) TimeUnit.SECONDS.toMillis(Integer.parseInt(casTimeout));
		    			}else{
		    				 millstimeOut = 120000;
		    			}
		    			
		    			sftpChannel.connect(millstimeOut);
		    			scan.swgwScanOperation();
		    			session = sftpChannel.getSession();
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (sftpChannel != null) {
						sftpChannel.exit();
			        }
			        if (session != null && session.isConnected()) {
			            session.disconnect();
			        }
				}
			//}
			
	}

		public static long getDelay() {
			String scanInterval = IniConfig.getCascDASscanInterval();
			long daScanInterval = (!StringUtils.isOnlyWhitespaceOrEmpty(scanInterval) )? Long.parseLong(scanInterval) : 0;
			return TimeUnit.MINUTES.toMillis(daScanInterval);
		}
	}
	
	


	/**
	 * @param loadProperties
	 */
	public static String getDistributionMode() {
		IniConfig iniConfig = new IniConfig();
		 String daScanInterval = iniConfig.getCascDASscanInterval();
		 String daMode = "";
        if (!StringUtils.isOnlyWhitespaceOrEmpty(daScanInterval) && !daScanInterval.equalsIgnoreCase("0") && !(Constants.NULL).equals(daScanInterval)) {
			if (daScanInterval.matches(NUM_REGEX)) {
				daMode = Constants.SCAN_MODE;
				} 
            }else{ 
            	daMode = Constants.SWGW_NOTIFICATION_MODE;
        }
      //  daMode=Constants.SWGW_NOTIFICATION_MODE;
        System.out.println("getDistributionMode:::::::::::::"+daMode);
		return daMode;
	}

	/**
	 * @return returns respective response message.
	 */
	@ResponseBody
	@PostMapping(value = "/configure", consumes = {	MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> setConfigure(
			@RequestBody Map<String, Object> payload,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) MediaType contentType) {
		logger.info("::::::::::::::::In setConfigure Method:::::::::");
		
		String clientKey ="";
		String secretKey ="";
		int euft;
		String message = null;
		if(payload.containsKey(Constants.CLIENT_KEY) && !(Constants.NULL).equals((payload.get(Constants.CLIENT_KEY))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.CLIENT_KEY))){
			clientKey = (String) payload.get(Constants.CLIENT_KEY);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					response(Constants.MESSAGE, Constants.ERROR_CLIENT_KEY));
		}
		
		if(payload.containsKey(Constants.SECRET_KEY) && !(Constants.NULL).equals((payload.get(Constants.SECRET_KEY))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.SECRET_KEY))){
			secretKey = (String) payload.get(Constants.SECRET_KEY);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					response(Constants.MESSAGE, Constants.ERROR_SECRET_KEY));
		}
		if(payload.containsKey(Constants.Euft) && !(Constants.NULL).equals((payload.get(Constants.Euft))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.Euft)) && ((String) payload.get(Constants.Euft)).matches(NUM_REGEX)){
			euft = Integer.parseInt((String) payload.get(Constants.Euft));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					response(Constants.MESSAGE, Constants.ERROR_EUFT));
		}
		
		logger.debug("client_key :::::::::::"+clientKey);
		logger.debug("euft ::::::::"+euft);
		/*DBUtils dbUtils = new DBUtils();
		String message = dbUtils.updateConfig(client_key,secret_key);*/
		
		
		try {
			DerbyDBService.insertingData(clientKey,secretKey,euft);
		} catch (SQLException e1) {
			e1.printStackTrace();
			message = "The operation was aborted because the euft key value is not a unique one or already exists in the configurations";
		}
		System.out.println("message :::"+message);
		if (!StringUtils.isOnlyWhitespaceOrEmpty(message)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					response(Constants.MESSAGE, message));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(
				response(Constants.MESSAGE, "setConfigure Sucess"));
		}
	}
	
	/**
	 * @return returns respective response message.
	 */
	@GetMapping(value = "/getConfig")
	public ResponseEntity<String> getConfig(@RequestParam int euft) {
		logger.info("::::::::::::::::In getConfig Method:::::::::");
		/*Map<String,String> configMap = DBUtils.getConfig();*/
		Map<String, String> configMap = new HashMap<>();
		try {
			configMap = DerbyDBService.getData(euft);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				response(Constants.MESSAGE,configMap.toString()));
	}
	

	/**
	 * @param payload
	 *            it's stores the payload to the map in the form of Key and
	 *            Value pairs.
	 * @param contentType
	 *            to store the content-type media type from the request.
	 * @return returns respective response message.
	 */
	@ResponseBody
	@PostMapping(value = "/payload", consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createProduct(
			@RequestBody Map<String, Object> payload,
			@RequestHeader(HttpHeaders.CONTENT_TYPE) MediaType contentType) {
		
		System.out.println(":::::::::::::HIIIIIIIII::::::::::::::::::::::");
		logger.info("Received SWGW notification request, starting processing ...");
		try {
			distributionMode =  getDistributionMode();
			if(!distributionMode.equalsIgnoreCase(Constants.SWGW_NOTIFICATION_MODE)){
				String msg = "Running DA with distribution method :::"+ distributionMode + " mode" ;
				logger.info(msg);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						response(Constants.MESSAGE, msg));
			}
			String message = processPayload(payload, contentType);
			if(!StringUtils.isOnlyWhitespaceOrEmpty(message)){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						response(Constants.MESSAGE, message));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(response(Constants.MESSAGE, e.getMessage()));
		}
		logger.info("SWGW notification request, Completed ...");
		return ResponseEntity.status(HttpStatus.OK).body(
				response(Constants.MESSAGE, Constants.SUCCESS_MESSAGE));
	}

	/**
	 * @param payload
	 * @param contentType
	 * @throws Exception
	 */
	String processPayload(Map<String, Object> payload,MediaType contentType) throws Exception {
		logger.debug("payload::::::::" + payload);
		logger.info("format:::::::" + contentType);
		String storeMultipleFile = IniConfig.getDAStoreMultipleFile();
		FileUtils.savePayload(payload, contentType, storeMultipleFile);
		Webhook webhook = new Webhook();
		String message = PayloadValidation.validatePayload(payload, webhook);
		logger.info("message::::::::::" + message);
		if (!StringUtils.isOnlyWhitespaceOrEmpty(message)) {
			logger.error("SWGW notification request, Failed..."+message);
			return message;
		} else {
			logger.info("Running DA with distribution_method :::"+ distributionMode + " mode" );
			swgwNotificationMode( webhook);
			logger.info("SWG W notification request, Success ...");			
		}
		return "";
	}

	

	public static void swgwNotificationMode(Webhook webhook) {
		new Thread() {
			@Override
			public void run() {
				try {
					//String wEuft = webhook.getEuft();
					//configureSecrects(wEuft);
					TriggerDownloadNotification.swgwTriggerServices(webhook);
					logger.info("......./End Of DA run");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * @param euft
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	private static Map getSecrects(String euft) throws SQLException {
        Map<String, String> configMap = new HashMap<>();
        if (!StringUtils.isOnlyWhitespaceOrEmpty(euft)) {
            if (euft.matches(NUM_REGEX)) {
                configMap = DerbyDBService.getData(Integer.parseInt(euft));
            }
        }
		return configMap;
    }
	
	/**
	 * @param loadProperties
	 * @param wEuft
	 * @throws SQLException
	 */
	public void configureSecrects(String wEuft) throws SQLException {
		@SuppressWarnings("unchecked")
		Map<String, String> configureSecrects = getSecrects(wEuft);
		String clientKey = configureSecrects.get(Constants.CLIENT_KEY);
		String secretKey = configureSecrects.get(Constants.SECRET_KEY);
		IniConfig.setSwgwClientKey(clientKey);
		IniConfig.setSwgwSecretKey(secretKey);
	}

	/**
	 * @param strResponseKey
	 *            to store response key
	 * @param strResponseValue
	 *            to store response message value
	 * @return return response in the form of map
	 */
    public String response(String strResponseKey, String strResponseValue) {
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put(strResponseKey, strResponseValue);
		String response = null;
		try {
			response = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(responseMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static void getPortInfo(IniConfig iniConfig){
		  String propertyPort = iniConfig.getDAServerPort();
          if (!StringUtils.isOnlyWhitespaceOrEmpty(propertyPort)) {
              if (propertyPort.matches(NUM_REGEX)) {
                  int serverPort = Integer.parseInt(propertyPort);
                  port = serverPort;
              } 
          }
	}
	
	
	@Component
    public class CustomPort implements
            WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        @Override
        public void customize(ConfigurableServletWebServerFactory server) {
        	server.setPort(port);
        }
    }

	
	/*
	 * @Bean public ServletWebServerFactory servletContainer() {
	 * TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	 * protected void postProcessContext(Context context) { SecurityConstraint
	 * securityConstraint = new SecurityConstraint();
	 * securityConstraint.setUserConstraint("CONFIDENTIAL"); SecurityCollection
	 * collection = new SecurityCollection(); collection.addPattern("/*");
	 * securityConstraint.addCollection(collection);
	 * context.addConstraint(securityConstraint); } };
	 * tomcat.addAdditionalTomcatConnectors(redirectConnector()); return tomcat; }
	 * 
	 * // Custom Connector - redirecting http (8080) request to to https (8443)
	 * private Connector redirectConnector() {
	 * 
	 * System.out.println("port :::::::::::::"+port); Connector connector = new
	 * Connector("org.apache.coyote.http11.Http11NioProtocol");
	 * connector.setScheme("http"); connector.setPort(0);
	 * connector.setSecure(false); connector.setRedirectPort(port);
	 * 
	 * return connector; }
	 */
	
	@Configuration
	@EnableScheduling
	public class DynamicSchedulingConfig implements SchedulingConfigurer {
	    @Bean
	    public Executor taskExecutor() {
	        return Executors.newSingleThreadScheduledExecutor();
	    }

	    @Override
	    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	        taskRegistrar.setScheduler(taskExecutor());
	        taskRegistrar.addTriggerTask(
	          new Runnable() {
	              @Override
	              public void run() {
	            	  DistributionAgentMode.scanMode();
	              }
	          },
	          new Trigger() {
	              @Override
	              public Date nextExecutionTime(TriggerContext context) {
	                  Optional<Date> lastCompletionTime =
	                    Optional.ofNullable(context.lastCompletionTime());
	                  Instant nextExecutionTime =
	                    lastCompletionTime.orElseGet(Date::new).toInstant()
	                      .plusMillis(DistributionAgentMode.getDelay());
	                  return Date.from(nextExecutionTime);
	              }
	          }
	        );
	    }
	  }
}
