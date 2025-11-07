/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

public class FileConfigurationResourceFactoryImpl implements FileConfigurationResourceFactory {

  @Override
  public ConfigurationResource getFileConfigurationResource( String pFileName ) throws JEAFSystemException {
    return new TestConfigurationResource();
  }
}
