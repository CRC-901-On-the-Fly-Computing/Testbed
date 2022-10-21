package de.upb.crc901.otftestbed.otfprovider.gatekeeper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.upb.crc901.otftestbed.otfprovider.gatekeeper.generated.spring_server.api.GatekeeperApiDelegate;
import de.upb.crc901.testbed.otfproviderregistry.OTFProvider;
import de.upb.crc901.testbed.otfproviderregistry.common.Message;
import de.upb.crc901.testbed.otfproviderregistry.common.MessageType;

@Component
public class OtfproviderGatekeeperDelegate implements GatekeeperApiDelegate {

  private static final Logger log = LoggerFactory.getLogger(OtfproviderGatekeeperDelegate.class);

  @Autowired
  private OTFProvider otfProvider;

  public ResponseEntity<Void> receiveMessage(Message message) {
    Message newMessage = new Message();
    newMessage.addParameter(MessageType.valueOf((String)message.getParameters()[0])); //this is message type
    newMessage.addParameter(message.getParameters()[1]);
    for(int i = 2; i < message.getParameters().length; i++){
      newMessage.addParameter(message.getParameters()[i]);
    }
    this.otfProvider.receiveMessage(newMessage);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
