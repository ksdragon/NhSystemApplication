package com.skoneczny.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.passay.AbstractMessageResolver;

public class MyPropertiesMessageResolver extends AbstractMessageResolver {

	static Locale locale = Locale.getDefault();
	public static final String nameLocalePropertiesFile = "/messages_" + locale.getLanguage() + ".properties";

	/** Classpath location of default message map. */
	public static final String DEFAULT_MESSAGE_PATH = nameLocalePropertiesFile;

	/** Maps message keys to message strings. */
	private final Properties messageProperties;

	/** Creates a new message resolver with the default message map. */
	public MyPropertiesMessageResolver() {
		this(getDefaultProperties());
	}

	/**
	 * Creates a new message resolver with the supplied message map.
	 *
	 * @param properties map of keys to messages.
	 */
	public MyPropertiesMessageResolver(final Properties properties) {
		if (properties == null) {
			throw new IllegalArgumentException("Properties cannot be null.");
		}
		messageProperties = properties;
	}

	@Override
	protected String getMessage(final String key) {
		return messageProperties.getProperty(key);
	}

	/**
	 * Returns the default mapping of message keys to message strings.
	 *
	 * @return default message mapping.
	 */
	public static Properties getDefaultProperties() {
		final Properties props = new Properties();
		InputStream in = null;
		try {
			in = MyPropertiesMessageResolver.class.getResourceAsStream(DEFAULT_MESSAGE_PATH);
			props.load(in);
		} catch (Exception e) {
			throw new IllegalStateException("Error loading default message properties.", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}

}
