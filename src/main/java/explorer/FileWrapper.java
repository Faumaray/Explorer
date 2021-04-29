package explorer;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.Graphics;

public class FileWrapper{
    private File file;//holds the file object
    private SimpleObjectProperty<ImageView> Icon;
    private SimpleStringProperty Name;//a property for the Name of the file
    private SimpleStringProperty Size;
    private SimpleObjectProperty<Date> Date;
    private SimpleStringProperty Type;
    private SimpleBooleanProperty isDirectory;
    private SimpleBooleanProperty isWritable;

    FileWrapper(File file) throws IOException{
        Icon = new SimpleObjectProperty<ImageView>();
        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
        BufferedImage image = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        Icon.set(new ImageView(SwingFXUtils.toFXImage(image, null)));
        Name = new SimpleStringProperty();
        Name.set(file.getName());
        Size = new SimpleStringProperty();
        Size.set(formatFileLength(file.length()));  
        Date = new SimpleObjectProperty<Date>();
        Date.set(new Date(file.lastModified()));
        Type = new SimpleStringProperty();
        Type.set(Files.probeContentType(file.toPath()));
        isDirectory = new SimpleBooleanProperty();
        isDirectory.set(file.isDirectory());
        isWritable = new SimpleBooleanProperty();
        isWritable.set(Files.isWritable(file.toPath()));
        this.file = file;
    }

    @Override
    public String toString() {
<<<<<<< Updated upstream
    return Name.getValue();
=======
    return Name.getValue(); 
    }

    public String getPath()
    {
        return file.getAbsolutePath();
    }

    public void deleete()
    {
        
    }

    public void setName(String newName)
    {
        Name.set(newName);
        File newDir = new File(file.getParent() + "/" + newName);
        file.renameTo(newDir);
>>>>>>> Stashed changes
    }

    public File getFile()
    {
        return file;
    }

    public ObjectProperty<ImageView> IconProperty()
    {
        return Icon;
    }

    public ImageView getImageView()
    {
        return Icon.getValue();
    }
    //this method returns the property of the Name and is used by the table to chose the value of
    //the column using it.
    public BooleanProperty isDirectoryProperty()
    {
        return isDirectory;
    }

    public String getName()
    {
        return Name.getValue();
    }

    public Boolean isDirectory()
    {
        return isDirectory.get();
    }

    public StringProperty TypeProperty()
    {
        return Type;
    }
    public StringProperty NameProperty(){
        return Name;
    }
    public StringProperty SizeProperty(){
        return Size;
    }
    public ObjectProperty<Date> DateProperty(){
        return Date;
    }
    private String formalDateModified(long date)
    {
        return Objects.toString(date, null);
    }
    private String formatFileLength(long length) {
        final String[] unitNames = {"bytes", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"};
        int i ;
        for (i = 0 ; length > 1024 && i < unitNames.length - 1 ; i++) {
            length = length / 1024 ;
        }
        return String.format("%,d %s", length, unitNames[i]);
    }
}