/**
 * Copyright 2004 - 2019 anaptecs GmbH, Burgstr. 96, 72764 Reutlingen, Germany
 *
 * All rights reserved.
 */
package com.anaptecs.jeaf.xfun.test.runtime;

import com.anaptecs.jeaf.xfun.annotations.ConfigurationProviderConfig;
import com.anaptecs.jeaf.xfun.annotations.RuntimeInfo;
import com.anaptecs.jeaf.xfun.annotations.TraceConfig;
import com.anaptecs.jeaf.xfun.api.info.RuntimeEnvironment;
import com.anaptecs.jeaf.xfun.fallback.info.InfoProviderFactoryImpl;
import com.anaptecs.jeaf.xfun.fallback.trace.FallbackTraceProviderFactoryImpl;

@com.anaptecs.jeaf.xfun.annotations.XFunConfig(
    verifierFactory = VerifierFactoryImpl.class,
    infoProviderFactory = InfoProviderFactoryImpl.class,
    messageRepositoryFactory = MessageRepositoryFactoryImpl.class,
    configurationProviderFactory = ConfigurationProviderFactoryImpl.class,
    localeProviderFactory = LocaleProviderFactoryImpl.class,
    principalProviderFactory = PrincipalProviderFactoryImpl.class,
    datatypeConverterRegistryFactory = DatatypeConverterRegistryFactoryImpl.class,
    traceProviderFactory = FallbackTraceProviderFactoryImpl.class)

@ConfigurationProviderConfig(
    componentConfigurationResourceFactory = ComponentConfigurationResourceFactoryImpl.class,
    environmentConfigurationResourceFactory = EnvironmentConfigurationResourceFactoryImpl.class,
    fileConfigurationResourceFactory = FileConfigurationResourceFactoryImpl.class,
    resourceBundleConfigurationResourceFactory = ResourceBundleConfigurationResourceFactoryImpl.class,
    resourceConfigurationResourceFactory = ResourceConfigurationResourceFactoryImpl.class,
    systemPropertiesConfigurationResourceFactory = SystemPropertiesConfigurationResourceFactoryImpl.class)

@TraceConfig

@RuntimeInfo(runtimeEnvironment = RuntimeEnvironment.JSE)
public interface XFunConfig {
}
