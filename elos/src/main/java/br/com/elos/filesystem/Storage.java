package br.com.elos.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.elos.App;
import br.com.elos.helpers.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class Storage {

	private boolean status;
	private App app;
	private String filePath;
	private File file;
	private ServletFileUpload upload;
	private String fileName;
	private String filePersistName;
	private String extension;
	private long size;
	
	public Storage() {
		this.status = false;
		this.app = App.getInstance();
	}
	
	public boolean isbStatus() {
        return status;
    }
	
	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePersistName() {
		return filePersistName;
	}

	public String getExtension() {
		return extension;
	}

	public long getSize() {
		return size;
	}

	private void urlResolve(String url) {
		if (url == null) {
			return;
		}
		
		if (url.startsWith("/")) {
			url = url.substring(1);
		}
		
		if (!url.endsWith("/")) {
			url += "/";
		}
		
		this.filePath += url;
	}
	
	private void buildPath(String path) {
		//this.urlResolve(app.server_url);
		//this.urlResolve(app.storage_location);
		this.filePath = app.storage_location;
		this.urlResolve(path);
		this.file = new File(this.filePath);
	}
	
	private boolean isValidExtension(String fileName) {
		if (fileName == null) {
            return false;
        }
		
		String extension = this.getExtension(fileName);
        
        for (String extensionPerm : app.storage_extensions) {
            if (extensionPerm.equals(extension)) {
            	return true;
            }
        }
        
        return false;
    }
	
	public boolean isValidSize(long size) {
		return (app.storage_maxsize == 0)
		    || (size <= app.storage_maxsize);	
	}
	//implementar function
	public boolean isValidFile(HttpServletRequest request, String fileName) {
		if ((request == null) || (fileName == null)) {
			return false;
		}
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			return false;
		}
		
		return false;
    }
	
	public long bytesToMegabytes(long bytes) {
		return (bytes / 1048576);
	}
	
	public long megabytesToBytes(long megabytes) {
		return (size / 1048576);
	}
	
	private String generateName(String fileName) {
		return (this.app.storage_generateuuid == false) ? fileName : new Util().random(40) + "." + this.getExtension(fileName);
	}
	
	private String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }
	
	private void uploadFactory() {
		DiskFileItemFactory diskFactory = new DiskFileItemFactory();
		diskFactory.setRepository(this.file);
		this.upload = new ServletFileUpload(diskFactory);
		
		if (this.app.storage_maxrequestsize > 0) {
			this.upload.setSizeMax(this.app.storage_maxrequestsize);
		}
		
		if (this.app.storage_maxsize > 0) {
			this.upload.setFileSizeMax(this.app.storage_maxsize);
		}
	}
	
	private void processUploadedFile(FileItem item) throws Exception {
		File uploadedFile = new File(this.filePath, this.filePersistName);
	    item.write(uploadedFile);
	}
	
	public Storage upload(String path, HttpServletRequest request) {
		this.status = false;
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			return this;
		}
		
		this.buildPath(path);
		this.file.mkdirs();
		this.uploadFactory();
		
		System.out.println("Upload files to: " + this.file.getAbsolutePath());
		
		try {
			List<FileItem> itemFiles = this.upload.parseRequest(request);
			
			for (FileItem item : itemFiles) {
				if ((item.isFormField()) || (!isValidExtension(item.getName()))) {
					continue;
				}
				
				this.fileName = item.getName();
				this.filePersistName = this.generateName(this.fileName);
				this.extension = this.getExtension(this.fileName);
				this.size = item.getSize();
				this.processUploadedFile(item);
				this.status = true;
				
				System.out.println("File: " + this.filePersistName +  " (" + this.size + " bytes)");
			}
		} catch (Exception ex) {
			this.status = false;
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return this;
	}
	
	public Storage download(String path, HttpServletResponse response) {
		this.status = false;
		
		try {
			this.buildPath(path);
			
			if (!this.file.exists()) {
				System.out.println("File not found " + this.filePath);
				return null;
			}
			
			System.out.println("Download file from: " + this.file.getAbsolutePath());
            
            //configura response
            response.setContentType("application/octet-stream");
            response.setHeader("content-disposition", "attachment; filename=\"" + this.file.getName() + "\"");
            response.setContentLength((int) this.file.length());
            
            FileInputStream fileInputStream = new FileInputStream(this.file);
            OutputStream outputStream = response.getOutputStream();
            
            byte[] buffer = new byte[4096];
            int bytesRead;
        
            while ((bytesRead = fileInputStream.read(buffer)) > -1) {
            	outputStream.write(buffer, 0, bytesRead);
            }
         
            fileInputStream.close();
            outputStream.flush();
            
            this.fileName = this.file.getName();
			this.filePersistName = this.fileName;
			this.extension = this.getExtension(this.fileName);
			this.size = this.file.length();
			this.status = true;
        } catch (IOException ex) {
        	this.status = false;
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this;
	}
	
	public boolean delete(String path, HttpServletRequest request) {
		this.buildPath(path);
		
		System.out.println("Delete file from: " + this.file.getAbsolutePath());
		
		if (this.file.exists()) {
			return this.file.delete();
		} else {
			return true;
		}
    }
	
}
