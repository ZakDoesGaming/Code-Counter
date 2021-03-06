package net.gogo98901.codeCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import net.gogo98901.util.log.Log;

public class Searcher {
	private List<String> paths = new ArrayList<String>();
	private String path;

	private int lines, whiteSpace, files, dir;

	private List<String> exclude = new ArrayList<String>();

	public Searcher() {
		path = "";
		exclude.add(".png");
		exclude.add(".psd");
		exclude.add(".db");
		exclude.add(".ttf");
		exclude.add(".dll");
		exclude.add(".class");
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void search() {
		File filePath = new File(path);
		if (!filePath.exists()) {
			Log.info("Searching... Failed");
			return;
		}
		paths.clear();
		lines = 0;
		whiteSpace = 0;
		files = 0;
		dir = 0;

		File[] start = filePath.listFiles();
		scan(start);
		files = paths.size();
		Log.info("Searching... Finished");
		Log.info("Found " + files + " files and " + dir + " directories");
		Log.info(lines + " lines, but with " + whiteSpace + " white space lines");
		Log.info("");
	}

	private void scan(File[] group) {
		dir++;
		for (File file : group) {
			Log.info(file);
			if (file.isDirectory()) scan(file.listFiles());
			else if (check(file.getName())) {
				count(file);
				paths.add(file.getAbsolutePath());
			}
		}
	}

	private void count(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String data;
			while ((data = br.readLine()) != null) {
				if (data.length() == 0) whiteSpace++;
				lines++;
			}
			br.close();
		} catch (Exception e) {
			Log.warn("Could not read '" + file.getAbsolutePath() + "'");
			Log.stackTrace(e);
		}
	}

	private boolean check(String file) {
		for (String type : exclude) {
			if (file.toLowerCase().endsWith(type)) return false;
		}
		return true;
	}

	public List<String> getPaths() {
		return paths;
	}

	public String getPath() {
		return path;
	}

	public int getLines() {
		return lines;
	}

	public int getWhiteSpace() {
		return whiteSpace;
	}

	public int getFiles() {
		return files;
	}

	public int getDirs() {
		return dir;
	}
}
