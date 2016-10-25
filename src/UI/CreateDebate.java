package UI;

import JSON_translation.*;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.BoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateDebate extends JPanel implements ActionListener {
	// Mods and Admin only
	public JLabel date1, date2, title1, description1;
	public JFormattedTextField startDate, endDate;
	public JTextField title, description;
	public JButton submit;
	public Date d1;
	public String start, end;
	public DebateQuery dq;
	public Frame frame;
	
	public CreateDebate(Frame frame) {
		
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       
        startDate = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
        startDate.setColumns(20);
        
        endDate = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
        endDate.setColumns(20);
        
		this.frame = frame;
		dq = new DebateQuery();
		this.setLayout(new BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
		date1 = new JLabel("Start Date (Enter date as yyyy-MM-dd hh:mm:ss): ");
		this.add(date1);
		
		this.add(startDate);
		date2 = new JLabel("End Date (Enter date as yyyy-MM-dd hh:mm:ss): ");
		this.add(date2);
		endDate = new JFormattedTextField();
		this.add(endDate);
		title1 = new JLabel("Title: ");
		this.add(title1);
		title = new JTextField();
		this.add(title);
		description1 = new JLabel("Description: ");
		this.add(description1);
		description = new JTextField();
		this.add(description);
		submit = new JButton("Submit Post");
		submit.setActionCommand("submit");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getActionCommand().equals("sumbit")) {
			//start = dq.convertDateToString(startDate.getText());
			dq.checkForDuplicateDebateTitle(title.getText());
			dq.checkForOverLappingDates(startDate.getText(), endDate.getText());
		}
	}
	
    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
	
	
}
