package com.hand.spc.job.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import com.hand.spc.job.service.ISampleCalculationSService;
import com.hand.spc.job.service.ISampleCountCalculationSService;
import com.hand.spc.utils.components.MyWebSocketHandler;
import com.hand.spc.utils.components.WebSocketHandler;

@Controller
public class WebSocketTestController extends BaseController {

	@Autowired
	private MyWebSocketHandler myWebSocketHandler;

	@Autowired
	private WebSocketHandler webSocketHandler;

	@RequestMapping(value = "/pspc/mywebsocket/test", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData query1(HttpSession session, HttpServletRequest request)//@RequestParam String username
			throws Exception {
		myWebSocketHandler.sendMessage("111111111");
		return new ResponseData();
	}

	@RequestMapping(value = "/pspc/spcwebsocket/test", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData query2(HttpSession session,HttpServletRequest request, @RequestParam String entityCode)
			throws Exception {
		//entityCode="KOIL-TEMP-TEMP32";
		webSocketHandler.sendMessage(entityCode);
		return new ResponseData();
	}

}