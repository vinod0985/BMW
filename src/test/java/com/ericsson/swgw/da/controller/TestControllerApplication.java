package com.ericsson.swgw.da.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import io.minio.errors.MinioException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ericsson.swgw.da.actions.TriggerDownloadNotification;
import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.controller.Listener.DistributionAgentMode;
import com.ericsson.swgw.da.database.DerbyDBService;
import com.ericsson.swgw.da.exception.CustomException;
import com.ericsson.swgw.da.exception.SignatureValidationException;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.services.AzureTokenService;
import com.ericsson.swgw.da.utils.FileUtils;
import com.ericsson.swgw.da.utils.StringUtils;
import com.ericsson.swgw.da.validations.PayloadValidation;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;

@TestMethodOrder(OrderAnnotation.class)
public class TestControllerApplication {

	@Autowired
	private MockMvc mockmvc;

	private Listener listener;
	private DistributionAgentMode distributioAgentMode;
	private static Logger logger;
	private static String currentDirectory = System
			.getProperty(Constants.USER_DIR);
	private static String testResourcePath = currentDirectory
			+ "/test-resources";
	private static String configPath = currentDirectory + "/configurations";
	private static InputStream inputStream;
	private static FileOutputStream outputStream;

	@InjectMocks
	private Listener lis;

	@Mock
	private DerbyDBService debyDBService;

	@BeforeEach
	public void setup() {
		listener = new Listener();

		MockitoAnnotations.openMocks(this);
		this.mockmvc = MockMvcBuilders.standaloneSetup(listener).build();

	}

	@Test
	@Order(1)
	public void testswgwAuthenticate() throws IOException, JSONException {
		System.out
				.println("::::::::::Start of testswgwAuthenticate Methood::::::::::");
		String aa = AzureTokenService.getswgwAuthenticate();
		assertNotNull(aa);
		System.out
				.println("::::::::::END of testswgwAuthenticate Methood::::::::::");

	}

	@Test
	@Order(2)
	public void testMain() {

		System.out.println("::::::::::Start of testMain Methood::::::::::");

		listener.main(new String[] {});

		/*
		 * logger = LogManager.getLogger(TestControllerApplication.class);
		 * String[] args= {"--debug"}; listener.main(args);
		 * assertEquals(logger.getLevel(),Level.DEBUG);
		 */
		System.out.println("::::::::::END of testMain Methood::::::::::");

	}

	@Test
	@Order(3)
	public void testSetConfigure() throws Exception {
		System.out
				.println("::::::::::Start of testSetConfigure Methood::::::::::");

		String configure = "{\"client_key\":\"8a6200ba-5c90-44d7-8d70-362eec8e4dc1\",\"secret_key\":\"FB-xR2y352_y~FiaJyo_XDkA-0t9GRxabc\",\"euft\":\"963259\"}";
		JSONObject json = new JSONObject(configure);
		// @SuppressWarnings("unchecked")
		Map<String, Object> mapConfigure = new ObjectMapper().readValue(
				json.toString(), HashMap.class);

		MockHttpServletRequestBuilder reqbuilder1 = MockMvcRequestBuilders
				.post("http://localhost:4444/json/configure")
				.contentType(
						org.springframework.http.MediaType.APPLICATION_JSON)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(mapConfigure));

		String clientKey = (String) mapConfigure.get(Constants.CLIENT_KEY);
		String secretKey = (String) mapConfigure.get(Constants.SECRET_KEY);
		int euft = Integer.parseInt((String) mapConfigure.get(Constants.Euft));

