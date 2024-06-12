package com.ssm.llp.base.common.db;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet Filter to implement the OpenSession-In-View pattern with Managed Session Context introduced in Hibernate 3.x.
 * The filter obtains Hibernate's {@code SessionFactory} from a Spring bean named {@code sessionFactory}.
 * <br/>
 * @see com.ssm.base.common.web.StripesHibernateInterceptor<br/>
 * {@link http://www.hibernate.org/43.html}<br/>
 * {@link http://www.hibernate.org/42.html}
 * @author yee
 *
 */
public class HibernateSessionManagerFilter implements Filter
{
	private static Logger logger = LoggerFactory.getLogger(HibernateSessionManagerFilter.class);

	private SessionFactory sessionFactory;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException,	ServletException
	{
		Session currentSession = null;

        currentSession = sessionFactory.openSession();
        currentSession.setFlushMode(FlushMode.COMMIT);

//        logger.debug("Binding the current Session");
        ManagedSessionContext.bind(currentSession);

		// Call the next filter (continue request processing)
		chain.doFilter(request, response);
		
    	Transaction tx = currentSession.getTransaction();
		try {

        	if (tx.isActive()) {
                logger.debug("rollback active transaction");
            	tx.rollback();
            }
        	//logger.debug("Flushing the session");
        	//currentSession.flush();
			
        } catch (Throwable ex) {
            logger.error("Could not rollback transaction after exception!", ex);

            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);

        } finally {
//            logger.debug("Closing and unbinding the Session after processing");
            currentSession.close();
            currentSession = ManagedSessionContext.unbind(sessionFactory);
        }
        
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		// Obtain the session factory from Spring's Application Context.
		// If not using Spring to configure Session Factory, it has to be obtained
		// from somewhere else, for example HibernateUtil
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		sessionFactory = (SessionFactory) ctx.getBean("sessionFactory");
	}

	public void destroy()
	{
	}
}
