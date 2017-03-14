package br.com.elos.filesystem;

import java.io.File;
import java.io.IOException;

public class Files {
    
    public String getAbsolutePath(File files, String path) throws IOException {
        String dir = null;
        
        for (File file: files.listFiles()) {
            if (dir != null) {
                break;
            }
            
            if (!file.isDirectory()) {
                continue;
            }
            
            if (this.findPath(file, path)) {
                dir = file.getCanonicalPath();
                break;
            }
            
            dir = this.getAbsolutePath(file, path);
        }
        
        return dir;
    }
    
    @Deprecated
    public String getAbsolutePathFile(File files, String filename) throws IOException {
        String dir = null;
        
        for (File file: files.listFiles()) {
            if (dir != null) {
                break;
            }
            System.out.println(file.getCanonicalFile()+" " + file.isFile());
            
            if (!file.isFile()) {
                continue;
            }
            
            if (this.findFile(file, filename)) {
                dir = file.getCanonicalPath();
                break;
            }
            
            dir = this.getAbsolutePathFile(file, filename);
        }
        
        return dir;
    }
    
    public boolean findPath(File files, String path) {
        if ((files == null) || (!files.isDirectory())) {
            return false;
        }
        
        if (files.getName().equals(path)) {
            return true;
        }
        
        boolean found = false;
        
        for (File file: files.listFiles()) {
            if ((!file.isDirectory()) || (!file.getName().equals(path))) {
                continue;
            }
            
            found = true;
        }
        
        return found;
    }
    
    public boolean findFile(File files, String filename) throws IOException {
        //System.out.println(files);
        if ((files == null) || (!files.isFile())) {
            return false;
        }
        
        if (files.getName().equals(filename)) {
            return true;
        }
        
        return false;
        
        /*boolean found = false;
        
        for (File file: files.listFiles()) {
            System.out.println(file.getAbsolutePath());
            if ((!file.isFile()) || (!file.getName().equals(filename))) {
                continue;
            }
            
            found = true;
        }
        
        return found;*/
    }
    
}
