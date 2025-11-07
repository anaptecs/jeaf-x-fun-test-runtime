/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.config.ComponentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;

public class ComponentConfigurationResourceFactoryImpl implements ComponentConfigurationResourceFactory {

  @Override
  public ConfigurationResource getComponentConfigurationResource( ComponentID pComponentID )
    throws JEAFSystemException {
    return new TestConfigurationResource();
  }
}
