package net.ko.creator.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.ko.creator.Activator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class KZip {
	public static ZipInputStream getInputStream(String srcLocationInJar,String srcFileName) throws URISyntaxException, IOException{
		Bundle bundle=Platform.getBundle(Activator.PLUGIN_ID);
		Path path = new Path(srcLocationInJar);
		URL fileURL = FileLocator.find(bundle, path, null);
		URL url=new URL(fileURL.toURI().toString()+srcFileName);
		InputStream is=url.openStream();
		ZipInputStream zis = new ZipInputStream(is);
		return zis;
	}
	public static void unzipFromRessource(String srcLocationInJar,String srcFileName,String folderDest) throws URISyntaxException{
		byte[] buffer = new byte[1024];

		try{

			File folder = new File(folderDest);
			if(!folder.exists()){
				folder.mkdir();
			}

			ZipInputStream zis = getInputStream(srcLocationInJar,srcFileName);

			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){

				String fileName = ze.getName();
				File newFile = new File(folderDest + File.separator + fileName);
				new File(newFile.getParent()).mkdirs();
				if(ze.isDirectory())
					newFile.mkdir();
				else{
					FileOutputStream fos = new FileOutputStream(newFile);
	
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
	
					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

		}catch(IOException ex){
			ex.printStackTrace(); 
		}
	}
}
