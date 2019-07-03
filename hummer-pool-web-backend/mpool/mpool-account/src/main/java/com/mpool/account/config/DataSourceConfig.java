package com.mpool.account.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.StringUtils;

import com.zaxxer.hikari.HikariDataSource;

//@Configuration
//@ConfigurationProperties(prefix = "sharding.datasource")
/**
 * 弃用
 * 
 * @author chenjian2
 *
 */
@Deprecated
public class DataSourceConfig {
	private static final String HIKARI = "com.zaxxer.hikari.HikariDataSource";
	private static final String TOMCAT = "org.apache.tomcat.jdbc.pool.DataSource";
	private String master;
	private List<String> slaves;
	private List<String> names;
	private Map<String, DataSourceProperties> dataSourceProperties;

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public List<String> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<String> slaves) {
		this.slaves = slaves;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public Map<String, DataSourceProperties> getDataSourceProperties() {
		return dataSourceProperties;
	}

	public void setDataSourceProperties(Map<String, DataSourceProperties> dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	public Map<String, DataSource> buildDataSourceMap() {
		if (dataSourceProperties == null || dataSourceProperties.isEmpty()) {

		}
		Map<String, DataSource> datasourceMap = new HashMap<>();

		names.stream().forEach(name -> {
			DataSourceProperties properties = dataSourceProperties.get(name);
			if (properties.getType() == null || properties.getType().getName().equals(HIKARI)) {
				HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
				if (StringUtils.hasText(properties.getName())) {
					dataSource.setPoolName(properties.getName());
				}
				datasourceMap.put(name, dataSource);
			}
		});
		return datasourceMap;
	}

	@SuppressWarnings("unchecked")
	private <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataSourceConfig [master=").append(master).append(", slaves=").append(slaves).append(", names=")
				.append(names).append(", dataSourceProperties=").append(dataSourceProperties).append("]");
		return builder.toString();
	}

}
