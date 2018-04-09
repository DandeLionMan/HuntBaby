package com.dandelion.controller;

import com.dandelion.domain.WXSocketEntity;
import com.dandelion.message.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", name = "微信API")
@Api(description = "微信API")
public class WXSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private Message message = new Message();

    @Autowired
    public void WXSocketController(){

    }

    @ResponseBody
    @RequestMapping(value = "wx/socket", method = RequestMethod.POST)
    @ApiOperation(value = "微信消息接收接口", notes = "不认证")
    public ResponseEntity<Message> socketConnectMethod(@RequestBody WXSocketEntity socketEntity){
        LOGGER.warn("socketEntity：" + socketEntity);
        message.setMsg(200 , "success");
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

}