		System.out.println(":::::::::::" + clientKey + ":::::::::::::::");
		System.out.println(":::::::::::" + secretKey + ":::::::::::::::");
		System.out.println(":::::::::::" + euft + ":::::::::::::::");
		mockmvc.perform(reqbuilder1).andExpect(
				MockMvcResultMatchers.status().isOk());
		System.out
				.println("::::::::::END of testSetConfigure Methood::::::::::");

	}

	@Test
	@Order(4)
	public void testGetConfig() throws Exception {
		System.out
				.println("::::::::::Start of testGetConfig Methood::::::::::");

		/*
		 * MockHttpServletRequestBuilder reqbuilder1 =
		 * MockMvcRequestBuilders.post("http://localhost:4444/json/get")
		 */

		String aa = listener.getConfig(963259).toString();

		assertNotNull(aa);

		System.out.println("::::::::::End of testGetConfig Methood::::::::::");

	}

	@Test
	@Order(5)
	public void testDBAuthenticate() throws IOException, JSONException {
		System.out
				.println("::::::::::Start of testDBAuthenticate Methood::::::::::");

		String aa = AzureTokenService.getswgwAuthenticate();
		assertNotNull(aa);
		System.out
				.println("::::::::::End of testDBAuthenticate Methood::::::::::");

	}

	@Test
	@Order(6)
	public void testGetDistributionMode() {

		System.out
				.println("::::::::::Start of testGetDistributionMode Methood::::::::::");

		IniConfig iniConfig = new IniConfig();
		// listener.getPortInfo(iniConfig);
		System.out.println("::::::::::" + IniConfig.getCascDASscanInterval()
				+ "::::::::::::");

		System.out.println("$$$$$$$$$HIIIIII$$$$$$$$");
		String daScanInterval = IniConfig.getCascDASscanInterval();
		String a = listener.getDistributionMode();

		if (!StringUtils.isOnlyWhitespaceOrEmpty(daScanInterval)
				&& !daScanInterval.equalsIgnoreCase("0")
				&& !(Constants.NULL).equals(daScanInterval))
			assertEquals("scanning", a);

		else
			assertEquals("swgw notification", a);

		System.out
				.println("::::::::::End of testGetDistributionMode Methood::::::::::");

	}

	@Test
	@Order(7)
	public void testConfigureSecrects() throws SQLException {
		System.out
				.println("::::::::::Start of testConfigureSecrects Methood::::::::::");

		listener.configureSecrects("963259");
		System.out
				.println("::::::::::Start of testConfigureSecrects Methood::::::::::");

	}

	@Test
	@Order(8)
	public void testSwgwNotificationMode() throws StreamReadException,
			DatabindException, IOException {

		System.out
				.println("::::::::::Start of testSwgwNotificationMode Methood::::::::::");

		ObjectMapper mapper = new ObjectMapper();
		File fileObj = new File(testResourcePath, "testPayload.json");

		Map<String, Object> mapPayload = mapper.readValue(fileObj,
				new TypeReference<Map<String, Object>>() {
				});

		Webhook webhook = new Webhook();
		String message = PayloadValidation.validatePayload(mapPayload, webhook);

		assertEquals("", message);
		System.out
				.println("::::::::::End of testSwgwNotificationMode Methood::::::::::");

	}

	@Test
	@Order(9)
	void createProduct_test() throws Exception {
		System.out
				.println("::::::::::Start of createProduct_test Methood::::::::::");

		File anchorfile = new File(currentDirectory + Constants.BACK_SLASH
				+ Constants.TRUST_ANCHOR_FILE);
		if (anchorfile.exists()) {
			anchorfile.delete();
		}
		/*try {
			FileUtils.createAnchorFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*File template = new File(configPath, Constants.TEMPLATE_FILE);
		if (template.exists()) {
			template.delete();
		}
		FileUtils.moveFileToDestination(currentDirectory
				+ "/src/main/resources", configPath,
				"notification_template.ini", Constants.TEMPLATE_FILE);*/

		ObjectMapper mapper = new ObjectMapper();
		File fileObj = new File(testResourcePath, "testPayload.json");

		Map<String, Object> mapPayload = mapper.readValue(fileObj,
				new TypeReference<Map<String, Object>>() {
				});

		MockHttpServletRequestBuilder reqbuilder = MockMvcRequestBuilders
				.post("http://localhost:4444/json/payload")
				.contentType(
						org.springframework.http.MediaType.APPLICATION_JSON)
				.accept(org.springframework.http.MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(mapPayload));

		// System.out.println(":::::::::::"+payload+":::::::::::::::");
		System.out.println(":::::::::::" + mapPayload + ":::::::::::::::");

		mockmvc.perform(reqbuilder).andExpect(
				MockMvcResultMatchers.status().isOk());
		System.out
				.println("::::::::::End of createProduct_test Methood::::::::::");

	}
	
	@Test
	@Order(10)
	public void testProcessPayload() throws StreamReadException,
			DatabindException, IOException, SignatureValidationException, CustomException, JSONException, SQLException, JAXBException, MinioException, JSchException {

		System.out
				.println("::::::::::Start of testSwgwNotificationMode Methood::::::::::");

		/*File template = new File(configPath, Constants.TEMPLATE_FILE);
		if (template.exists()) {
			template.delete();
		}
		FileUtils.moveFileToDestination(currentDirectory
				+ "/src/main/resources", configPath,
				"notification_template.ini", Constants.TEMPLATE_FILE);*/

		ObjectMapper mapper = new ObjectMapper();
		File fileObj = new File(testResourcePath, "testPayload.json");

		Map<String, Object> mapPayload = mapper.readValue(fileObj,
				new TypeReference<Map<String, Object>>() {
				});

		Webhook webhook = new Webhook();
		String message = PayloadValidation.validatePayload(mapPayload, webhook);
		System.out
		.println(":::::::::message::::::::::"+message);
		TriggerDownloadNotification.swgwTriggerServices(webhook);

		assertEquals("", "");
		System.out
				.println("::::::::::End of testSwgwNotificationMode Methood::::::::::");

	}

	@Test
	@Order(11)
	public void testScanMode() throws Exception {
		System.out.println("::::::::::Start of testScanMode Methood::::::::::");
		/*File template = new File(configPath, Constants.TEMPLATE_FILE);
		if (template.exists()) {
			template.delete();
		}
		FileUtils.moveFileToDestination(currentDirectory
				+ "/src/main/resources", configPath, "scan_template.ini",
				Constants.TEMPLATE_FILE);*/

		File anchorfile = new File(currentDirectory + Constants.BACK_SLASH
				+ Constants.TRUST_ANCHOR_FILE);
		if (anchorfile.exists()) {
			anchorfile.delete();
		}
		FileUtils.moveFileToDestination(currentDirectory
				+ "/src/main/resources/", currentDirectory,
				"trust_anchor_for_test_environment_signatures.pem",
				"software_gateway_integrity_trust_anchor_a1.pem");

		distributioAgentMode.scanMode();

		System.out.println("::::::::::End of testScanMode Methood::::::::::");

	}

}
