/*****************************************************************************
 * dpcs/UninstallPrivApps.java: Priv-apps uninstaller for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2017 Karanvir Singh
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package dpcs;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.commons.io.IOUtils;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Color;

@SuppressWarnings("serial")
public class UninstallPrivApps extends JFrame {

	JLabel PrivAppUninstallDone;
	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	JList list;
	List<String> lines;
	String[] values;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UninstallPrivApps frame = new UninstallPrivApps();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public UninstallPrivApps() {
		setResizable(false);
		setTitle("Uninstall Priv-apps");
		setIconImage(Toolkit.getDefaultToolkit().getImage(UninstallSystemApps.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 482, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel AppStatus = new JLabel("");
		AppStatus.setBounds(8, 404, 456, 17);
		contentPane.add(AppStatus);

		PrivAppUninstallDone = new JLabel("");
		PrivAppUninstallDone.setText("");
		PrivAppUninstallDone.setBounds(151, 312, 186, 56);
		contentPane.add(PrivAppUninstallDone);

		JLabel lblSelect = new JLabel("Select an app to remove");
		lblSelect.setBounds(25, 12, 405, 17);
		contentPane.add(lblSelect);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 41, 428, 259);
		contentPane.add(scrollPane);

		final JButton btnUninstall = new JButton("Uninstall");
		btnUninstall.setToolTipText("Uninstall the selected app");
		btnUninstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrivAppUninstallDone.setText("");
				if (list.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select an app first");
				} else {
					try {
						AppStatus.setText("Uninstalling...");
						Process p1 = Runtime.getRuntime().exec("adb remount");
						p1.waitFor();
						String[] commands = new String[3];
						commands[0] = "adb shell su -c rm -r";
						commands[1] = "/system/priv-app/";
						commands[2] = " " + list.getSelectedValue();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						Process p3 = Runtime.getRuntime()
								.exec("adb shell ls /system/priv-app/ > /sdcard/.privapps.txt");
						p3.waitFor();
						Process p4 = Runtime.getRuntime().exec("adb pull /sdcard/.privapps.txt");
						p4.waitFor();
						Process p5 = Runtime.getRuntime().exec("adb shell rm /sdcard/.privapps.txt");
						p5.waitFor();
						lines = IOUtils.readLines(new FileInputStream(".privapps.txt"));
						values = new String[lines.size()];
						values = lines.toArray(values);
						list = new JList();
						list.setModel(new AbstractListModel() {
							public int getSize() {
								return values.length;
							}

							public Object getElementAt(int index) {
								return values[index];
							}
						});
						scrollPane.setViewportView(list);

						File file = new File(".privapps.txt");
						if (file.exists() && !file.isDirectory()) {
							file.delete();
						}
						AppStatus.setText("App has been uninstalled successfully");
						PrivAppUninstallDone
								.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Smalldone.png")));
						btnUninstall.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});
		btnUninstall.setBounds(26, 327, 107, 27);
		contentPane.add(btnUninstall);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Refresh the apps list");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell ls /system/priv-app/ > /sdcard/.privapps.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.privapps.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.privapps.txt");
					p3.waitFor();
					lines = IOUtils.readLines(new FileInputStream(".privapps.txt"));
					values = new String[lines.size()];
					values = lines.toArray(values);
					list = new JList();
					list.setModel(new AbstractListModel() {
						public int getSize() {
							return values.length;
						}

						public Object getElementAt(int index) {
							return values[index];
						}
					});
					scrollPane.setViewportView(list);
					File file = new File(".privapps.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setBounds(344, 327, 107, 27);
		contentPane.add(btnRefresh);

		try {
			Process p1 = Runtime.getRuntime().exec("adb shell ls /system/priv-app/ > /sdcard/.privapps.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.privapps.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.privapps.txt");
			p3.waitFor();
			lines = IOUtils.readLines(new FileInputStream(".privapps.txt"));
			values = new String[lines.size()];
			values = lines.toArray(values);
			list = new JList();
			list.setModel(new AbstractListModel() {
				public int getSize() {
					return values.length;
				}

				public Object getElementAt(int index) {
					return values[index];
				}
			});
			scrollPane.setViewportView(list);

			JLabel lblNewLabel = new JLabel("Note: You should also remove app's odex file if it exits ");
			lblNewLabel.setBounds(25, 374, 438, 17);
			contentPane.add(lblNewLabel);

			JLabel lblNeedsRootAnd = new JLabel("Needs root and does not work on production android builds!");
			lblNeedsRootAnd.setBounds(25, 426, 454, 17);
			contentPane.add(lblNeedsRootAnd);

			JLabel lblOnlyForAndroid = new JLabel("Uninstallation only for android 4.4.x and higher!");
			lblOnlyForAndroid.setBounds(8, 452, 450, 15);
			contentPane.add(lblOnlyForAndroid);
			File file = new File(".privapps.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
