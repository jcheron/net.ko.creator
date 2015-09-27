package net.ko.creator.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.ko.creator.WorkbenchUtils;
import net.ko.utils.KProperties;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class FrameworkUtils {
	public static String getBaseFolder() {
		String result = "";
		if (WorkbenchUtils.isDynamicWebProject())
			result = "WebContent/";
		return result;
	}

	public static KProperties koConfigFile() throws IOException {
		KProperties result = new KProperties();
		result.loadFromFile(getCompleteFileName("config.ko"));
		return result;
	}

	public static String getCompleteFileName(String fileName) {
		fileName = getBaseFolder() + fileName;
		IFile f = WorkbenchUtils.getActiveProject().getFile(fileName);
		return f.getLocationURI().getPath();
	}

	public static String getFileContent(File aFile) throws CoreException, IOException {
		InputStream in = aFile.getContents();
		InputStreamReader is = new InputStreamReader(in);
		boolean fileUpdated = false;
		String content = convertStreamToString(is);
		is.close();
		return content;
	}

	public static String convertStreamToString(InputStreamReader is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		s.close();
		return result;
	}

	public static String getOSPath(String path) {
		Path path2 = new Path(path);
		return path2.toOSString();
	}

	public static String getPortablePath(String path) {
		Path path2 = new Path(path);
		return path2.toPortableString();
	}
}
