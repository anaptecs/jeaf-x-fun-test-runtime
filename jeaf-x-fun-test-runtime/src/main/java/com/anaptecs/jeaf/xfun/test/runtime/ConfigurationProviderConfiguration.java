/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.anaptecs.jeaf.xfun.annotations.ConfigurationProviderConfig;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.config.AnnotationBasedConfiguration;
import com.anaptecs.jeaf.xfun.api.config.ComponentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.EnvironmentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceBundleConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.SystemPropertiesConfigurationResourceFactory;

public class ConfigurationProviderConfiguration extends AnnotationBasedConfiguration<ConfigurationProviderConfig> {
  private static final ConfigurationProviderConfiguration INSTANCE = new ConfigurationProviderConfiguration();

  public static ConfigurationProviderConfiguration getInstance( ) {
    return INSTANCE;
  }

  public ConfigurationProviderConfiguration( ) {
    // Try to suppress exceptions whenever possible.
    super(ConfigurationProviderConfig.CONFIG_PROVIDER_CONFIG_RESOURCE_NAME, XFun.X_FUN_BASE_PATH, false);
  }

  @Override
  protected Class<ConfigurationProviderConfig> getAnnotationClass( ) {
    return ConfigurationProviderConfig.class;
  }

  @Override
  protected String getDefaultConfigurationClass( ) {
    return XFun.DEFAULT_CONFIGURATION_CLASS;
  }

  @Override
  public ConfigurationProviderConfig getEmptyConfiguration( ) {
    return new ConfigurationProviderConfig() {

      @Override
      public Class<? extends Annotation> annotationType( ) {
        return ConfigurationProviderConfig.class;
      }

      @Override
      public Class<? extends SystemPropertiesConfigurationResourceFactory> systemPropertiesConfigurationResourceFactory( ) {
        return null;
      }

      @Override
      public Class<? extends ResourceConfigurationResourceFactory> resourceConfigurationResourceFactory( ) {
        return null;
      }

      @Override
      public Class<? extends ResourceBundleConfigurationResourceFactory> resourceBundleConfigurationResourceFactory( ) {
        return null;
      }

      @Override
      public Class<? extends FileConfigurationResourceFactory> fileConfigurationResourceFactory( ) {
        return null;
      }

      @Override
      public Class<? extends EnvironmentConfigurationResourceFactory> environmentConfigurationResourceFactory( ) {
        return null;
      }

      @Override
      public Class<? extends ComponentConfigurationResourceFactory> componentConfigurationResourceFactory( ) {
        return null;
      }
    };
  }

  @Override
  public List<String> checkCustomConfiguration( ConfigurationProviderConfig pCustomConfiguration ) {
    // As the configuration just defines classes of interface implementations we just have to ensure that
    // the defined classes are real classes and not just interfaces.
    List<String> lConfigErrors = new ArrayList<>(0);

    // Check factories
    this.tryNewInstance(pCustomConfiguration.componentConfigurationResourceFactory(),
        ComponentConfigurationResourceFactory.class, lConfigErrors);
    this.tryNewInstance(pCustomConfiguration.environmentConfigurationResourceFactory(),
        EnvironmentConfigurationResourceFactory.class, lConfigErrors);
    this.tryNewInstance(pCustomConfiguration.fileConfigurationResourceFactory(), FileConfigurationResourceFactory.class,
        lConfigErrors);
    this.tryNewInstance(pCustomConfiguration.resourceBundleConfigurationResourceFactory(),
        ResourceBundleConfigurationResourceFactory.class, lConfigErrors);
    this.tryNewInstance(pCustomConfiguration.systemPropertiesConfigurationResourceFactory(),
        SystemPropertiesConfigurationResourceFactory.class, lConfigErrors);

    // Return result of configuration check.
    return lConfigErrors;
  }

  public ComponentConfigurationResourceFactory getComponentConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.componentConfigurationResourceFactory(),
        defaultConfig.componentConfigurationResourceFactory(), exceptionOnError);
  }

  public EnvironmentConfigurationResourceFactory getEnvironmentConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.environmentConfigurationResourceFactory(),
        defaultConfig.environmentConfigurationResourceFactory(), exceptionOnError);
  }

  public FileConfigurationResourceFactory getFileConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.fileConfigurationResourceFactory(),
        defaultConfig.fileConfigurationResourceFactory(), exceptionOnError);
  }

  public ResourceConfigurationResourceFactory getResourceConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.resourceConfigurationResourceFactory(),
        defaultConfig.resourceConfigurationResourceFactory(), exceptionOnError);
  }

  public ResourceBundleConfigurationResourceFactory getResourceBundleConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.resourceBundleConfigurationResourceFactory(),
        defaultConfig.resourceBundleConfigurationResourceFactory(), exceptionOnError);
  }

  public SystemPropertiesConfigurationResourceFactory getSystemPropertiesConfigurationResourceFactory( ) {
    // Resolve impl in a 2-way approach. First we look at the custom configuration afterwards we do the fallback to
    // our default configuration.
    return this.newInstance(customConfig.systemPropertiesConfigurationResourceFactory(),
        defaultConfig.systemPropertiesConfigurationResourceFactory(), exceptionOnError);
  }
}
