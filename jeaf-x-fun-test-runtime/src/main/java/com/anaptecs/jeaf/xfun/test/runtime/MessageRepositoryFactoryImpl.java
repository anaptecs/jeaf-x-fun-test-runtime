/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepositoryFactory;

public class MessageRepositoryFactoryImpl implements MessageRepositoryFactory {

  @Override
  public MessageRepository getMessageRepository( ) {
    return new MessageRepositoryImpl();
  }

}
