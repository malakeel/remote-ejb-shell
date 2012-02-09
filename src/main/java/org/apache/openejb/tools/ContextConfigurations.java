package org.apache.openejb.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import jline.ANSIBuffer;
import jline.ConsoleReader;


public class ContextConfigurations extends Properties {

	private synchronized void load(File file) throws IOException {
		super.load(new FileReader(file));
	}

	public void loadContextConfig(String fileName) throws IOException {
		this.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.openejb.client.RemoteInitialContextFactory");
		this.put(Context.PROVIDER_URL, "ejbd://localhost:4201");

		// load the file to over write default properties
		this.load(new File(fileName));
		
		String username = (new ConsoleReader()).readLine("UserName:  ");
		String password = (new ConsoleReader()).readLine("Password:  ",
				new Character('*'));

		this.put("java.naming.security.principal", username);
		this.put("java.naming.security.credentials", password);
	}

	public void reload() {

	}

	public void error(String msg) {
		msg = (new ANSIBuffer()).red(msg).toString();
		System.out.println(msg);
		System.out.println();
	}

	public void warn(String msg) {
		msg = (new ANSIBuffer()).yellow(msg).toString();
		System.out.println(msg);
		System.out.println();
	}

	public void info(String msg) {
		msg = (new ANSIBuffer()).green(msg).toString();
		System.out.println(msg);
		System.out.println();
	}

}
