import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JOptionPane;

public class NitPad extends JFrame implements Runnable
{
  private JTextArea textArea;
  private File currentFile;
  private boolean isSaved;
  public NitPad()
  {
    textArea = new JTextArea();
    currentFile = null;
    updateTitleBar();
    isSaved = false;
    textArea.getDocument().addDocumentListener(new DocumentListener(){
      public void changedUpdate(DocumentEvent e)
      {
        isSaved = false;
        updateTitleBar();
      }
      public void removeUpdate(DocumentEvent e){}
      public void insertUpdate(DocumentEvent e)
      {
        updateTitleBar();
      }
    });
  }
  public void run()
  {
    setSize(600,600);
    makeMenus();
    getContentPane().add(textArea);
    
    setVisible(true);
  }
  private void makeMenus()
  {
    JMenuBar mbar = new JMenuBar();
    setJMenuBar(mbar);
    JMenu fileMenu = new JMenu("File");
    mbar.add(fileMenu);
    JMenuItem newItem = new JMenuItem("New");
    JMenuItem openItem = new JMenuItem("Open...");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem saveAsItem = new JMenuItem("Save As...");
    JMenuItem quitItem = new JMenuItem("Quit");
    fileMenu.add(newItem);
    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.add(saveAsItem);
    fileMenu.add(quitItem);
    newItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
      }
    });
    openItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        if(!isSaved)
        {
          int choice = JOptionPane.showConfirmDialog(NitPad.this, "Would you like to save?");
          if(choice == JOptionPane.NO_OPTION)
          {}
          else if (choice == JOptionPane.YES_OPTION)
          {
            saveIt();
          }
          else if(choice == JOptionPane.CANCEL_OPTION)
          {
            return;
          }
        }
        JFileChooser jfc = new JFileChooser();
        int choice = jfc.showOpenDialog(NitPad.this);
        if(choice == JFileChooser.CANCEL_OPTION)
        {
          return;
        }
        if(choice == JFileChooser.APPROVE_OPTION)
        {
          currentFile = jfc.getSelectedFile();
          isSaved = true;
          updateTitleBar();
          readAndDisplayCurrentFile();
        }
      }
    });
    saveItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        saveIt();
      }
    });
    quitItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e)
      {
        if(!isSaved)
        {
          int choice = JOptionPane.showConfirmDialog(NitPad.this, "Would you like save before quitting?");
          if(choice == JOptionPane.NO_OPTION)
          {
            System.exit(0);
          }
          else if(choice == JOptionPane.YES_OPTION)
          {
            saveIt();
          }
          else if(choice == JOptionPane.CANCEL_OPTION)
          {
            return;
          }
        }
        System.exit(0);
      }
    });
  }
  private void updateTitleBar()
  {
    if(currentFile != null)
    {
      String s = "NitPad: " + currentFile.getAbsolutePath();
      if(isSaved)
      {
        s += "*";
      }
      setTitle(s);
    }
    else
    {
      setTitle("NitPad: Untitled *");
    }
  }
  public boolean saveIt()
  {
    int choice = -1;
    if(currentFile == null)
    {
      JFileChooser jfc = new JFileChooser();
      choice = jfc.showSaveDialog(NitPad.this);
      if(choice == JFileChooser.APPROVE_OPTION)
      {
        currentFile = jfc.getSelectedFile();
        writeToCurrentFile();
        return writeToCurrentFile();
      }
      else
      {
        return false;
      }
    }
    else
    {
      return writeToCurrentFile();
    }
  }
  private void readAndDisplayCurrentFile()
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(currentFile));
      StringBuffer sb = new StringBuffer();
      String line = "";
      while( (line = br.readLine()) != null)
      {
        sb.append(line + "\n");
      }
      br.close();
      textArea.setText(sb.toString());
      isSaved = true;
    }
    catch(FileNotFoundException ex)
    {
      //TODO
    }
    catch(IOException ex)
    {
      //TODO
    }
  }
  private boolean writeToCurrentFile()
  {
   try
   {
    BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile)); 
    bw.write(textArea.getText());
    bw.close();
    isSaved = true;
    updateTitleBar();
    return true;
   }
   catch(FileNotFoundException ex)
   {
   }
   catch(IOException ex)
   {
   }
   return false;
  }
  public static void main(String[] args)
  {
    NitPad np = new NitPad();
    javax.swing.SwingUtilities.invokeLater(np);
  }
}
