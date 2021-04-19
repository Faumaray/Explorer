package com;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

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

    final String rootPath = "C:\\Users\\sirog\\Documents\\GitProjects\\FileExplorer\\Explorer";
    String curPath;
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
    void doubleClick(MouseEvent event) throws IOException 
    {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2)
        {
            FileWrapper selected = objects.getSelectionModel().getSelectedItem();
            action(selected);
        }
    }

    @FXML
    void initialize() throws IOException {
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

    void action(FileWrapper selected) throws IOException
    {
        if(selected.isDirectory())
            {
                backs.add(curPath);
                curPath += "\\" + selected.getName();
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
            openFile(curPath + "\\" + selected.getName());
        }
    }
    void openFile(String curPath) throws IOException
    {
        Desktop desktop = Desktop.getDesktop();
        File file = new File(curPath);
        desktop.open(file);
    }

    void createTree(File file, TreeItem<FileWrapper> rootItem) throws IOException {
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
    public void displayTreeView() throws IOException {
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

