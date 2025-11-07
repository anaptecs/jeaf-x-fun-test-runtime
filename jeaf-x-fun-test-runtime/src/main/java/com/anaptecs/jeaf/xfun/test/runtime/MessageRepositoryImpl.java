/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.anaptecs.jeaf.xfun.api.errorhandling.ErrorCode;
import com.anaptecs.jeaf.xfun.api.errorhandling.SystemException;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedObject;
import com.anaptecs.jeaf.xfun.api.messages.LocalizedString;
import com.anaptecs.jeaf.xfun.api.messages.MessageDefinition;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.messages.MessageRepository;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

public class MessageRepositoryImpl implements MessageRepository {
  private static final long serialVersionUID = 1L;

  @Override
  public void loadResource( String pMessageResource ) throws SystemException {
  }

  @Override
  public void addAllMessages( List<MessageDefinition> pMessages ) {
  }

  @Override
  public List<MessageDefinition> getAllMessages( ) {
    return Collections.emptyList();
  }

  @Override
  public LocalizedObject getLocalizedObject( int pLocalizationID ) throws SystemException {
    return new LocalizedString(pLocalizationID);
  }

  @Override
  public MessageID getMessageID( int pMessageCode ) throws SystemException {
    return new MessageID(pMessageCode, TraceLevel.ERROR);
  }

  @Override
  public boolean existsMessage( int pMessageCode ) {
    return true;
  }

  @Override
  public ErrorCode getErrorCode( int pErrorCode ) throws SystemException {
    return new ErrorCode(pErrorCode, TraceLevel.ERROR);
  }

  @Override
  public LocalizedString getLocalizedString( int pLocalizationID ) throws SystemException {
    return new LocalizedString(pLocalizationID);
  }

  @Override
  public String getMessage( LocalizedObject pLocalizedObject, String... pMessageParameters ) {
    StringBuilder lMessage = new StringBuilder();
    lMessage.append(pLocalizedObject.getLocalizationID());
    if (pMessageParameters != null && pMessageParameters.length > 0) {
      lMessage.append(":");
      for (int i = 0; i < pMessageParameters.length; i++) {
        lMessage.append(" ");
        lMessage.append(pMessageParameters[i]);
      }
    }
    return lMessage.toString();
  }

  @Override
  public String getMessage( LocalizedObject pLocalizedObject, Locale pLocale, String... pMessageParameters ) {
    return this.getMessage(pLocalizedObject, pMessageParameters);
  }

  @Override
  public String getTraceMessage( LocalizedObject pLocalizedObject, String... pMessageParameters ) {
    return this.getMessage(pLocalizedObject, pMessageParameters);
  }
}
