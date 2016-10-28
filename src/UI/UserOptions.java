package UI;
import objects.*;
import objects.User.UserType;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserOptions extends JPanel implements ActionListener {
	public User user;
	public JLabel username, type, status;
	public JButton mod, admin, norm, ban, unban;
	public Admin ad;
	public UserOptions(User user, Admin ad) {
		this.ad = ad;
		if (user != null) {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.user = user;
			this.username = new JLabel(user.getUsername());
			this.type = new JLabel();
			
			norm = new JButton("User");
			mod = new JButton("Moderator");
			admin = new JButton("Administrator");
			ban = new JButton("Ban");
			unban = new JButton("Unban");
			
			norm.setActionCommand("setUser");
			norm.addActionListener(this);
			mod.setActionCommand("setMod");
			mod.addActionListener(this);
			admin.setActionCommand("setAdmin");
			admin.addActionListener(this);
			ban.setActionCommand("banUser");
			ban.addActionListener(this);
			unban.setActionCommand("unbanUser");
			unban.addActionListener(this);
			if (user.getUserType().equals(UserType.USER)) {
				type.setText("	User");
				norm.setEnabled(false);
			}
			else if (user.getUserType().equals(UserType.MOD)) {
				type.setText("	Moderator");
				mod.setEnabled(false);
			}
			else {
				type.setText("	Administrator");
				admin.setEnabled(false);
			}
			
			this.status = new JLabel();
			if (user.isBanned()) {
				status.setText("	Banned");
				ban.setEnabled(false);
			}
			else {
				status.setText("	Not Banned");
				unban.setEnabled(false);
			}
			JPanel top = new JPanel();
			top.add(username);
			top.add(type);
			top.add(status);
			
			JPanel mid = new JPanel();
			mid.add(norm);
			mid.add(mod);
			mid.add(admin);
			
			JPanel bottom = new JPanel();
			bottom.add(ban);
			bottom.add(unban);
			
			this.add(top);
			this.add(mid);
			this.add(bottom);
			this.setVisible(true);
		}
	}
	
	public void setTypeDisabled() {
		this.norm.setEnabled(false);
		this.mod.setEnabled(false);
		this.admin.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getActionCommand());
		
		if (arg0.getActionCommand().equals("banUser")) {
			ad.frame.uq.updateBanStatus(ad.u.getUsername(), true);
			ban.setEnabled(false);
			unban.setEnabled(true);
			status.setText("	Banned");

		}
		else if (arg0.getActionCommand().equals("unbanUser")) {
			ad.frame.uq.updateBanStatus(ad.u.getUsername(), false);
			ban.setEnabled(true);
			unban.setEnabled(false);
			status.setText("	Not Banned");

		}
		else if (arg0.getActionCommand().equals("setUser")) {
			ad.frame.uq.modifyUserPriv(ad.u.getUsername(), UserType.USER);
			norm.setEnabled(false);
			mod.setEnabled(true);
			admin.setEnabled(true);
			type.setText("	User");

		}
		else if (arg0.getActionCommand().equals("setMod")) {
			ad.frame.uq.modifyUserPriv(ad.u.getUsername(), UserType.MOD);
			norm.setEnabled(true);
			mod.setEnabled(false);
			admin.setEnabled(true);
			type.setText("	Moderator");

		}
		else if (arg0.getActionCommand().equals("setAdmin")) {
			ad.frame.uq.modifyUserPriv(ad.u.getUsername(), UserType.ADMIN);
			norm.setEnabled(true);
			mod.setEnabled(true);
			admin.setEnabled(false);
			type.setText("	Administrator");
		}
		ad.frame.repaint();
	}
}
