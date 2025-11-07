/**
 * Copyright 2004 - 2020 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;

public class TestConfigurationResource implements ConfigurationResource {
  Map<String, String> properties = new HashMap<>();

  String resourceName;

  String resourceLocation;

  @Override
  public String getConfigurationValue( String pPropertyName ) throws MissingResourceException {
    if (this.hasConfigurationValue(pPropertyName) == true) {
      return properties.get(pPropertyName);
    }
    else {
      throw new MissingResourceException("", pPropertyName, pPropertyName);
    }
  }

  @Override
  public String getResourceName( ) {
    return resourceName;
  }

  @Override
  public String getResourceLocation( ) {
    return resourceLocation;
  }

  @Override
  public boolean hasConfigurationValue( String pKey ) {
    return properties.containsKey(pKey);
  }

  @Override
  public List<String> getAllConfigurationKeys( ) {
    return Arrays.asList(properties.keySet().toArray(new String[] {}));
  }
}
