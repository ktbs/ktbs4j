package org.liris.ktbs.test.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class KtbsServer {
	
	public static void main(String[] args) throws Exception {
		KtbsServer server = KtbsServer.newInstance("http://localhost:8001/", System.out);
		server.start();
		server.populateKtbs();
		server.populateT01();
	}
	
	public static final String KTBS_HOME = "/home/damien/Applications/ktbs/";
	
	public static final String KTBS_COMMAND = KTBS_HOME + "bin/ktbs";
	public static final String POPULATE_KTBS_COMMAND = KTBS_HOME + "tests/populate-ktbs";
	public static final String POPULATE_T01_COMMAND = KTBS_HOME + "tests/populate-t01";

	public static KtbsServer newInstance(String rootUri, PrintStream out) {
		return new KtbsServer(rootUri, out);
	}
	
	private String rootUri;
	private PrintStream out;
	private Process ktbsProcess;
	
	private KtbsServer(String rootUri, PrintStream out) {
		super();
		this.rootUri = rootUri;
		this.out = out;
	}

	private class ProcessWatcher implements Runnable {

		private Process p;
		private volatile boolean finished = false;

		public ProcessWatcher(Process p) {
			this.p = p;
			new Thread(this).start();
		}

		public boolean isFinished() {
			return finished;
		}

		public void run() {
			try {
				p.waitFor();
			} catch (Exception e) {}
			finished = true;
		}
	}

	private Thread shutdownHook = new Thread() {
		@Override
		public void run() {
			ktbsProcess.destroy();
		}
	};

	private Thread checkServerStarted = new Thread() {
		@Override
		public void run() {
			try {
				int statusCode = HttpStatus.SC_NOT_FOUND;
				boolean requestOk = true;
				HttpClient defaultHttpClient = new DefaultHttpClient();
				do {
					Thread.sleep(100);
					HttpGet get = new HttpGet(rootUri);
					HttpResponse response = null;
					try {
						response = defaultHttpClient.execute(get);
						requestOk = true;
					} catch (Exception e) {
						requestOk = false;
					}
					if(response != null)
					statusCode = response.getStatusLine().getStatusCode();
				} while(!requestOk || statusCode != HttpStatus.SC_OK);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	private Thread displayOutput = new Thread() {
		public void run() {
			ProcessWatcher pw = new ProcessWatcher(ktbsProcess);
			InputStream output = ktbsProcess.getInputStream();
			while(!pw.isFinished()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(output));
				String line;
				try {
					while((line = reader.readLine()) != null) {
						out.println(line);
					}
					Thread.sleep(500);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	};
	
	public void stop() {
		ktbsProcess.destroy();
	}
	
	public void start() throws Exception {
		ProcessBuilder builder = new ProcessBuilder(KTBS_COMMAND,"-4");
		builder.redirectErrorStream(true);
		ktbsProcess = builder.start();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		displayOutput.start();
		checkServerStarted.start();
		checkServerStarted.join();
	}

	public void populateKtbs() throws Exception {
		executeAndWait(POPULATE_KTBS_COMMAND);
	}

	public void populateT01() throws Exception {
		executeAndWait(POPULATE_T01_COMMAND);
	}

	private void executeAndWait(String command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectErrorStream(true);
		Process popKtbs = builder.start();
		popKtbs.waitFor();
	}
}
