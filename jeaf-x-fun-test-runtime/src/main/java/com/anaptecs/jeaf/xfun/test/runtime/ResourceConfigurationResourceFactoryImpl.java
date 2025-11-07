/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.config.ResourceConfigurationResourceFactory;

public class ResourceConfigurationResourceFactoryImpl implements ResourceConfigurationResourceFactory {

  @Override
  public ConfigurationResource getResourceConfigurationResource( String pResourceName ) {
    return new TestConfigurationResource();
  }

}
