package UI;
import objects.*;
import objects.User.UserType;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserOptions extends JPanel {
	public User user;
	public JLabel username, type, status;
	public JButton mod, admin, norm, ban, unban;
	public UserOptions(User user) {
		this.setLayout(new BorderLayout());
		this.user = user;
		this.username = new JLabel(user.getUsername());
		this.type = new JLabel();
		
		norm = new JButton("User");
		norm.setActionCommand("setUser");
		mod = new JButton("Moderator");
		mod.setActionCommand("setMod");
		admin = new JButton("Administrator");
		admin.setActionCommand("setAdmin");
		ban = new JButton("Ban");
		ban.setActionCommand("banUser");
		unban = new JButton("Unban");
		unban.setActionCommand("unbanUser");
		
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
		this.add(username, BorderLayout.PAGE_START);
		this.add(type, BorderLayout.PAGE_START);
		this.add(status, BorderLayout.PAGE_START);
		this.add(norm, BorderLayout.LINE_START);
		this.add(mod, BorderLayout.CENTER);
		this.add(admin, BorderLayout.LINE_END);
		this.add(ban, BorderLayout.PAGE_END);
		this.add(unban, BorderLayout.PAGE_END);
		
	}
	
	public void setTypeDisabled() {
		this.norm.setEnabled(false);
		this.mod.setEnabled(false);
		this.admin.setEnabled(false);
	}
}
