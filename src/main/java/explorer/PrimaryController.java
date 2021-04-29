package explorer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FilenameUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PrimaryController {

    final String rootPath = "../";
    String curPath;
<<<<<<< Updated upstream
=======
    String trashcan = "../Trashcan/";
    String system = System.getProperty("user.dir");
>>>>>>> Stashed changes
    LinkedList<String> backs = new LinkedList<String>();
    LinkedList<String> forwards = new LinkedList<String>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private SplitPane root;

    @FXML
    private TextField path;

    @FXML
    private Button back;

    @FXML
    private Button forward;

    @FXML
    private Button home;

    @FXML
    private TextField search;

    @FXML
    private Button cancel;

    @FXML
    private TreeView<FileWrapper> tree;

    @FXML
    private TableView<FileWrapper> objects;

    @FXML
    private TableColumn<FileWrapper, String> objectsName;

    @FXML
    private TableColumn<FileWrapper, Long> objectsDate;

    @FXML
    private TableColumn<FileWrapper, String> objectsType;

    @FXML
    private TableColumn<FileWrapper, String> objectsSize;

    @FXML
    private TableColumn<FileWrapper, String> icon;

    @FXML
<<<<<<< Updated upstream
=======
    private MenuItem Open;

    @FXML
    private MenuItem OpenInNewWindow;

    @FXML
    private MenuItem Rename;

    @FXML
    private MenuItem Delete;

    @FXML
    private MenuItem Cut;

    @FXML
    private MenuItem Copy;

    @FXML
    private MenuItem Paste;

    @FXML
    private MenuItem createFolder;

    @FXML
    private MenuItem createFile;

    LinkedList<FileWrapper> cut = new LinkedList<FileWrapper>();
    LinkedList<FileWrapper> copy = new LinkedList<FileWrapper>();
    // TODO Посмотреть в методичке можно ли копировать или перемещать
    @FXML
    void Copy(ActionEvent event) throws IOException 
    {
        cut.clear();
        copy(objects.getSelectionModel().getSelectedItems());
        Paste.setVisible(true);
    }

    public void copy(ObservableList<FileWrapper> files) throws IOException
    {
        for(FileWrapper file : files)
        {
            copy.add(file);
            if(file.isDirectory())
            {
               for(File tmp :  Files.walk(Paths.get(file.getPath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList()))
                {
                    copy.add(new FileWrapper(tmp));
                }
            }
        }
    }

    @FXML
    void Cut(ActionEvent event) throws IOException 
    {
        copy.clear();
        cut(objects.getSelectionModel().getSelectedItems());
        Paste.setVisible(true);
    }
    public void cut(ObservableList<FileWrapper> files) throws IOException
    {
        for(FileWrapper file : files)
        {
            cut.add(file);
            if(file.isDirectory())
            {
               for(File tmp :  Files.walk(Paths.get(file.getPath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList()))
                {
                    cut.add(new FileWrapper(tmp));
                }
            }
        }
    }

    @FXML
    void Delete(ActionEvent event) throws IOException 
    {
        for(FileWrapper selected : objects.getSelectionModel().getSelectedItems())
        {
            if(selected.getPath().contains(trashcan) && !selected.getPath().equals(trashcan))
            {
                    if(selected.isDirectory())
                    FileUtils.deleteDirectory(new File(selected.getPath()));
                    else
                    FileUtils.forceDelete(selected.getFile()); 
            }
            else
            {
                if(selected.getPath().contains("../Explorer-1") || selected.getPath().equals(system) || selected.getPath().equals(trashcan))
                {
                    throw new AccessDeniedException("Системная папка\\файл");
                    //TODO SYS DELT ERR
                }
                else
                {
                    if(selected.isDirectory())
                    FileUtils.moveDirectory(new File(selected.getPath()),new File(trashcan+selected.getName()));
                    else
                    FileUtils.moveFileToDirectory(new File(selected.getPath()), new File(trashcan), false);
        
                    
                }
            
            }
        }
        showFiles(curPath);
    }

    @FXML
    void Paste(ActionEvent event) throws IOException 
    {
        if(!cut.isEmpty())
        {
            for(FileWrapper file : cut)
            {
                Files.move(Path.of(file.getPath()), Path.of(curPath+"/"+file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
            Paste.setVisible(false);
            cut.clear();
        }
        if(!copy.isEmpty())
        {
            for(FileWrapper file : copy)
            {
                Files.copy(Path.of(file.getPath()), Path.of(curPath+"/"+file.getName()), StandardCopyOption.REPLACE_EXISTING);
            }
            Paste.setVisible(false);
            copy.clear();
        }
        
    }

    @FXML
    void createFile(ActionEvent event) throws IOException 
    {
        if(curPath.contains("../Explorer-1") || curPath.equals(system) || curPath.equals(trashcan) || curPath.contains(trashcan))
            {
                throw new AccessDeniedException("Системная папка\\файл");
            }
            else
            {
                int i = 0;
                File newfile = new File(curPath + "/newfilename");
                newfile.createNewFile();
                while(newfile.createNewFile() != true)
                {
                    i++;
                    newfile = new File(curPath + "/newfilename" + i);
                }
                String filename = newfile.getName();
                showFiles(curPath);
                objects.getSelectionModel().select(objects.getItems().stream().filter(carnet -> filename.equals(carnet.getName())).findFirst().orElse(null));
                int selectedRowIndex = objects.getSelectionModel().getSelectedIndex();
                objects.edit(selectedRowIndex, objects.getColumns().get(1));
            }
    }

    @FXML
    void createFolder(ActionEvent event) throws IOException 
    {
        System.out.println(curPath);
        if(curPath.contains("../Explorer-1") || curPath.equals(system) || curPath.equals(trashcan) || curPath.contains(trashcan))
            {
                throw new AccessDeniedException("Системная папка\\файл");
            }
            else
            {
                int i = 0;
                File newdir = new File(curPath + "/newfoldername");
                while(true)
                {
                    try
                    {
                        Files.createDirectory(newdir.toPath());
                        break;
                    }
                    catch(FileAlreadyExistsException e)
                    {
                        i++;
                        newdir = new File(curPath + "/newfoldername" + i);
                    }
                }
                String dirname = newdir.getName();
                showFiles(curPath);
                objects.getSelectionModel().select(objects.getItems().stream().filter(carnet -> dirname.equals(carnet.getName())).findFirst().orElse(null));
                int selectedRowIndex = objects.getSelectionModel().getSelectedIndex();
                objects.edit(selectedRowIndex, objects.getColumns().get(1));
            }
    }

    @FXML
    void Open(ActionEvent event) throws IOException, URISyntaxException 
    {
        FileWrapper selected = objects.getSelectionModel().getSelectedItem();
        action(selected);
    }

    @FXML
    void OpenNewWindow(ActionEvent event) throws IOException 
    {
        FileWrapper selected = objects.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        Stage stage = new Stage();
        stage.setTitle(curPath + "/" + selected.getName());
        stage.setScene(scene);
        PrimaryController controller=fxmlLoader.<PrimaryController>getController();
        controller.initData(curPath + "/" + selected.getName());
        stage.show();
    }

    @FXML
    void Rename(ActionEvent event) 
    {
        if(curPath.contains(system) || curPath.equals(system) ||curPath.equals(system))
            {
                //TODO SYS DELT ERR
            }
            else
            {
                int selectedRowIndex = objects.getSelectionModel().getSelectedIndex();
                objects.edit(selectedRowIndex, objects.getColumns().get(1));
            }
    }

    @FXML
    void renamecom(CellEditEvent<FileWrapper, String> event) throws IOException, URISyntaxException 
    {
        FileWrapper selected = objects.getSelectionModel().getSelectedItem();
        selected.setName(event.getNewValue());
        showFiles(curPath);
    }


    @FXML
    void contextFiles(ContextMenuEvent event) 
    {

    }

    @FXML
>>>>>>> Stashed changes
    void back(ActionEvent event) throws IOException
    {
        if(!backs.isEmpty())
        {
            forwards.add(curPath);
            curPath = backs.getLast();
            backs.removeLast();
            showFiles(curPath);
        }

    }

    @FXML
    void enter(KeyEvent event) throws IOException
    {
        if(event.getCode().toString().equals("ENTER"))
        {
            backs.add(curPath);
            if(!forwards.isEmpty())
            {
                if(!forwards.getLast().equals(path.getText()))
                {
                    forwards.clear();
                }
            }
            showFiles(path.getText());
            curPath = path.getText();
        }
    }

    @FXML
    void expand(MouseEvent event) throws IOException
    {
        TreeItem<FileWrapper> item = tree.getSelectionModel().getSelectedItem();
        FileWrapper fwItem = item.getValue();
        actionTree(fwItem);
    }

    @FXML
    void forward(ActionEvent event) throws IOException
    {
        if(!forwards.isEmpty())
        {
            backs.add(curPath);
            curPath = forwards.getLast();
            forwards.removeLast();
            showFiles(curPath);
        }
    }

    @FXML
    void home(ActionEvent event) throws IOException
    {
        forwards.clear();
        backs.add(curPath);
        curPath = rootPath;
        showFiles(rootPath);
    }

    @FXML
    void search(KeyEvent event) throws IOException
    {   
        if(event.getCode().toString().equals("ENTER"))
        {
            objects.getItems().clear();
            backs.add(curPath);
            path.setText("Search Results");
            find(search.getText(),new File(curPath));
        }
    }

    @FXML
    void cancel(ActionEvent event) throws IOException
    {
        search.clear();
        curPath = backs.getLast();
        backs.removeLast();
        showFiles(curPath);
    }

    @FXML
    void doubleClick(MouseEvent event) throws IOException, URISyntaxException
    {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2)
        {
            FileWrapper selected = objects.getSelectionModel().getSelectedItem();
            action(selected);
        }
    }

    @FXML
    void initialize() throws IOException{
        icon.setCellValueFactory(new PropertyValueFactory<FileWrapper, String>("Icon"));
        objectsName.setCellValueFactory(new PropertyValueFactory<FileWrapper, String>("Name"));
        objectsDate.setCellValueFactory(new PropertyValueFactory<FileWrapper, Long>("Date"));
        objectsType.setCellValueFactory(new PropertyValueFactory<FileWrapper, String>("Type"));
        objectsSize.setCellValueFactory(new PropertyValueFactory<FileWrapper, String>("Size"));
        path.setText(rootPath);
        showFiles(rootPath);
        curPath = rootPath;
        displayTreeView();
    }
    void showFiles(String curPath) throws IOException
    {   
        if(!curPath.contains(rootPath))
        {
            // TODO Проброс сообщения о несуществовании
        }
        else
        {
            objects.getItems().clear();
            File fileDir = new File(curPath);
            LinkedList<FileWrapper> files = new LinkedList<FileWrapper>();
            for (File file : fileDir.listFiles()) {
                files.add(new FileWrapper(file));
            }
            objects.getItems().addAll(files);
            path.setText(curPath);
        }
    }

    public void find(String file, File directory) throws IOException
    {
        File[] files = directory.listFiles();
        for (File f : files) 
        {
            System.out.println(f.getName());
            if(f.isDirectory())
            {
                find(file, f);
            }
            if(FilenameUtils.wildcardMatchOnSystem(f.getName(), file))
            {
            objects.getItems().add(new FileWrapper(f));
            }
        }
    }

    void actionTree(FileWrapper selected) throws IOException
    {
        backs.add(curPath);
        curPath = selected.getFile().getPath();
        if(!forwards.isEmpty())
        {
            if(!forwards.getLast().equals(curPath))
            {
                forwards.clear();
            }
        }
        showFiles(curPath);
    }

    void action(FileWrapper selected) throws IOException, URISyntaxException
    {
        if(selected.isDirectory())
            {
                backs.add(curPath);
<<<<<<< Updated upstream
                curPath += "/" + selected.getName();
=======
                if(curPath.equals(App.rootPath))
                {
                    curPath += selected.getName();
                }
                else
                {
                    curPath += "/"+selected.getName();
                }
                
>>>>>>> Stashed changes
                if(!forwards.isEmpty())
                {
                    if(!forwards.getLast().equals(curPath))
                    {
                        forwards.clear();
                    }
                }
                showFiles(curPath);
            }
        else
        {
            openFile(curPath + "/" + selected.getName());
        }
    }
    void openFile(String curPath) throws IOException, URISyntaxException
    {
        File file = new File(curPath);
        File myFile = new File(file.getCanonicalPath());
        DesktopApi.open(myFile);
    }

    void createTree(File file, TreeItem<FileWrapper> rootItem) throws IOException{
        if (file.isDirectory()) 
        {
            FileWrapper fw = new FileWrapper(file);
            TreeItem<FileWrapper> treeItem = new TreeItem<>(fw, fw.getImageView());
            rootItem.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        }
        else{}
    }
    public void displayTreeView() throws IOException{
        // Creates the root item.
        FileWrapper root = new FileWrapper(new File(rootPath));
        TreeItem<FileWrapper> rootItem = new TreeItem<FileWrapper>(root,root.getImageView());
    
        // Hides the root item of the tree view.
        tree.setShowRoot(false);
        // Get a list of files.
        File fileInputDirectoryLocation = new File(rootPath);
        File fileList[] = fileInputDirectoryLocation.listFiles();
    
        // create tree
        for (File file : fileList) {
            createTree(file, rootItem);
        }
    
        tree.setRoot(rootItem);
    }
}

