/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.filemanagement;

/**
 * Constants used for encode types
 */
public enum EncodeType {
    RLE("RLE",  new String[]{"*.rle"}),
    LIFE105 ("Life 1.05", new String[]{"*.lif","*.life"} ),
    LIFE106 ("Life 1.06", new String[]{"*.lif","*.life"} );
    
    private String name;
    private String[] fileExtensions;

    EncodeType(String name, String[] fileExtensions ){
        this.name = name;
        this.fileExtensions = fileExtensions;
    }

    /**
     * Returns the name
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the file extensions
     * @return The file extensions
     */
    public String[] getFileExtensions(){
        return fileExtensions;
    }
}
