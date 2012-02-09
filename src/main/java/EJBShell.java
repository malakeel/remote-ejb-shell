//package org.apache.openejb.tools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import jline.ANSIBuffer;
import jline.ClassNameCompletor;
import jline.ConsoleReader;
import jline.FileNameCompletor;

import org.apache.openejb.client.JNDIContext;
import org.apache.openejb.tools.ContextConfigurations;
import org.codehaus.groovy.tools.shell.CommandsMultiCompletor;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.ReflectionCompletor;

public class EJBShell {

	private Context context;

	public EJBShell() throws IOException {

		int count = 3;
		ContextConfigurations prop;
		prop = new ContextConfigurations();
		
		while (count > 0)
			try {

				prop.loadContextConfig("ejb-shell.properties");
				this.context = new InitialContext(prop);
				break;
			} catch (Exception e) {
				prop.error(e.getMessage());
				count--;
			}

		if (count == 0) {
			quit();
		}
	}

	private void quit() {
		String msg = (new ANSIBuffer()).green("Leaving .... Good bye!")
				.toString();
		System.err.println(msg);
		System.out.println();
		System.exit(0);
	}

	public static void main(String[] args) throws NamingException, IOException {
		EJBShell myShell = new EJBShell();
		myShell.startShell();
	}

	void startShell() throws NamingException, IOException {

		groovy.lang.Binding binding = new groovy.lang.Binding();

		this.bindAll("", this.context, binding);

		IO io = new IO(System.in, System.out, System.err);
		Groovysh shell = new Groovysh(binding, io);

		ConsoleReader reader = new ConsoleReader(io.inputStream,
				new PrintWriter(io.outputStream));

		reader.addCompletor(new ReflectionCompletor(shell));
		reader.addCompletor(new CommandsMultiCompletor());
		reader.addCompletor(new ClassNameCompletor());
		reader.addCompletor(new FileNameCompletor());
		reader.addCompletor(new jline.MultiCompletor());

		shell.run();
		quit();
	}

	private void bindAll(final String path, Context ctx,
			groovy.lang.Binding bdng) throws NamingException {
		NamingEnumeration<Binding> listBindings = ctx.listBindings("");
		String name = "";
		Object val = null;
		while (listBindings.hasMoreElements()) {
			Binding pair = listBindings.nextElement();
			name = pair.getName();
			val = pair.getObject();
			if (name.trim().equals("."))
				continue;
			System.out.println(name + "=" + val.getClass().getName());
			bdng.setVariable(path + name, val);
			if (val instanceof JNDIContext) {
				bindAll(name + "_", (JNDIContext) val, bdng);
			}
		}
	}
}
