package com.ericsson.swgw.da.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.URLs;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ericsson.swgw.da.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.test.context.junit4.SpringRunner;

/*
//@WebMvcTest(value = Listener.class)
@SpringBootTest(classes = Listener.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc*/
public class ControllerApplicationTests {
	/*
	 * //public Listener listner;
	 * 
	 * @Autowired private MockMvc mockmvc;
	 * 
	 * 
	 * //@Autowired(required=true) //private Listener listner;
	 * 
	 * @MockBean private Listener listner; // private static DerbyDBService dao;
	 * 
	 * ObjectMapper objectMapper = new ObjectMapper(); ObjectWriter objectwriter =
	 * objectMapper.writer();
	 * 
	 * //@BeforeAll
	 * 
	 * @BeforeEach public void SetupContext(){
	 * 
	 * MockitoAnnotations.openMocks(this); this.mockmvc =
	 * MockMvcBuilders.standaloneSetup(listner).build(); }
	 * 
	 * 
	 * 
	 * 
	 * @Test void createProduct_test() throws Exception{
	 * 
	 * String payload =
	 * "{\"Ticket\":\"T-139185\",\"Products\":[{\"Number\":\"CXP2020276\",\"Version\":\"R2H\",\"Checksum\":{\"SHA256\":\"78681872cd45744dffc4dfc66340cd43f5133b2d18037fdd0aab64f2245e8df81234\",\"MD5\":\"188036cc9dddb397273fc8b59942ac27123\"},\"FunctionDesignation\":\"MINI-LINK6600SBL-Standard\",\"ExternalAccess\":\"No\"}],\"EUFTGroupName\":\"ERICSSONBORAS(SWPTDFLOW)\",\"TicketHeader\":\"MINI-LINK6600&63661.19PRA3SW\",\"EUFT\":\"963259\"}";
	 * JSONObject json = new JSONObject(payload);
	 * 
	 * Map<String, Object> mapPayload = new
	 * ObjectMapper().readValue(json.toString(), HashMap.class);
	 * 
	 * 
	 * Mockito.when(listner.processPayload(mapPayload,
	 * MediaType.APPLICATION_JSON)).thenReturn("Payload working...");
	 * 
	 * 
	 * MockHttpServletRequestBuilder reqbuilder =
	 * MockMvcRequestBuilders.post("http://localhost:4444/json/payload")
	 * .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
	 * .accept(org.springframework.http.MediaType.APPLICATION_JSON) .content(new
	 * ObjectMapper().writeValueAsString(json));
	 * 
	 * 
	 * 
	 * //mockmvc.perform(reqbuilder).andExpect(MockMvcResultMatchers.status().isOk()
	 * ResultActions perform = mockmvc.perform(reqbuilder); MvcResult mvcResult =
	 * perform.andReturn(); MockHttpServletResponse response =
	 * mvcResult.getResponse(); int ststus = response.getStatus();
	 * System.out.println("getErrorMessage:::::::::::::"+response.getErrorMessage()+
	 * ":::::::"); assertEquals(200, ststus);
	 * 
	 * }
	 * 
	 * 
	 * @Test public void testAdd() { System.out.println("::::::::::HI::::::");
	 * Listener liss=new Listener(); int r=liss.add(100,500);
	 * 
	 * System.out.println("::::::::::Byeeeee::::::"+r+":::");
	 * assertEquals(600,r,"Faileddddd so what"); }
	 * 
	 * @Test public void getPortInfo_test() throws Exception{ String payload =
	 * "4444"; MockHttpServletRequestBuilder mockbuilder =
	 * MockMvcRequestBuilders.post("http://localhost:4444/json/getConfig")
	 * .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
	 * .accept(org.springframework.http.MediaType.APPLICATION_JSON) .content(new
	 * ObjectMapper().writeValueAsString(payload));
	 * 
	 * mockmvc.perform(mockbuilder).andReturn(); }
	 * 
	 * 
	 * @Test public void getConfig_test() throws Exception{
	 * 
	 * HttpHeaders header = new HttpHeaders();
	 * header.setContentType(MediaType.APPLICATION_JSON);
	 * 
	 * ResponseEntity<String> resp = new ResponseEntity<String>(
	 * "setConfigure Success", header, HttpStatus.OK );
	 * 
	 * Mockito.when(listner.getConfig(963259)).thenReturn(resp);
	 * MockHttpServletRequestBuilder reqBuilder =
	 * MockMvcRequestBuilders.get("https://localhost:4444/json/getConfig");
	 * ResultActions rsAction= mockmvc.perform(reqBuilder); MvcResult mvcResult =
	 * rsAction.andReturn(); MockHttpServletResponse res = mvcResult.getResponse();
	 * int status = res.getStatus(); Assertions.assertEquals(resp, status,
	 * "Success Result!");
	 * 
	 * }
	 * 
	 * @Test public void getSecrects_test (){
	 * 
	 * String actual = "[0-9]+";
	 * Assertions.assertEquals("FB-xR2y352_y~FiaJyo_XDkA-0t9GRxabc", actual);
	 * 
	 * }
	 */
}
