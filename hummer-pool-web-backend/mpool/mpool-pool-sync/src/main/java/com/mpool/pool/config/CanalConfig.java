package com.mpool.pool.config;

import java.net.InetSocketAddress;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

@Configuration
@ConfigurationProperties(prefix = "com.mpool.canal")
public class CanalConfig {

	private static final Logger log = LoggerFactory.getLogger(CanalConfig.class);

	/**
	 * Canal 服务地址
	 */
	protected String serveraddress = "localhost";
	/**
	 * Canal 端口号
	 */
	protected int port = 11111;
	/**
	 * destination
	 */
	protected String destination = "zec_example";
	protected String username = "";
	protected String password = "";
	@NotNull
	protected String regionId;

	protected String subscribe;
	protected Integer batchSize = 128;

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getServeraddress() {
		return serveraddress;
	}

	public void setServeraddress(String serveraddress) {
		this.serveraddress = serveraddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public CanalConnector getCanalConnector() {
		if (this.regionId == null) {
			throw new RuntimeException("regionId is null");
		}
		if (log.isDebugEnabled()) {
			log.debug("canal config -> {}", this.toString());
		}
		return CanalConnectors.newSingleConnector(new InetSocketAddress(this.serveraddress, this.port),
				this.destination, this.username, this.password);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CanalConfig [serveraddress=").append(serveraddress).append(", port=").append(port)
				.append(", destination=").append(destination).append(", username=").append(username)
				.append(", password=").append(password).append("]");
		return builder.toString();
	}

}
