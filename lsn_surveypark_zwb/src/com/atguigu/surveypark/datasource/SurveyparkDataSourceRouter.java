package com.atguigu.surveypark.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义数据源路由器(分布式数据库)
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource {

	/**
	 * 检测当前key
	 */
	protected Object determineCurrentLookupKey() {
		// 得到当前线程绑定的令牌
		SurveyparkToken token = SurveyparkToken.getCurrentToken();
		if (token != null) {
			Integer id = token.getSurvey().getId();
			// 解除令牌的绑定      //保存完答案之后令牌需要接触：
			SurveyparkToken.unbindToken();
			return ((id % 2) == 0) ? "even" : "odd";
		}
		return null;
	}
}