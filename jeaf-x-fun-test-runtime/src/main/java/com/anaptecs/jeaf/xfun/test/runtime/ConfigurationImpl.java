/**
 * Copyright 2004 - 2021 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import com.anaptecs.jeaf.xfun.annotations.TraceObjectFormatter;
import com.anaptecs.jeaf.xfun.api.XFun;
import com.anaptecs.jeaf.xfun.api.XFunMessages;
import com.anaptecs.jeaf.xfun.api.checks.Assert;
import com.anaptecs.jeaf.xfun.api.checks.Check;
import com.anaptecs.jeaf.xfun.api.config.Configuration;
import com.anaptecs.jeaf.xfun.api.config.ConfigurationResource;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverter;
import com.anaptecs.jeaf.xfun.api.datatypeconverter.DatatypeConverterRegistry;
import com.anaptecs.jeaf.xfun.api.messages.MessageID;
import com.anaptecs.jeaf.xfun.api.trace.Trace;

@TraceObjectFormatter(supportedClasses = ConfigurationImpl.class)
public class ConfigurationImpl implements Configuration {
  /**
   * Configuration resource from which configuration keys should be read.
   */
  private final ConfigurationResource configurationResource;

  /**
   * Initialize object.
   * 
   * @param pConfigurationResource Configuration resource from which configuration entries should be read. The parameter
   * must not be null.
   * @param pType Class object of the type for which this converter should be used.
   */
  public ConfigurationImpl( ConfigurationResource pConfigurationResource ) {
    // Check parameter.
    Check.checkInvalidParameterNull(pConfigurationResource, "pConfigurationResource");

    configurationResource = pConfigurationResource;
  }

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return T Value of the configuration entry. The method returns null if the configuration entry is not defined or
   * not set.
   */
  @Override
  public <T> T getConfigurationValue( String pConfigurationKey, Class<T> pType ) {
    // Return optional configuration entry.
    return this.getConfigurationValue(pConfigurationKey, false, pType);
  }

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pRequired Indicates whether the configuration entry is required or not. If pRequired is <code>true</code>
   * then the configuration entry must be exist.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return T Value of the configuration entry with the passed name. The method never returns null. If the
   * configuration entry is not defined or not set but required then a {@link MissingResourceException} will be thrown.
   * @throws MissingResourceException if configuration does not exist or is not set but is required.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <T> T getConfigurationValue( String pConfigurationKey, boolean pRequired, Class<T> pType )
    throws MissingResourceException {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");
    Check.checkInvalidParameterNull(pType, "pType");

    // Get property value and convert it.
    T lConfigurationValue;

    // Configuration entry is defined.
    if (configurationResource.hasConfigurationValue(pConfigurationKey) == true) {
      String lValue = this.getValueFromConfigurationResource(pConfigurationKey);

      // Configuration entry is set, so let's convert it to the requested type.
      if (lValue != null) {
        // In case that the expected type is an enumeration then we have to use a different approach as in case of
        // "normal" objects.
        if (pType.isEnum()) {
          Class<? extends Enum> lEnumType = (Class<? extends Enum>) pType;
          lConfigurationValue = (T) Enum.valueOf(lEnumType, lValue);
        }
        // In standard case we make use of datatype converters.
        else {
          DatatypeConverter<String, T> lConverter =
              XFun.getDatatypeConverterRegistry().getConverter(String.class, pType);
          lConfigurationValue = lConverter.convert(lValue);
        }
      }
      // Configuration value is defined but not set.
      else {
        // Configuration entry is optional
        if (pRequired == false) {
          lConfigurationValue = null;
        }
        // Throw MissingResourceException as mandatory configuration value is missing.
        else {
          throw this.createMissingResourceException(pConfigurationKey);
        }
      }
    }
    // Configuration value does not exist
    else {
      // Configuration entry is optional
      if (pRequired == false) {
        lConfigurationValue = null;
      }
      // Throw MissingResourceException as mandatory configuration value is missing.
      else {
        throw this.createMissingResourceException(pConfigurationKey);
      }
    }
    // Return property value.
    return lConfigurationValue;
  }

  /**
   * Method returns the value of the configuration entry with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that will be returned if the configuration entry is not defined, not set or can
   * not be converted. The parameter may be null.
   * @return T Value of the configuration entry with the passed name. The method returns the passed default value if the
   * configuration entry is not defined or not set.
   */
  @Override
  public <T> T getConfigurationValue( String pConfigurationKey, T pDefaultValue, Class<T> pType ) {
    // Lookup configuration entry
    T lConfigurationValue;
    try {
      lConfigurationValue = this.getConfigurationValue(pConfigurationKey, false, pType);
    }
    // In case of an exception during the conversion we will continue with the default value.
    catch (RuntimeException e) {
      String lMessage = "Exception occured when trying to resolve configuration value '" + pConfigurationKey + " as "
          + pType.getName();
      XFun.getTrace().error(lMessage, e);
      lConfigurationValue = null;
    }

    // Use default value of the entry does not exist.
    if (lConfigurationValue == null) {
      lConfigurationValue = pDefaultValue;
    }
    return lConfigurationValue;
  }

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. The method never returns null. If the entry is not defined
   * or not set then an empty list will be returned.
   */
  @Override
  public <T> List<T> getConfigurationValueList( String pConfigurationKey, Class<T> pType ) {
    return this.getConfigurationValueList(pConfigurationKey, false, pType);
  }

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set but
   * required then a {@link MissingResourceException} will be thrown.
   * @throws MissingResourceException if configuration does not exist or is not set but is required.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <T> List<T> getConfigurationValueList( String pConfigurationKey, boolean pRequired, Class<T> pType )
    throws MissingResourceException {

    // Check parameter
    Check.checkInvalidParameterNull(pConfigurationKey, "pConfigurationKey");
    Check.checkInvalidParameterNull(pType, "pType");

    // Configuration entry is defined.
    List<T> lConfigurationValues;
    if (configurationResource.hasConfigurationValue(pConfigurationKey) == true) {
      // Determine list of configuration values.
      List<String> lRawValues = this.getValueListFromConfigurationResource(pConfigurationKey);

      // Configuration entry is set, so let's convert it to the requested type.
      if (lRawValues.isEmpty() == false) {
        lConfigurationValues = new ArrayList<>(lRawValues.size());

        // In case that the expected type is an enumeration then we have to use a different approach as in case of
        // "normal" objects.
        if (pType.isEnum()) {
          Class<? extends Enum> lEnumType = (Class<? extends Enum>) pType;
          for (String lNextRawValue : lRawValues) {
            lConfigurationValues.add((T) Enum.valueOf(lEnumType, lNextRawValue));
          }
        }
        // In standard case we make use of datatype converters.
        else {
          // Lookup required converter.
          DatatypeConverterRegistry lRegistry = XFun.getDatatypeConverterRegistry();
          DatatypeConverter<String, T> lConverter = lRegistry.getConverter(String.class, pType);

          // Process all values.
          for (String lNextRawValue : lRawValues) {
            lConfigurationValues.add(lConverter.convert(lNextRawValue));
          }
        }
      }
      // Configuration value is defined but not set.
      else {
        // Configuration entry is optional
        if (pRequired == false) {
          lConfigurationValues = Collections.emptyList();
        }
        // Throw MissingResourceException as mandatory configuration value is missing.
        else {
          throw this.createMissingResourceException(pConfigurationKey);
        }
      }
    }
    // Configuration value does not exist
    else {
      // Configuration entry is optional
      if (pRequired == false) {
        lConfigurationValues = Collections.emptyList();
      }
      // Throw MissingResourceException as mandatory configuration value is missing.
      else {
        throw this.createMissingResourceException(pConfigurationKey);
      }
    }
    // Return property value.
    return lConfigurationValues;
  }

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that should be used in case that the configuration entry is not defined or not
   * set. The parameter may be null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set then
   * the passed default value will be returned wrapped in a list. The method never returns null. If pDefaultValue is
   * null then an empty list will be returned.
   */
  @Override
  public <T> List<T> getConfigurationValueList( String pConfigurationKey, T pDefaultValue, Class<T> pType ) {
    List<T> lConfigurationValues = this.getConfigurationValueList(pConfigurationKey, false, pType);

    // No values are configured but a "real" default value was defined.
    if (lConfigurationValues.isEmpty() == true && pDefaultValue != null) {
      lConfigurationValues = new ArrayList<>(1);
      lConfigurationValues.add(pDefaultValue);
    }
    return lConfigurationValues;
  }

  /**
   * Method returns the value of the configuration entry with the passed name as list of values. The list entries need
   * to be separated by {@link #LIST_SEPERATOR}
   * 
   * @param pConfigurationKey Name of the configuration entry whose value should be returned. The parameter must not be
   * null.
   * @param pDefaultValue Default value that should be used in case that the configuration entry is not defined or not
   * set. The parameter may be null.
   * @param pType Type as which the configuration value should be returned. The parameter must not be null.
   * @return {@link List} List with all configuration values. If the configuration entry is not defined or not set then
   * the passed default value will be returned.
   */
  @Override
  public <T> List<T> getConfigurationValueList( String pConfigurationKey, List<T> pDefaultValue, Class<T> pType ) {
    List<T> lConfigurationValues = this.getConfigurationValueList(pConfigurationKey, false, pType);

    // No values are configured but a "real" default value was defined.
    if (lConfigurationValues.isEmpty() == true && pDefaultValue != null) {
      lConfigurationValues = pDefaultValue;
    }
    return lConfigurationValues;
  }

  /**
   * Method writes trace information about the resource access provider to the trace file. The method uses INFO as trace
   * level.
   */
  public final void writeTrace( ) {
    Trace lTrace = XFun.getTrace();
    if (XFunMessages.LOADING_JEAF_PROPERTIES.isEnabled()) {
      // Trace from where all property values are loaded.
      lTrace.write(XFunMessages.LOADING_JEAF_PROPERTIES, configurationResource.getResourceName());
      lTrace.write(XFunMessages.PROPERTIES_LOCATION, configurationResource.getResourceLocation());

      // Get all property keys and sort them.
      List<String> lKeys = configurationResource.getAllConfigurationKeys();
      Collections.sort(lKeys);

      // Trace name and value of all properties.
      MessageID lMessageID = XFunMessages.TRACE_PROPERTY_VALUE;
      for (String lNextKey : lKeys) {
        lTrace.write(lMessageID, lNextKey, this.getValueFromConfigurationResource(lNextKey));
      }
    }
  }

  /**
   * Method returns the configuration value with the passed name.
   * 
   * @param pConfigurationKey Name of the configuration key whose value should be returned. The parameter must not be
   * null.
   * @return String Value of the property with the passed name. The method returns null if the property is not required
   * and not defined or set. In the case that the property is required the method never returns null. The returned value
   * will be trimmed using <code>java.lang.String.trim()</code>.
   * @throws MissingResourceException if the property with the passed name does not exist within the resource or if the
   * property is required but not set.
   */
  private String getValueFromConfigurationResource( String pConfigurationKey ) throws MissingResourceException {
    // Check parameter.
    Assert.assertNotNull(pConfigurationKey, "pPropertyName");

    // Get property value and trim it. A zero length string will be set to null.
    String lPropertyValue = configurationResource.getConfigurationValue(pConfigurationKey);

    // Check if String object is empty.
    if (lPropertyValue != null) {
      lPropertyValue = lPropertyValue.trim();
      if (lPropertyValue.length() > 0) {
        lPropertyValue = this.replaceSystemProperties(lPropertyValue);
      }
      // String is empty, so set property value to null.
      else {
        lPropertyValue = null;
      }
    }

    return lPropertyValue;
  }

  private List<String> getValueListFromConfigurationResource( String pConfigurationKey ) {
    String lValue = this.getValueFromConfigurationResource(pConfigurationKey);

    List<String> lValueList;
    if (lValue != null) {
      StringTokenizer lTokenizer = new StringTokenizer(lValue, LIST_SEPERATOR);
      lValueList = new ArrayList<>(lTokenizer.countTokens());

      // Add all parts to the created collection.
      while (lTokenizer.hasMoreTokens()) {
        lValueList.add(lTokenizer.nextToken().trim());
      }
    }
    // Configuration entry is not set.
    else {
      lValueList = Collections.emptyList();
    }
    return lValueList;
  }

  /**
   * Method replaces may be existing place holders for system properties inside the passed string. Place holders are
   * defined by a leading '${' and an ending '}'.
   * 
   * @param pValue String inside which system properties should be replaced.
   * @return String with replaced system properties. If no system properties are defined inside the string then a same
   * string will be returned.
   */
  private String replaceSystemProperties( String pValue ) {
    // The passed value must have a minimum length of 4 character in oder to be able to contain a system property e.g.
    // '${a}'
    String lResult;
    if (pValue != null && pValue.length() > 3) {
      int lCurrentIndex = 0;
      StringBuilder lBuilder = new StringBuilder();
      while (true) {
        int lStartIndex = pValue.indexOf("${", lCurrentIndex);
        if (lStartIndex >= 0) {
          // Look for end token
          int lEndIndex = pValue.indexOf("}", lStartIndex);
          if (lEndIndex - 3 > lStartIndex) {
            // Resolve name of system property that should be replaced.
            String lSubstring = pValue.substring(lCurrentIndex, lStartIndex);
            String lPropertyName = pValue.substring(lStartIndex + 2, lEndIndex);
            String lPropertyValue = System.getProperty(lPropertyName);
            if (lPropertyValue != null) {
              lBuilder.append(lSubstring);
              lBuilder.append(lPropertyValue);
            }
            // We can not substitute the placeholder. System property is not set.
            else {
              String lMessage =
                  "Unable to substitue placeholder for system property " + lPropertyName + ". Property is not set.";
              XFun.getTrace().warn(lMessage);
              lBuilder.append(pValue.substring(lCurrentIndex, lEndIndex + 1));
            }
            lCurrentIndex = lEndIndex + 1;
          }
          // Invalid system property definition.
          else {
            lBuilder.append(pValue);
            break;
          }
        }
        // Nothing to replace (any more).
        else {
          lBuilder.append(pValue.substring(lCurrentIndex, pValue.length()));
          break;
        }
      }
      lResult = lBuilder.toString();
    }
    // Passed value can not contain any system properties that should be replaced.
    else {
      lResult = pValue;
    }
    return lResult;
  }

  private MissingResourceException createMissingResourceException( String pConfigurationKey ) {
    String lResourceName = configurationResource.getResourceName();
    Assert.assertNotNull(lResourceName, "lResourceName");

    // Create new MissingResourceException and throw it.
    StringBuilder lMessage = new StringBuilder();
    lMessage.append("Required property with the name '");
    lMessage.append(pConfigurationKey);
    lMessage.append("' is not set.");
    return new MissingResourceException(lMessage.toString(), lResourceName, pConfigurationKey);
  }
}
