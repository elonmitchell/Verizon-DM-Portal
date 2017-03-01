package motive.reports.reportconsole;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class CustomJNDIHelper {
	
	  private static final Logger logger = Logger.getLogger(CustomJNDIHelper.class);
	  private static final String REPORT_MANAGER_MODULE_NAME = "report-manager-service-ejb";

	  private static InitialContext context = null;

	  private static Map<String, String> mappings = new ConcurrentHashMap();

	  private static Map<String, Object> rInterfaceMap = new ConcurrentHashMap();

	  private static String getJNDIName(Class localInterface)
	    throws Exception
	  {
	    String jndiName = (String)mappings.get(localInterface.getName());
	    if (jndiName == null) {
	      logger.debug("JNDI name not found in the cache, name:" + localInterface.getName());
	      Class c = Class.forName(localInterface.getName());
	      Field f = c.getDeclaredField("JNDI_NAME");
	      jndiName = (String)f.get(null);
	      mappings.put(localInterface.getName(), jndiName);
	    }

	    if (logger.isDebugEnabled()) {
	      logger.debug("Interface:" + localInterface.getName() + ", jndiName:" + jndiName);
	    }
	    return jndiName;
	  }

	  private static String getShortInterfaceName(String jndiName)
	    throws Exception
	  {
	    int last = jndiName.lastIndexOf("/");
	    return jndiName.substring(last + 1);
	  }

	  private static Object lookup(InitialContext ctx, String jndiName) throws Exception
	  {
	    if (logger.isDebugEnabled()) {
	      logger.debug("JNDI lookup, jndiName:" + jndiName);
	    }
	    return  ctx.lookup(jndiName);
	  }
	  
	  public static Object getLocalInterface(Class localInterface) {
		  return getLocalInterface(localInterface, REPORT_MANAGER_MODULE_NAME);
	  } 

	  public static Object getLocalInterface(Class localInterface, String moduleName)
	  {
	    String jndiName = null;
		logger.info("............ JNDI helper [ report-console ] .............");
	    try {
	      jndiName = getJNDIName(localInterface);
	      logger.info("jndiName: " + jndiName);
	      logger.info("localInterface: " + localInterface.getName());
	      if (context != null) {
	        logger.info("Returning interface from injected InitialContext");
	        String openEJBJNDIName = getShortInterfaceName(jndiName) + "ImplLocal";
	        System.out.println("JNDIHelper: openEJB local JNDI name:" + openEJBJNDIName);
	        return lookup(context, openEJBJNDIName);
	      }
	      Properties prop = new Properties();
	      InputStream in = CustomJNDIHelper.class.getResourceAsStream("appname.properties");
	      prop.load(in);
	      in.close();
	      InitialContext ctx = new InitialContext();
	      //String localJNDIName = "java:comp/env";
	      String localJNDIName = prop.getProperty("reporting.app.name");
	      logger.info("localJNDIName: " + localJNDIName);
	      //String moduleName = "report-manager-service-ejb";
	      String beanName = localInterface.getSimpleName();
	      String distinctName = "";
	      final String interfaceName = localInterface.getName();
	      if (jndiName.startsWith("/"))
	        //localJNDIName = localJNDIName + jndiName;
	    	  localJNDIName = "java:global/" + localJNDIName + moduleName + "/" +
	                distinctName    + "/" + beanName + "!" + interfaceName;
	      else {
	        //localJNDIName = localJNDIName + "/" + jndiName;
	        
	        localJNDIName = "java:global/" + localJNDIName + "/" + moduleName + "/" +
	                distinctName    + "/" + beanName + "!" + interfaceName;
	        
	        
	      }
			logger.info("localJNDIName: " + localJNDIName);
			logger.info("...........................................................");
	      return lookup(ctx, localJNDIName);
	    }
	    catch (Throwable t)
	    {
	      logger.info("Can not lookup local interface :" + localInterface.getName(), t);

	      logger.info("Returning Remote Interface, add the local interface definition to web.xml or ejb-jar.xml");
	    }
	    return getRemoteInterface(localInterface);
	  }

	  public static Object getRemoteInterface(Class remoteInterface)
	    {
	    	Object t = null;
	      try
	      {
	        String jndiName = getJNDIName(remoteInterface);
	         t = rInterfaceMap.get(jndiName);
	        if (t == null) {
	          if (context != null) {
	            logger.info("Returning remote interface from injected InitialContext");
	            t = lookup(context, jndiName);
	            rInterfaceMap.put(jndiName, t);
	            return t;
	          }

	          InitialContext ctx = new InitialContext();
	          t = lookup(ctx, jndiName + "#" + remoteInterface.getName());
	          rInterfaceMap.put(jndiName, t);
	          return t;
	        }

	        
	      } catch (Throwable e) {
	        logger.error("Can not lookup remote interface :" + remoteInterface.getName(), e);
	      }finally{
	    	  return t;
	      }//throw new RuntimeException(e);
	    }

	  public static void setInitialContext(InitialContext ctx)
	  {
	    context = ctx;
	  }
}

