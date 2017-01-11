package com.san.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/****
 * Class to frame all HTTP methods****
 * 
 * @author Santosh
 *
 */
public abstract class BaseServiceConsumer {
	// public BaseServiceConsumer() {
	// super();
	// }

	String endPointURL = null;
	Invocation.Builder invocationBuilder = null;
	public boolean REQ_HEADERS = true;
	public boolean DEFAULT_HEADERS = true;
	public String PROPERTIES_FILE_NAME = "services-endpoints.properties";
	Properties properties = null;
	protected boolean MULTI_FORM_DATA = false;
	Client client = null;
	MultivaluedMap<String, Object> multivaluedMap;

	/**
	 * create builder instance by service endpoint URL.
	 *
	 * @param url
	 */
	protected void prepareInvocation(String url, boolean DEFHEADERS) {
		MultivaluedMap<String, Object> multivaluedMap = null;
		if (MULTI_FORM_DATA)
			// multiform-data type requests
			client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		else
			// for application/json type requests
			client = ClientBuilder.newClient();
		client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		client.register(CustomObjectMapperContextResolver.class);
		WebTarget target = client.target(url);
		invocationBuilder = target.request();
		// headers not required !!!
		if (!REQ_HEADERS)
			return;
		// setting headers
		else if (REQ_HEADERS && DEFHEADERS) {
			if (this.properties == null)
				// loading header properties
				this.properties = readingProperties();
			multivaluedMap = getHeaders();
			this.invocationBuilder = invocationBuilder.headers(multivaluedMap);
		} else {
			multivaluedMap = getHeaders();
			this.invocationBuilder = invocationBuilder.headers(multivaluedMap);
		}

	}

	/**
	 * GET operation call
	 *
	 * @return
	 */
	protected Response executeGET(String URL, boolean DEFAULT_HEADERS) {
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.get();
	}

	/**
	 * POST operation call with entity body
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePOST(String URL, boolean DEFAULT_HEADERS, Entity<?> entity) {
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.post(entity);
	}

	/**
	 * POST operation call with entity body and MULTI_FORM_DATA for uploading
	 * file as pay load
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePOST(String URL, boolean DEFAULT_HEADERS, Entity<?> entity, boolean MULTI_FORM_DATA) {
		this.MULTI_FORM_DATA = MULTI_FORM_DATA;
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.post(entity);
	}

	/**
	 * DELETE operation call
	 *
	 * @return
	 */
	protected Response executeDELETE(String URL, boolean DEFAULT_HEADERS) {
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.delete();
	}

	/**
	 * PUT operation call with entity
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePUT(String URL, boolean DEFAULT_HEADERS, Entity<?> entity) {
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.put(entity);
	}

	/**
	 * PATCH operation call with entity
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePATCH(String URL, boolean DEFAULT_HEADERS, Entity<?> entity) {
		prepareInvocation(URL, DEFAULT_HEADERS);
		return invocationBuilder.method("PATCH", entity);
	}

	/**
	 * Setting headers
	 */
	private MultivaluedMap<String, Object> getHeaders() {
		multivaluedMap = new MultivaluedHashMap<String, Object>();

		// Set<Object> keys = properties.keySet();
		// String headerKeys[] = { "realmName", "Authorization", "Content-Type",
		// "Accept", "flowid" };
		// for (Object k : headerKeys) {
		// String key = (String) k;
		// String value = getPropertyValue(key);
		// multivaluedMap.putSingle(key, value);
		// }
		return multivaluedMap;

	}

	/**
	 * Reading properties file from src/main/resources
	 *
	 * @return
	 */
	protected Properties readingProperties() {
		InputStream is = null;
		properties = new Properties();
		is = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE_NAME);
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Reading properties file from src/main/resources
	 *
	 * @return
	 * @throws IOException
	 */
	protected void removingProperty(String key) {
		try {
			// File file = new File("./src/main/resources/" +
			// PROPERTIES_FILE_NAME);
			properties.remove(key);
			// OutputStream out = new FileOutputStream(file);
			// properties.store(out, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Based on property name ,will get the corresponding value from properties
	 * file..
	 *
	 * @param name
	 * @return
	 */
	private String getPropertyValue(String name) {
		if (properties == null) {
			throw new RuntimeException(" properties instance is not invoked");
		}
		String value = properties.getProperty(name);
		return value;

	}

	/**
	 * Setting properties
	 *
	 * @param name
	 * @param value
	 */
	public void setPropertyValue(String name, String value) {
		if (properties == null) {
			throw new RuntimeException(" properties instance is not invoked");
		}
		properties.setProperty(name, value);

	}

	private void setDynamicProperties() {
		if (this.properties == null) {
			this.properties = readingProperties();
			multivaluedMap = getHeaders();
		}
	}

}
