package org.apache.openejb.tools;

import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.openejb.assembler.Deployer;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;

public class OpenEJBTask extends Task {

	private DirSet files;
	private String username = "admin";
	private String password = "admin";

	private Properties getProperties() {
		Properties config = new Properties();

		config.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.RemoteInitialContextFactory");

		config.put(Context.PROVIDER_URL, "ejbd://localhost:4201");
		config.put("java.naming.security.principal", this.username);
		config.put("java.naming.security.credentials", this.password);

		return config;
	}

	public void setUsername() {

	}

	public void addConfiguredDeployPaths(DirSet deployPaths) {
		this.files = deployPaths;
	}

	public void execute() {
		try {

			Properties prop = getProperties();
			Context context = new InitialContext(prop);

			Object obj = context.lookup("openejb/Deployer");
			Deployer deployer = (Deployer) obj;

			Iterator<Resource> it = files.iterator();

			while (it.hasNext()) {
				Resource resource = it.next();
				resource.getLocation().getFileName();
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
