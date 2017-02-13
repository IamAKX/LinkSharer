/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akashapplications;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.Popup;

/**
 *
 * @author akash
 */
public class RunInTray {
   static TrayIcon trayIcon;
   static MainInterface mainInterface;
   static JCheckBox chkbox;
   public static void show() throws AWTException
   {
       if(!SystemTray.isSupported())
       {
           JOptionPane.showMessageDialog(null, "System tray is not supported", "Cannot run in background", JOptionPane.ERROR_MESSAGE);
       }
       trayIcon = new TrayIcon(createIcon("icon.png", "icon"));
       trayIcon.setToolTip("Link Sharer is running");
       
       PopupMenu popupMenu = new PopupMenu();
       
       MenuItem itemShow = new MenuItem("Show Window");
       MenuItem itemExit = new MenuItem("Exit");
       popupMenu.add(itemShow);
       popupMenu.addSeparator();
       popupMenu.add(itemExit);
       
       itemShow.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              mainInterface.setVisible(true);
              chkbox.setSelected(false);
              SystemTray.getSystemTray().remove(trayIcon);
           }
       });
       itemExit.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               System.exit(0);
           }
       });
       trayIcon.setPopupMenu(popupMenu);
       
       
       SystemTray.getSystemTray().add(trayIcon);
   }
   protected static Image createIcon(String path,String desc)
   {
       URL imageUrl = RunInTray.class.getResource(path);
       return (new ImageIcon(imageUrl,desc)).getImage();
   }
    
   RunInTray(MainInterface aThis, JCheckBox backgroundCheckBox) {
        mainInterface = aThis;
        chkbox = backgroundCheckBox;
    }

}
