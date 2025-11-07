/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.anaptecs.jeaf.xfun.annotations.StartupInfoWriterImpl;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.common.ComponentID;
import com.anaptecs.jeaf.xfun.api.config.ComponentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationProvider;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.config.EnvironmentConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.FileConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceBundleConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.ResourceConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.config.SystemPropertiesConfigurationResourceFactory;
import com.anaptecs.jeaf.xfun.api.errorhandling.JEAFSystemException;
import com.anaptecs.jeaf.xfun.api.trace.StartupInfoWriter;
import com.anaptecs.jeaf.xfun.api.trace.Trace;
import com.anaptecs.jeaf.xfun.api.trace.TraceLevel;

/**
 * Class collects all the different types of resource access providers that are required for the different source of
 * configuration parameters.
 */
@StartupInfoWriterImpl
public final class ConfigurationProviderImpl implements ConfigurationProvider, StartupInfoWriter {

  /**
   * Reference to configured ComponentConfigurationProvider.
   */
  private final ComponentConfigurationResourceFactory componentConfigurationResourceFactory;

  /**
   * Reference to configured EnvironmentConfigurationProvider.
   */
  private final EnvironmentConfigurationResourceFactory environmentConfigurationResourceFactory;

  /**
   * Reference to configured FileConfigurationProvider.
   */
  private final FileConfigurationResourceFactory fileConfigurationResourceFactory;

  /**
   * Reference to configured ResourceBundleConfigurationProvider.
   */
  private final ResourceConfigurationResourceFactory resourceConfigurationResourceFactory;

  /**
   * Reference to configured ResourceBundleConfigurationProvider.
   */
  private final ResourceBundleConfigurationResourceFactory resourceBundleConfigurationProvider;

  /**
   * Reference to configured SystemPropertiesConfigurationProvider.
   */
  private final SystemPropertiesConfigurationResourceFactory systemPropertiesConfigurationProvider;

  /**
   * Initialize object. All configured configuration providers will be resolved when the object is created.
   */
  public ConfigurationProviderImpl( ) {
    // Lookup all provider that are configured.
    ConfigurationProviderConfiguration lConfig = ConfigurationProviderConfiguration.getInstance();
    componentConfigurationResourceFactory = lConfig.getComponentConfigurationResourceFactory();
    environmentConfigurationResourceFactory = lConfig.getEnvironmentConfigurationResourceFactory();
    fileConfigurationResourceFactory = lConfig.getFileConfigurationResourceFactory();
    resourceConfigurationResourceFactory = lConfig.getResourceConfigurationResourceFactory();
    resourceBundleConfigurationProvider = lConfig.getResourceBundleConfigurationResourceFactory();
    systemPropertiesConfigurationProvider = lConfig.getSystemPropertiesConfigurationResourceFactory();
  }

  @Override
  public Configuration getComponentConfiguration( ComponentID pComponentID ) throws JEAFSystemException {
    ConfigurationResource lResource =
        componentConfigurationResourceFactory.getComponentConfigurationResource(pComponentID);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getEnvironmentConfiguration( ) {
    ConfigurationResource lResource = environmentConfigurationResourceFactory.getEnvironmentConfigurationResource();
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getFileConfiguration( String pFileName ) throws JEAFSystemException {
    ConfigurationResource lResource = fileConfigurationResourceFactory.getFileConfigurationResource(pFileName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getResourceConfiguration( String pResourceName ) {
    ConfigurationResource lResource =
        resourceConfigurationResourceFactory.getResourceConfigurationResource(pResourceName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getResourceBundleConfiguration( String pResourceBundleName ) {
    ConfigurationResource lResource =
        resourceBundleConfigurationProvider.getResourceBundleConfigurationResource(pResourceBundleName);
    return new ConfigurationImpl(lResource);
  }

  @Override
  public Configuration getSystemPropertiesConfiguration( ) {
    ConfigurationResource lResource = systemPropertiesConfigurationProvider.getSystemPropertiesConfigurationResource();
    return new ConfigurationImpl(lResource);
  }

  /**
   * Method replaces may be existing place holders for system properties inside the passed string. Place holders are
   * defined by a leading '${' and an ending '}'.
   * 
   * @param pValue String inside which system properties should be replaced. The parameter may be null.
   * @return String with replaced system properties. If no system properties are defined inside the string then a same
   * string will be returned. The method may return null.
   */
  @Override
  public String replaceSystemProperties( String pValue ) {
    String lResult;
    if (pValue != null) {
      // Detect system properties that need to be replaced.
      Set<String> lSystemProperties = this.detectSystemProperties(pValue);
      lResult = pValue;

      for (String lPropertyName : lSystemProperties) {
        // Try to resolve value of system property.
        String lPropertyValue = System.getProperty(lPropertyName);
        if (lPropertyValue != null) {
          // Replace all occurrences of the system property with its value.
          lResult = lResult.replaceAll("\\$\\{" + lPropertyName + "\\}", lPropertyValue);
        }
        // We can not substitute the placeholder. System property is not set.
        else {
          String lMessage =
              "Unable to substitue placeholder for system property " + lPropertyName + ". System-Property is not set.";
          XFun.getTrace().warn(lMessage);
        }
      }
    }
    else {
      lResult = null;
    }
    return lResult;
  }

  /**
   * Method detects an "hidden" reference to a system property inside a String.
   * 
   * @param pValue String that should be checked for contained system property references. The parameter must not be
   * null.
   * @return {@link Set} Set containing the names of all system properties that are inside the passed string.
   */
  private Set<String> detectSystemProperties( String pValue ) {
    Set<String> lSystemProperties;
    if (pValue.length() > 3) {
      lSystemProperties = new HashSet<>();
      int lCurrentIndex = 0;
      StringBuilder lBuilder = new StringBuilder();

      boolean lDone = false;
      while (lDone == false) {
        int lStartIndex = pValue.indexOf("${", lCurrentIndex);
        if (lStartIndex >= 0) {
          // Look for end token
          int lEndIndex = pValue.indexOf("}", lStartIndex);
          if (lEndIndex - 3 > lStartIndex) {
            // Resolve name of system property that should be replaced.
            String lPropertyName = pValue.substring(lStartIndex + 2, lEndIndex);
            lSystemProperties.add(lPropertyName);
            lCurrentIndex = lEndIndex + 1;
          }
          // Invalid system property definition.
          else {
            lBuilder.append(pValue);
            lDone = true;
          }
        }
        // Nothing to replace (any more).
        else {
          lBuilder.append(pValue.substring(lCurrentIndex, pValue.length()));
          lDone = true;
        }
      }
    }
    // String can not contain system properties
    else {
      lSystemProperties = Collections.emptySet();
    }
    return lSystemProperties;
  }

  @Override
  public Class<?> getStartupCompletedEventSource( ) {
    return XFun.class;
  }

  @Override
  public void traceStartupInfo( Trace pTrace, TraceLevel pTraceLevel ) {
    pTrace.writeInitInfo("JEAF X-Fun uses the following configuration providers:", pTraceLevel);
    pTrace.writeInitInfo("Component-Impl:             " + componentConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Environment-Impl:           " + environmentConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("File-Configuration:         " + fileConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Resource-Impl:              " + resourceConfigurationResourceFactory.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("Resource-Bundle-Impl:       " + resourceBundleConfigurationProvider.getClass().getName(),
        pTraceLevel);
    pTrace.writeInitInfo("System-Properties-Impl:     " + systemPropertiesConfigurationProvider.getClass().getName(),
        pTraceLevel);
  }
}
