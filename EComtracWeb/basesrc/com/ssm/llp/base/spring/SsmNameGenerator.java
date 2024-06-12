/**
 * 
 */
package com.ssm.llp.base.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * @author zamzam
 *
 */
public class SsmNameGenerator implements BeanNameGenerator{

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.support.BeanNameGenerator#generateBeanName(org.springframework.beans.factory.config.BeanDefinition, org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public String generateBeanName(BeanDefinition beanDefinition,
			BeanDefinitionRegistry arg1) {
		String className = beanDefinition.getBeanClassName();
		className = className.substring(className.lastIndexOf(".")+1);
		className = StringUtils.replace(className, "Impl", "");
		return className;
	}

}